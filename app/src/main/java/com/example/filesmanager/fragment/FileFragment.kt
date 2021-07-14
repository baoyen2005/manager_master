package com.example.filesmanager.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.FileAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.MainActivity
import com.example.filesmanager.utils.FileExtractUtils
import com.example.filesmanager.utils.FileOpen
import com.example.filesmanager.utils.FileShare
import com.example.filesmanager.utils.FindInformation
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class FileFragment : Fragment(), FileAdapter.OnItemClickListener ,Toolbar.OnMenuItemClickListener{

    private val fileList = ArrayList<File>()
    lateinit var fileAdapter: FileAdapter
    private var filePath: String? = null
    private var fileClick : File? = null
    private var stFileClick = Stack<String>()
    lateinit var tvInformation :TextView
    lateinit var drawerLayoutFile:DrawerLayout
    lateinit var toolbar : Toolbar
    private var title :String = "Search"
    private var icon: Int = R.drawable.ic_baseline_search_24
    lateinit var  linearSearch :LinearLayout
    lateinit var searchView:SearchView
    lateinit var tvDelete :TextView
    lateinit var txtSave :TextView
    private var isEnable :Boolean = false
    lateinit var recyclerView:RecyclerView
    private var isList :Boolean = false
    private var listBackGrid = false
    lateinit var imgSearch:ImageView
    private var nameSongs = ArrayList<String>()
    private var musics = ArrayList<File>()
    private lateinit var musicAdapter : ArrayAdapter<String>
    private var check = 1
    private var arrayListCopy = ArrayList<File>()
    fun newInstance(): FileFragment {
        return FileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
       // fileAdapter.isList = false
        isList = true
        listBackGrid = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file, container, false)
        drawerLayoutFile = (requireActivity() as MainActivity).drawer!!
        tvInformation = (requireActivity() as MainActivity).txtInform!!

         searchView = view.findViewById(R.id.searchView)
        imgSearch = view.findViewById(R.id.imgSearch)
        // tvDelete = view.findViewById(R.id.tvDelete)
        txtSave = view.findViewById(R.id.txtSave)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycle_internal)

        Log.d("aaaaaa",tvInformation.text.toString())


        toolbar = view.findViewById<Toolbar>(R.id.toolbar_menu)
        toolbar.setOnMenuItemClickListener(this)

        imgSearch.setOnClickListener{
            searchView.isIconified = false
//            searchView.requestFocusFromTouch();

            txtSave.visibility = View.INVISIBLE
            imgSearch.visibility = View.INVISIBLE
            searchView.visibility =View.VISIBLE
            searchView.setOnQueryTextListener (object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    TODO("Not yet implemented")
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    var text:String  = newText.toString()
                    var file :ArrayList<File> = filter(text)
                    fileAdapter.updateData(file)
                    return  false
                }
            })
        }
        searchView.setOnCloseListener {
            searchView.isIconified = false
            txtSave.visibility = View.VISIBLE
            imgSearch.visibility = View.VISIBLE
            searchView.visibility =View.INVISIBLE
            fileAdapter.updateData(fileList)
            return@setOnCloseListener false
        }
        return view
    }
    private fun filter(charSequence: String) :ArrayList<File>{
        arrayListCopy.clear()
        arrayListCopy.addAll(fileList)
        Log.d("noti", arrayListCopy.toString())
        var tempArraylist = ArrayList<File>()
        tempArraylist.clear()
        var checkNull = false
        if (charSequence.isNotEmpty()) {
            for (file in fileList) {
                if (file.name.lowercase(Locale.getDefault()).contains(charSequence)) {
                    tempArraylist.add(file)
                    checkNull = true
                }
            }
        } else {
            tempArraylist.addAll(fileList)
        }
        arrayListCopy.clear()

        arrayListCopy.addAll(tempArraylist)
        tempArraylist.clear()
        return arrayListCopy
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        filePath = bundle?.getString("path")
        setUpRecyclerView()
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

    private fun findFiles(root: File): ArrayList<File> {
        Log.d("aaa", "findFiles: "+root.absolutePath)
        val arrayList = ArrayList<File>()
        val files = root.listFiles()
        if(root.absolutePath == Environment.getExternalStorageDirectory().toString()){
            if (files != null  ) {
                arrayList.clear()
                for(fi in files ){
                    if(!fi.isHidden&& fi.isDirectory)
                        arrayList.add(fi)
                }
            }
        }
        else{
            if (files != null  ) {
                arrayList.clear()
                //arrayList.addAll(files)
                for(fi in files){
                    if(!fi.isHidden)
                        arrayList.add(fi)
                }
            }
        }
        Log.d("aaa", "findFiles: " + arrayList.size)

        return arrayList


    }

    private fun displayFiles() {


        val DIR_INTERNAL = Environment.getExternalStorageDirectory().toString()
        val storage = File(DIR_INTERNAL)
            fileList.clear()
            fileList.addAll(findFiles(storage))
        arrayListCopy.addAll(fileList)
        fileAdapter.updateData(fileList)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_menu, menu)
      //  var menu1: Menu = toolbar.menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(file: File, position: Int) {
        stFileClick.add(file.absolutePath)

        Log.d("yen",stFileClick.toString()+ "   "+file.name+"  "+ file.absolutePath)
       if (file.isDirectory){
           val arrayList = ArrayList<File>()
           val files = file.listFiles()
           if (files != null ) {
               arrayList.clear()
               arrayList.addAll(files)
           }

           fileList.clear()
           fileList.addAll(findFiles(File(file.absolutePath)))
           if(isList && listBackGrid && check == 1) fileAdapter.notifyDataSetChanged()
           else{
               isList = true
               listBackGrid = true
               check = 1
               recyclerView.layoutManager =
                   LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
               fileAdapter = FileAdapter(check,listBackGrid,isList,requireContext(), fileList, this)
               recyclerView.adapter = fileAdapter
               fileAdapter.updateData(fileList)
           }
       }

        else{
           //stFileClick.add(file.absolutePath)
           try {
               var fileOpen = FileOpen()
               fileOpen.openFile(requireContext(),file)
           } catch (e: IOException) {
               e.printStackTrace()
           }
       }
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
            else if (item.itemId == R.id.ic_press) {
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

    @SuppressLint("SetTextI18n")
//    private fun findInformation(file: File, position:Int) {
//      val find = FindInformation(file)
//        tvInformation.text = find.findInfor()
//    }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                @SuppressLint("WrongConstant")
                override fun handleOnBackPressed() {
                  if(stFileClick.size > 1 ){
                      fileList.clear()
                      stFileClick.pop()
                      fileList.addAll(findFiles(File(stFileClick[stFileClick.size-1])))
                      Log.d("yen","\n"+fileList.toString())
                  }
                  else if(stFileClick.size == 1){
                      fileList.clear()
                      fileList.addAll(findFiles(File(Environment.getExternalStorageDirectory().toString())))
                      stFileClick.pop()
                  }
                  else if(drawerLayoutFile.isDrawerOpen(Gravity.RIGHT)){
                            drawerLayoutFile.closeDrawer(Gravity.RIGHT)
                        }
                  else{
                      activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                      (context as Activity).finish()
                  }

                   //fileAdapter.notifyDataSetChanged()
                    setUpRecyclerViewAdapter()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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