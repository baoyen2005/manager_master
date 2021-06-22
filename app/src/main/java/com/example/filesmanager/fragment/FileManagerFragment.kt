package com.example.filesmanager.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.filesmanager.R



class FileManagerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trans = childFragmentManager.beginTransaction()
        val fragment = FileFragment()
        trans.replace(R.id.host_frg, fragment)
        trans.commit()
    }

    fun goNextFolder(filePath : String){
        val trans = childFragmentManager.beginTransaction()
        var bundle = Bundle()
        bundle.putString("path",filePath)
        val fragment = FileFragment()
        fragment.arguments = bundle
        trans.replace(R.id.host_frg, fragment)
        trans.addToBackStack(null)
        trans.commit()
    }

}