package com.example.hotelify.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelify.R
import com.example.hotelify.adapters.TaskRecyclerViewAdapter
import com.example.hotelify.database.TaskDB
import com.example.hotelify.databinding.ActivityTaskShowBinding
import com.example.hotelify.viewmodel.ShowTaskViewModel
import com.example.hotelify.viewmodel.ShowTaskViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_task_show.*

class ShowTask : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {


    lateinit var viewModel: ShowTaskViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityTaskShowBinding = DataBindingUtil.setContentView(this , R.layout.activity_task_show)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this ,drawer,toolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val application = requireNotNull(this).application

        val dataSource = TaskDB.getInstance(application).taskDao()

        val factory = ShowTaskViewModelFactory(dataSource,application)

        viewModel = ViewModelProviders.of(this , factory).get(ShowTaskViewModel::class.java)

        val adapter = TaskRecyclerViewAdapter()

        viewModel.taskList.observe(this, Observer {
        it?.let {
            adapter.submitList(it)
        }
        })

        binding.taskListRV.adapter = adapter
        binding.taskListRV.layoutManager = LinearLayoutManager(this)

        task_create_fab.setOnClickListener {
            startActivity( Intent(this@ShowTask, TaskAdd::class.java))
        }


        val simpleItemTouchCallback = object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Toast.makeText(this@ShowTask,"On Move" , Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val data = viewModel.taskList.value?.get(position)
                if (data != null) {
                    val check  = viewModel.onTaskdelete(data)
                    Log.d("Delete" , " DataDeleted ${check.toString()}")
                }
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.taskListRV)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
     menuInflater.inflate(R.menu.option_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.deleteAll){
            viewModel.database.deleteAll()
        }
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.AboutMenuItem -> startActivity(Intent(this@ShowTask, AboutUs::class.java))
            R.id.SettingMenuItem -> startActivity(Intent(this@ShowTask, Setting::class.java))
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
