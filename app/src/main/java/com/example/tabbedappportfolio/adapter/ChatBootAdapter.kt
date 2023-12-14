package com.example.tabbedappportfolio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.model.Message


class ChatBootAdapter(private var itemList: List<Message>) : RecyclerView.Adapter<ChatBootAdapter.MyViewHolder>(),
    ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatboot_recycle, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.question.text = item.question
        holder.response.text = item.response

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val question: TextView = itemView.findViewById(R.id.idTVQuestion)
        val response: TextView = itemView.findViewById(R.id.idTVResponse)
    }

    override fun onItemDismiss(position: Int) {

    }


}
