package com.example.tabbedappportfolio.activitys

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import android.Manifest
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.adapter.SampleAdapter
import com.example.tabbedappportfolio.fragments.FragmentFour
import com.example.tabbedappportfolio.fragments.FragmentOne
import com.example.tabbedappportfolio.fragments.FragmentThree
import com.example.tabbedappportfolio.fragments.FragmentTwo
import com.google.android.material.tabs.TabLayout

open class MainActivity : AppCompatActivity() {
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 123
    private lateinit var  addButton: Button
    private var REQUEST_CODE = 22



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        Log.d("log","onCreate")


        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val adapter = SampleAdapter(supportFragmentManager)



        addButton = findViewById(R.id.button)

        viewPager.adapter = adapter
        val bundle = Bundle()
        val addButton: Button = findViewById(R.id.button)


// Check if the app has permission to read external storage
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed with your logic
            // ...
        }



        addButton.setOnClickListener {

            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra("categoriesNumber", tabLayout.selectedTabPosition)
            startActivityForResult(intent, REQUEST_CODE)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val position = it.position
                    viewPager.currentItem = position
                    bundle.putInt("categoriesNumber", position)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                if (adapter.getItem(position) is FragmentFour ||adapter.getItem(position) is FragmentThree) {
                    hideButton()
                }else{
                    showButton()
                }
            }

            override fun onPageSelected(position: Int) {
                if (adapter.getItem(position) is FragmentFour) {
                    hideButton()
                }else{
                    showButton()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        // Set up the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager)

}

    fun showButton() {
        addButton.visibility = View.VISIBLE
    }

    fun hideButton() {
        addButton.visibility = View.GONE

    }


    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Oumaima"
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {


            val bundle = data.getBundleExtra("bundle")
            val selectedTabIndex = bundle?.getInt("selectedTabIndex", 0) ?: 0

            // Update the data in the corresponding fragment
            val fragment = supportFragmentManager.fragments[selectedTabIndex]
            if(fragment is FragmentOne){
                fragment.updateData()
            }else if (fragment is FragmentTwo){
                fragment.updateData()
            }


        }
    }override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with your logic
                    Log.d("Permission", "Read external storage permission granted")
                } else {
                    // Permission denied, handle accordingly
                    Log.e("Permission", "Read external storage permission denied")
                }
            }
            // Handle other permission requests if needed
        }
    }




}