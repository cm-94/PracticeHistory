package com.example.customviewpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_textview.*
import kotlinx.android.synthetic.main.custom_textview.inputButton
import kotlinx.android.synthetic.main.custom_textview.inputText
import kotlinx.android.synthetic.main.custom_textview.myTextView
import kotlinx.android.synthetic.main.custom_textview.view.*
import kotlinx.android.synthetic.main.custom_textview.view.inputText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputButton.setOnClickListener{
            if(inputText!=null){
                myTextView.inputText(inputText.text.toString())
            }
        }
    }
}

