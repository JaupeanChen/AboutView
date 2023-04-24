package com.example.customdrugview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class NestScrollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_scroll)
        Log.d("CheckBool", "result: ${checkBool()}")
    }

    private fun checkBool(): Boolean {
        boolB()
        return true
    }

    private fun boolB(): Boolean {
        return false
    }

}