package com.example.tabbedappportfolio.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.adapter.TaskAdapter
import com.example.tabbedappportfolio.dbhelper.TaskDBHelper
import com.example.tabbedappportfolio.model.TaskItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FragmentThree: Fragment() {

    private lateinit var dbHelper: TaskDBHelper
    private lateinit var items: ArrayList<TaskItem>
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var lvItems: ListView
    private lateinit var btnAddItem: Button
    private lateinit var etNewItem: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.todo_list_layout, container, false)

        val todayDateTextView = rootView.findViewById<TextView>(R.id.todayDateTextView)
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate: String = dateFormat.format(calendar.time)
        todayDateTextView.text = "Today's Date: $currentDate"


        lvItems = rootView.findViewById(R.id.lvItems)
        btnAddItem = rootView.findViewById(R.id.btnAddItem)
        etNewItem = rootView.findViewById(R.id.etNewItem)
        dbHelper = TaskDBHelper(requireContext())
        items = ArrayList()

        btnAddItem.setOnClickListener {

            val newTask = TaskItem(etNewItem.text.toString(), 0)
            items.add(newTask)
            dbHelper.insertItem(newTask)
            taskAdapter.notifyDataSetChanged()
            etNewItem.text.clear()

        }


        items.addAll(dbHelper.getItems())

        taskAdapter = TaskAdapter(requireContext(), items, dbHelper)
        lvItems.adapter = taskAdapter


        return rootView
    }




    companion object {
        fun newInstance(): FragmentThree = FragmentThree()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
        }







