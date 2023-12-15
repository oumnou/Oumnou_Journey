package com.example.tabbedappportfolio.adapter


import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.dbhelper.TaskDBHelper
import com.example.tabbedappportfolio.model.TaskItem

class TaskAdapter(private val context: Context, private val items: ArrayList<TaskItem>, val dbHelper: TaskDBHelper) : BaseAdapter() { //The 'item' class extends BaseAdapter and represents the adapter for your to-do list items.



    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.task_recycler, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val checkBox = viewHolder.checkBox
        val textView = viewHolder.textView
        val deleteIcon = viewHolder.deleteIcon

        checkBox.isChecked = items[position].isChecked==1
        textView.text =  items[position].text

        deleteIcon.setOnClickListener {
            dbHelper.deleteItem(items[position])
            items.remove(items[position])
            notifyDataSetChanged()
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                items[position].isChecked = 1
            } else {
                items[position].isChecked = 0
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            dbHelper.updateCheckedState(items[position])
            notifyDataSetChanged()
        }


        return view

     }

    private class ViewHolder(view: View) {
        var checkBox: CheckBox = view.findViewById(R.id.myCheckBox)
        var textView: TextView = view.findViewById(R.id.myTextView)
        var deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)


    }
}
