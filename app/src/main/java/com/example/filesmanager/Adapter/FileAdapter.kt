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
import com.example.filesmanager.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FileAdapter (var back:Boolean,var isList :Boolean ,private var context: Context, private var fileList: ArrayList<File>, private val listener:OnItemClickListener):
    RecyclerView.Adapter<RecyclerView. ViewHolder>() {

    var quanlityFile = mutableMapOf<Int, Int>()
    var lastModified = ArrayList<String>()
    var arrayListCopy = ArrayList<File>()
    var isPosition: Int = 0
    //var isList :Boolean = false
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
                val view = inflater.inflate(R.layout.file_container, parent, false)
                FileViewHolder1(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.file_container_file2, parent, false)
                FileViewHolder2(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.apply {
            when(holder){
                is FileViewHolder1 -> {
                    val fileItem = fileList[position]
                    holder.tvName.text = fileItem.name
                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvTime.text = simpleDateFormat.format(date)
                    lastModified.add(position, simpleDateFormat.format(date))
                    if(!fileList[position].isDirectory) {


                        if (fileList[position].name.lowercase(Locale.getDefault())
                                .endsWith(".mp3") || fileList[position].name.lowercase(
                                Locale.getDefault()
                            ).endsWith(".mp4")
                        ) {
                            holder.imgFile.setImageResource(R.drawable.mp3)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".gif")) {
                            holder.imgFile.setImageResource(R.drawable.gif)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".apk")) {
                            holder.imgFile.setImageResource(R.drawable.apk)
                        } else if (fileList[position].name.lowercase(Locale.getDefault())
                                .endsWith(".jpeg") || (fileList[position].name.lowercase(
                                Locale.getDefault()
                            )
                                .endsWith(".jpg"))
                            || fileList[position].name.lowercase(Locale.getDefault()).endsWith(".png")
                        ) {
                            holder.imgFile.setImageResource(R.drawable.jpg)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".pdf")) {
                            holder.imgFile.setImageResource(R.drawable.pdf)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".docx")) {
                            holder.imgFile.setImageResource(R.drawable.doc)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".pptx")) {
                            holder.imgFile.setImageResource(R.drawable.ppt)
                        } else if (fileList[position].name.lowercase(Locale.getDefault())
                                .endsWith(".xlsx") || fileList[position].name.lowercase(
                                Locale.getDefault()
                            )
                                .endsWith(".xls")
                        ) {
                            holder.imgFile.setImageResource(R.drawable.excel)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".txt")) {
                            holder.imgFile.setImageResource(R.drawable.txt)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".wav")) {
                            holder.imgFile.setImageResource(R.drawable.wav)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".webp")) {
                            holder.imgFile.setImageResource(R.drawable.webp)
                        } else if (fileList[position].name.lowercase(Locale.getDefault()).endsWith(".xml")) {
                            holder.imgFile.setImageResource(R.drawable.xml)
                        } else {
                            holder.imgFile.setImageResource(R.drawable.unknow)
                        }
                    }

                    var length = 0
                    if (fileItem.isDirectory) {
                        val files = fileItem.listFiles()
                        if (files != null) {
                            length = files.size
                            Log.d("aaa", "onBindViewHolder: " + length)
                            if (length >= 1) {
                                if (length == 1) {
                                    holder.tvSize.text = "1 file"
                                    quanlityFile.put(position,1)
                                } else {
                                    holder.tvSize.text = "$length files"
                                    quanlityFile.put(position,length)
                                }
                            } else {
                                holder.tvSize.text = "empty"
                                if(holder.tvSize.text == "empty")  quanlityFile.put(position ,0)
                            }
                        }else {
                            holder.tvSize.text = "empty"
                            //quanlityFile.clear()
                            if(holder.tvSize.text == "empty") quanlityFile.put(position,0)
                        }
                        Log.d("bbbb", quanlityFile.toString())

                    }
                    else{
                        val files = fileList[position]
                        val size  = files.length()
                        if(size >= 1024){
                            var sizeF :Double = (size/1024).toDouble()
                            holder.tvSize.text = "$sizeF Mb"
                        }
                        else
                        {
                            holder.tvSize.text = "$size Kb"
                        }
                    }
                    //quanlityFile = 0
                    // ko phai thu muc thi ko cho an tiep
                    holder.itemView.setOnClickListener {

                            listener.onItemClick(fileItem,position)
                    }
                    holder.tvOption.setOnClickListener {
                        listener.onOptionsMenuClicked(it!!, fileItem,position)
                    }
                }
                is FileViewHolder2 ->{
                    val fileItem = fileList[position]
                    holder.tvNameFile.text = fileItem.name
                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)
                    lastModified.add(position, simpleDateFormat.format(date))
                    holder.itemView.setOnClickListener {
                        listener.onItemClick(fileItem,position)
                    }

                    var length = 0
                    if (fileItem.isDirectory) {
                        val files = fileItem.listFiles()
                        if (files != null) {
                            length = files.size
                            Log.d("aaa", "onBindViewHolder: " + length)
                            if (length >= 1) {
                                if (length == 1) {

                                    quanlityFile.put(position,1)
                                } else {

                                    quanlityFile.put(position,length)
                                }
                            } else {

                                 quanlityFile.put(position ,0)
                            }
                        }else {
                         quanlityFile.put(position,0)
                        }
                        Log.d("bbbb", quanlityFile.toString())

                    }

                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClicked(it!!, fileItem,position)
                    }
                }
            }
        }

//        holder.itemView.setOnClickListener {
//
//            listener.onItemClick(fileList[position])
//        }
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    inner class FileViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName : TextView =
            itemView.findViewById(R.id.tv_fileName)
        var tvSize : TextView = itemView.findViewById(R.id.tvFileSize)

        var tvTime: TextView = itemView.findViewById(R.id.tv_time)

        var container : ConstraintLayout =
            itemView.findViewById(R.id.container)
        var imgFile : ImageView = itemView.findViewById(R.id.img_fileType)
        var tvOption :TextView = itemView.findViewById(R.id.textViewOptions)



    }
    inner class FileViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {

                var tvNameFile : TextView =
                    itemView.findViewById(R.id.txtfileName)
                var tvDate: TextView = itemView.findViewById(R.id.tv_date)
                var container2 : ConstraintLayout =
                    itemView.findViewById(R.id.container2)
                //var imgFile : ImageView = itemView.findViewById(R.id.img_fileType)
                var tvMenu :TextView = itemView.findViewById(R.id.txtmenu)



    }
    interface OnItemClickListener{
        fun onItemClick(file: File, position: Int)
        fun  onOptionsMenuClicked(view:View, file: File,position: Int)
      //  fun onBackPressed()
    }

    fun updateData(newList : List<File>){
        fileList = newList as ArrayList<File>
        Log.d("aaa", "updateData: "+newList.size)
        notifyDataSetChanged()
    }
    fun filter(charSequence: String){
        arrayListCopy.clear()
        arrayListCopy.addAll(fileList)
        Log.d("noti", arrayListCopy.toString())
        var tempArraylist = ArrayList<File>()
        tempArraylist.clear()
        if(charSequence.isNotEmpty()){
            for(file in fileList){
                if(file.name.lowercase(Locale.getDefault()).contains(charSequence)){
                    tempArraylist.add(file)
                }
            }
        }
        else{
            tempArraylist.addAll(arrayListCopy)
        }
        fileList.clear()
        fileList.addAll(tempArraylist)
        notifyDataSetChanged()
        tempArraylist.clear()
    }
//    enum class ViewType(val type:Int){
//        TYPE_ONE(0),
//        TYPE_TWO(1)
//    }
}