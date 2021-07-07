package com.example.filesmanager.utils

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import com.example.filesmanager.model.App

abstract class AppAsynTask(val context: Context): AsyncTask<Unit, Unit, ArrayList<App>>() {

    override fun doInBackground(vararg params: Unit?): ArrayList<App> {
        val nonFilteredAppsTemp = ArrayList<App>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            val profiles = launcherApps.profiles
            for (userHandle in profiles) {
                val apps = launcherApps.getActivityList(null, userHandle)
                for (info in apps) {
                    val app = App(context.packageManager, info)
                    app.userHandle = userHandle
                    nonFilteredAppsTemp!!.add(app)
                }
                // appAdapter.updateDataApp(listApplication)
                Log.d("sssyen", "doInBackground: "+apps.size)
            }
        } else {
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val activitiesInfo =
                context.packageManager.queryIntentActivities(intent, 0)
            for (info in activitiesInfo) {
                val app = App(context.packageManager, info)
                nonFilteredAppsTemp!!.add(app)
            }

        }
        return nonFilteredAppsTemp
    }



}