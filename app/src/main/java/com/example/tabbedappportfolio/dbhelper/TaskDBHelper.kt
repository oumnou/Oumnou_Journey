package com.example.tabbedappportfolio.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tabbedappportfolio.model.TaskContract
import com.example.tabbedappportfolio.model.TaskItem

class TaskDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "tasks.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create your tasks table
        val createTableQuery =

            "CREATE TABLE ${TaskContract.TaskEntry.TABLE_NAME} ( " +
                    "${TaskContract.TaskEntry.COLUMN_TASK} TEXT ,"+
                    "${TaskContract.TaskEntry.COLUMN_CHECKED} INTEGER );"


        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades
    }

    fun getItems(): ArrayList<TaskItem> {
        val tasks = ArrayList<TaskItem>()
        val db = readableDatabase
        db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            arrayOf(TaskContract.TaskEntry.COLUMN_TASK, TaskContract.TaskEntry.COLUMN_CHECKED),
            null,
            null,
            null,
            null,
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                val task = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TASK))
                val isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CHECKED))
                tasks.add(TaskItem(task, isChecked))
            }
        }
        return tasks
    }

    fun deleteItem(task: TaskItem):Int{
        val db = writableDatabase
        val selection = "${TaskContract.TaskEntry.COLUMN_TASK} LIKE ?"
        val selectionArgs = arrayOf(task.text)
        return db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs)
    }


    fun insertItem(task: TaskItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_TASK, task.text)
            put(TaskContract.TaskEntry.COLUMN_CHECKED, task.isChecked)
        }

        return db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
    }

    fun updateCheckedState(task: TaskItem): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_CHECKED, task.isChecked)
        }

        val selection = "${TaskContract.TaskEntry.COLUMN_TASK} LIKE ?"
        val selectionArgs = arrayOf(task.text)

        return db.update(TaskContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs)

}
    }