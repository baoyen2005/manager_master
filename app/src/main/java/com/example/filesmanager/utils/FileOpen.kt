package com.example.filesmanager.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import com.example.filesmanager.BuildConfig
import java.io.File
import java.io.IOException

class FileOpen {
    @Throws(IOException::class)
    public fun openFile(context: Context, file: File) {

        val uri = uriFromFile(context,file)
        Log.d("uri", "readFile+ uri: "+ uri.toString())
        var intent: Intent = Intent()
        intent.action = Intent.ACTION_VIEW
        if (uri.toString().contains(".docx")) {
            intent.setDataAndType(uri,"application/msword")
        } else if (uri.toString().contains(".pdf")) {
            intent.setDataAndType(uri,"application/pdf")
        } else if (uri.toString().contains(".pptx")||uri.toString().contains(".ppt") ) {
            intent.setDataAndType(uri,"application/vnd.ms-powerpoint")
        }
        else if (uri.toString().contains(".xlsx") ) {
            intent.setDataAndType(uri,"application/vnd.ms-excel")
        }
        else if (uri.toString().contains(".wav")) {
            intent.setDataAndType(uri,"audio/x-wav")
        } else if (uri.toString().contains(".zip") || uri.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav")
        } else if (uri.toString().toLowerCase().contains(".jpeg")
            || uri.toString().toLowerCase().contains(".jpg")
            || uri.toString().toLowerCase().contains(".png")
        ) {
            intent.setDataAndType(uri,"image/jpeg")
        } else if (uri.toString().contains(".mp4")) {
            intent.setDataAndType(uri,"video/*")
        } else if (uri.toString().contains(".mp3")) {
            intent.setDataAndType(uri, "audio/*")
        } else {
            intent.setDataAndType(uri, "*/*")
        }

      //  intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
         try {
              context.startActivity(Intent.createChooser(intent, "Read by:"))
          } catch (e: Exception) {
               e.printStackTrace()
         }
    }
    fun uriFromFile(context: Context, file: File): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                return FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                )
            } catch (e: Exception) {
                return Uri.fromFile(file)
            }

        } else {
            return Uri.fromFile(file)
        }
    }
}