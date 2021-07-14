package com.example.filesmanager.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.FileAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.PhotoActivity
import com.example.filesmanager.utils.FileExtractUtils
import com.example.filesmanager.utils.FileOpen
import com.example.filesmanager.utils.FileShare
import com.example.filesmanager.utils.FindInformation
import java.io.File
import java.io.IOException

class DocumentFragment : Fragment(), FileAdapter.OnItemClickListener {
    lateinit var mGridViewTaiLieu: RecyclerView

    lateinit var adapter: FileAdapter
    var listFolderTaiLieu = ArrayList<File>()
    private var isList = false
    private var back = false
    lateinit var drawerLayoutFile: DrawerLayout
    lateinit var tvInformation: TextView
    lateinit var imgTransfer: ImageView
    lateinit var imgOrder: ImageView
    private var transferType = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerLayoutFile = (requireActivity() as PhotoActivity).drawerPhoto!!
        tvInformation = (requireActivity() as PhotoActivity).txtInformPhoto!!
        imgTransfer = (requireActivity() as PhotoActivity).imgGridToolBar!!
        imgOrder = (requireActivity() as PhotoActivity).imgOrderToolBar!!
        transferType = (requireActivity() as PhotoActivity).checkTransfer
        (activity as PhotoActivity).txtAnh.text = "Document"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_document, container, false)
        mGridViewTaiLieu = view.findViewById(R.id.mGridViewTaiLieu)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerViewType()
        displayTaiLieu()
        initTransfer()
    }
    private fun setUpRecyclerViewType() {
        if(!isList && !back){
            adapter= FileAdapter(3,back,isList,requireContext(),listFolderTaiLieu,this)
            mGridViewTaiLieu.layoutManager =
                GridLayoutManager(context,2)
            mGridViewTaiLieu.adapter = adapter
        }
    }
    private fun findFileTaiLieu(file: File): ArrayList<File> {
        var arrayList = ArrayList<File>()
        val files = file.listFiles()


        for (singleImg in files) {
            if (!singleImg.isHidden && singleImg.isDirectory) {
                arrayList.addAll(findFileTaiLieu(singleImg))
            } else {
                if (singleImg.extension =="docx"
                    ||singleImg.extension=="pdf"
                    || singleImg.extension=="pptx"
                    ||singleImg.extension=="doc"
                    ||singleImg.extension=="ppt") {
                    arrayList.add(singleImg)
                }
            }
        }

        return arrayList
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun displayTaiLieu(){
        listFolderTaiLieu.clear()
        val txtanh =  (activity as PhotoActivity).txtAnh.text.toString()

        val rootDir2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        listFolderTaiLieu.addAll(findFileTaiLieu(rootDir2))

        Log.d("yenn", "listImgRecently: "+listFolderTaiLieu.size)
        adapter.updateData(listFolderTaiLieu)
    }



    override fun onItemClick(file: File, position: Int) {
        try {
            var fileOpen = FileOpen()
            fileOpen.openFile(requireContext(),file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


            inflater.inflate(R.menu.file_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsMenuClicked(view: View, file: File, position: Int) {
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
                            listFolderTaiLieu.set(position,destination)
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
                dialogYesOrNo(requireContext(),"Delete","You want to delete file?",
                    DialogInterface.OnClickListener{dialog, id ->
                        val temp = listFolderTaiLieu.remove(file)
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
        val find = FindInformation(file ,position)
        tvInformation.text = find.findInfor()
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

    fun initTransfer() {

        imgTransfer.setOnClickListener {
            if (transferType) {
                transferType = false
                isList = true
                back = true
                mGridViewTaiLieu.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                adapter =
                    FileAdapter(1,true, true, requireContext(), listFolderTaiLieu, this)
                mGridViewTaiLieu.adapter = adapter
                Log.d("islist", "getItemViewType: isList" + isList.toString())
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_linear)
            } else {
                back = false
                isList = false
                transferType = true
                adapter= FileAdapter(3,false,false,requireContext(),listFolderTaiLieu,this)
                mGridViewTaiLieu.layoutManager =
                    GridLayoutManager(context,2)
                mGridViewTaiLieu.adapter = adapter
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_grid)


            }
        }

        imgOrder.setOnClickListener {
            val popup = PopupMenu(context, it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.tool_toolbar_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                if (item.itemId == R.id.orderByName_tool) {
                    listFolderTaiLieu.sortBy { it.name.lowercase() }
                    adapter.updateData(listFolderTaiLieu)
                    return@OnMenuItemClickListener true

                } else if (item.itemId == R.id.orderByTime_tool) {
                    listFolderTaiLieu.sortByDescending { FileExtractUtils.getFileLastModified(it) }
                    adapter.updateData(listFolderTaiLieu)

                    return@OnMenuItemClickListener true
                } else if (item.itemId == R.id.orderBySizeTool) {
                    var checkIsDirectory = false
                    for (file in listFolderTaiLieu){
                        if(!file.isDirectory){
                            checkIsDirectory = true
                        }
                    }
                    if(checkIsDirectory){
                        listFolderTaiLieu.sortByDescending { FileExtractUtils.getFileSize(it) }
                    }
                    else listFolderTaiLieu.sortByDescending { FileExtractUtils.getQuanlityFile(it)}
                    adapter.updateData(listFolderTaiLieu)
                    return@OnMenuItemClickListener true
                }
                false
            })


        }
    }
}