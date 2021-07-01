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

class TypeAdapter(private var context: Context, private var typeList: ArrayList<Type>,private val listener: OnItemClickListenerType):
    RecyclerView.Adapter<TypeAdapter. TypeViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type_tools, parent, false)
        return TypeViewHolder(view)

    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.imgTypeTool.setImageResource(typeList[position].image)
        holder.txtName.text = typeList[position].name
        holder.itemView.setOnClickListener {
            listener.onItemClickType(typeList[position],position)
        }
    }
    override fun getItemCount(): Int {
        return typeList.size
    }

    inner class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgTypeTool : ImageView = itemView.findViewById(R.id.imgType_tool)
        var txtName :TextView = itemView.findViewById(R.id.txtNameType_Tool)

    }
    interface OnItemClickListenerType{
        fun onItemClickType(type: Type,position: Int)
      //  fun  onOptionsMenuClickedType(view:View, type: Type,position: Int)
    }
    fun updateDataType(newList : List<Type>){
        typeList = newList as ArrayList<Type>
        Log.d("yenn", "updateDataType : "+newList.size)
        notifyDataSetChanged()
    }


}