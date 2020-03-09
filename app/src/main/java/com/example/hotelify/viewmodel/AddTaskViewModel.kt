package com.example.hotelify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import com.example.hotelify.database.TaskDao
import com.example.hotelify.model.Task
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class AddTaskViewModel(val database: TaskDao, application: Application) : AndroidViewModel(application){

    val viewModeljob = Job()

    val viewScope = CoroutineScope(Dispatchers.Main + viewModeljob)


    fun onTaskSubmit(task: Task){
        viewScope.launch {
            insertTask(task)
        }

    }

    private suspend fun insertTask(task:Task){
        withContext(Dispatchers.IO){
            database.addTask(task)
        }

    }



    override fun onCleared() {
        super.onCleared()
        viewModeljob.cancel()
    }
}



class AddTaskViewModelFactory(val database: TaskDao , val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)){
            return  AddTaskViewModel(database,application) as T
        }
        throw IllegalArgumentException("Unknown AddTaskViewModel Class")
    }

}