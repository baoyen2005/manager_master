package com.example.filesmanager.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.filesmanager.R
import com.example.filesmanager.fragment.ToolFragment
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class RecentlyImageAdapter(var back:Boolean, var isList:Boolean, private var context: Context, private var fileImage: ArrayList<File>, private val listener: RecentlyImageAdapter.OnItemClickListenerTool):
    RecyclerView.Adapter<RecyclerView. ViewHolder>() {

    var quanlityFile = mutableMapOf<Int, Int>()
    var lastModified = ArrayList<String>()
    var arrayListCopy = ArrayList<File>()
    var isPosition: Int = 0
    val TYPE_A = 1
    val TYPE_B= 2

    override fun getItemViewType(position: Int): Int {
        return if (isList&& back) {
            TYPE_A
        } else {
            TYPE_B
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView. ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return when(viewType){
            TYPE_A ->{
                val view = inflater.inflate(R.layout.item_image_recently_tool, parent, false)
                FileViewHolder1(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_image_recently_tools_gridview, parent, false)
                FileViewHolder2(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.apply {
            when(holder){
                is FileViewHolder1 -> {
                    val f = fileImage[position]
                    val url: String = f.absolutePath
                    val myFragment= ToolFragment()
                    Glide
                        .with(context)
                        .load(File(url))
                        .centerCrop()
                        .into(holder.imgFile)
                    val fileItem = fileImage[position]

                    holder.itemView.setOnClickListener {
                        listener.onItemClickTool(fileItem,position)
                    }
                }
                is FileViewHolder2 ->{
                    val fileItem = fileImage[position]
                    holder.imgFile.setImageURI(Uri.parse(fileImage[position].toString()))
                    holder.tvNameFile.text = fileItem.name
                    var date: Date = Date()
                    //holder.tvDate.text =fileImage[position].date
                    holder.tvNameFile.text = fileImage[position].name
                    //holder.tvSize.text = fileImage[position].size.toString()
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
    interface OnItemClickListenerTool{
        fun onItemClickTool(file: File,position: Int)
        fun  onOptionsMenuClickedTool(view:View, file: File,position: Int)
    }

    fun updateDataTool(newList : List<File>){
        fileImage = newList as ArrayList<File>
        Log.d("yenn", "updateData: "+newList.size)
        notifyDataSetChanged()
    }

}