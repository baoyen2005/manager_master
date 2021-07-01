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

class FileShare {
    @Throws(IOException::class)
    fun shareFile(context: Context, file: File) {
        val uri = uriFromFile(context,file)
        Log.d("uri", "shareFile+ uri: "+ uri.toString())
        var intent: Intent = Intent()
        intent.action = Intent.ACTION_SEND
        if (uri.toString().contains(".docx")) {
            intent.type = "application/msword"
        } else if (uri.toString().contains(".pdf")) {
            intent.type = "application/pdf"
        } else if (uri.toString().contains(".pptx")) {
            intent.type = "application/ppt"
        } else if (uri.toString().contains(".wav")) {
            intent.type = "audio/x-wav"
        } else if (uri.toString().contains(".zip") || uri.toString().contains(".rar")) {
            // WAV audio file
            intent.type = "application/x-wav";
        } else if (uri.toString().toLowerCase().contains(".jpeg")
            || uri.toString().toLowerCase().contains(".jpg")
            || uri.toString().toLowerCase().contains(".png")
        ) {
            intent.type = "image/jpeg"
        } else if (uri.toString().contains(".mp4")) {
            intent.type = "video/*"
        } else if (uri.toString().contains(".mp3")) {
            intent.type = "audio/*"
        } else {
            intent.type = "*/*"
        }
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            context.startActivity(Intent.createChooser(intent, "Chia sẻ qua"))
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
