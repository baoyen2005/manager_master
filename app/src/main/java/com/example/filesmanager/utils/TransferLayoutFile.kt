package com.example.filesmanager.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.RecentlyImageAdapter
import com.example.filesmanager.R
import java.io.File

class TransferLayoutFile(var imgTransfer: ImageView,
                         var imgOrder :ImageView,
                         var transferType :Boolean,
                         var isList :Boolean,
                         var check:String,
                         var mGridViewImgPhoto : RecyclerView,
                         var imgAdapter :RecentlyImageAdapter,
                         var listFile: ArrayList<File>,
                         var context :Context,
                         var lister : RecentlyImageAdapter.OnItemClickListenerTool
) {
    fun initTransfer() {

        imgTransfer.setOnClickListener {
            if (transferType) {
                transferType = false
                isList = true
                mGridViewImgPhoto.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                imgAdapter =
                    RecentlyImageAdapter(check, isList, context, listFile, lister)
                mGridViewImgPhoto.adapter = imgAdapter
                Log.d("islist", "getItemViewType: isList" + isList.toString())
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_linear)
            } else {
                isList = false
                transferType = true
                mGridViewImgPhoto.layoutManager =
                    GridLayoutManager(context, 2)
                imgAdapter =
                    RecentlyImageAdapter(check, isList, context, listFile, lister)
                mGridViewImgPhoto.adapter = imgAdapter
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_grid)


            }
        }

        imgOrder.setOnClickListener {
            val popup = PopupMenu(context, it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.tool_toolbar_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                if (item.itemId == R.id.orderByName_tool) {
                    listFile.sortBy { it.name.lowercase() }
                    imgAdapter.updateDataTool(listFile)
                    return@OnMenuItemClickListener true

                } else if (item.itemId == R.id.orderByTime_tool) {
                    listFile.sortByDescending { FileExtractUtils.getFileLastModified(it) }
                    imgAdapter.updateDataTool(listFile)

                    return@OnMenuItemClickListener true
                } else if (item.itemId == R.id.orderBySizeTool) {
                    listFile.sortByDescending { it.length()}
                    imgAdapter.updateDataTool(listFile)
                    return@OnMenuItemClickListener true
                }
                false
            })


        }
    }
}