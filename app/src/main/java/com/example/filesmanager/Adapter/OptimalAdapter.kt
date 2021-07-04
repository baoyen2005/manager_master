package com.example.filesmanager.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filesmanager.R
import com.example.filesmanager.model.Type

class OptimalAdapter(private var context: Context, private var optimalList: ArrayList<Type>, private val listener: OnItemClickListenerOptimal):
    RecyclerView.Adapter<OptimalAdapter. TypeViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_optimal_phone_item, parent, false)
        return TypeViewHolder(view)

    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.imgToiUutool.setImageResource(optimalList[position].image)
        holder.txtSaveTool.text = optimalList[position].name
        holder.itemView.setOnClickListener {
            listener.onItemClickOptimal(optimalList[position],position)
        }
    }
    override fun getItemCount(): Int {
        return optimalList.size
    }

    inner class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgToiUutool : ImageView = itemView.findViewById(R.id.imgToiUu_tool)
        var txtSaveTool :TextView = itemView.findViewById(R.id.txtSave_Tool)

    }
    interface OnItemClickListenerOptimal{
        fun onItemClickOptimal(type: Type,position: Int)
      //  fun  onOptionsMenuClickedType(view:View, type: Type,position: Int)
    }
    fun updateDataOptimal(newList : List<Type>){
        optimalList = newList as ArrayList<Type>
        Log.d("yenn", "updateDataType : "+newList.size)
        notifyDataSetChanged()
    }


}