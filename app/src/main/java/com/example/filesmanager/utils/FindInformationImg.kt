package com.example.filesmanager.utils

import android.annotation.SuppressLint
import java.io.File

@SuppressLint("AppCompatCustomView")
class FindInformationImg (var file: File,var position :Int){
    private var tvInformation :String = ""

    fun sizeFolder(file: File):Long{
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
        var size  :Long = 0
        if(file.isDirectory){
            size = sizeFolder(file)//Byte
        }
        else size = file.length()
        val fsize = FileExtractUtils.getFileSize(file)
        if(file.isDirectory && size >= 1024*1024 && FileExtractUtils.getQuanlityFile(file)!! >1 ){
           
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} MB"+
                    "\nContent: ${FileExtractUtils.getQuanlityFile(file)} files"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(file.isDirectory && size >= 1024*1024 && FileExtractUtils.getQuanlityFile(file)!! == 1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} /MB"+
                    "\nContent: ${FileExtractUtils.getQuanlityFile(file)} file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(file.isDirectory && size >= 1024*1024 && FileExtractUtils.getQuanlityFile(file)!! == 0 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} MB"+
                    "\nContent: 0 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory && 1024 <= size && size< 1024*1024&& FileExtractUtils.getQuanlityFile(file)!!>1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} KB"+
                    "\nContent: ${FileExtractUtils.getQuanlityFile(file)!!} files"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory && 1024 <= size && size< 1024*1024 && FileExtractUtils.getQuanlityFile(file)==1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} KB"+
                    "\nContent: 1 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"
        }
        else  if(file.isDirectory && 1024 <= size && size< 1024*1024 && FileExtractUtils.getQuanlityFile(file)==0 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} KB"+
                    "\nContent: 0 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"
        }
        else  if(file.isDirectory && size<1024&& FileExtractUtils.getQuanlityFile(file)==1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} KB"+
                    "\nContent: 1 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory && size<1024&& FileExtractUtils.getQuanlityFile(file)!!>1 ){
            tvInformation+="${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} B"+
                    "\nContent: ${FileExtractUtils.getQuanlityFile(file)} files"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else  if(file.isDirectory && size<1024&& FileExtractUtils.getQuanlityFile(file)==1 ){
            tvInformation+= "${file.name}" +
                    "\n\nType: folder"+
                    "\nSize: ${String.format("%.2f", fsize)} B"+
                    "\nContent: 0 file"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(!file.isDirectory && size >= 1024*1024){
            tvInformation+= "${file.name}" +
                    "\n\nType: file"+
                    "\nSize: ${String.format("%.2f", fsize)} MB"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(!file.isDirectory && size>=1024 && size < 1024*1024){
            tvInformation += "${file.name}" +
                    "\n\nType: file"+
                    "\nSize: ${String.format("%.2f", fsize)} KB"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}"

        }
        else if(!file.isDirectory&&size<1024){
            tvInformation += ("${file.name}" +
                    "\n\nType: file"+
                    "\nSize: ${String.format("%.2f", fsize)} B"+
                    "\nLast Modified: ${FileExtractUtils.getFileLastModified(file)}"+
                    "\nProcedure: ${file.absolutePath}")
        }
        return tvInformation
    }
}