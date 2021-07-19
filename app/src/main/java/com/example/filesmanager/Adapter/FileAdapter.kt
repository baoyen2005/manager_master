package com.example.filesmanager.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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
import kotlin.collections.ArrayList

class FileAdapter(
    var check: Int,
    var back: Boolean,
    var isList: Boolean,
    private var context: Context,
    private var fileList: ArrayList<File>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() ,Filterable{
    val TYPE_A = 1
    val TYPE_B = 2
    val TYPE_C = 3
    var arrayListCopy = ArrayList<File>()


    override fun getItemViewType(position: Int): Int {
        if (isList && back && check == 1) {
            return TYPE_A
        } else if (check == 3) {
            return TYPE_C
        } else {
            return TYPE_B
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return when (viewType) {
            TYPE_A -> {
                val view = inflater.inflate(R.layout.file_container, parent, false)
                FileViewHolder1(view)
            }
            TYPE_B -> {
                val view = inflater.inflate(R.layout.file_container_file2, parent, false)
                FileViewHolder2(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.file_container_file2, parent, false)
                FileViewHolder3(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.apply {
            when (holder) {
                is FileViewHolder1 -> {
                    val fileItem = fileList[position]
                        holder.tvName.text = fileItem.name

                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                   if(fileItem.isDirectory){
                       holder.tvTime.text = ""
                   }
                    else {
                       holder.tvTime.text = simpleDateFormat.format(date)
                   }
                    if (!fileList[position].isDirectory) {

                        val ext = fileItem.extension
                        when (ext) {
                            "mp3" -> holder.imgFile.setImageResource(R.drawable.nhacmp3)

                            "gif" -> holder.imgFile.setImageResource(R.drawable.gif)

                            "apk" -> holder.imgFile.setImageResource(R.drawable.apk)

                            "jpeg", "jpg", "png" ,"mp4"-> {
                                val url: String = fileItem.absolutePath
                                Glide
                                    .with(context)
                                    .load(File(url))
                                    .centerCrop()
                                    .into(holder.imgFile)
                            }
                            "pdf" -> holder.imgFile.setImageResource(R.drawable.pdf)
                            "docx", "doc" -> holder.imgFile.setImageResource(R.drawable.doc)
                            "pptx", "ppt" -> holder.imgFile.setImageResource(R.drawable.ppt)
                            "xlsx", "xls" -> holder.imgFile.setImageResource(R.drawable.excel)
                            "txt" -> holder.imgFile.setImageResource(R.drawable.txt)
                            "wav" -> holder.imgFile.setImageResource(R.drawable.wav)
                            "webp" -> holder.imgFile.setImageResource(R.drawable.webp)
                            "xml" -> holder.imgFile.setImageResource(R.drawable.xml)
                            else -> holder.imgFile.setImageResource(R.drawable.unknow)
                        }
                    }

                    var length = 0
                    if (fileItem.isDirectory) {
                        val quanlity = FileExtractUtils.getQuanlityFile(fileItem)
                        if(quanlity == 0){
                            holder.tvSize.text = "empty"
                        }
                        else if(quanlity == 1){
                            holder.tvSize.text = "1 file"
                        }
                        else {
                            holder.tvSize.text = "$quanlity files"
                        }

                    } else {

                        val files = fileList[position]
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

                        listener.onItemClick(fileItem, position)
                    }
                    holder.tvOption.setOnClickListener {
                        listener.onOptionsMenuClicked(it!!, fileItem, position)
                    }
                }
                is FileViewHolder2 -> {
                    val fileItem = fileList[position]

                    holder.tvNameFile.text = fileItem.name

                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)

                    holder.itemView.setOnClickListener {
                        listener.onItemClick(fileItem, position)
                    }

                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClicked(it!!, fileItem, position)
                    }
                }
                is FileViewHolder3 -> {
                    val fileItem = fileList[position]
                    holder.tvNameFile.text = fileItem.name

                    var date: Date = Date()
                    date.time = fileItem.lastModified() // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
                    holder.tvDate.text = simpleDateFormat.format(date)
                    holder.itemView.setOnClickListener {
                        listener.onItemClick(fileItem, position)
                    }
                    holder.imgAnh.setImageResource(R.drawable.doccccccc)

                    var length = 0

                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClicked(it!!, fileItem, position)
                    }
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    inner class FileViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView =
            itemView.findViewById(R.id.tv_fileName)
        var tvSize: TextView = itemView.findViewById(R.id.tvFileSize)

        var tvTime: TextView = itemView.findViewById(R.id.tv_time)

        var container: ConstraintLayout =
            itemView.findViewById(R.id.container)
        var imgFile: ImageView = itemView.findViewById(R.id.img_fileType)
        var tvOption: TextView = itemView.findViewById(R.id.textViewOptions)


    }

    inner class FileViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvNameFile: TextView =
            itemView.findViewById(R.id.txtfileName)
        var tvDate: TextView = itemView.findViewById(R.id.tv_date)
        var container2: ConstraintLayout =
            itemView.findViewById(R.id.container2)
        var tvMenu: TextView = itemView.findViewById(R.id.txtmenu)


    }

    inner class FileViewHolder3(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvNameFile: TextView =
            itemView.findViewById(R.id.txtfileName)
        var tvDate: TextView = itemView.findViewById(R.id.tv_date)
        var container2: ConstraintLayout =
            itemView.findViewById(R.id.container2)
        var tvMenu: TextView = itemView.findViewById(R.id.txtmenu)
        var imgAnh: ImageView = itemView.findViewById(R.id.imgAnhFile)

    }

    interface OnItemClickListener {
        fun onItemClick(file: File, position: Int)
        fun onOptionsMenuClicked(view: View, file: File, position: Int)
    }

    fun updateData(newList: List<File>) {
        fileList = newList as ArrayList<File>
        Log.d("aaa", "updateData: " + newList.size)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch== null ||charSearch.isEmpty()){
                    arrayListCopy.clear()
                    arrayListCopy.addAll(fileList)
                    Log.d("filter", "performFiltering: filelist "+ fileList.size)
                    Log.d("filter", "performFiltering: "+ arrayListCopy)
                }
                else{
                    val result = ArrayList<File>()
                    for(file in fileList){
                        if (file.name.lowercase().contains(charSearch.lowercase())) {
                            val boolean = file.name.lowercase().contains(charSearch.lowercase())
                            result.add(file)
                            Log.d("filter", "performFiltering if not null: "+ file.name)
                        }
                    }
                    arrayListCopy.clear()
                    arrayListCopy.addAll(result)
                    Log.d("filter", "performFiltering: if not null "+ arrayListCopy)
                }
                val filterResut = FilterResults()
                filterResut.values = arrayListCopy
                return filterResut
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               var newList = ArrayList<File>()
                newList.clear()
                newList.addAll(results!!.values as ArrayList<File>)
                updateData(newList)
            }

        }
    }
//    fun filter(charSequence: String) {
//        arrayListCopy.clear()
//        arrayListCopy.addAll(fileList)
//        Log.d("noti", arrayListCopy.toString())
//        var tempArraylist = ArrayList<File>()
//        tempArraylist.clear()
//        for (file in fileList) {
//            if (file.name.lowercase(Locale.getDefault()).contains(charSequence)) {
//                    tempArraylist.add(file)
//            }
//        }
//        arrayListCopy.clear()
//        arrayListCopy.addAll(tempArraylist)
//        tempArraylist.clear()
//        updateData(tempArraylist)
//    }




}