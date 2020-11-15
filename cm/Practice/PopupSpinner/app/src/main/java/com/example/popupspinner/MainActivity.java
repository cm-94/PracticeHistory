package com.example.popupspinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Addr_Popup.onAddrViewClickListener,
        View.OnClickListener{
    /** Popup */
    private Addr_Popup addr_popup;

    /** View */
    private TextView txt_city;
    private TextView txt_county;

    /** Data */
    private ArrayList<String> mCityList;
    private ArrayList<String> mCountyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();
        txt_city = findViewById(R.id.txt_city);
        txt_county = findViewById(R.id.txt_county);
        txt_city.setOnClickListener(this);
        txt_county.setOnClickListener(this);

        mCityList = new ArrayList<String>();
        mCityList.add("==선택==");
        mCityList.add("강원도");
        mCityList.add("경기도");
        mCityList.add("서울특별시");
        mCityList.add("==인천광역시");
        mCityList.add("제주특별자치도");

        mCountyList = new ArrayList<String>();
        mCountyList.add("==선택==");
        mCountyList.add("영등포구");
        mCountyList.add("강남구");
        mCountyList.add("동작구");
        mCountyList.add("마포구");
        mCountyList.add("구로구");
        mCountyList.add("동대문구");

        addr_popup = new Addr_Popup(this,0);
        addr_popup.setOnDialogViewClickListener(this);

        /**
         * 주소검색 클릭 시 시도명(TR 0726) 요청 및 데이터 세팅
         * 수신 데이터로 팝업 내 시도명 데이터 세팅
         */
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_city:
                addr_popup.show();
                addr_popup.setCityList(mCityList);
                break;

            case R.id.txt_county:
                addr_popup.show();
                break;
        }
    }

    @Override
    public void onAddrViewClick(View v,String data) {
        Log.d("PopupPrxactice_Main","메인 onDialogViewClick()!! ID:"+v.getId());
        Log.d("PopupPractice_Main","spinner_city ID:"+R.id.spinner_city);
        Log.d("PopupPractice_Main","spinner_county ID:"+R.id.spinner_county);
        switch(v.getId()){
            case R.id.spinner_city:
                Log.d("PopupPractice_Main","시도명 스피너 클릭!! -> "+data);
                // TODO : 1. 시도명 데이터 세팅
                // TODO : 2. 전달받은 시도명 (data) 로 시군구명 TR 데이터 요청!!
                // TODO : 3. 수신 데이터로 popup 시도군명 데이터 및 스피너 세팅!!
                //      단!! 수신된 데이터(String)이 [mCityList.get(0) || "" ] 아닐때만!!
                if(data.equals(mCityList.get(0))||data.equals("")){
                    break;
                }else{
                    addr_popup.setCountyList(mCountyList);
                }
                break;
            // TODO : 시군구명 데이터 세팅
            case R.id.spinner_county:
                Log.d("PopupPractice_Main","시군구명 스피너 클릭!! -> "+data);
                break;
            // TODO : 사용자가 입력한 주소 데이터
            //  popup에서 데이터 3개(시도명, 시도군명, 상세주소)가 다 설정되어 있어야 넘어옴..!!
            //  여기서 data 3개 한번 더 [null,==선택==] 등 체크 해서 가공 후 영문주소 TR 요청!!
            //  onTrData() - AQ3501 : 수신된 TR을 Popup의 ListView용 데이터로 Setting!! -> Addr_Popup에서 메서드 정의..
            case R.id.btn_addr_lookup:

        }

    }
}
