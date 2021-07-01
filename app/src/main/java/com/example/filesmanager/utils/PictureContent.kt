package com.example.filesmanager.utils

import android.net.Uri
import com.example.filesmanager.model.Image
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PictureContent {
   var items = ArrayList<Image>()
    fun loadSavedImages(dir: File) {
        items.clear()
        if (dir.exists()) {
            val files = dir.listFiles()
            for (file in files) {
                val absolutePath = file.absolutePath
                val extension = absolutePath.substring(absolutePath.lastIndexOf("."))
                if (extension == ".jpg") {
                    loadImage(file)
                }
            }
        }
    }

    private fun getDateFromUri(uri: Uri): String {
        val split = uri.path!!.split("/").toTypedArray()
        val fileName = split[split.size - 1]
        val fileNameNoExt = fileName.split("\\.").toTypedArray()[0]
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(Date(fileNameNoExt.toLong()))
    }

    fun loadImage(file: File?) {
        val newItem = Image()
        newItem.uri = Uri.fromFile(file)
       // newItem.date = getDateFromUri(newItem.uri)
       // addItem(newItem)
    }
}