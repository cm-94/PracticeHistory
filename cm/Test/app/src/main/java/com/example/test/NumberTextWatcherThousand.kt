package com.example.test

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

class NumberTextWatcherThousand(var editText: EditText) : TextWatcher {
    private var bEdit = true

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(view: Editable) {
        if (bEdit) {
            bEdit = false;
            return;
        }
        bEdit = true
        var str: String? = null
        var a = 0L
        if (view.toString().isNotEmpty()) {
            a = view.toString().replace(",", "").toLong()
        }

        str = String.format("%,d", a)
        Log.d("Test_Str", "a:  ${a.toString()}, a.len:  ${a.toString().length}")
        Log.d("Test_Str", "str : $str")
        editText.setText(str)
        editText.text?.length?.let { editText.setSelection(it) };


        // Set s back to the view after temporarily removing the text change listener

    }
}