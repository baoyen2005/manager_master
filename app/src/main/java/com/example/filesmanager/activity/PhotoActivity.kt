package com.example.filesmanager.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.filesmanager.R
import com.example.filesmanager.fragment.DocumentFragment
import com.example.filesmanager.fragment.ImageFragment
import com.example.filesmanager.fragment.VideoFragment

class PhotoActivity : AppCompatActivity() {
    private var bundle = Bundle()
    lateinit var txtAnh :TextView
    var drawerPhoto : DrawerLayout? = null
    var txtInformPhoto :TextView ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        txtAnh = findViewById(R.id.txtHinhAnh)
        drawerPhoto = findViewById<DrawerLayout>(R.id.drawerLayout)
        txtInformPhoto = findViewById(R.id.txt_infomationphot)
        bundle = intent.extras!!
        Log.d("anh", "anh "+ bundle.getString("anh"))
        if(bundle.getString("anh") == "Hình ảnh"){
            this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, ImageFragment())
                .commit()
        }
        else if(bundle.getString("anh") == "Video"){
            this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, VideoFragment(bundle.getString("anh")!!))
                .commit()
        }
        else if(bundle.getString("anh") == "Tài liệu"){
            this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, DocumentFragment())
                .commit()
        }
        else{
            this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, VideoFragment(bundle.getString("anh")!!))
                .commit()
        }
    }
}