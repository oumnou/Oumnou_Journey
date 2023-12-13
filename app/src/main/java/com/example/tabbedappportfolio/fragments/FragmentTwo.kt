package com.example.tabbedappportfolio.fragments

import com.example.tabbedappportfolio.dbhelper.MyDBHelper
import com.example.tabbedappportfolio.adapter.RecycleAdapter
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.model.MyItem

class FragmentTwo  : Fragment(){

    var categoryType = "education"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecycleAdapter // Assume you have already defined MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.education, container, false)

        // Assuming you have a RecyclerView with the ID 'recyclerView' in your me_beautiful_me.xml layout
        recyclerView = rootView.findViewById(R.id.recycleEducation)

        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // saveItemsToDatabase(categoryType,itemList)
        adapter = RecycleAdapter(retrieveDataFromDatabase())
        recyclerView.adapter = adapter
        return rootView
    }



    private fun retrieveDataFromDatabase(): List<MyItem> {
        return MyDBHelper(requireContext(), categoryType).getAllItems()

    }


    companion object {
        fun newInstance(): FragmentTwo = FragmentTwo()
    }


}