package com.example.filesmanager.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ads.control.Admod
import com.ads.control.funtion.AdCallback
import com.example.filesmanager.Adapter.FileAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.MainActivity
import com.example.filesmanager.utils.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class FileFragment : Fragment(), FileAdapter.OnItemClickListener ,Toolbar.OnMenuItemClickListener{

     val fileList = ArrayList<File>()
    lateinit var fileAdapter: FileAdapter
    private var filePath: String? = null
    private var fileClick : File? = null
    var stFileClick = Stack<String>()
    lateinit var tvInformation :TextView
    lateinit var drawerLayoutFile:DrawerLayout
    lateinit var toolbar : Toolbar
    private var title :String = "Search"
    private var icon: Int = R.drawable.ic_baseline_search_24
    lateinit var  linearSearch :LinearLayout
    lateinit var searchView:androidx.appcompat.widget.SearchView
    lateinit var tvDelete :TextView
    lateinit var txtSave :TextView
    private var isEnable :Boolean = false
    lateinit var recyclerView:RecyclerView
    private var isList :Boolean = false
    private var listBackGrid = false
     lateinit var imgSearch:ImageView

    private var check = 1
    var isOpeningFile = false
    val PRODUCT_ID = "android.test.purchased"
     var frAds: FrameLayout? = null
    private val unifiedNativeAd: UnifiedNativeAd? = null
    private var arrayListCopy = ArrayList<File>()
    private var countToShowAds = 0
    private var countToShowFirebase = 0
    lateinit var firebaseAnalytics : FirebaseAnalytics
    lateinit var   mInterstitialAd :com.google.android.gms.ads.InterstitialAd
     var checkNativeds = false

    fun newInstance(): FileFragment {
        return FileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
       // fileAdapter.isList = false
        isList = true
        listBackGrid = true
        firebaseAnalytics = Firebase.analytics
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file, container, false)
        drawerLayoutFile = (requireActivity() as MainActivity).drawer!!
        tvInformation = (requireActivity() as MainActivity).txtInform!!

        searchView = view.findViewById(R.id.searchView)
        imgSearch = view.findViewById(R.id.imgSearch)
        txtSave = view.findViewById(R.id.txtSave)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycle_internal)
        frAds = view.findViewById<FrameLayout>(R.id.fr_ads)
        Log.d("aaaaaa", tvInformation.text.toString())
        toolbar = view.findViewById<Toolbar>(R.id.toolbar_menu)

        toolbar.setOnMenuItemClickListener(this)

        txtSave.visibility = View.VISIBLE
        imgSearch.visibility = View.VISIBLE
        searchView.visibility = View.INVISIBLE
        searchView.setQuery("",false)
        searchView.clearFocus()
        imgSearch.setOnClickListener {
            searchView.isIconified = false

            txtSave.visibility = View.INVISIBLE
            imgSearch.visibility = View.INVISIBLE
            searchView.visibility = View.VISIBLE

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    if(query == null || query.isEmpty()){
                        fileAdapter.updateData(fileList)
                    }
                    else{
                        fileAdapter.filter.filter(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("filter", "onQueryTextChange: " + newText)
                    Log.d("filter", "onQueryTextChange: length= " + newText!!.length)
                    if(newText == null || newText.isEmpty()){
                        fileAdapter.updateData(fileList)
                    }
                    else{
                        fileAdapter.filter.filter(newText)
                    }
                    return false
                }
            })


            searchView.setOnCloseListener {
                searchView.isIconified = false
                txtSave.visibility = View.VISIBLE
                imgSearch.visibility = View.VISIBLE
                searchView.visibility = View.INVISIBLE
                fileAdapter.updateData(fileList)
                return@setOnCloseListener false
            }
        }
        return view
    }


    fun loadAds() {
        Admod.getInstance().loadUnifiedNativeAd(context,getString(R.string.id_native_top_list_file),object :AdCallback(){
            override fun onUnifiedNativeAdLoaded(unifiedNativeAd: UnifiedNativeAd?) {
                super.onUnifiedNativeAdLoaded(unifiedNativeAd)
                val adview = LayoutInflater.from(context).inflate(R.layout.custom_native,null)
                frAds!!.addView(adview)
                Admod.getInstance().populateUnifiedNativeAdView(unifiedNativeAd,
                    adview as UnifiedNativeAdView?
                )
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        filePath = bundle?.getString("path")
        setUpRecyclerView()
        txtSave.visibility = View.VISIBLE
        imgSearch.visibility = View.VISIBLE
        searchView.visibility = View.INVISIBLE
        searchView.setQuery("",false)
        searchView.clearFocus()
        displayFiles()

    }

    private fun setUpRecyclerView() {

        if(isList&& listBackGrid ){
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            fileAdapter = FileAdapter(check,listBackGrid,isList,requireContext(), fileList, this)
            recyclerView.adapter = fileAdapter
        }
        else{
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            fileAdapter = FileAdapter(check,listBackGrid,isList,requireContext(), fileList, this)
            recyclerView.adapter = fileAdapter
        }
    }

    fun findFiles(root: File): ArrayList<File> {
        Log.d("aaa", "findFiles: "+root.absolutePath)
        val arrayList = ArrayList<File>()
        val files = root.listFiles()
        if(root.absolutePath == Environment.getExternalStorageDirectory().toString()){
            if (files != null && !files.isEmpty() ) {
                arrayList.clear()
                for(fi in files ){
                    if(!fi.isHidden && fi.isDirectory)
                        arrayList.add(fi)
                }

            }
        }
        else{
            if (files != null && !files.isEmpty() ) {
                arrayList.clear()
                for(fi in files){
                    arrayList.add(fi)
                }
            }
        }
        Log.d("aaa", "findFiles: " + arrayList.size)
        return arrayList
    }

     fun displayFiles() {
        val DIR_INTERNAL = Environment.getExternalStorageDirectory().toString()
        val storage = File(DIR_INTERNAL)

         if (!stFileClick.contains(storage.absolutePath)) {
             stFileClick.add(storage.absolutePath)
         }
            fileList.clear()
            fileList.addAll(findFiles(storage))

         if (!stFileClick.contains(storage.absolutePath)) {
             stFileClick.add(storage.absolutePath)

         }
         checkNativeds = false
        arrayListCopy.addAll(fileList)
        fileAdapter.updateData(fileList)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(file: File, position: Int) {
        stFileClick.add(file.absolutePath) // luuw đường daanxx mỗi khi click 1 file or folder
        Log.d("yen",stFileClick.toString()+ "   "+file.name+"  "+ file.absolutePath)
       if (file.isDirectory){
           countToShowAds ++
           countToShowFirebase++

           loadAds()

           checkNativeds = true
           val arrayList = ArrayList<File>()
           val files = file.listFiles()
           if (files != null ) {
               arrayList.clear()
               arrayList.addAll(files)
           }
           fileList.clear()
           fileList.addAll(findFiles(File(file.absolutePath)))
            if(countToShowAds % 3==0){
              showInterstitialAds(fileList)
           }
           showListFile(fileList)
           Log.d("fire",countToShowFirebase.toString())
           firebaseAnalytics.logEvent("prox_rating_layout") {
               param("event_type", "click_Folder")
               param("click", countToShowFirebase.toString())
           }
       }
        else{
           stFileClick.pop() // click file thì xóa đường dẫn
           try {
               var fileOpen = FileOpen()
               fileOpen.openFile(requireContext(),file)
           } catch (e: IOException) {
               e.printStackTrace()
           }
       }
    }

    private fun showListFile(list: ArrayList<File>){
        if(isList && listBackGrid && check == 1) fileAdapter.notifyDataSetChanged()
        else{
            isList = true
            listBackGrid = true
            check = 1
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            fileAdapter = FileAdapter(check,listBackGrid,isList,requireContext(), list, this)
            recyclerView.adapter = fileAdapter
            fileAdapter.updateData(list)
        }
    }


    private fun showInterstitialAds(list : ArrayList<File>) {

        Admod.getInstance().forceShowInterstitial(context, InterstitialUtils.getInteeClickFIle(), object : AdCallback() {
            override fun onAdClosed() {
                showListFile(list)
            }
        })
        countToShowAds = 0
    }
    override fun onOptionsMenuClicked(view: View, file: File,position:Int) {
        val popupMenu = PopupMenu(context,view)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.ic_information) {
                drawerLayoutFile.openDrawer(Gravity.RIGHT)
                Log.d("size", "onOptionsMenuClicked: infor"+ file.name+ "\n"+ position)
                val find = FindInformation(file,position)
                tvInformation.text = find.findInfor()
                return@OnMenuItemClickListener true


            }
            else if (item.itemId == R.id.ic_rename) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Rename")
                var editText :EditText = EditText(context)
                editText.gravity = Gravity.START

                val params: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(50,0,0,0)
                editText.layoutParams = params

                editText.setText(file.name.toString())
                builder.setView(editText)
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->

                   var newName:String = editText.text.toString().trim()
                    if(!newName.isEmpty()){
                        var current :File = File(file.absolutePath)
                        var destination :File = File(file.absolutePath.replace(file.name,newName))
                        if(current.renameTo(destination)){
                            fileList.set(position,destination)
                            fileAdapter.notifyDataSetChanged()
                        }
                    }
                    dialog.dismiss()
                })

                builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->

                    dialog.dismiss()
                })
                var alertDialog : AlertDialog = builder.create()
                alertDialog.show()

                return@OnMenuItemClickListener true
            }
            else if (item.itemId == R.id.ic_delete) {
                    dialogYesOrNo(requireContext(),"Delete","You want to delete file?",
                    DialogInterface.OnClickListener{dialog, id ->
                        val temp = fileList.remove(file)
                        fileAdapter.notifyDataSetChanged()
                    })
                return@OnMenuItemClickListener true
            }

            else if (item.itemId == R.id.ic_share) {
                    val share  = FileShare()
                    share.shareFile(requireContext(),file)
                return@OnMenuItemClickListener true
            }
            false
        })
        popupMenu.inflate(R.menu.file_menu)

        popupMenu.show()
    }

    fun dialogYesOrNo(context: Context,  title: String, message: String, listener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            listener.onClick(dialog, id)
        })
        builder.setNegativeButton("No", null)
        val alert = builder.create()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()
    }


    fun setUpRecyclerViewAdapter(){
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fileAdapter = FileAdapter(1 ,true,true,requireContext(), fileList, this)
        recyclerView.adapter = fileAdapter
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){

            R.id.orderByName ->{

                var fileCopy = ArrayList<File>()
                fileCopy.addAll(fileList)
                fileCopy.sortBy{it.name.lowercase()}
                fileAdapter.updateData(fileCopy)
               // Toast.makeText(context,"Sap xep", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.orderBySize ->{
                var fileCopy = ArrayList<File>()
                fileCopy.addAll(fileList)
                var checkIsDirectory = false
                for (file in fileCopy){
                    if(!file.isDirectory){
                        checkIsDirectory = true
                    }
                }
                if(checkIsDirectory){
                    fileCopy.sortByDescending { FileExtractUtils.getFileSize(it) }
                }
                else fileCopy.sortByDescending { FileExtractUtils.getQuanlityFile(it)}
                fileAdapter.updateData(fileCopy)
                return true
            }
            R.id.orderByTime -> {
                var fileCopy = ArrayList<File>()
                fileCopy.addAll(fileList)
                fileCopy.sortByDescending{ FileExtractUtils.getFileLastModified(it)}
                fileAdapter.updateData(fileCopy)
               // Toast.makeText(context,"Sap xep", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.viewerByList -> {
                isList = true
                listBackGrid = true
                recyclerView.layoutManager =
                    LinearLayoutManager(context,  LinearLayoutManager.VERTICAL,false)
                fileAdapter = FileAdapter(1,listBackGrid,isList,requireContext(), fileList, this)
                recyclerView.adapter = fileAdapter
                fileAdapter.updateData(fileList)
            }
            R.id.viewerByO -> {
                isList = false
                listBackGrid = false
                recyclerView.layoutManager =
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,false)
               fileAdapter = FileAdapter(2,listBackGrid,isList,requireContext(), fileList, this)
                recyclerView.adapter = fileAdapter
                fileAdapter.updateData(fileList)
            }
            R.id.createFile ->{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Create")
                var editText :EditText = EditText(context)
                editText.hint= "File name"
                //editText.setText(file.name.toString())
                builder.setView(editText)
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->

                    var edt:String = editText.text.toString().trim()
                    val DIR_INTERNAL = Environment.getExternalStorageDirectory().toString()
                    val storage = File(DIR_INTERNAL)
                    val newFile = File(storage, edt)
                    fileList.add(newFile)
                    fileAdapter.notifyItemInserted(fileList.size-1)
                    
                    dialog.dismiss()
                })
                builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
                var alertDialog : AlertDialog = builder.create()
                alertDialog.show()
            }
            R.id.createFolder -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Create")
                var editText :EditText = EditText(context)
                editText.hint= "Folder Name"
                //editText.setText(file.name.toString())
                builder.setView(editText)
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->

                    var edt:String = editText.text.toString().trim()
                    val DIR_INTERNAL = Environment.getExternalStorageDirectory().toString()
                    val storage = File(DIR_INTERNAL)
                    val newFile = File(storage, edt)
                    fileList.add(newFile)
                    fileAdapter.notifyItemInserted(fileList.size-1)

                    dialog.dismiss()
                })
                builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
                var alertDialog : AlertDialog = builder.create()
                alertDialog.show()
            }
        }
        return false
    }
}