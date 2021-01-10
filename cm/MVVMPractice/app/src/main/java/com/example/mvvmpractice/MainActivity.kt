package com.example.mvvmpractice

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mainData = ArrayList<Model>()
    private var count = 0

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MainViewModel::class.java)

        list_data.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list_data.adapter = DataAdapter(viewModel.items){
            Toast.makeText(this,"메인 -> ${it.name} 클릭 됨!!",Toast.LENGTH_SHORT).show()
        }
        viewModel.data.observe(this, Observer {
            mainData = it
            (list_data.adapter as DataAdapter).notifyDataSetChanged()
        })

        btn_click.setOnClickListener {
            count ++
            viewModel.requestNewData(count)
            txt_count.text = getString(R.string.txt_count,count)
        }

        list_data.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 내가 쓴 글 목록이 끝까지 갔을 때
                if(!list_data.canScrollVertically(1)){
                    count ++
                    viewModel.requestNewData(count) // n번째 데이터 요청(연속조회 term 으로 수정 필요)
                }
            }
        })
    }


}
