package com.example.filesmanager.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.FileAdapter
import com.example.filesmanager.activity.PlayerMusicActivity
import com.example.filesmanager.R
import com.example.filesmanager.activity.MainActivity
import com.example.filesmanager.utils.FileOpen
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
    lateinit var linearSave :TextView
    private var isEnable :Boolean = false
    lateinit var recyclerView:RecyclerView
    private var isList :Boolean = false
    private var listBackGrid = false
    private var nameSongs = ArrayList<String>()
    private var musics = ArrayList<File>()
    private lateinit var musicAdapter : ArrayAdapter<String>

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

         linearSearch = view.findViewById(R.id.linearSearch)
         searchView = view.findViewById(R.id.searchView)
         tvDelete = view.findViewById(R.id.tvDelete)
        linearSave = view.findViewById(R.id.txtSave)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycle_internal)

        Log.d("aaaaaa",tvInformation.text.toString())


        toolbar = view.findViewById<Toolbar>(R.id.toolbar_menu)
        toolbar.setOnMenuItemClickListener(this)

        //setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        filePath = bundle?.getString("path")
        setUpRecyclerView()

        checkPermission()

    }

    private fun setUpRecyclerView() {


        if(isList&& listBackGrid ){
            recyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            fileAdapter = FileAdapter(listBackGrid,isList,requireContext(), fileList, this)
            recyclerView?.adapter = fileAdapter
        }
        else{
            recyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            fileAdapter = FileAdapter(listBackGrid,isList,requireContext(), fileList, this)
            recyclerView?.adapter = fileAdapter
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity, READ_EXTERNAL_STORAGE
                    )
                ) {
                    Toast.makeText(context, "Chờ cấp quyền truy cập", Toast.LENGTH_SHORT).show()
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity, arrayOf(
                            READ_EXTERNAL_STORAGE,
                            WRITE_EXTERNAL_STORAGE
                        ), 305
                    )
                }
            } else {
                displayFiles()
            }


        } else {
            displayFiles()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 305) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayFiles()
            } else {
                Toast.makeText(context, "permission not run", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findFiles(root: File): ArrayList<File> {
        Log.d("aaa", "findFiles: "+root.absolutePath)
        val arrayList = ArrayList<File>()
        val files = root.listFiles()
        if(root.absolutePath == Environment.getExternalStorageDirectory().toString()){
            if (files != null  ) {
                arrayList.clear()
                //arrayList.addAll(files)
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
    private fun findMusicFiles(file: File): ArrayList<File> {

        val arrayListMusic = ArrayList<File>()
        val files = file.listFiles()


        arrayListMusic.clear()
         for(fi in files ){
              if(fi.isHidden&& !fi.isDirectory)
                  arrayListMusic.addAll(findMusicFiles(fi))

              else{
                  if(fi.name.endsWith(".mp3")||fi.name.endsWith(".mp4")||
                      fi.name.endsWith(".wav")){
                      arrayListMusic.add(fi)
                  }
              }
         }


        return arrayListMusic


    }
    private fun displayFiles() {


        val DIR_INTERNAL = Environment.getExternalStorageDirectory().toString()
        val storage = File(DIR_INTERNAL)
            fileList.clear()
            fileList.addAll(findFiles(storage))

        fileAdapter.updateData(fileList)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_menu, menu)
        var menu1: Menu = toolbar.menu
        var menuItem :MenuItem = menu1.findItem(R.id.search)
        if(isEnable){
            isEnable= false
            menuItem.title = "Delete"
            menuItem.icon = resources.getDrawable(R.drawable.ic_baseline_clear_24)
        }
        else {
            isEnable = true
            menuItem.title = "Search"
            menuItem.icon = resources.getDrawable(R.drawable.ic_baseline_search_24)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(file: File,position: Int) {

       if (file.isDirectory){
           stFileClick.add(file.absolutePath)
           val arrayList = ArrayList<File>()
           val files = file.listFiles()
           if (files != null ) {
               arrayList.clear()
               arrayList.addAll(files)
           }

           fileList.clear()
           fileList.addAll(findFiles(File(file.absolutePath)))
           if(isList && listBackGrid) fileAdapter.notifyDataSetChanged()
           else{
               isList = true
               listBackGrid = true
               recyclerView?.layoutManager =
                   LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
               fileAdapter = FileAdapter(listBackGrid,isList,requireContext(), fileList, this)
               recyclerView?.adapter = fileAdapter
               fileAdapter.updateData(fileList)
           }
       }
        else if(file.name.endsWith(".mp3")||file.name.endsWith(".mp4")||
                   file.name.endsWith(".wav")){
            musics.clear()

           musics = findMusicFiles(Environment.getExternalStorageDirectory())
            for(i in 0 .. musics.size){
                nameSongs[i] = musics[i].name
            }
          // musicAdapter = ArrayAdapter(requireContext(),R.layout.activity_player_music)
            var intent : Intent=  Intent(context, PlayerMusicActivity::class.java)
                .putExtra("songsList",musics)
                .putExtra("position",position)
            startActivity(intent)

       }
        else{
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
                findInformation(file,position)

                Toast.makeText(context, "Item 1", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true


            } else if (item.itemId == R.id.ic_mark) {
                Toast.makeText(requireContext(), "Item 2", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true

            } else if (item.itemId == R.id.ic_rename) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Rename")

                var editText :EditText = EditText(context)
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




                Toast.makeText(context, "doi ten", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true
            }
            else if (item.itemId == R.id.ic_copy) {
                Toast.makeText(context, "sao chep", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true
            } else if (item.itemId == R.id.ic_cut) {
                Toast.makeText(requireContext(), "cut", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true
            } else if (item.itemId == R.id.ic_delete) {
                    dialogYesOrNo(requireContext(),"Delete","Bạn có chắc muốn xóa file không?",
                    DialogInterface.OnClickListener{dialog, id ->
                        val temp = fileList.remove(file)
                        fileAdapter.notifyDataSetChanged()
                    })
                return@OnMenuItemClickListener true
            }
            else if (item.itemId == R.id.ic_press) {
                Toast.makeText(context, "nens", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true
            } else if (item.itemId == R.id.ic_share) {
                Toast.makeText(requireContext(), "share", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true
            } else if (item.itemId == R.id.ic_bank) {
                Toast.makeText(context, "bank", Toast.LENGTH_SHORT).show()
                return@OnMenuItemClickListener true
            }
            false
        })
        popupMenu.inflate(R.menu.file_menu)

        popupMenu.show()
    }

    private fun findInformation(file: File,position:Int) {
        val size  = file.length()//Byte
        var sizeMB :Double = 0.toDouble() // Mb
        var sizeGB:Double = 0.toDouble() // GB
        if(size >= 1024){
            sizeMB = (size/1024).toDouble()

        }
        else if(size> 1024*1024){
            sizeGB = (sizeMB)/1024
        }
        if(file.isDirectory && size > 1024*1024 && fileAdapter.quanlityFile.get(position)!! >1 ){
            Log.d("yen", fileAdapter.quanlityFile.toString())
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeGB Gb"+
                    "\nNội dung: ${fileAdapter.quanlityFile.get(position)} files"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(file.isDirectory && size > 1024*1024 && fileAdapter.quanlityFile.get(position)!! == 1 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeGB Gb"+
                    "\nNội dung: ${fileAdapter.quanlityFile.get(position)} file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(file.isDirectory && size > 1024*1024 && fileAdapter.quanlityFile.get(position)!! == 0 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeGB Gb"+
                    "\nNội dung: 0 file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified.get(position)}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && 1024 < size && size< 1024*1024&& fileAdapter.quanlityFile.get(position)!!>1 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeMB Mb"+
                    "\nNội dung: ${fileAdapter.quanlityFile.get(position)!!} files"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && 1024 < size && size< 1024*1024 && fileAdapter.quanlityFile.get(position)==1 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeMB Mb"+
                    "\nNội dung: ${fileAdapter.quanlityFile[position]} file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"
        }
        else  if(file.isDirectory && 1024 < size && size< 1024*1024 && fileAdapter.quanlityFile.get(position)==0 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeMB Mb"+
                    "\nNội dung: 0 file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"
        }
        else  if(file.isDirectory && size<1024&& fileAdapter.quanlityFile.get(position)==1 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $size Kb"+
                    "\nNội dung: ${fileAdapter.quanlityFile[position]} file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && size<1024&& fileAdapter.quanlityFile.get(position)!!>1 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $size Kb"+
                    "\nNội dung: ${fileAdapter.quanlityFile[position]} files"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && size<1024&& fileAdapter.quanlityFile.get(position)==1 ){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $size Kb"+
                    "\nNội dung: 0 file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(!file.isDirectory&& size > 1024*1024){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: file"+
                    "\nKích thước: $sizeGB Gb"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(!file.isDirectory&&size>1024 && size < 1024*1024){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: file"+
                    "\nKích thước: $sizeMB Mb"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(!file.isDirectory&&size<1024){
            tvInformation.text ="${file.name}" +
                    "\n\nKiểu: file"+
                    "\nKích thước: $size Kb"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                @SuppressLint("WrongConstant")
                override fun handleOnBackPressed() {

                  if(stFileClick.size >1||drawerLayoutFile.isDrawerOpen(Gravity.RIGHT) ){

                      fileList.clear()

                      fileList.addAll(findFiles(File(stFileClick[stFileClick.size-1])))
                      stFileClick.pop()
                      drawerLayoutFile.closeDrawer(Gravity.RIGHT)
                  }
                  else if(stFileClick.size == 1){
                      fileList.clear()
                      fileList.addAll(findFiles(File(Environment.getExternalStorageDirectory().toString())))
                      stFileClick.pop()

                  }
                  else  if(drawerLayoutFile.isDrawerOpen(Gravity.RIGHT)){
                            drawerLayoutFile.closeDrawer(Gravity.RIGHT)
                        }
                  else{
                      activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                      (context as Activity).finish()
                  }

                    //fileAdapter.notifyDataSetChanged()
                    setUpRecyclerView()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.search ->{
                if(item?.title == "Delete" ){
                    item.icon = resources.getDrawable(R.drawable.sss)
                    item.title = "Search"
                    Toast.makeText(context,"${item.title}",Toast.LENGTH_SHORT).show()
                    linearSearch.visibility = View.INVISIBLE
                    linearSave.visibility = View.VISIBLE
                }
                else{
                    item.icon = resources.getDrawable(R.drawable.xxx)
                    item.title = "Delete"
                  //  Toast.makeText(context,"${item.title}",Toast.LENGTH_SHORT).show()
                    linearSearch.visibility = View.VISIBLE
                    linearSave.visibility = View.INVISIBLE
                    searchView.setOnQueryTextListener (object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            TODO("Not yet implemented")
                        }
                        override fun onQueryTextChange(newText: String?): Boolean {
                           var text:String  = newText.toString()
                            fileAdapter.filter(text)
                            return  false
                        }
                    })
                }
                return true

            }
            R.id.orderByName ->{

                var fileCopy = ArrayList<File>()
                fileCopy.addAll(fileList)
                fileCopy.sortBy{it.name}
                fileAdapter.updateData(fileCopy)
               // Toast.makeText(context,"Sap xep", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.orderBySize ->{
                var fileCopy = ArrayList<File>()
                fileCopy.addAll(fileList)
                fileCopy.sortByDescending{it.totalSpace}
                fileAdapter.updateData(fileCopy)
               // Toast.makeText(context,"Sap xep", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.orderByTime -> {
                var fileCopy = ArrayList<File>()
                fileCopy.addAll(fileList)
                fileCopy.sortByDescending{it.lastModified()}
                fileAdapter.updateData(fileCopy)
               // Toast.makeText(context,"Sap xep", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.viewerByList -> {
                isList = true
                listBackGrid = true
                recyclerView?.layoutManager =
                    LinearLayoutManager(context,  LinearLayoutManager.VERTICAL,false)
                fileAdapter = FileAdapter(listBackGrid,isList,requireContext(), fileList, this)
                recyclerView?.adapter = fileAdapter
                fileAdapter.updateData(fileList)
            }
            R.id.viewerByO -> {
                isList = false
                listBackGrid = false
                recyclerView?.layoutManager =
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,false)
               fileAdapter = FileAdapter(listBackGrid,isList,requireContext(), fileList, this)
                recyclerView?.adapter = fileAdapter
                fileAdapter.updateData(fileList)
            }
            R.id.createFile ->{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Create")
                var editText :EditText = EditText(context)
                editText.hint= "Tên File"
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
                editText.hint= "Tên Folder"
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