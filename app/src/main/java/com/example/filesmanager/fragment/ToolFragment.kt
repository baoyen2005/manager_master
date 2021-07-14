package com.example.filesmanager.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.OptimalAdapter
import com.example.filesmanager.Adapter.RecentlyImageAdapter
import com.example.filesmanager.Adapter.TypeAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.MainActivity
import com.example.filesmanager.activity.PhotoActivity
import com.example.filesmanager.model.Type
import com.example.filesmanager.utils.FileOpen
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.IOException

class ToolFragment : Fragment(), RecentlyImageAdapter.OnItemClickListenerTool,
    Toolbar.OnMenuItemClickListener, TypeAdapter.OnItemClickListenerType,
    OptimalAdapter.OnItemClickListenerOptimal{

    private var listImgRecently = ArrayList<File>()
    lateinit var mRecycleViewRecently: RecyclerView
    lateinit var adapterImage: RecentlyImageAdapter
    lateinit var mGridViewImg: RecyclerView
    private var listType = ArrayList<Type>()
    private var isList: Boolean = false
    private var listBackGrid = false
    lateinit var toolbarTool: MaterialToolbar
    lateinit var rs: Cursor
    lateinit var mList: ArrayList<String>
    lateinit var typeAdapter: TypeAdapter
    lateinit var mGridViewType: RecyclerView
    private var sdCard = ""
    lateinit var mGridviewOptimal: RecyclerView
    lateinit var optimalAdapter: OptimalAdapter
    private var optimalList = ArrayList<Type>()
    lateinit var canhbao : ImageView
    lateinit var btnNavigation :ImageView
    lateinit var drawerLayoutFile :DrawerLayout
    lateinit var navi :NavigationView
    lateinit var btnTangTocTool :Button
    fun newInstance(): ToolFragment {
        return ToolFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isList = true
        listBackGrid = true

        setHasOptionsMenu(true)

    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_tool, container, false)
        drawerLayoutFile = (requireActivity() as MainActivity).drawer!!
        navi = (requireActivity()as MainActivity).navigationViewStart
        mRecycleViewRecently = view.findViewById(R.id.recycle_recently)
        mGridViewType = view.findViewById(R.id.mGridViewType)
        mGridviewOptimal = view.findViewById(R.id.mGridViewMin)
        toolbarTool = view.findViewById(R.id.toolbarTool)
        canhbao = view.findViewById(R.id.canhbao)
        btnNavigation = view.findViewById(R.id.btnNavigation)
        btnTangTocTool = view.findViewById(R.id.btnTangTocTool)

        btnTangTocTool.setOnClickListener {
            Toast.makeText(context,"Feature is updating",Toast.LENGTH_SHORT).show()
        }
        canhbao.setOnClickListener {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(intent)
        }
        btnNavigation.setOnClickListener{
            drawerLayoutFile.openDrawer(Gravity.START)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("yennn", "tool fragment onViewCreated: ")
        setUpRecyclerView()
        displayImage()
        setUpRecyclerViewType()
        displayType()
        setUpRecyclerViewOptimal()
        displayOptimal()

        super.onViewCreated(view, savedInstanceState)
    }


    private fun setUpRecyclerViewOptimal() {
        optimalAdapter = OptimalAdapter(requireContext(), optimalList, this)
        mGridviewOptimal.layoutManager =
            GridLayoutManager(context, 4)
        mGridviewOptimal.adapter = optimalAdapter
    }

    private fun displayOptimal() {

        optimalList.clear()
        optimalList.add(Type("Saving", R.drawable.tietkiempin))
        optimalList.add(Type("Fresh CPU", R.drawable.mat))
        optimalList.add(Type("Security", R.drawable.baove))
        optimalList.add(Type("Clean", R.drawable.dondep))

        Log.d("yenn", listType[1].name.toString())

        optimalAdapter.updateDataOptimal(optimalList)
    }

    private fun setUpRecyclerViewType() {
        typeAdapter = TypeAdapter(requireContext(), listType, this)
        mGridViewType.layoutManager =
            GridLayoutManager(context, 4)
        mGridViewType.adapter = typeAdapter
        //mGridViewType.isNestedScrollingEnabled = false
    }

    private fun displayType() {
        listType = ArrayList()
        listType.clear()
        listType.add(Type("Image", R.drawable.anh))
        listType.add(Type("Video", R.drawable.video))
        listType.add(Type("Music", R.drawable.amthanh))
        listType.add(Type("Document", R.drawable.tailieu))
        listType.add(Type("Application", R.drawable.ungdung))
        Log.d("yenn", listType[1].name.toString())

        typeAdapter.updateDataType(listType)
    }

    private fun setUpRecyclerView() {

        if (isList) {
            mRecycleViewRecently.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapterImage =
                RecentlyImageAdapter("anh", isList, requireContext(), listImgRecently, this)
            mRecycleViewRecently.adapter = adapterImage
        } else {
            mRecycleViewRecently.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapterImage =
                RecentlyImageAdapter("anh", isList, requireContext(), listImgRecently, this)
            mRecycleViewRecently.adapter = adapterImage
        }
    }

    private var arrayList = ArrayList<File>()
    private fun findFileImage(file: File) {
        //arrayList= ArrayList<File>()
        val files = file.listFiles()
        if (files == null || files.isEmpty()) {
            return
        } else {
            for (singleImg in files) {
                if (!singleImg.isHidden && singleImg.isDirectory) {
                    findFileImage(singleImg)
                } else {
                    if (singleImg.name.endsWith("png") ||
                        singleImg.name.endsWith("jpg") ||
                        singleImg.name.endsWith("jpeg") ||
                        singleImg.name.endsWith("mp4")
                    ) {
                        arrayList.add(singleImg)
                    }
                }
            }
        }
    }

    fun displayImage() {
        listImgRecently.clear()
        sdCard = Environment.getExternalStorageDirectory().absolutePath + "/Pictures"
        val root4 = Environment.getExternalStorageDirectory().absolutePath + "/DCIM"
        Log.d("yenn", sdCard)
        arrayList.clear()
        findFileImage(File(sdCard))
        findFileImage(File(root4))
        listImgRecently.addAll(arrayList)
        listImgRecently.sortByDescending { it.lastModified() }
        Log.d("yenn", "listImgRecently: " + listImgRecently.size)
        adapterImage.updateDataTool(listImgRecently)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onItemClickTool(file: File, position: Int) {
        try {
            var fileOpen = FileOpen()
            fileOpen.openFile(requireContext(),file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onOptionsMenuClickedTool(view: View, file: File, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClickType(type: Type, position: Int) {
        var bundle = Bundle()
        bundle.putString("anh", type.name)
        Log.d("anh", "anh1 " + bundle.getString("anh"))
        var intent = Intent(context, PhotoActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.d("onresume", "onResume: " + sdCard)
        if (sdCard == "") displayImage()

    }

    override fun onItemClickOptimal(type: Type, position: Int) {
        Toast.makeText(context,"Tính năng đang update!!", Toast.LENGTH_SHORT).show()
    }


}