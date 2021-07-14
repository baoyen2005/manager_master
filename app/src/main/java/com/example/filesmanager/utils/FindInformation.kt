package com.example.filesmanager.utils

import android.annotation.SuppressLint
import android.util.Log
import java.io.File

@SuppressLint("AppCompatCustomView")
class FindInformation (var file: File, var position: Int){
    private var tvInformation :String = ""

    fun sizeFolder(file: File) :Long{
        var lenght: Long =0
        for (child in file.listFiles()) {
            if(child.isDirectory){
                lenght += sizeFolder(child)
            }
            else lenght += child.length()
        }
        return lenght
    }
    fun findInfor():String{
        val size  = FileExtractUtils.getFileSize(file)
        var lenght :Long = 0
        if(file.isDirectory){
           lenght = sizeFolder(file)
        }
        else lenght = file.length()
        Log.d("size", "findInfor: "+ size)
        if(file.isDirectory && lenght >= 1024*1024 && FileExtractUtils.getQuanlityFile(file) >1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} MB"+
                    "\nContent: ${FileExtractUtils.getQuanlityFile(file)} files"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(file.isDirectory && lenght >= 1024*1024 && FileExtractUtils.getQuanlityFile(file) == 1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} MB"+
                    "\nContent: $1 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(file.isDirectory && lenght >= 1024*1024 && FileExtractUtils.getQuanlityFile(file) == 0 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} MB"+
                    "\nContent: 0 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory &&  lenght >=1024 && lenght < 1024*1024&& FileExtractUtils.getQuanlityFile(file)>1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} KB"+
                    "\nContent: ${FileExtractUtils.getQuanlityFile(file)} files"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory && lenght >=1024 && lenght < 1024*1024 && FileExtractUtils.getQuanlityFile(file)==1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} KB"+
                    "\nContent: 1 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"
        }
        else if(file.isDirectory && lenght >=1024 && lenght < 1024*1024 && FileExtractUtils.getQuanlityFile(file)==0 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} KB"+
                    "\nContent: 0 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"
        }
        else  if(file.isDirectory && lenght<=1024 && FileExtractUtils.getQuanlityFile(file)==1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} KB"+
                    "\nContent: 1 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory && lenght<=1024&& FileExtractUtils.getQuanlityFile(file) >1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} B"+
                    "\nContent: ${FileExtractUtils.getQuanlityFile(file)} files"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory && lenght <1024&& FileExtractUtils.getQuanlityFile(file)==1 ){
            tvInformation+= "${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", size)} B"+
                    "\nContent: 0 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(!file.isDirectory && file.length() >= 1024*1024){
            tvInformation+= "${file.name}" +
                    "\n\nType: file"+
                    "\nSize: ${String.format("%.2f", size)} MB"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(!file.isDirectory && file.length()>=1024 && file.length() < 1024*1024){
            tvInformation += "${file.name}" +
                    "\n\nType: file"+
                    "\nSize: ${String.format("%.2f", size)} KB"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(!file.isDirectory&&file.length()<1024){
            tvInformation += ("${file.name}" +
                    "\n\nType: file"+
                    "\nSize: ${String.format("%.2f", size)} B"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}")
        }
        return tvInformation
    }
}