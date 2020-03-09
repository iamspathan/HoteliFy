package com.example.hotelify.view

import android.app.*
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.hotelify.R
import com.example.hotelify.database.TaskDB
import com.example.hotelify.databinding.ActivityTaskAddBinding
import com.example.hotelify.model.Task
import com.example.hotelify.receiver.NotificationPublisher
import com.example.hotelify.viewmodel.AddTaskViewModel
import com.example.hotelify.viewmodel.AddTaskViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_task_add.*
import kotlinx.coroutines.delay
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class TaskAdd : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityTaskAddBinding = DataBindingUtil.setContentView(this , R.layout.activity_task_add)

        val application = requireNotNull(this).application

        val dataSource = TaskDB.getInstance(application).taskDao()

        val factory = AddTaskViewModelFactory(dataSource,application)

        val viewModel = ViewModelProviders.of(this,factory).get(AddTaskViewModel::class.java)
        makeToDoFloatingActionButton.setOnClickListener {submitTask(binding, viewModel)}
        layoutSwitchCompatListener(binding)
        dateEditTextListener(binding.newTodoDateEditText)
        timeEditTextListener(binding.newTodoTimeEditText)
    }

    private fun submitTask(
        binding: ActivityTaskAddBinding,
        viewModel: AddTaskViewModel
    ) {
        if (checkValid(binding.userToDoTitleEditText) && checkValid(binding.userToDoDescription) && checkValid(
                binding.newTodoDateEditText
            ) && checkValid(binding.newTodoTimeEditText)
        ) {
            val title = binding.userToDoTitleEditText.text.toString()
            val description = binding.userToDoDescription.text.toString()
            val date = binding.newTodoDateEditText.text.toString()
            val time = binding.newTodoTimeEditText.text.toString()
            viewModel.database.addTask(
                (Task(
                    taskName = title,
                    taskDescription = description,
                    taskDate = date,
                    taskTime = time
                ))
            )

            if(toDoHasDateSwitchCompat.isChecked){
            setAlarm(getNotification(title,description),10000)
            }

            startActivity(Intent(this@TaskAdd, ShowTask::class.java))
        } else {
            when (true) {
                !checkValid(binding.userToDoTitleEditText) -> showSnack("Title Required")
                !checkValid(binding.userToDoDescription) -> showSnack("Description Required")
                !checkValid(binding.newTodoDateEditText) -> showSnack("Date Required")
                !checkValid(binding.newTodoTimeEditText) -> showSnack("Time Required")
            }
        }
    }

    private fun dateEditTextListener(editText: TextInputEditText) {
       editText.setOnClickListener {
           val calendar = Calendar.getInstance()
           val mYear = calendar.get(Calendar.YEAR)
           val mMonth = calendar.get(Calendar.MONTH)
           val mDay = calendar.get(Calendar.DAY_OF_MONTH)
           val datePickerDialog = DatePickerDialog(this , DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
               editText.setText("$year-$month-$dayOfMonth")
           },mYear,mMonth,mDay)
           datePickerDialog.show()
       }

       editText.addTextChangedListener(object  : TextWatcher{
           override fun afterTextChanged(s: Editable?) { newTodoTimeEditText.isEnabled = true }

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
       })

    }

    private fun timeEditTextListener(editText: TextInputEditText){
        editText.setOnClickListener {
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this , TimePickerDialog.OnTimeSetListener{view, hourOfDay, minute ->
            val amPm = if(hourOfDay < 12) "AM" else "PM"
            editText.setText("$hourOfDay:$minute $amPm")
        },mHour,mMinute,false)
        timePickerDialog.show()}

        editText.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(s: Editable?) { toDoHasDateSwitchCompat.isEnabled = true }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun layoutSwitchCompatListener(binding: ActivityTaskAddBinding) {
        binding.toDoHasDateSwitchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.newToDoDateTimeReminderTextView.visibility = View.VISIBLE
//                setReminderTextView(binding)
            } else {
                binding.newToDoDateTimeReminderTextView.visibility = View.GONE
            }
        }
    }

    private fun setAlarm(notification: Notification, time: Int) {
        val intent = Intent(this, NotificationPublisher::class.java)
        intent.putExtra(NotificationPublisher.NOTIFICATION_ID,1)
        intent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(this , 0,intent , PendingIntent.FLAG_UPDATE_CURRENT)
        val timeInMillisecond = SystemClock.elapsedRealtime() + time
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,timeInMillisecond,pendingIntent)
    }


    private fun getNotification(title:String , description: String): Notification {

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val channel = NotificationChannel("MyNotification" , "TodoFy Notification", NotificationManager.IMPORTANCE_DEFAULT)
           channel.setShowBadge(true)
           channel.lockscreenVisibility
           channel.vibrationPattern
           val manager = getSystemService(NotificationManager::class.java)
           manager.createNotificationChannel(channel)
       }

        val builder  = NotificationCompat.Builder(this ,"MyNotification" )
        builder.setContentTitle(title)
        builder.setContentText(description)
        builder.setSmallIcon(R.drawable.ic_add_alarm_black_24dp)
        return builder.build()
    }


    private fun checkValid(textInputEditText: TextInputEditText):Boolean{
        return textInputEditText.text.toString().isNotEmpty()
    }


    private fun showSnack(string: String){
        Snackbar.make(root,string,Snackbar.LENGTH_SHORT).show()
    }

    private fun setReminderTextView(binding: ActivityTaskAddBinding){
//        val userTodoDate = binding.newTodoDateEditText.text.toString()
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val simpleTimeFormat = SimpleDateFormat("HH-mm-ss-zzz")

    }


}
