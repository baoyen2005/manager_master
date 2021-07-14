package com.example.filesmanager.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.ImageAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.PhotoActivity
import com.example.filesmanager.model.FolerImage
import com.example.filesmanager.utils.FileExtractUtils
import com.example.filesmanager.utils.FileShare
import java.io.File


class ImageFragment : Fragment(), ImageAdapter.OnItemClickListenerTool {
    lateinit var mGridViewImgPhoto: RecyclerView
    lateinit var imgAdapter: ImageAdapter
    var listFolderImage = ArrayList<FolerImage>()
    private var isList = false
    private var transferType = true

    @RequiresApi(Build.VERSION_CODES.Q)
    lateinit var drawerLayoutFile: DrawerLayout
    lateinit var tvInformation: TextView
    lateinit var imgTransfer: ImageView
    lateinit var imgOrder: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerLayoutFile = (requireActivity() as PhotoActivity).drawerPhoto!!
        tvInformation = (requireActivity() as PhotoActivity).txtInformPhoto!!
        imgTransfer = (requireActivity() as PhotoActivity).imgGridToolBar!!
        imgOrder = (requireActivity() as PhotoActivity).imgOrderToolBar!!
        transferType = (requireActivity() as PhotoActivity).checkTransfer

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mGridViewImgPhoto = view.findViewById(R.id.mGridViewImgPhoto)
        setUpRecyclerView()
        listAllImage()
        initTransfer()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpRecyclerView() {

        if (isList) {
            mGridViewImgPhoto.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            imgAdapter = ImageAdapter("picture", isList, requireContext(), listFolderImage, this)
            mGridViewImgPhoto.adapter = imgAdapter
        } else {
            mGridViewImgPhoto.layoutManager =
                GridLayoutManager(requireContext(), 2)
            imgAdapter = ImageAdapter("picture", isList, requireContext(), listFolderImage, this)
            mGridViewImgPhoto.adapter = imgAdapter
        }

    }


    override fun onItemClickTool(file: FolerImage, position: Int) {
        val fragment = DisplayAllImageFragment.newInstance(file.name, file.path)
        (activity as PhotoActivity).txtAnh.text = file.name
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.img_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsMenuClickedTool(view: View, file: FolerImage, position: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.ic_inforImg) {
                drawerLayoutFile.openDrawer(Gravity.RIGHT)
                findInformation(file, position)
                return@OnMenuItemClickListener true


            } else if (item.itemId == R.id.ic_deleteImg) {

                dialogYesOrNo(requireContext(), "Delete", "You want to delete file?",
                    DialogInterface.OnClickListener { dialog, id ->
                        listFolderImage.remove(file)
                        imgAdapter.updateDataTool(listFolderImage)
                    })

                return@OnMenuItemClickListener true
            } else if (item.itemId == R.id.ic_shareImg) {
                val share = FileShare()
                share.shareFile(requireContext(), file.file)
                return@OnMenuItemClickListener true
            }
            false
        })
        popupMenu.inflate(R.menu.img_menu)

        popupMenu.show()
    }

    private fun findInformation(file: FolerImage, position: Int) {

        tvInformation.text = "${file.name}" +
                "\n\nType: folder" +
                "\nSize:"+
                "\nLast Modified: ${imgAdapter.lastModified[position]}"

    }

    fun dialogYesOrNo(
        context: Context, title: String, message: String, listener: DialogInterface.OnClickListener
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

    fun listImage(file: File) {
        if (file.isDirectory && !file.isHidden) {
            val listFile = file.listFiles()
            if (listFile != null && listFile.size > 0) {
                for (f in listFile) {
                    listImage(f)
                }
            }
        } else {
            val ext = file.extension
            when (ext) {
                "png", "jpg", "jpeg", "gif" -> {
                    val parent = file.parent
                    val folder = FolerImage(parent)
                    var find = false
                    for (temp in listFolderImage) {
                        if (temp.path == folder.path) {
                            temp.listImage.add(file)
                            find = true
                            break
                        }
                    }
                    if (!find) {
                        folder.listImage.add(file)
                        listFolderImage.add(folder)
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun listAllImage() {

        val rootDir2 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        listImage(rootDir2)

        val root = Environment.getExternalStorageDirectory().absolutePath + "/Pictures"
        listImage(File(root))
        val root4 = Environment.getExternalStorageDirectory().absolutePath + "/DCIM"
        listImage(File(root4))
        val root22 = Environment.getExternalStorageDirectory().absolutePath + "/ObjectRemover"
        listImage(File(root22))

        imgAdapter.updateDataTool(listFolderImage)
        for (f in listFolderImage) {
            Log.d("ddd", "listAllImage: " + f.name)
            Log.d("ddd", "listAllImage: " + f.lastModify)
            Log.d("ddd", "listAllImage: " + f.listImage.size)
        }
    }

    @SuppressLint("ResourceType")
    private fun initTransfer() {

        imgTransfer.setOnClickListener {
            if (transferType) {
                transferType = false
                isList = true
                mGridViewImgPhoto.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                imgAdapter =
                    ImageAdapter("picture", isList, requireContext(), listFolderImage, this)
                mGridViewImgPhoto.adapter = imgAdapter
                Log.d("islist", "getItemViewType: isList" + isList.toString())
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_linear)
            } else {
                isList = false
                transferType = true
                mGridViewImgPhoto.layoutManager =
                    GridLayoutManager(requireContext(), 2)
                imgAdapter =
                    ImageAdapter("picture", isList, requireContext(), listFolderImage, this)
                mGridViewImgPhoto.adapter = imgAdapter
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
                    listFolderImage.sortBy { it.name.lowercase() }
                    imgAdapter.updateDataTool(listFolderImage)
                    return@OnMenuItemClickListener true

                } else if (item.itemId == R.id.orderByTime_tool) {
                    listFolderImage.sortByDescending { FileExtractUtils.getFileLastModified(it.file) }
                    imgAdapter.updateDataTool(listFolderImage)

                    return@OnMenuItemClickListener true
                } else if (item.itemId == R.id.orderBySizeTool) {
                    var checkIsDirectory = false
                    listFolderImage.sortByDescending { it.listImage.size}
                    imgAdapter.updateDataTool(listFolderImage)
                    return@OnMenuItemClickListener true
                }
                false
            })


        }
    }
}
