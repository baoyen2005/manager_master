package com.example.filesmanager.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.filesmanager.R
import com.example.filesmanager.fragment.ImageFragment

class PhotoActivity : AppCompatActivity() {
    private var bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        bundle = intent.extras!!
        Log.d("anh", "anh "+ bundle.getString("anh"))
        if(bundle.getString("anh") == "Hình ảnh"){
            this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, ImageFragment())
                .commit()
        }
    }
}