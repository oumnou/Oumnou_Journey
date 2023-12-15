package com.example.tabbedappportfolio.activity

import com.example.tabbedappportfolio.dbhelper.ItemsDBHelper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tabbedappportfolio.R
import com.example.tabbedappportfolio.model.Item
import kotlin.properties.Delegates

class FormActivity : AppCompatActivity() {

    //region Declaration
    private lateinit var newData: Item
    private val PICK_IMAGE_REQUEST = 1
    private var bundle: Int = 0
    private lateinit var imagePath: String
    private var categoryNumber by Delegates.notNull<Int>()
    private lateinit var textView: TextView
    private lateinit var titleView: TextView
    private lateinit var imageView: ImageView
    private lateinit var okButton: Button
    private lateinit var categorytv: TextView
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_layout)

        textView = findViewById(R.id.form_text)
        titleView = findViewById(R.id.form_title)
        imageView = findViewById(R.id.imageView)
        okButton = findViewById(R.id.form_add)
        categorytv = findViewById(R.id.category)
        imagePath = ""
        val bundleIntent = intent.extras

        if (bundleIntent != null) {
            bundle = bundleIntent.getInt("categoriesNumber", 0)
        }
        categoryNumber = bundle
        val category = when (bundle) {
            0 -> "About me"
            else -> "About me"
        }

        categorytv.text = category
        okButton.setOnClickListener {

            val title = titleView.text.toString()
            val text = textView.text.toString()

            if (title.isNotEmpty()) {
                newData = if (imagePath!=""){

                    Item(
                        title = title,
                        description = text,
                        imageResource = imagePath
                    )
                }else{
                    Item(
                        title = title,
                        description = text,
                        imageResource = ""
                    )
                }
                saveItemsToDatabase(category, newData)
            }

            finish()
        }

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }


    //region Functions
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri)

            // Get the absolute path of the image file
            imagePath = selectedImageUri?.let { getRealPathFromURI(it) }.toString()

        }
    }
    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path
    }
    private fun saveItemsToDatabase(categoryType: String, item: Item) {
        val dbHelper = ItemsDBHelper(this, categoryType)
        dbHelper.insertItem(item.title, item.description, item.imageResource)
    }
    override fun finish() {
        val returnIntent = Intent()
        val bundle = Bundle()
        bundle.putInt("selectedTabIndex", categoryNumber)
        returnIntent.putExtra("bundle", bundle)
        setResult(RESULT_OK, returnIntent)

        super.finish()
    }
    //endregion
}
