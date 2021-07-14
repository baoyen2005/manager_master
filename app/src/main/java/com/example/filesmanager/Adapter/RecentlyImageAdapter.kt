package com.example.filesmanager.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filesmanager.R
import com.example.filesmanager.utils.FileExtractUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class RecentlyImageAdapter(
    var nameFolder: String,
    var isList: Boolean,
    private var context: Context,
    private var fileImage: ArrayList<File>,
    private val listener: RecentlyImageAdapter.OnItemClickListenerTool
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //    var quanlityFile = mutableMapOf<Int, Int>()
//    var lastModified = ArrayList<String>()
    // var fileSize = mutableMapOf<Int, String>()
    var isPosition: Int = 0
    val TYPE_A = 1
    val TYPE_B = 2
    val TYPE_IMG = 3
    val TYPE_MP3 = 5
    val TYPE_LIST = 4
    override fun getItemViewType(position: Int): Int {
        if (isList && nameFolder == "anh") {
            return TYPE_A
        } else if (!isList && (nameFolder == "Video" || nameFolder == "picture" || nameFolder == "anh")) {
            return TYPE_B
        } else if (!isList && nameFolder == "Music") {
            return TYPE_MP3
        } else if (isList && ((nameFolder == "picture") || (nameFolder == "Video") || (nameFolder == "Music")
                    || (nameFolder == "Document"))
        ) {
            Log.d("islist", "name: isList" + nameFolder.toString())
            return TYPE_LIST
        } else
            return TYPE_A
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return when (viewType) {
            TYPE_A -> {
                val view = inflater.inflate(R.layout.item_image_recently_tool, parent, false)
                FileViewHolder1(view)
            }
            TYPE_B -> {
                Log.d("islist", "namejjj: isList" + nameFolder.toString())
                val view =
                    inflater.inflate(R.layout.item_image_recently_tools_gridview, parent, false)
                FileViewHolder2(view)
            }
            TYPE_LIST -> {
                val view = inflater.inflate(R.layout.file_container, parent, false)
                FileViewHolder4(view)
            }
            else -> {
                val view =
                    inflater.inflate(R.layout.item_video_recently_tools_gridview, parent, false)
                FileViewHolder3(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.apply {
            when (holder) {
                is FileViewHolder1 -> {
                    val f = fileImage[position]
                    val url: String = f.absolutePath
                    Glide
                        .with(context)
                        .load(File(url))
                        .centerCrop()
                        .into(holder.imgFile)
                    val fileItem = fileImage[position]

                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem, position)
                    }
                }
                is FileViewHolder2 -> {
                    val fileItem = fileImage[position]
                    val url: String = fileItem.absolutePath
                    Glide
                        .with(context)
                        .load(File(url))
                        .centerCrop()
                        .into(holder.imgFile)


                    holder.tvNameFile.text = fileItem.name

                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)

                    var length = 0
                    if (fileItem.isDirectory) {
                        val quanlity = FileExtractUtils.getQuanlityFile(fileItem)
                        if (quanlity == 0) {
                            holder.tvSize.text = "empty"
                        } else if (quanlity == 1) {
                            holder.tvSize.text = "1 file"
                        } else {
                            holder.tvSize.text = "$quanlity files"
                        }

                    } else {

                        val files = fileImage[position]
                        val size = files.length()
                        Log.d("bbss", size.toString())
                        if (size < 1024) {
                            holder.tvSize.text = size.toString() + "B"

                        } else if (size >= 1024 && size < 1024 * 1024) {

                            var s = String.format("%.2f", (size.toDouble() / 1024))
                            holder.tvSize.text = s + "KB"
                        } else {
                            var s = String.format("%.2f", (size.toDouble() / (1024 * 1024)))
                            holder.tvSize.text = s + "MB"
                        }
                    }

                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem, position)
                    }


                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClickedTool(it!!, fileItem, position)
                    }
                }
                is FileViewHolder3 -> {
                    val fileItem = fileImage[position]
                    val url: String = fileItem.absolutePath
                    holder.imgFile.setImageResource(R.drawable.nhac)
                    holder.tvNameFile.text = fileItem.name
                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)
                    //lastModified.add(position, simpleDateFormat.format(date))


                    var length = 0
                    if (fileItem.isDirectory) {
                        val quanlity = FileExtractUtils.getQuanlityFile(fileItem)
                        if (quanlity == 0) {
                            holder.tvSize.text = "empty"
                        } else if (quanlity == 1) {
                            holder.tvSize.text = "1 file"
                        } else {
                            holder.tvSize.text = "$quanlity files"
                        }

                    } else {

                        val files = fileImage[position]
                        val size = files.length()
                        Log.d("bbss", size.toString())
                        if (size < 1024) {
                            holder.tvSize.text = size.toString() + "B"
                        } else if (size >= 1024 && size < 1024 * 1024) {

                            var s = String.format("%.2f", (size.toDouble() / 1024))
                            holder.tvSize.text = s + "KB"
                        } else {

                            var s = String.format("%.2f", (size.toDouble() / (1024 * 1024)))
                            holder.tvSize.text = s + "MB"
                        }
                    }

                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem, position)
                    }


                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClickedTool(it!!, fileItem, position)
                    }
                }
                is FileViewHolder4 -> {
                    val fileItem = fileImage[position]
                    holder.tvName.text = fileItem.name

                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvTime.text = simpleDateFormat.format(date)

                    if (nameFolder == "picture") {
                        holder.imgFile.setImageResource(R.drawable.anh)
                    } else if (nameFolder == "Video") {
                        holder.imgFile.setImageResource(R.drawable.video)
                    } else if (nameFolder == "Music") {
                        holder.imgFile.setImageResource(R.drawable.nhac)
                    } else {
                        holder.imgFile.setImageResource(R.drawable.ungdung)
                    }
                    var length = 0

                    val files = fileImage[position]
                    val size = files.length()
                    Log.d("bbss", size.toString())
                    if (size < 1024) {
                        holder.tvSize.text = size.toString() + "B"

                    } else if (size >= 1024 && size < 1024 * 1024) {

                        var s = String.format("%.2f", (size.toDouble() / 1024))
                        holder.tvSize.text = s + "KB"
                    } else {
                        var s = String.format("%.2f", (size.toDouble() / (1024 * 1024)))
                        holder.tvSize.text = s + "MB"
                    }
                    holder.itemView.setOnClickListener {

                        listener.onItemClickTool(fileItem, position)
                    }
                    holder.tvOption.setOnClickListener {
                        listener.onOptionsMenuClickedTool(it, fileItem, position)
                    }
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return fileImage.size
    }

    inner class FileViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgFile: ImageView = itemView.findViewById(R.id.imgRecently_tool)
    }

    inner class FileViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNameFile: TextView =
            itemView.findViewById(R.id.txtfileNameGrid)
        var tvDate: TextView = itemView.findViewById(R.id.txtTimeImgToolGrid)
        var imgFile: ImageView = itemView.findViewById(R.id.imgRecently_tool_grid)
        var tvMenu: TextView = itemView.findViewById(R.id.txtmenuToolImgGrid)
        var tvSize: TextView = itemView.findViewById(R.id.txtSizeImgToolGrid)

    }

    inner class FileViewHolder3(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNameFile: TextView =
            itemView.findViewById(R.id.txtVideoName)
        var tvDate: TextView = itemView.findViewById(R.id.txtVideTime)
        var imgFile: ImageView = itemView.findViewById(R.id.video)
        var tvMenu: TextView = itemView.findViewById(R.id.txtVideoMenu)
        var tvSize: TextView = itemView.findViewById(R.id.txtVideoSize)

    }

    inner class FileViewHolder4(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView =
            itemView.findViewById(R.id.tv_fileName)
        var tvSize: TextView = itemView.findViewById(R.id.tvFileSize)

        var tvTime: TextView = itemView.findViewById(R.id.tv_time)

        var container: ConstraintLayout =
            itemView.findViewById(R.id.container)
        var imgFile: ImageView = itemView.findViewById(R.id.img_fileType)
        var tvOption: TextView = itemView.findViewById(R.id.textViewOptions)

    }

    interface OnItemClickListenerTool {
        fun onItemClickTool(file: File, position: Int)
        fun onOptionsMenuClickedTool(view: View, file: File, position: Int)
    }

    fun updateDataTool(newList: List<File>) {
        fileImage = newList as ArrayList<File>
        Log.d("yenn", "updateData: " + newList.size)
        notifyDataSetChanged()
    }

}