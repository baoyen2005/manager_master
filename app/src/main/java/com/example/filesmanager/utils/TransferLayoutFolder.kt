package com.example.filesmanager.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.Adapter.ImageAdapter
import com.example.filesmanager.R
import com.example.filesmanager.model.FolerImage

class TransferLayoutFolder(var imgTransfer: ImageView,
                           var imgOrder :ImageView,
                           var transferType :Boolean,
                           var isList :Boolean,
                           var mGridViewImgPhoto : RecyclerView,
                           var imgAdapter :ImageAdapter,
                           var listFolderImage: ArrayList<FolerImage>,
                           var context :Context,
                           var lister : ImageAdapter.OnItemClickListenerTool,
                           var check :String
) {
    fun initTransfer() {

        imgTransfer.setOnClickListener {
            if (transferType) {
                transferType = false
                isList = true
                mGridViewImgPhoto.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                imgAdapter =
                    ImageAdapter(check, isList, context, listFolderImage, lister)
                mGridViewImgPhoto.adapter = imgAdapter
                Log.d("islist", "getItemViewType: isList" + isList.toString())
                imgTransfer.setImageResource(R.drawable.ic_baseline_view_linear)
            } else {
                isList = false
                transferType = true
                mGridViewImgPhoto.layoutManager =
                    GridLayoutManager(context, 2)
                imgAdapter =
                    ImageAdapter(check, isList, context, listFolderImage, lister)
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
                    listFolderImage.sortBy { it.name.lowercase() }
                    imgAdapter.updateDataTool(listFolderImage)
                    return@OnMenuItemClickListener true

                } else if (item.itemId == R.id.orderByTime_tool) {
                    listFolderImage.sortByDescending { FileExtractUtils.getFileLastModified(it.file) }
                    imgAdapter.updateDataTool(listFolderImage)

                    return@OnMenuItemClickListener true
                } else if (item.itemId == R.id.orderBySizeTool) {
                    var checkIsDirectory = false
                    listFolderImage.sortByDescending { it.listImage.size}
                    imgAdapter.updateDataTool(listFolderImage)
                    return@OnMenuItemClickListener true
                }
                false
            })


        }
    }
}