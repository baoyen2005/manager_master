package com.example.filesmanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.filesmanager.R
import com.example.filesmanager.fragment.CleanFragment
import com.example.filesmanager.fragment.ToolFragment
import com.example.filesmanager.Adapter.ViewPagerAdapter
import com.example.filesmanager.fragment.FileFragment
import com.example.filesmanager.fragment.FileManagerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ViewPagerAdapter
     var drawer : DrawerLayout? = null
    var txtInform :TextView ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED);
        drawer = findViewById<DrawerLayout>(R.id.drawerLayoutFile)
        txtInform = findViewById(R.id.txt_infomation)
        setFragment()
    }

    private fun setFragment() {
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.botton_navigation)
        adapter  = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(CleanFragment(), "Làm")
        adapter.addFragment(ToolFragment(), "Công cụ")
        adapter.addFragment(FileFragment(), "Quản lý tập tin")
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0-> {
                        bottomNavigationView.menu.findItem(R.id.ic_cleanup).isChecked = true
                        return
                    }
                    1-> {
                        bottomNavigationView.menu.findItem(R.id.ic_tool).isChecked = true
                        return
                    }
                    2-> {
                        bottomNavigationView.menu.findItem(R.id.ic_file).isChecked = true
                        return
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            return@setOnNavigationItemSelectedListener when (item.itemId){
                R.id.ic_cleanup  ->{
                    viewPager.currentItem= 0
                    true
                }
                R.id.ic_tool  ->{
                    viewPager.currentItem = 1
                    true

                }
                R.id.ic_file  -> {
                    viewPager.currentItem = 2
                    true
                }

                else -> { false }
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        else {
                super.onBackPressed()
        }
    }

}