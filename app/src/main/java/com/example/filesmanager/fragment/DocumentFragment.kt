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
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.FileAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.PhotoActivity
import com.example.filesmanager.utils.FileOpen
import com.example.filesmanager.utils.FileShare
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerLayoutFile = (requireActivity() as PhotoActivity).drawerPhoto!!
        tvInformation = (requireActivity() as PhotoActivity).txtInformPhoto!!
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
                if (singleImg.name.endsWith("docx")
                    ||singleImg.name.endsWith("pdf")
                    || singleImg.name.endsWith("pptx")
                    ||singleImg.name.endsWith("doc")
                    ||singleImg.name.endsWith("ppt")) {
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
                dialogYesOrNo(requireContext(),"Delete","Bạn có chắc muốn xóa file không?",
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


}