package com.example.popupspinner;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Addr_Popup extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    /** Context */
    private Context mContext;

    /** View */
    private View mView;
    private Spinner city_spinner;
    private Spinner county_spinner;

    /** Data */
    private ArrayList<String> city_data;
    private ArrayList<String> county_data;



    /** Popup View Click Listener */
    public interface onAddrViewClickListener {
        void onAddrViewClick(View v, String data);
    }
    private onAddrViewClickListener mAddrViewClickListener;

    public void setOnDialogViewClickListener(onAddrViewClickListener listener){
        Log.d("PopupPractice_Popup","팝업 리스너 등록!!");
        this.mAddrViewClickListener = listener;
    }


    public Addr_Popup(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;

        city_data = new ArrayList<>();
        county_data = new ArrayList<>();

        InitView();


    }

    @Override
    public void show() {
        super.show();
    }

    private void InitView(){
        /** View */
        mView = View.inflate(mContext,R.layout.addr_popup,null);
        city_spinner = mView.findViewById(R.id.spinner_city);
        county_spinner = mView.findViewById(R.id.spinner_county);

        /** Spinner Item Selected 이벤트!! */
        city_spinner.setOnItemSelectedListener(this);
        county_spinner.setOnItemSelectedListener(this);

        setContentView(mView);


    }



    @Override
    public void onClick(View view) {
        Log.d("PopupPractice_Popup","팝업 onClick()!!");
        switch(view.getId()){

        }
        mAddrViewClickListener.onAddrViewClick(view,"");
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("PopupPractice_Popup","onItemSelected() 클릭!! 선택 item ID:"+adapterView.getId());
        switch(adapterView.getId()){
            case R.id.spinner_city:
                if(city_data.size()>0){
                    mAddrViewClickListener.onAddrViewClick(adapterView,city_data.get(i));
                    break;
                }

            case R.id.spinner_county:
                if(county_data.size()>0){
                    mAddrViewClickListener.onAddrViewClick(adapterView,county_data.get(i));
                    break;
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }





    public void setCityList(ArrayList<String> data){
        Log.d("PopupPractice_Popup","setCityList()!!");
        city_data = data;
        //스피너와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter=new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, data);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //스피너의 어댑터 지정
        city_spinner.setAdapter(spinner_adapter);

    }
    public void setCountyList(ArrayList<String> data){
        Log.d("PopupPractice_Popup","setCountyList()!!");

        county_data = data;
        ArrayAdapter<String> spinner_adapter=new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, data);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //스피너의 어댑터 지정
        county_spinner.setAdapter(spinner_adapter);
    }

    @Override
    public void dismiss() {
        city_data = null;
        county_data = null;
        super.dismiss();
    }
}
