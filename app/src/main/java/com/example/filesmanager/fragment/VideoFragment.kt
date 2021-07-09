package com.example.filesmanager.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.AppAdapter
import com.example.filesmanager.Adapter.ImageAdapter
import com.example.filesmanager.Adapter.RecentlyImageAdapter
import com.example.filesmanager.R
import com.example.filesmanager.activity.PhotoActivity
import com.example.filesmanager.model.App
import com.example.filesmanager.model.FolerImage
import com.example.filesmanager.utils.AppAsynTask
import com.example.filesmanager.utils.FileShare
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VideoFragment(val check: String) : Fragment(), ImageAdapter.OnItemClickListenerTool {
    lateinit var mGridViewImgVideo: RecyclerView
    lateinit var imgAdapter: ImageAdapter
    lateinit var reAdapter: RecentlyImageAdapter
    private var listFolderVideo = ArrayList<FolerImage>()
    private var listFolderMusic = ArrayList<FolerImage>()
    private var listApplication = ArrayList<App>()
    lateinit var appAdapter: AppAdapter
    private var isList = false
    lateinit var drawerLayoutFile: DrawerLayout
    lateinit var tvInformation: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        mGridViewImgVideo = view.findViewById(R.id.mGridViewImgVideo)
        drawerLayoutFile = (requireActivity() as PhotoActivity).drawerPhoto!!
        tvInformation = (requireActivity() as PhotoActivity).txtInformPhoto!!
        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as PhotoActivity).txtAnh.text = check
        if (check == "Video") {
            setUpRecyclerView(listFolderVideo)
            listAllVideo()

        } else if (check == "Âm nhạc") {
            setUpRecyclerView(listFolderMusic)
            listAllMusic()
        } else if (check == "Ứng dụng") {

            val task = @SuppressLint("StaticFieldLeak")
            object : AppAsynTask(requireActivity()), AppAdapter.OnItemClickListenerApp {
                val app = ArrayList<App>()
                override fun onPostExecute(result: ArrayList<App>?) {

                    if (result != null) {
                        Log.e("yennnn", "onPostExecute: " + result.size )
                        app.addAll(result)
                        if (isList) {
                            mGridViewImgVideo.layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            appAdapter = AppAdapter(isList, requireContext(), result, this)
                            mGridViewImgVideo.adapter = appAdapter
                        } else {
                            mGridViewImgVideo.layoutManager =
                                GridLayoutManager(requireContext(), 2)
                            appAdapter = AppAdapter(isList, requireContext(), result, this)
                            mGridViewImgVideo.adapter = appAdapter
                        }
                        result.sortBy { it.label }
                        appAdapter.updateDataApp(result)
                    }
                    super.onPostExecute(result)
                }

                override fun onOptionsMenuClickedApp(view: View, file: App, position: Int) {
                    val popupMenu = PopupMenu(context, view)
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        if (item.itemId == R.id.ic_openApp) {
                            val launchApp :Intent = requireActivity().packageManager
                                .getLaunchIntentForPackage(file.packageName)!!
                            startActivity(launchApp)
                        }
                        else if(item.itemId == R.id.ic_uninstallApp){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle(file.label)
                            builder.setMessage("Bạn có muốn gỡ cài đặt không?")
                            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                                Log.d("yennnn", "app size start : ${app.size}")
                                app.remove(file)
                                appAdapter.updateDataApp(app)
                                dialog.dismiss()
                            })

                            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->

                                dialog.dismiss()
                            })
                            var alertDialog : AlertDialog = builder.create()
                            alertDialog.show()

                            return@OnMenuItemClickListener true
                        }
                        else {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", file.packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        false
                    })
                    popupMenu.inflate(R.menu.app_menu)
                    popupMenu.show()
                }
            }
            task.execute()
        }
    }


    private fun setUpRecyclerView(list: ArrayList<FolerImage>) {
        if (isList) {
            mGridViewImgVideo.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            imgAdapter = ImageAdapter(check, isList, requireContext(), list, this)
            mGridViewImgVideo.adapter = imgAdapter
        } else {
            mGridViewImgVideo.layoutManager =
                GridLayoutManager(requireContext(), 2)
            imgAdapter = ImageAdapter(check, isList, requireContext(), list, this)
            mGridViewImgVideo.adapter = imgAdapter
        }
    }

    override fun onItemClickTool(file: FolerImage, position: Int) {

        val fragment = DisplayAllVideoFragment.newInstance(file.name, file.path, check)
        (activity as PhotoActivity).txtAnh.text = file.name
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        if (check == "Ứng dụng") {
            inflater.inflate(R.menu.app_menu, menu)
        } else {
            inflater.inflate(R.menu.img_menu, menu)
        }
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
               if(check == "Video"){
                   dialogYesOrNo(requireContext(), "Delete", "Bạn có chắc muốn xóa file không?",
                       DialogInterface.OnClickListener { dialog, id ->
                            listFolderVideo.remove(file)
                           imgAdapter.updateDataTool(listFolderVideo)
                       })
               }
                else if(check == "Âm nhạc"){
                   dialogYesOrNo(requireContext(), "Delete", "Bạn có chắc muốn xóa file không?",
                       DialogInterface.OnClickListener { dialog, id ->
                           listFolderMusic.remove(file)
                           imgAdapter.updateDataTool(listFolderMusic)
                       })
               }
                return@OnMenuItemClickListener true
            }
             else if (item.itemId == R.id.ic_shareImg) {
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
                "\n\nKiểu: folder" +
                "\nKích thước:" +
                "\nSửa đổi lần cuối: ${imgAdapter.lastModified[position]}"

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
    private fun findVideo(file: File) {
        if (file.isDirectory && !file.isHidden) {
            val listFile = file.listFiles()
            if (listFile != null && listFile.size > 0) {
                for (f in listFile) {
                    findVideo(f)
                }
            }
        } else {
            val ext = file.extension
            when (ext) {
                "mp4" -> {
                    val parent = file.parent
                    val folder = FolerImage(parent)
                    var find = false
                    for (temp in listFolderVideo) {
                        if (temp.path == folder.path) {
                            temp.listImage.add(file)
                            find = true
                            break
                        }
                    }
                    if (!find) {
                        folder.listImage.add(file)
                        listFolderVideo.add(folder)
                    }
                }
            }
        }
    }

    private fun findMusic(file: File) {
        if (file.isDirectory && !file.isHidden) {
            val listFile = file.listFiles()
            if (listFile != null && listFile.size > 0) {
                for (f in listFile) {
                    findMusic(f)
                }
            }
        } else {
            val ext = file.extension
            when (ext) {
                "mp3" -> {
                    val parent = file.parent
                    val folder = FolerImage(parent)
                    var find = false
                    for (temp in listFolderMusic) {
                        if (temp.path == folder.path) {
                            temp.listImage.add(file)
                            find = true
                            break
                        }
                    }
                    if (!find) {
                        folder.listImage.add(file)
                        listFolderMusic.add(folder)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun listAllVideo() {

        val rootDir2 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        findVideo(rootDir2)
        val rootDir3 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS)
        findVideo(rootDir3)
        val root = Environment.getExternalStorageDirectory().absolutePath + "/Pictures"
        findVideo(File(root))
        val root2 = Environment.getExternalStorageDirectory().absolutePath + "/Movies"
        findVideo(File(root2))
        val root4 = Environment.getExternalStorageDirectory().absolutePath + "/DCIM"
        findVideo(File(root4))
        val root22 = Environment.getExternalStorageDirectory().absolutePath + "/ObjectRemover"
        findVideo(File(root22))
        imgAdapter.updateDataTool(listFolderVideo)
        for (f in listFolderVideo) {
            Log.d("ddd", "listAllImage: " + f.name)
            Log.d("ddd", "listAllImage: " + f.lastModify)
            Log.d("ddd", "listAllImage: " + f.listImage.size)
        }
    }

    private fun listAllMusic() {
        val rootDir2 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        findMusic(rootDir2)
        val root = Environment.getExternalStorageDirectory().absolutePath + "/Zing MP3"
        findMusic(File(root))
        val root2 = Environment.getExternalStorageDirectory().absolutePath + "/Music"
        findMusic(File(root2))
        imgAdapter.updateDataTool(listFolderMusic)
    }
}