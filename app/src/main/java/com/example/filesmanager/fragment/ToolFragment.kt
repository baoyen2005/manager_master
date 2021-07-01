package com.example.filesmanager.fragment

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.RecentlyImageAdapter
import com.example.filesmanager.Adapter.TypeAdapter
import com.example.filesmanager.R
import com.example.filesmanager.model.Type
import com.google.android.material.appbar.MaterialToolbar
import java.io.File

class ToolFragment : Fragment(), RecentlyImageAdapter.OnItemClickListenerTool , Toolbar.OnMenuItemClickListener,TypeAdapter.OnItemClickListenerType {

    private var listImgRecently = ArrayList<File>()
    lateinit var mRecycleViewRecently : RecyclerView
    lateinit var adapterImage : RecentlyImageAdapter
    lateinit var mGridViewImg : RecyclerView
    private var listType= ArrayList<Type>()
    private var isList :Boolean = false
    private var listBackGrid = false
    lateinit var toolbarTool:MaterialToolbar
    lateinit var  rs:Cursor
    lateinit var mList: ArrayList<String>
    lateinit var typeAdapter:TypeAdapter
    lateinit var mGridViewType:RecyclerView

    public fun newInstance(): ToolFragment {
        return ToolFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isList = true
        listBackGrid = true

        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_tool, container, false)
        mRecycleViewRecently = view.findViewById(R.id.recycle_recently)
        mGridViewType = view.findViewById(R.id.mGridViewType)
        toolbarTool = view.findViewById(R.id.toolbarTool)
        toolbarTool.setOnMenuItemClickListener(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        displayImage()
        setUpRecyclerViewType()
        displayType()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpRecyclerViewType() {
        typeAdapter = TypeAdapter(requireContext(),listType,this)
        mGridViewType.layoutManager =
            GridLayoutManager(context,4)
        mGridViewType.adapter = typeAdapter
    }

    private fun displayType() {
        listType = ArrayList()
        listType.clear()
        listType.add(Type("Hình ảnh",R.drawable.anh))
        listType.add(Type("Video", R.drawable.video))
        listType.add(Type("Âm nhạc", R.drawable.amthanh))
        listType.add(Type("Tài liệu", R.drawable.tailieu))
        listType.add(Type("Ứng dụng", R.drawable.ungdung))
        listType.add(Type("Tải xuống", R.drawable.taixuong))
        Log.d("yenn",listType[1].name.toString())

        typeAdapter.updateDataType(listType)
    }

    private fun setUpRecyclerView() {

        if(isList&& listBackGrid ){
            mRecycleViewRecently.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapterImage = RecentlyImageAdapter(listBackGrid,isList,requireContext(), listImgRecently, this)
            mRecycleViewRecently.adapter = adapterImage
        }
        else{
            mRecycleViewRecently.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapterImage = RecentlyImageAdapter(listBackGrid,isList,requireContext(), listImgRecently, this)
            mRecycleViewRecently.adapter = adapterImage
        }
    }
    private var  arrayList = ArrayList<File>()
    private fun findFileImage(file :File) {
        //arrayList= ArrayList<File>()
        val files = file.listFiles()
        if (files == null || files.isEmpty()){
            return
        }
        else{
            for(singleImg in files ){
                if(!singleImg.isHidden && singleImg.isDirectory){
                    findFileImage(singleImg)
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
        }
    }
    private fun displayImage(){
        listImgRecently.clear()
        val sdCard = System.getenv("EXTERNAL_STORAGE")

        Log.d("yenn", sdCard)
        arrayList.clear()
        findFileImage(File("$sdCard\Pictures"))
        listImgRecently.addAll(arrayList)
        listImgRecently.sortByDescending { it.lastModified() }
        Log.d("yenn", "listImgRecently: "+listImgRecently.size)
        adapterImage.updateDataTool(listImgRecently)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onItemClickTool(file: File, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onOptionsMenuClickedTool(view: View, file: File, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClickType(type: Type, position: Int) {
        TODO("Not yet implemented")
    }


}