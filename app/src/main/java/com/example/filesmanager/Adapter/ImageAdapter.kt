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
import com.example.filesmanager.model.FolerImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ImageAdapter(
    var check: String,
    var isList: Boolean,
    private var context: Context,
    private var fileImage: ArrayList<FolerImage>,
    private val listener: ImageAdapter.OnItemClickListenerTool
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var quanlityFile = mutableMapOf<Int, Int>()
    var lastModified = ArrayList<String>()
    var arrayListCopy = ArrayList<File>()
    var isPosition: Int = 0
    val TYPE_A = 1
    val TYPE_B = 2
    val TYPE_MP3 = 3
    val TYPE_lIST = 4
    override fun getItemViewType(position: Int): Int {
        if (!isList && (check == "picture" || check == "Video")) {
            return TYPE_B
        } else if (check == "Music" && !isList) {
            return TYPE_MP3
        } else if (isList && ((check =="picture") || (check == "Video")||(check == "Music") ||( check == "Application"))) {
            Log.d("islist", "getItemViewType: isList"+ isList.toString())
            return TYPE_lIST
        } else {
            return TYPE_A
        }
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
            TYPE_lIST -> {
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
                    val url: String = f.listImage[f.listImage.size - 1].absolutePath
                    Glide
                        .with(context)
                        .load(url)
                        .centerCrop()
                        .into(holder.imgFile)
                    val fileItem = fileImage[position]

                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem, position)
                    }
                }
                is FileViewHolder2 -> {
                    val fileItem = fileImage[position]
                    val url: String = fileItem.listImage[fileItem.listImage.size - 1].absolutePath
                    Glide
                        .with(context)
                        .load(url)
                        .centerCrop()
                        .into(holder.imgFile)

                    holder.tvNameFile.text = fileItem.name
                    var date: Date = Date()
                    date.time = fileItem.lastModify // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)
                    lastModified.add(position, simpleDateFormat.format(date))


                    var length = 0
                    holder.tvSize.text = fileItem.listImage.size.toString()


                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem, position)
                    }


                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClickedTool(it!!, fileItem, position)
                    }
                }
                is FileViewHolder3 -> {
                    val fileItem = fileImage[position]
                    val url: String = fileItem.listImage[fileItem.listImage.size - 1].absolutePath
                    holder.imgFile.setImageResource(R.drawable.zing)

                    holder.tvNameFile.text = fileItem.name
                    var date: Date = Date()
                    date.time = fileItem.lastModify // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)
                    lastModified.add(position, simpleDateFormat.format(date))


                    var length = 0
                    holder.tvSize.text = fileItem.listImage.size.toString()


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
                    date.time = fileItem.lastModify // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvTime.text = simpleDateFormat.format(date)
                    lastModified.add(position, simpleDateFormat.format(date))
                    Log.d("yennnn", "check = : "+ check)
                    if (check == "picture") {
                        holder.imgFile.setImageResource(R.drawable.anh)
                    } else if (check == "Video") {
                        holder.imgFile.setImageResource(R.drawable.video)
                    } else if (check == "Music") {
                        holder.imgFile.setImageResource(R.drawable.nhac)
                    } else {
                        holder.imgFile.setImageResource(R.drawable.ungdung)
                    }
                    var length = 0

                    val files = fileItem.listImage
                    if (files != null) {
                        length = files.size
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
        var tvName : TextView =
            itemView.findViewById(R.id.tv_fileName)
        var tvSize : TextView = itemView.findViewById(R.id.tvFileSize)

        var tvTime: TextView = itemView.findViewById(R.id.tv_time)

        var container : ConstraintLayout =
            itemView.findViewById(R.id.container)
        var imgFile : ImageView = itemView.findViewById(R.id.img_fileType)
        var tvOption :TextView = itemView.findViewById(R.id.textViewOptions)

    }

    interface OnItemClickListenerTool {
        fun onItemClickTool(file: FolerImage, position: Int)
        fun onOptionsMenuClickedTool(view: View, file: FolerImage, position: Int)
    }

    fun updateDataTool(newList: List<FolerImage>) {
        fileImage = newList as ArrayList<FolerImage>
        Log.d("yenn", "updateDataTool: " + newList.size)
        notifyDataSetChanged()
    }

}