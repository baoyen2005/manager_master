package com.example.filesmanager.activity

import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.filesmanager.Adapter.ViewPagerAdapter
import com.example.filesmanager.R
import com.example.filesmanager.fragment.CleanFragment
import com.example.filesmanager.fragment.FileFragment
import com.example.filesmanager.fragment.ToolFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ViewPagerAdapter
    var drawer : DrawerLayout? = null
    var txtInform :TextView ? = null
    lateinit var viewPager:ViewPager
    lateinit var bottomNavigationView: BottomNavigationView
    private var fileFrag = FileFragment()
    private var toolFrag = ToolFragment()
    private var cleanFrag = CleanFragment()
    var sdCard :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer = findViewById<DrawerLayout>(R.id.drawerLayoutFile)
        txtInform = findViewById(R.id.txt_infomation)

        val home = CleanFragment()
        val tool = ToolFragment()
        val file = FileFragment()
        fileFrag = file.newInstance()
        toolFrag = tool.newInstance()
        cleanFrag = home.newInstance()

        viewPager = findViewById<ViewPager>(R.id.view_pager)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.botton_navigation)
        adapter  = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(cleanFrag, "Làm")
        adapter.addFragment(toolFrag, "Công cụ")
        adapter.addFragment(fileFrag, "Quản lý tập tin")
        viewPager.adapter = adapter



        requestPermission()
        setFragment()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Toast.makeText(this, "Chờ cấp quyền truy cập", Toast.LENGTH_SHORT).show()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(permission.WRITE_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        toolFrag.displayImage()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setFragment() {
        var fragment : Fragment? = null
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
                    fragment = cleanFrag
                    true
                }
                R.id.ic_tool  ->{
                    viewPager.currentItem = 1
                    fragment = toolFrag
                    true

                }
                R.id.ic_file  -> {
                    fragment = fileFrag
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