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
import com.example.filesmanager.utils.FindInformationImg
import java.io.File
import java.io.IOException


class DisplayAllVideoFragment : Fragment(), RecentlyImageAdapter.OnItemClickListenerTool {
    lateinit var mRecycleVideo: RecyclerView
    private var listVideo = ArrayList<File>()
    private var listMusic = ArrayList<File>()
    private var isList = false
    private var bundle: String = ""
    lateinit var adapter: RecentlyImageAdapter
    private var path: String = ""
    private var check: String = ""
    lateinit var drawerLayoutFile: DrawerLayout
    lateinit var tvInformation: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it.getString("video").toString()
            path = it.getString("video").toString()
            check = it.getString("check").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_display_all_video, container, false)
        mRecycleVideo = view.findViewById(R.id.mRecycleVideo)
        drawerLayoutFile = (requireActivity() as PhotoActivity).drawerPhoto!!
        tvInformation = (requireActivity() as PhotoActivity).txtInformPhoto!!
        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (check == "Video") {
            setUpRecyclerViewType(listVideo)
            displayVideo()
        } else if (check == "Âm nhạc") {
            setUpRecyclerViewType(listMusic)
            displayMusic()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.file_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpRecyclerViewType(list: ArrayList<File>) {
        if (!isList) {
            adapter = RecentlyImageAdapter(check, isList, requireContext(), list, this)
            mRecycleVideo.layoutManager =
                GridLayoutManager(context, 2)
            mRecycleVideo.adapter = adapter
        }
    }

    private fun findFileVideo(file: File): ArrayList<File> {
        var arrayList = ArrayList<File>()
        val files = file.listFiles()


        for (singleImg in files) {
            if (!singleImg.isHidden && singleImg.isDirectory) {
                arrayList.addAll(findFileVideo(singleImg))
            } else {
                if (singleImg.name.endsWith("mp4")
                ) {
                    arrayList.add(singleImg)
                }
            }
        }

        return arrayList
    }

    private fun findFileMusic(file: File): ArrayList<File> {
        var arrayList = ArrayList<File>()
        val files = file.listFiles()
        for (singleImg in files) {
            if (!singleImg.isHidden && singleImg.isDirectory) {
                arrayList.addAll(findFileMusic(singleImg))
            } else {
                if (singleImg.name.endsWith(".mp3")
                ) {
                    arrayList.add(singleImg)
                }
            }
        }

        return arrayList
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun displayVideo() {
        listVideo.clear()
        val txtanh = (activity as PhotoActivity).txtAnh.text.toString()
        listVideo.addAll(findFileVideo(File(path)))
        Log.d("yenn", "listvideotly: " + listVideo.size)
        adapter.updateDataTool(listVideo)
    }

    private fun displayMusic() {
        listMusic.clear()
        val txtanh = (activity as PhotoActivity).txtAnh.text.toString()
        listMusic.addAll(findFileMusic(File(path)))
        Log.d("yenn", "listMusictly: " + listMusic.size)
        adapter.updateDataTool(listMusic)
    }

    private var ok = false
    override fun onItemClickTool(file: File, position: Int) {
        ok = true
        if (file.isDirectory) {
            val arrayList = ArrayList<File>()
            arrayList.clear()
            arrayList.addAll(findFileVideo(File(file.absolutePath)))
            adapter.updateDataTool(arrayList)
        } else {
            try {
                var fileOpen = FileOpen()
                fileOpen.openFile(requireContext(), file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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
                        if(check == "Video"){

                            if(current.renameTo(destination)){
                                listVideo.set(position,destination)
                                adapter.notifyDataSetChanged()
                            }
                        }
                        else if(check == "Âm nhạc"){
                            if(current.renameTo(destination)){
                                listVideo.set(position,destination)
                                adapter.notifyDataSetChanged()
                            }
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
                if(check == "Video"){
                    dialogYesOrNo(requireContext(), "Delete", "Bạn có chắc muốn xóa file không?",
                        DialogInterface.OnClickListener { dialog, id ->
                            listVideo.remove(file)
                            adapter.updateDataTool(listVideo)
                        })
                }
                else if(check == "Âm nhạc"){
                    dialogYesOrNo(requireContext(), "Delete", "Bạn có chắc muốn xóa file không?",
                        DialogInterface.OnClickListener { dialog, id ->
                            listMusic.remove(file)
                            adapter.updateDataTool(listMusic)
                        })
                }
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
        val find = FindInformationImg(file , position, adapter)
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
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                @SuppressLint("WrongConstant")
                override fun handleOnBackPressed() {
                    if (check == "Video") {
                        adapter.updateDataTool(listVideo)
                        (activity as PhotoActivity).txtAnh.text == bundle
                    } else if (check == "Âm nhạc") {
                        adapter.updateDataTool(listMusic)
                        (activity as PhotoActivity).txtAnh.text == bundle
                    }
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, VideoFragment(check))
                        .commit()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String, check: String) =
            DisplayAllVideoFragment().apply {
                arguments = Bundle().apply {
                    putString("video", param1)
                    putString("video", param2)
                    putString("check", check)
                }
            }
    }
}