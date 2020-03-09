package com.example.hotelify.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hotelify.model.Task

@Database(entities = [Task::class],version = 1,exportSchema = false)
abstract class TaskDB : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object{

        @Volatile
        private var INSTANCE : TaskDB? =null

        fun getInstance(context: Context):TaskDB{
            synchronized(this){
                var instance  = INSTANCE
                if(instance==null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext, TaskDB::class.java, "task_database"
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
                return instance
            }
        }

    }

}


@Dao
interface TaskDao {

    @Query("Select * FROM Task")
    fun getAllTask():LiveData<List<Task>>

    @Query("DELETE FROM Task")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun addTask(task: Task) :Long

    @Query("DELETE FROM Task WHERE taskId = :task_id ")
     fun deleteTask(task_id : Long)

}