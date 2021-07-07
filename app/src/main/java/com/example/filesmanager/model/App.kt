package com.example.filesmanager.model

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.LauncherActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.UserHandle
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class App {
    var icon: Drawable
    var label: String
    var date: String = ""
    var packageName: String
    var className: String
    var userHandle: UserHandle? = null
    var sizeee: String =""

    constructor(pm: PackageManager, info: ResolveInfo) {
        icon = info.loadIcon(pm)

        label = info.loadLabel(pm).toString()
        packageName = info.activityInfo.packageName
        className = info.activityInfo.name

        Log.d("sizeeee", "class:  " + className)
        val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        var fdate :Date= Date(packageInfo.firstInstallTime)
        val dateFormat = SimpleDateFormat("dd-MM")

        date += dateFormat.format(fdate)
        Log.d("sizeeee", "Installed time:  " + date)

    }


    @SuppressLint("NewApi")
    constructor(pm: PackageManager, info: LauncherActivityInfo) {
        icon = info.getIcon(0)
        label = info.label.toString()
        packageName = info.componentName.packageName
        className = info.name
        var fdate :Date= Date(info.firstInstallTime)
        val dateFormat = SimpleDateFormat("dd-MM")

        date += dateFormat.format(fdate)
        var file = File(pm.getApplicationInfo(packageName,0).publicSourceDir);
        val size  = file.length().toInt()//Byte
        Log.d("sizeeee", "size: "+ size)
        var sizeMB :Double = 0.toDouble() // Mb
        var sizeGB:Double = 0.toDouble() // GB
        if(size < 1024){
            sizeee += size.toString()+"B"
        }

        else if(size >= 1024 && size < 1024*1024){
            sizeMB = (size/1024).toDouble()
            sizeee += String.format("%.2f", sizeMB)  + "KB"

        }
        else {
            sizeGB = (size)/(1024*1024).toDouble()
            sizeee += String.format("%.2f", sizeGB)  + "MB"
        }

    }
    override fun equals(`object` : Any?): Boolean {
        return if (`object` is App) {
            packageName == `object`.packageName
        } else {
            false
        }
    }

    val componentName: String
        get() = ComponentName(packageName, className).toString()
}