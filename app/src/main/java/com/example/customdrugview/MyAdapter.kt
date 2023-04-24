package com.example.customdrugview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: ChenGuiPing
 * Date: 2022/9/19
 * Description:
 */
class MyAdapter(private val titles: List<String>, private val context: Context) :
    RecyclerView.Adapter<MyAdapter.MyHolder>() {

    private var currentPos = 0
    private val status = arrayListOf(1, 0, 0)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        Log.d("Adapter", parent.accessibilityClassName as String)
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return MyHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
//        if (currentPos == position) {
//            holder.button.setBackgroundColor(Color.CYAN)
//        } else {
//            holder.button.setBackgroundColor(Color.parseColor("#716F6F"))
//        }
//        holder.button.text = titles[position]
////        if (status[position] == 1) {
////            holder.button.setBackgroundColor(Color.CYAN)
////        } else {
////            holder.button.setBackgroundColor(Color.parseColor("#716F6F"))
////        }
//        holder.button.setOnClickListener {
//            Toast.makeText(context, "点击摄像头${position + 1}", Toast.LENGTH_SHORT).show()
//            currentPos = holder.adapterPosition
//            notifyDataSetChanged()
//        }
//        holder.itemView.setOnClickListener {
//            Toast.makeText(context, "item点击摄像头${position + 1}", Toast.LENGTH_SHORT).show()
//            status[position] = 1
////            currentPos = holder.adapterPosition
//            notifyDataSetChanged()
//
//
////            if (currentPos != position) {
////                notifyItemChanged(currentPos)
////            }
////            currentPos = holder.adapterPosition
////            notifyItemChanged(currentPos)
//        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            Log.d("Adapter-init", "更新item${position}")
            //payloads为空时，即不是通过notifyItemChanged(position,payloads)来刷新，也可以说是初始化执行时的操作
            if (currentPos == position) {
                holder.button.setBackgroundColor(Color.CYAN)
            } else {
                holder.button.setBackgroundColor(Color.parseColor("#716F6F"))
            }
            holder.button.text = titles[position]
            holder.button.setOnClickListener {
                Toast.makeText(context, "点击摄像头${position + 1}", Toast.LENGTH_SHORT).show()
                notifyItemChanged(currentPos, "item") //这样是不是只更新当前点击的item
                currentPos = holder.adapterPosition
                notifyItemChanged(position, "item")

//                if (currentPos != position) {
//                    notifyItemChanged(currentPos)
//                }
//                currentPos = holder.adapterPosition
//                notifyItemChanged(currentPos)
            }
            holder.itemView.setOnClickListener {
                Toast.makeText(context, "itemView-点击摄像头${position + 1}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("Adapter", "更新item$position")
            Log.d("Adapter", "payloads: $payloads")
            //payloads不为空时，即通过notifyItemChanged(position,payloads)来刷新，这里可以获取payloads中的数据进行局部刷新
            if (currentPos == position) {
                holder.button.setBackgroundColor(Color.CYAN)
            } else {
                holder.button.setBackgroundColor(Color.parseColor("#716F6F"))
            }
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    //这边很有意思，没加inner的话，访问不到itemView，但却能访问到button，即使单独把itemView设置为成员变量
    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.btn)
    }
}