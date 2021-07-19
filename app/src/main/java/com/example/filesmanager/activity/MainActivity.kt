package com.example.filesmanager.activity

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import com.xuandq.rate.ProxRateDialog
import com.xuandq.rate.RatingDialogListener
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var adapter: ViewPagerAdapter
    var drawer: DrawerLayout? = null
    var txtInform: TextView? = null
    lateinit var viewPager: ViewPager
    lateinit var bottomNavigationView: BottomNavigationView
    private var fileFrag = FileFragment()
    private var toolFrag = ToolFragment()
    private var cleanFrag = CleanFragment()
    var sdCard: String? = null
    lateinit var share: SharedPreferences
    lateinit var late :SharedPreferences
    lateinit var navigationViewStart: NavigationView
    lateinit var shareEdit: SharedPreferences.Editor
    lateinit var lateRate:SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        share = getSharedPreferences("ck", MODE_PRIVATE)
        shareEdit = share.edit()
        late = getSharedPreferences("ckccc", MODE_PRIVATE)
        lateRate = late.edit()
        //  Toast.makeText(this, "share "+ share.getBoolean("check", false), Toast.LENGTH_SHORT).show()

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
                PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Toast.makeText(
                        this, "\n" +
                                "Waiting for access permission", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(permission.WRITE_EXTERNAL_STORAGE),
                        PackageManager.PERMISSION_GRANTED
                    )
                }
            }
        } else {
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
                    fileFrag.displayFiles()
                    true
                }
                R.id.ic_tool -> {
                    toolFrag.displayImage()
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

        if(viewPager.currentItem == 0){

            if(fileFrag.stFileClick.size > 1 ){
                Log.d("yen","main + click 1 before pop "+ "   "+ fileFrag.stFileClick)
                fileFrag.stFileClick.pop()
                Log.d("yen","main + click 1 after pop"+ "   "+ fileFrag.stFileClick)
                fileFrag.fileList.addAll(fileFrag.findFiles(File(fileFrag.stFileClick[fileFrag.stFileClick.size-1])))
                fileFrag.fileAdapter.updateData(fileFrag.fileList)

                Log.d("yen","main + click 1 list"+ "   "+ fileFrag.fileList)

            }
            else if(fileFrag.stFileClick.size ==1){
                Log.d("yen","main + click 3"+ "   "+ fileFrag.stFileClick)
                fileFrag.fileList.addAll(fileFrag.findFiles(File(fileFrag.stFileClick[fileFrag.stFileClick.size-1])))
                fileFrag.fileAdapter.updateData(fileFrag.fileList)
                fileFrag.stFileClick.pop()
                Log.d("yen","main + click 3 list"+ "   "+ fileFrag.fileList)
            } else {
                Log.d("yen","main + click 4")
                if (!share.getBoolean("check", false)) {
                    val config = ProxRateDialog.Config()
                    config.setListener(object : RatingDialogListener {
                        override fun onSubmitButtonClicked(rate: Int, comment: String?) {
                            shareEdit.putBoolean("check", true)
                            shareEdit.apply()
                            finish()
                        }

                        override fun onLaterButtonClicked() {
                            lateRate.putBoolean("late",false)
                            lateRate.apply()
                            fileFrag.displayFiles()
                        }

                        override fun onChangeStar(rate: Int) {
                            if (rate >= 4) {
                                shareEdit.putBoolean("check", true)
                                shareEdit.apply()
                                finish()

                            }
                        }
                    })
                    ProxRateDialog.init(this, config)
                    ProxRateDialog.showIfNeed(supportFragmentManager)
                }
                else  super.onBackPressed()
                if(!late.getBoolean("late",false)){
                        val config = ProxRateDialog.Config()
                        config.setListener(object : RatingDialogListener {
                            override fun onSubmitButtonClicked(rate: Int, comment: String?) {
                                shareEdit.putBoolean("check", true)
                                shareEdit.apply()
                                finish()
                            }

                            override fun onLaterButtonClicked() {
                                lateRate.putBoolean("late",false)
                                lateRate.apply()
                                fileFrag.displayFiles()
                            }

                            override fun onChangeStar(rate: Int) {
                                if (rate >= 4) {
                                    shareEdit.putBoolean("check", true)
                                    shareEdit.apply()
                                    finish()

                                }
                            }
                        })
                        ProxRateDialog.init(this, config)
                        ProxRateDialog.showIfNeed(supportFragmentManager)
                    }


            }
            fileFrag.setUpRecyclerViewAdapter()

        }
        else if(viewPager.currentItem> 0){
            viewPager.currentItem =0
            fileFrag.displayFiles()
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
            R.id.cleanPhone -> {
                viewPager.currentItem = 2
            }
            R.id.tool -> {
                viewPager.currentItem = 1
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