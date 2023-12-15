package com.example.tabbedappportfolio.model

import android.provider.BaseColumns

object TaskContract {
    // Inner class that defines the table contents
    class TaskEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "tasks"
            const val COLUMN_TASK = "task"
            const val COLUMN_CHECKED = "isChecked"
            }
       }
}