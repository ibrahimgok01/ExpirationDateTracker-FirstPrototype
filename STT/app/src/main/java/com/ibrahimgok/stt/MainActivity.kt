package com.ibrahimgok.stt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun button(view: View){
        val intent = Intent(applicationContext,qrCode::class.java)
        startActivity(intent)
    }

    fun button2(view: View){
        val intent = Intent(applicationContext,Urunler::class.java)
        startActivity(intent)
    }
}