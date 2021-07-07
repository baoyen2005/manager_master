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
import com.example.filesmanager.model.FolerImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ImageAdapter( var check :String,var isList:Boolean, private var context: Context, private var fileImage: ArrayList<FolerImage>, private val listener: ImageAdapter.OnItemClickListenerTool):
    RecyclerView.Adapter<RecyclerView. ViewHolder>() {
    var quanlityFile = mutableMapOf<Int, Int>()
    var lastModified = ArrayList<String>()
    var arrayListCopy = ArrayList<File>()
    var isPosition: Int = 0
    val TYPE_A = 1
    val TYPE_B= 2
    val TYPE_MP3 = 3
    override fun getItemViewType(position: Int): Int {
        if (isList) {
            return TYPE_A
        } else if(!isList && check == "Hình ảnh" || check == "Video") {
            return TYPE_B
        }
        else if(check == "Âm nhạc" && !isList) {
            return TYPE_MP3
        }
        else{
            return TYPE_A
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView. ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return when(viewType){
            TYPE_A ->{
                val view = inflater.inflate(R.layout.item_image_recently_tool, parent, false)
                FileViewHolder1(view)
            }
            TYPE_B -> {
                val view = inflater.inflate(R.layout.item_image_recently_tools_gridview, parent, false)
                FileViewHolder2(view)
            }
            else ->{
                val view = inflater.inflate(R.layout.item_image_recently_tools_gridview, parent, false)
                FileViewHolder3(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.apply {
            when(holder){
                is FileViewHolder1 -> {
                    val f = fileImage[position]
                    val url: String = f.listImage[f.listImage.size-1].absolutePath
                    Glide
                        .with(context)
                        .load(url)
                        .centerCrop()
                        .into(holder.imgFile)
                    val fileItem = fileImage[position]

                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem,position)
                    }
                }
                is FileViewHolder2 ->{
                    val fileItem = fileImage[position]
                    val url: String = fileItem.listImage[fileItem.listImage.size-1].absolutePath
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
                    holder.tvSize.text= fileItem.listImage.size.toString()


                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem,position)
                    }


                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClickedTool(it!!, fileItem,position)
                    }
                }
                is FileViewHolder3 ->{
                    val fileItem = fileImage[position]
                    val url: String = fileItem.listImage[fileItem.listImage.size-1].absolutePath
                    holder.imgFile.setImageResource(R.drawable.zing)

                    holder.tvNameFile.text = fileItem.name
                    var date: Date = Date()
                    date.time = fileItem.lastModify // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)
                    lastModified.add(position, simpleDateFormat.format(date))


                    var length = 0
                    holder.tvSize.text= fileItem.listImage.size.toString()


                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem,position)
                    }


                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClickedTool(it!!, fileItem,position)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return fileImage.size
    }

    inner class FileViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgFile : ImageView = itemView.findViewById(R.id.imgRecently_tool)
    }
    inner class FileViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
                var tvNameFile : TextView =
                    itemView.findViewById(R.id.txtfileNameGrid)
                var tvDate: TextView = itemView.findViewById(R.id.txtTimeImgToolGrid)
                var imgFile : ImageView = itemView.findViewById(R.id.imgRecently_tool_grid)
                var tvMenu :TextView = itemView.findViewById(R.id.txtmenuToolImgGrid)
                var tvSize :TextView = itemView.findViewById(R.id.txtSizeImgToolGrid)

    }
    inner class FileViewHolder3(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNameFile : TextView =
            itemView.findViewById(R.id.txtfileNameGrid)
        var tvDate: TextView = itemView.findViewById(R.id.txtTimeImgToolGrid)
        var imgFile : ImageView = itemView.findViewById(R.id.imgRecently_tool_grid)
        var tvMenu :TextView = itemView.findViewById(R.id.txtmenuToolImgGrid)
        var tvSize :TextView = itemView.findViewById(R.id.txtSizeImgToolGrid)

    }
    interface OnItemClickListenerTool{
        fun onItemClickTool(file: FolerImage,position: Int)
        fun  onOptionsMenuClickedTool(view:View, file: FolerImage,position: Int)
    }

    fun updateDataTool(newList : List<FolerImage>){
        fileImage = newList as ArrayList<FolerImage>
        Log.d("yenn", "updateDataTool: "+newList.size)
        notifyDataSetChanged()
    }

}