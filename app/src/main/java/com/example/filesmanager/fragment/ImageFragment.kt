package com.example.filesmanager.fragment

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.ImageAdapter
import com.example.filesmanager.Adapter.RecentlyImageAdapter
import com.example.filesmanager.Adapter.ViewPagerAdapter
import com.example.filesmanager.R
import com.example.filesmanager.model.FolerImage
import java.io.File


class ImageFragment : Fragment(),ImageAdapter.OnItemClickListenerTool,RecentlyImageAdapter.OnItemClickListenerTool {
    lateinit var mGridViewImgPhoto: RecyclerView
    lateinit var imgAdapter: ImageAdapter
    lateinit var reAdapter: RecentlyImageAdapter
    var listFolderImage = ArrayList<FolerImage>()
    private var isList = false
    private var listBackGrid = false
    private var listImg = ArrayList<File>()
    @RequiresApi(Build.VERSION_CODES.Q)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mGridViewImgPhoto = view.findViewById(R.id.mGridViewImgPhoto)
        setUpRecyclerView()
        //setUpRecyclerViewRecent()
//        displayImage()
        listAllImage()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setUpRecyclerView() {

        if (isList && listBackGrid) {
            mGridViewImgPhoto.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            imgAdapter = ImageAdapter(listBackGrid, isList, requireContext(), listFolderImage, this)
            mGridViewImgPhoto.adapter = imgAdapter
        } else {
            mGridViewImgPhoto.layoutManager =
                GridLayoutManager(requireContext(), 2)
            imgAdapter = ImageAdapter(listBackGrid, isList, requireContext(), listFolderImage, this)
            mGridViewImgPhoto.adapter = imgAdapter
        }
    }
    private fun findFileImage(file :File):ArrayList<File> {
        var arrayList= ArrayList<File>()
        val files = file.listFiles()


        for(singleImg in files ){
            if(!singleImg.isHidden && singleImg.isDirectory){
                arrayList.addAll(findFileImage(singleImg))
            }
            else{
                if(singleImg.name.endsWith("png")||
                    singleImg.name.endsWith("jpg")||
                    singleImg.name.endsWith("jpeg")||
                    singleImg.name.endsWith("mp4")){
                    arrayList.add(singleImg)
                }
            }
        }

        return arrayList
    }
    private lateinit var adapter: ViewPagerAdapter
    override fun onItemClickTool(file: FolerImage, position: Int) {

        if(file.listImage[position].isDirectory){

        }
    }

    override fun onItemClickTool(file: File, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onOptionsMenuClickedTool(view: View, file: File, position: Int) {
        TODO("Not yet implemented")
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
    fun listAllImage(){
        val rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        listImage(rootDir)
        val rootDir2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        listImage(rootDir2)
        val rootDir3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS)


        //  listImage(rootDir4)
        listImage(rootDir3)
        imgAdapter.updateDataTool(listFolderImage)
        for (f in listFolderImage){
            Log.d("ddd", "listAllImage: " + f.name)
            Log.d("ddd", "listAllImage: " + f.lastModify)
            Log.d("ddd", "listAllImage: " + f.listImage.size)
        }
    }
}