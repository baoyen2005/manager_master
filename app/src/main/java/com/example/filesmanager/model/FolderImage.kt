package com.example.filesmanager.model

import java.io.File

data class FolerImage(val path : String){
    val file = File(path)
    val listImage = ArrayList<File>()
    val name = file.name
    val lastModify = file.lastModified()
}