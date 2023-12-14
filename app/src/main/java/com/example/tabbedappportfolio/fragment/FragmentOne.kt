package com.example.tabbedappportfolio.fragment

import com.example.tabbedappportfolio.dbhelper.ItemsDBHelper
import com.example.tabbedappportfolio.adapter.RecyclerAdapter
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.adapter.ItemTouchHelperCallback
import com.example.tabbedappportfolio.model.Item

class FragmentOne : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerAdapter // Assume you have already defined MyAdapter
    private lateinit var categoryType : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.items_layout, container, false)

        // Assuming you have a RecyclerView with the ID 'recyclerView' in your me_beautiful_me.xml layout
        recyclerView = rootView.findViewById(R.id.recycleMe)
        categoryType = "About me"

        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


       // saveItemsToDatabase(categoryType,itemList)

        adapter = RecyclerAdapter(retrieveDataFromDatabase(), requireContext())
        recyclerView.adapter = adapter


        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter, requireContext(),"about me")
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }



    private fun retrieveDataFromDatabase(): List<Item> {
        return ItemsDBHelper(requireContext(), categoryType).getAllItems()

    }
    companion object {
        fun newInstance(): FragmentOne = FragmentOne()
    }

    fun updateData() {
        val updatedData = retrieveDataFromDatabase()

        if (updatedData.isNotEmpty()) {
            // Update the data in the adapter
            adapter.updateData(updatedData)
        }

    }







}
