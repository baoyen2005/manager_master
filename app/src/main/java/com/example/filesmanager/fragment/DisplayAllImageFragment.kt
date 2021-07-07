package com.example.filesmanager.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.RecentlyImageAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.PhotoActivity
import com.example.filesmanager.utils.FileOpen
import com.example.filesmanager.utils.FileShare
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class DisplayAllImageFragment() : Fragment(), RecentlyImageAdapter.OnItemClickListenerTool {
    lateinit var mRecycleAnh: RecyclerView
    private var listAnh = ArrayList<File>()
    private var isList = false
    private var  bundle : String =""
    lateinit var adapter :RecentlyImageAdapter
    private var path :String =""
    private var stack = Stack<String>()
    private var ok = false
    lateinit var drawerLayoutFile: DrawerLayout
    lateinit var tvInformation: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("yn", "onCreate: ")
        arguments?.let {
            bundle = it.getString("anh").toString()
            path = it.getString("path").toString()
            Log.d("yn", "bundle: "+ bundle)
            Log.d("yn", "path: "+ path)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_display_all_image, container, false)
        mRecycleAnh = view.findViewById(R.id.mRecycleAnh)
        drawerLayoutFile = (requireActivity() as PhotoActivity).drawerPhoto!!
        tvInformation = (requireActivity() as PhotoActivity).txtInformPhoto!!
        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerViewType()
        displayImg()
    }
    private fun setUpRecyclerViewType() {
        if(!isList){
            adapter= RecentlyImageAdapter("anh",isList,requireContext(),listAnh,this)
            mRecycleAnh.layoutManager =
                GridLayoutManager(context,2)
            mRecycleAnh.adapter = adapter
        }
    }
    private fun findFileImage(file: File): ArrayList<File> {
        var arrayList = ArrayList<File>()
        val files = file.listFiles()


        for (singleImg in files) {
            if (!singleImg.isHidden && singleImg.isDirectory) {
                arrayList.addAll(findFileImage(singleImg))
            } else {
                if (singleImg.name.endsWith("png") ||
                    singleImg.name.endsWith("jpg") ||
                    singleImg.name.endsWith("jpeg")
                ) {
                    arrayList.add(singleImg)
                }
            }
        }

        return arrayList
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun displayImg(){
        listAnh.clear()
        val txtanh =  (activity as PhotoActivity).txtAnh.text.toString()


            listAnh.addAll(findFileImage(File(path)))


        Log.d("yenn", "listImgRecently: "+listAnh.size)
        adapter.updateDataTool(listAnh)
    }
    override fun onItemClickTool(file: File, position: Int) {
        ok = true
        if (file.isDirectory){
            val arrayList = ArrayList<File>()
            arrayList.clear()
            arrayList.addAll(findFileImage(File(file.absolutePath)))
            adapter.updateDataTool(arrayList)
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.file_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsMenuClickedTool(view: View, file: File, position: Int) {
        val popupMenu = PopupMenu(context,view)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.ic_information) {
                drawerLayoutFile.openDrawer(Gravity.RIGHT)
                findInformation(file,position)
                return@OnMenuItemClickListener true


            }
            else if (item.itemId == R.id.ic_rename) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Rename")

                var editText : EditText = EditText(context)
                editText.setText(file.name.toString())
                builder.setView(editText)
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->

                    var newName:String = editText.text.toString().trim()
                    if(!newName.isEmpty()){
                        var current :File = File(file.absolutePath)
                        var destination :File = File(file.absolutePath.replace(file.name,newName))
                        if(current.renameTo(destination)){
                            listAnh.set(position,destination)
                            adapter.notifyDataSetChanged()
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
                dialogYesOrNo(requireContext(),"Delete","Bạn có chắc muốn xóa file không?",
                    DialogInterface.OnClickListener{ dialog, id ->
                        val temp = listAnh.remove(file)
                        adapter.notifyDataSetChanged()
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

    private fun findInformation(file: File,position:Int) {
        val size = file.length()//Byte
        var sizeMB: Double = 0.toDouble() // Mb
        var sizeGB: Double = 0.toDouble() // GB
        if (size >= 1024) {
            sizeMB = (size / 1024).toDouble()

        } else if (size > 1024 * 1024) {
            sizeGB = (sizeMB) / 1024
        }
        if (file.isDirectory && size > 1024 * 1024 && adapter.quanlityFile.get(position)!! > 1) {
            Log.d("yen", adapter.quanlityFile.toString())
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $sizeGB Gb" +
                    "\nNội dung: ${adapter.quanlityFile.get(position)} files" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (file.isDirectory && size > 1024 * 1024 && adapter.quanlityFile.get(position)!! == 1) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $sizeGB Gb" +
                    "\nNội dung: ${adapter.quanlityFile.get(position)} file" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (file.isDirectory && size > 1024 * 1024 && adapter.quanlityFile.get(position)!! == 0) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $sizeGB Gb" +
                    "\nNội dung: 0 file" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified.get(position)}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (file.isDirectory && 1024 < size && size < 1024 * 1024 && adapter.quanlityFile.get(
                position
            )!! > 1
        ) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $sizeMB Mb" +
                    "\nNội dung: ${adapter.quanlityFile.get(position)!!} files" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (file.isDirectory && 1024 < size && size < 1024 * 1024 && adapter.quanlityFile.get(
                position
            ) == 1
        ) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $sizeMB Mb" +
                    "\nNội dung: ${adapter.quanlityFile[position]} file" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"
        } else if (file.isDirectory && 1024 < size && size < 1024 * 1024 && adapter.quanlityFile.get(
                position
            ) == 0
        ) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $sizeMB Mb" +
                    "\nNội dung: 0 file" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"
        } else if (file.isDirectory && size < 1024 && adapter.quanlityFile.get(position) == 1) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $size Kb" +
                    "\nNội dung: ${adapter.quanlityFile[position]} file" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (file.isDirectory && size < 1024 && adapter.quanlityFile.get(position)!! > 1) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $size Kb" +
                    "\nNội dung: ${adapter.quanlityFile[position]} files" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (file.isDirectory && size < 1024 && adapter.quanlityFile.get(position) == 1) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: folder" +
                    "\nKích thước: $size Kb" +
                    "\nNội dung: 0 file" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (!file.isDirectory && size > 1024 * 1024) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: file" +
                    "\nKích thước: $sizeGB Gb" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (!file.isDirectory && size > 1024 && size < 1024 * 1024) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: file" +
                    "\nKích thước: $sizeMB Mb" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"

        } else if (!file.isDirectory && size < 1024) {
            tvInformation.text = "${file.name}" +
                    "\n\nKiểu: file" +
                    "\nKích thước: $size Kb" +
                    "\nSửa đổi lần cuối: ${adapter.lastModified[position]}" +
                    "\nQuy trình: ${file.absolutePath}"
        }
    }
    fun dialogYesOrNo(context: Context, title: String, message: String, listener: DialogInterface.OnClickListener
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
        Log.d("sssyen", "onAttach: ")
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                @SuppressLint("WrongConstant")
                override fun handleOnBackPressed() {
                    Log.d("sssyen", "handleOnBackPressed: "+ ok.toString())
                    adapter.updateDataTool(listAnh)

                    (activity as PhotoActivity).txtAnh.text = "Hình ảnh"
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, ImageFragment())
                        .commit()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    companion object {

        fun newInstance(param1: String,s :String ) =
            DisplayAllImageFragment().apply {
                arguments = Bundle().apply {
                    putString("anh", param1)
                    putString("path",s)
                }
            }

    }
}