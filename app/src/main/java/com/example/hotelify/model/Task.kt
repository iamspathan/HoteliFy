package com.example.hotelify.model

import android.telephony.mbms.StreamingServiceInfo
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(@PrimaryKey(autoGenerate = true) val taskId: Long = 0L,
                @ColumnInfo(name = "task_name")val taskName: String,
                @ColumnInfo(name = "task_desc")val taskDescription: String,
                @ColumnInfo(name = "task_date")val taskDate: String,
                @ColumnInfo(name = "task_time")val taskTime: String)
