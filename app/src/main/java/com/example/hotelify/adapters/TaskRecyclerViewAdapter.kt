package com.example.hotelify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelify.R
import com.example.hotelify.databinding.TaskItemLayoutBinding
import com.example.hotelify.model.Task


class TaskRecyclerViewAdapter() : ListAdapter<Task, TaskViewHolder>(TaskDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskItem  = getItem(position)
        holder.bind(taskItem)
    }

}

class TaskViewHolder private constructor (val binding : TaskItemLayoutBinding ) : RecyclerView.ViewHolder (binding.root){
    fun bind(task : Task) {
        binding.task = task
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup) : TaskViewHolder {
            val inflater  = LayoutInflater.from(parent.context)
            val binding =TaskItemLayoutBinding.inflate(inflater , parent,false)
            return TaskViewHolder(binding)
        }
    }
}

class TaskDiffCallBack : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem==newItem
    }

}





