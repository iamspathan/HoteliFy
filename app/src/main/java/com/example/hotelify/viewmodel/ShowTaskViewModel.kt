package com.example.hotelify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelify.database.TaskDao
import com.example.hotelify.model.Task
import kotlinx.coroutines.*

import java.lang.IllegalArgumentException


class ShowTaskViewModel (val database : TaskDao , application: Application) : AndroidViewModel(application) {

    val viewModeljob = Job()

    val viewModelScope = CoroutineScope(viewModeljob + Dispatchers.Main)

    val taskList = database.getAllTask()


    fun onTaskdelete(task: Task){
        viewModelScope.launch {
            deleteTask(task)
        }

    }

    private suspend fun deleteTask(task: Task){
        withContext(Dispatchers.IO){
            database.deleteTask(task.taskId)
        }

    }



    override fun onCleared() {
        super.onCleared()
        viewModeljob.cancel()
    }
}
















class ShowTaskViewModelFactory(val database: TaskDao ,val application: Application):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(ShowTaskViewModel::class.java))
        {
            return ShowTaskViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown Showtask ViewModel Class")
    }

}