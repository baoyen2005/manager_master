package com.example.filesmanager.utils

import android.annotation.SuppressLint
import android.util.Log
import com.example.filesmanager.Adapter.FileAdapter
import java.io.File

@SuppressLint("AppCompatCustomView")
class FindInformation (var file: File, var position:Int, var fileAdapter: FileAdapter){
    private var tvInformation :String = ""
    fun findInfor():String{
        val size  = file.length()//Byte
        var sizem :Double = 0.toDouble() // Mb
        var sizes :Double = 0.toDouble()
        var sizeMB :String =""
        var sizeGB:String=""

        if(size >= 1024){
            sizem = (size.toDouble()/1024)
            sizeMB += String.format("%.2f", sizem)

        }
        else if(size> 1024*1024){
            sizes = size.toDouble()/(1024*1024)
            sizeGB += String.format("%.2f", sizes)
        }
        if(file.isDirectory && size >= 1024*1024 && fileAdapter.quanlityFile.get(position)!! >1 ){
            Log.d("yen", fileAdapter.quanlityFile.toString())
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeGB MB"+
                    "\nNội dung: ${fileAdapter.quanlityFile.get(position)} files"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(file.isDirectory && size >= 1024*1024 && fileAdapter.quanlityFile.get(position)!! == 1 ){
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeGB /MB"+
                    "\nNội dung: ${fileAdapter.quanlityFile.get(position)} file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(file.isDirectory && size >= 1024*1024 && fileAdapter.quanlityFile.get(position)!! == 0 ){
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeGB MB"+
                    "\nNội dung: 0 file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified.get(position)}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && 1024 <= size && size< 1024*1024&& fileAdapter.quanlityFile.get(position)!!>1 ){
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeMB KB"+
                    "\nNội dung: ${fileAdapter.quanlityFile.get(position)!!} files"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && 1024 <= size && size< 1024*1024 && fileAdapter.quanlityFile.get(position)==1 ){
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeMB KB"+
                    "\nNội dung: ${fileAdapter.quanlityFile[position]} file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"
        }
        else  if(file.isDirectory && 1024 <= size && size< 1024*1024 && fileAdapter.quanlityFile.get(position)==0 ){
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $sizeMB KB"+
                    "\nNội dung: 0 file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"
        }
        else  if(file.isDirectory && size<1024&& fileAdapter.quanlityFile.get(position)==1 ){
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $size KB"+
                    "\nNội dung: ${fileAdapter.quanlityFile[position]} file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && size<1024&& fileAdapter.quanlityFile.get(position)!!>1 ){
            tvInformation+="${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $size B"+
                    "\nNội dung: ${fileAdapter.quanlityFile[position]} files"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else  if(file.isDirectory && size<1024&& fileAdapter.quanlityFile.get(position)==1 ){
            tvInformation+= "${file.name}" +
                    "\n\nKiểu: folder"+
                    "\nKích thước: $size B"+
                    "\nNội dung: 0 file"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(!file.isDirectory && size >= 1024*1024){
            tvInformation+= "${file.name}" +
                    "\n\nKiểu: file"+
                    "\nKích thước: ${fileAdapter.fileSize[position]}MB"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(!file.isDirectory && size>=1024 && size < 1024*1024){
            tvInformation += "${file.name}" +
                    "\n\nKiểu: file"+
                    "\nKích thước: ${fileAdapter.fileSize[position]} KB"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}"

        }
        else if(!file.isDirectory&&size<1024){
            tvInformation += ("${file.name}" +
                    "\n\nKiểu: file"+
                    "\nKích thước: ${fileAdapter.fileSize[position]} B"+
                    "\nSửa đổi lần cuối: ${fileAdapter.lastModified[position]}"+
                    "\nQuy trình: ${file.absolutePath}")
        }
        return tvInformation
    }
}