package com.example.tabbedappportfolio.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tabbedappportfolio.model.MyItem

class MyDBHelper(context: Context, private val categoryType: String) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Portfolio"
        const val DATABASE_VERSION = 1
        const val TABLE_PREFIX = "table"
        const val COLUMN_ID = "_id"
        const val COLUMN_TEXT1 = "title"
        const val COLUMN_TEXT2 = "text"
        const val COLUMN_TEXT3 = "category"
        const val COLUMN_IMAGE_RESOURCE = "image_resource"
    }

    // Dynamic table name for each category
    private val tableName = TABLE_PREFIX + "ensm".replace(" ", "_")

    private val CREATE_TABLE =
        "CREATE TABLE $tableName ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TEXT1 TEXT, " +
                "$COLUMN_TEXT2 TEXT, " +
                "$COLUMN_TEXT3 TEXT, " +
                "$COLUMN_IMAGE_RESOURCE TEXT);"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    fun insertItem(title: String, text: String, imageBytes: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEXT1, title)
            put(COLUMN_TEXT2, text)
            put(COLUMN_TEXT3, categoryType)
            put(COLUMN_IMAGE_RESOURCE, imageBytes)
        }
        db.insert(tableName, null, values)
        db.close()
    }

    fun getAllItems(): List<MyItem> {
        val items = mutableListOf<MyItem>()
        val db = readableDatabase

        val selection = "$COLUMN_TEXT3 = ?"  // Add the condition for the categoryType
        val selectionArgs = arrayOf("$categoryType")


        val cursor = db.query(
            tableName,
            arrayOf(COLUMN_TEXT1, COLUMN_TEXT2, COLUMN_IMAGE_RESOURCE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT1))
            val text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT2))
            val imageResource = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RESOURCE))

            items.add(MyItem(title, text, imageResource))
        }

        cursor.close()
        db.close()

        return items
    }

}