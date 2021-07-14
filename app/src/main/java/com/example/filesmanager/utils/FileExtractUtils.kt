package com.example.filesmanager.utils

import java.io.File
import java.text.SimpleDateFormat

object FileExtractUtils {

    fun getFileName(file: File): String {
        if (file.name.isNullOrBlank())
            return "un Name File"
        return file.name
    }

    fun getFileLastModified(file: File): String {
        val dateLong = file.lastModified()
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM")
        var date = simpleDateFormat.format(dateLong)
        return date
    }

    fun getQuanlityFile(file: File): Int {
        var length = 0
        val files = file.listFiles()
        if (files != null) {
            length = files.size
            if (length >= 1) {
                if (length == 1) {
                    return 1
                } else {
                    return length
                }
            } else {
                return 0
            }
        }
        else {
            return 0
        }
    }
    fun getFileSize(file:File):Double{

        var size: Long
        if (file.isDirectory) {
            size = 0
            for (child in file.listFiles()) {
                size += child.length()
            }
        } else {
            size = file.length()
        }
        if (size < 1024) {
            return size.toDouble()
        } else if (size >= 1024 && size < 1024 * 1024) {
            return size.toDouble()/1024
        } else {
            return size.toDouble() / (1024 * 1024)
        }
    }
}