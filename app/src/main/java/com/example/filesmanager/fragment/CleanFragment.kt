package com.example.filesmanager.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.filesmanager.R
import com.example.filesmanager.activity.MainActivity


class CleanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var canhbao : ImageView
    lateinit var btnNavigation : ImageView
    lateinit var drawerLayoutFile : DrawerLayout
    lateinit var btnDonDep :Button
    public fun newInstance(): CleanFragment {
        return CleanFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerLayoutFile = (requireActivity() as MainActivity).drawer!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clean, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        canhbao = view.findViewById(R.id.canhbaoClean)
        btnNavigation = view.findViewById(R.id.btnNavigationClean)
        btnDonDep = view.findViewById(R.id.btnDonDep)

        canhbao.setOnClickListener {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(intent)
        }
        btnNavigation.setOnClickListener{
            drawerLayoutFile.openDrawer(Gravity.START)
        }
        btnDonDep.setOnClickListener {
            Toast.makeText(context,"Feature is updating!", Toast.LENGTH_SHORT).show()
        }
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
    }

}