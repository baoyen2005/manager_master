package com.example.filesmanager.activity

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
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
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var adapter: ViewPagerAdapter
    var drawer: DrawerLayout? = null
    var txtInform: TextView? = null
    lateinit var viewPager: ViewPager
    lateinit var bottomNavigationView: BottomNavigationView
    private var fileFrag = FileFragment()
    private var toolFrag = ToolFragment()
    private var cleanFrag = CleanFragment()
    var sdCard: String? = null
    lateinit var navigationViewStart: NavigationView
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
        navigationViewStart = findViewById(R.id.navigationViewStart)
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(fileFrag, "File Manager")
        adapter.addFragment(toolFrag, "Tool")
        adapter.addFragment(cleanFrag, "Clean")
        viewPager.adapter = adapter
        navigationViewStart.setNavigationItemSelectedListener(this)


        requestPermission()
        setFragment()

    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, permission.READ_EXTERNAL_STORAGE)
                ) {
                    Toast.makeText(this, "\n" +
                            "Waiting for access permission", Toast.LENGTH_SHORT).show()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(permission.WRITE_EXTERNAL_STORAGE),
                        PackageManager.PERMISSION_GRANTED
                    )
                }
            }
        }
        else {
            fileFrag.displayFiles()
            toolFrag.displayImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fileFrag.displayFiles()
        toolFrag.displayImage()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setFragment() {
        var fragment: Fragment? = null
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigationView.menu.findItem(R.id.ic_file).isChecked = true
                        return
                    }
                    1 -> {
                        bottomNavigationView.menu.findItem(R.id.ic_tool).isChecked = true
                        return
                    }
                    2 -> {
                        bottomNavigationView.menu.findItem(R.id.ic_cleanup).isChecked = true
                        return
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.ic_file -> {
                    viewPager.currentItem = 0
                    fragment = fileFrag
                    true
                }
                R.id.ic_tool -> {
                    viewPager.currentItem = 1
                    fragment = toolFrag
                    true

                }
                R.id.ic_cleanup -> {
                    fragment = cleanFrag
                    viewPager.currentItem = 2
                    true
                }

                else -> {
                    false
                }

            }

        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("WrongConstant")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home -> {
                viewPager.currentItem = 0
            }
            R.id.luutru_noi_bo -> {

                viewPager.currentItem = 0

            }
            R.id.luutr_dien_thoai -> {
                viewPager.currentItem = 0

            }
            R.id.cleanPhone ->{
                viewPager.currentItem = 2
            }
            R.id.tool -> {
                viewPager.currentItem= 1
            }
            R.id.setting -> {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(intent)

            }
            R.id.photo -> {
                var bundle = Bundle()
                bundle.putString("anh", "ảnh chi tiết")
                Log.d("anh", "anh1 " + bundle.getString("anh"))
                var intent = Intent(this, PhotoActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.video -> {
                var bundle = Bundle()
                bundle.putString("anh", "video chi tiết")
                Log.d("anh", "anh1 " + bundle.getString("anh"))
                var intent = Intent(this, PhotoActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.music -> {
                var bundle = Bundle()
                bundle.putString("anh", "music chi tiết")
                Log.d("anh", "anh1 " + bundle.getString("anh"))
                var intent = Intent(this, PhotoActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.document -> {
                var bundle = Bundle()
                bundle.putString("anh", "tài liệu chi tiết")
                Log.d("anh", "anh1 " + bundle.getString("anh"))
                var intent = Intent(this, PhotoActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.apk -> {
                var bundle = Bundle()
                bundle.putString("anh", "apk")
                Log.d("anh", "anh1 " + bundle.getString("anh"))
                var intent = Intent(this, PhotoActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        drawer?.closeDrawer(Gravity.START)
        return true

    }
}