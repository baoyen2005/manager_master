package com.example.filesmanager.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.R
import com.example.filesmanager.model.App
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppAdapter(
    private var isList: Boolean,
    private var context: Context,
    var appList: ArrayList<App>,
    private val listener: AppAdapter.OnItemClickListenerApp
    ): RecyclerView.Adapter<RecyclerView. ViewHolder>() {

    var quanlityFile = mutableMapOf<Int, Int>()
    var lastModified = ArrayList<String>()
    var arrayListCopy = ArrayList<File>()
    var isPosition: Int = 0
    val TYPE_A = 1
    val TYPE_B= 2
    private val mAppList by lazy { ArrayList<App>() }
    override fun getItemViewType(position: Int): Int {
        if (isList) {
            return TYPE_A
        } else {
            return TYPE_B
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
                val view = inflater.inflate(R.layout.item_app_gridview, parent, false)
                FileViewHolder2(view)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Log.w("yennnn", "onBindViewHolder: " + appList.size )
        Log.w("yennnn", "--------------------------------: " + position )
        holder.setIsRecyclable(false)
        holder.apply {
            when(holder){
                is FileViewHolder1 -> {
//                    val f = appList[position]
//
//
//                    val fileItem = fileImage[position]
//
//                    holder.itemView.setOnClickListener {
//                        listener.onItemClickTool(fileItem,position)
//                    }
                }
                is FileViewHolder2 ->{
                    val fileItem = appList[position]
                    holder.imgFile.setImageDrawable(fileItem.icon)

                    holder.tvNameFile.text = fileItem.label
                    var date: Date = Date()
                    //date.time = fileItem.date // tra ve int
                    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")

                    holder.tvDate.text = fileItem.date.toString()

                    //lastModified.add(position, simpleDateFormat.format(date))
                    holder.tvSize.text= fileItem.sizeee

                    holder.tvMenu.setOnClickListener {
                        listener.onOptionsMenuClickedApp(it!!, fileItem,position)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        Log.e("yennnn", "getItemCount: " + appList.size )
        return appList.size
    }

    inner class FileViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //var imgFile : ImageView = itemView.findViewById(R.id.imgRecently_tool)
    }
    inner class FileViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
                var tvNameFile : TextView =
                    itemView.findViewById(R.id.txtAppNameGridview)
                var tvDate: TextView = itemView.findViewById(R.id.txtTimeAppGridview)
                var imgFile : ImageView = itemView.findViewById(R.id.imgApp_grid)
                var tvMenu :TextView = itemView.findViewById(R.id.txtmenuAppGridview)
                var tvSize :TextView = itemView.findViewById(R.id.txtSizeAppGridview)

    }

    interface OnItemClickListenerApp{
        fun  onOptionsMenuClickedApp(view:View, file: App,position: Int)
    }

    fun updateDataApp(newList : List<App>){
        val newList1 = arrayListOf<App>()
        newList1.addAll(newList)

         appList.clear()
        appList.addAll(newList1)
//        appList = newList as ArrayList<App>
        Log.d("yenn", "updateDataTool: "+newList.size)
        notifyDataSetChanged()
    }

}