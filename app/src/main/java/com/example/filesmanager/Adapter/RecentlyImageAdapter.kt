package com.example.filesmanager.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filesmanager.R
import com.example.filesmanager.fragment.ToolFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RecentlyImageAdapter(
    var bundle: String,
    var isList: Boolean,
    private var context: Context,
    private var fileImage: ArrayList<File>,
    private val listener: RecentlyImageAdapter.OnItemClickListenerTool
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var quanlityFile = mutableMapOf<Int, Int>()
    var lastModified = ArrayList<String>()
    var fileSize = mutableMapOf<Int, String>()
    var isPosition: Int = 0
    val TYPE_A = 1
    val TYPE_B = 2
    val TYPE_IMG = 3
    val TYPE_VIDEO = 4
    val TYPE_MP3 = 5
    override fun getItemViewType(position: Int): Int {
        if (isList && bundle == "anh") {
            return TYPE_A
        } else if (!isList && bundle == "Video" || bundle == "Hình ảnh" || bundle == "anh") {
            return TYPE_B
        } else if (!isList && bundle == "Âm nhạc") {
            return TYPE_MP3
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
                val view =
                    inflater.inflate(R.layout.item_image_recently_tools_gridview, parent, false)
                FileViewHolder2(view)
            }
            else -> {
                val view =
                    inflater.inflate(R.layout.item_image_recently_tools_gridview, parent, false)
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
                    val myFragment = ToolFragment()
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
                    lastModified.add(position, simpleDateFormat.format(date))


                    var length = 0
                    if (fileItem.isDirectory) {
                        val files = fileItem.listFiles()
                        if (files != null) {
                            length = files.size
                            Log.d("aaa", "onBindViewHolder: " + length)
                            if (length >= 1) {
                                if (length == 1) {
                                    holder.tvSize.text = "1 file"
                                    quanlityFile.put(position, 1)
                                } else {
                                    holder.tvSize.text = "$length files"
                                    quanlityFile.put(position, length)
                                }
                            } else {
                                holder.tvSize.text = "empty"
                                if (holder.tvSize.text == "empty") quanlityFile.put(position, 0)
                            }
                        } else {
                            holder.tvSize.text = "empty"
                            //quanlityFile.clear()
                            if (holder.tvSize.text == "empty") quanlityFile.put(position, 0)
                        }
                        Log.d("bbbb", quanlityFile.toString())

                    } else {

                        val files = fileImage[position]
                        val size = files.length()
                        Log.d("bbss", size.toString())
                        if (size < 1024) {
                            holder.tvSize.text = size.toString() + "B"
                            fileSize.put(position, size.toDouble().toString())
                        } else if (size >= 1024 && size < 1024 * 1024) {

                            var s = String.format("%.2f", (size.toDouble() / 1024))
                            fileSize.put(position, s)
                            holder.tvSize.text = s + "KB"
                        } else {
                            //var sizeF :Double = (size/1024*1024).toDouble()
                            var s = String.format("%.2f", (size.toDouble() / (1024 * 1024)))
                            fileSize.put(position, s)
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
                    lastModified.add(position, simpleDateFormat.format(date))


                    var length = 0
                    if (fileItem.isDirectory) {
                        val files = fileItem.listFiles()
                        if (files != null) {
                            length = files.size
                            Log.d("aaa", "onBindViewHolder: " + length)
                            if (length >= 1) {
                                if (length == 1) {
                                    holder.tvSize.text = "1 file"
                                    quanlityFile.put(position, 1)
                                } else {
                                    holder.tvSize.text = "$length files"
                                    quanlityFile.put(position, length)
                                }
                            } else {
                                holder.tvSize.text = "empty"
                                if (holder.tvSize.text == "empty") quanlityFile.put(position, 0)
                            }
                        } else {
                            holder.tvSize.text = "empty"
                            //quanlityFile.clear()
                            if (holder.tvSize.text == "empty") quanlityFile.put(position, 0)
                        }
                        Log.d("bbbb", quanlityFile.toString())

                    }  else {

                        val files = fileImage[position]
                        val size = files.length()
                        Log.d("bbss", size.toString())
                        if (size < 1024) {
                            holder.tvSize.text = size.toString() + "B"
                            fileSize.put(position, size.toDouble().toString())
                        } else if (size >= 1024 && size < 1024 * 1024) {

                            var s = String.format("%.2f", (size.toDouble() / 1024))
                            fileSize.put(position, s)
                            holder.tvSize.text = s + "KB"
                        } else {
                            //var sizeF :Double = (size/1024*1024).toDouble()
                            var s = String.format("%.2f", (size.toDouble() / (1024 * 1024)))
                            fileSize.put(position, s)
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
            itemView.findViewById(R.id.txtfileNameGrid)
        var tvDate: TextView = itemView.findViewById(R.id.txtTimeImgToolGrid)
        var imgFile: ImageView = itemView.findViewById(R.id.imgRecently_tool_grid)
        var tvMenu: TextView = itemView.findViewById(R.id.txtmenuToolImgGrid)
        var tvSize: TextView = itemView.findViewById(R.id.txtSizeImgToolGrid)

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