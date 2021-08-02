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
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.RecentlyImageAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.PhotoActivity
import com.example.filesmanager.utils.FileOpen
import com.example.filesmanager.utils.FileShare
import com.example.filesmanager.utils.FindInformationImg
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
    lateinit var imgTransfer: ImageView
    lateinit var imgOrder: ImageView
    private var transferType = true
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
        imgTransfer = (requireActivity() as PhotoActivity).imgGridToolBar!!
        imgOrder = (requireActivity() as PhotoActivity).imgOrderToolBar!!
        transferType = (requireActivity() as PhotoActivity).checkTransfer

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
        initTransfer()
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
            if (singleImg.extension=="png" ||
                singleImg.extension=="jpg" ||
                singleImg.extension=="jpeg"
            ) {
                arrayList.add(singleImg)
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
                dialogYesOrNo(requireContext(),"Delete","You want to delete file?",
                    DialogInterface.OnClickListener{ dialog, id ->
                        val temp = listAnh.remove(file)
                        adapter.notifyDataSetChanged()
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

    private fun findInformation(file: File,position:Int) {
        val find = FindInformationImg(file ,position)
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
    override fun onAttach(context: Context) {
        Log.d("sssyen", "onAttach: ")
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                @SuppressLint("WrongConstant")
                override fun handleOnBackPressed() {
                    Log.d("sssyen", "handleOnBackPressed: "+ ok.toString())
                    adapter.updateDataTool(listAnh)

                    (activity as PhotoActivity).txtAnh.text = "Image"
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, ImageFragment())
                        .commit()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    private fun initTransfer() {

        imgTransfer.setOnClickListener {
            if (transferType) {
                transferType = false
                isList = true
                mRecycleAnh.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                adapter = RecentlyImageAdapter("picture", true, requireContext(), listAnh, this)
                mRecycleAnh.adapter = adapter
                Log.d("islist", "click: isList" + isList.toString())
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_linear)
            } else {
                isList = false
                transferType = true
                mRecycleAnh.layoutManager =
                    GridLayoutManager(requireContext(), 2)
                adapter = RecentlyImageAdapter("picture", isList, requireContext(), listAnh, this)
                mRecycleAnh.adapter = adapter
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_grid)


            }
        }

        imgOrder.setOnClickListener {
            val popup = PopupMenu(requireActivity(), it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.tool_toolbar_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                if (item.itemId == R.id.orderByName_tool) {
                    listAnh.sortBy { it.name }
                    adapter.updateDataTool(listAnh)
                    return@OnMenuItemClickListener true


                } else if (item.itemId == R.id.orderByTime_tool) {

                    listAnh.sortByDescending { it.lastModified() }
                    adapter.updateDataTool(listAnh)

                    return@OnMenuItemClickListener true
                } else if (item.itemId == R.id.orderBySizeTool) {
                    listAnh.sortByDescending { it.length() }
                    adapter.updateDataTool(listAnh)
                    return@OnMenuItemClickListener true
                }
                false
            })


        }
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