package com.example.filesmanager.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File
import java.io.IOException

class FileOpen {
    @Throws(IOException::class)
    public fun openFile(context: Context, file: File) {

        val uri = Uri.parse(file.absolutePath)
        val intent = Intent(Intent.ACTION_VIEW)
        if (uri.toString().contains(".doc")) {
            intent.setDataAndType(uri, "application/msword")
        } else if (uri.toString().contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf")
        } else if (uri.toString().contains(".wav"))
         {
            intent.setDataAndType(uri, "audio/x-wav")
        }
        else if(uri.toString().contains(".zip") || uri.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        }

        else if (uri.toString().toLowerCase().contains(".jpeg") || uri.toString().toLowerCase()
                .contains(".jpg") || uri.toString().toLowerCase().contains(".png")
        ) {
            intent.setDataAndType(uri, "image/jpeg")
        } else if (uri.toString().contains(".mp4")) {
            intent.setDataAndType(uri, "video/*")
        }
        else if (uri.toString().contains(".mp3")){
                intent.setDataAndType(uri,  "audio/*");
        }
        else {
            intent.setDataAndType(uri, "*/*")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}