package com.example.eyecon.ui.settings

import android.Manifest
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eyecon.R
import com.example.eyecon.databinding.ActivityProfileBinding
import com.example.eyecon.databinding.ActivitySettingsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity(), View.OnClickListener, TimePickerFragment.DialogTimeListener {
    private lateinit var alarmReceiver: AlarmReceiver
    private var binding: ActivitySettingsBinding? = null
    private var isAlarmSet = false // Flag to track if the alarm is set
    private lateinit var sharedPreferences: SharedPreferences
    private val ALARM_STATE_KEY = "alarm_state" // Key for SharedPreferences
    private val REPEATING_TIME_KEY = "repeating_time" // Key for saving repeating time
    private lateinit var userEmail: String

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Get the current user's email from Firebase Auth
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            userEmail = it.email ?: "default_email"
        } ?: run {
             userEmail = "default_email"
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
        }

        // Initialize SharedPreferences with a unique file per user
        sharedPreferences = getSharedPreferences("settings_${userEmail}", MODE_PRIVATE)
        isAlarmSet = sharedPreferences.getBoolean(ALARM_STATE_KEY, false) // Retrieve alarm state
        val savedTime = sharedPreferences.getString(REPEATING_TIME_KEY, "00:00") // Retrieve saved time

        // Set the saved time into the TextView (if not null)
        binding?.tvRepeatingTime?.text = savedTime

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        supportActionBar?.title = "Settings"
        binding?.btnRepeatingTime?.setOnClickListener(this)
        binding?.btnSetRepeatingAlarm?.setOnClickListener(this)
        binding?.btnCancelRepeatingAlarm?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()

        // Initially set the buttons' state based on whether the alarm is set or not
        updateButtonStates()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_repeating_time -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_set_repeating_alarm -> {
                val repeatTime = binding?.tvRepeatingTime?.text.toString()
                val repeatMessage = "Sudahkah cek mata anda hari ini?"
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
                    repeatTime, repeatMessage)
                isAlarmSet = true // Set the alarm state
                // Save alarm state and repeating time using the user's email as part of the key
                sharedPreferences.edit().putBoolean(ALARM_STATE_KEY, isAlarmSet).apply()
                sharedPreferences.edit().putString(REPEATING_TIME_KEY, repeatTime).apply()
                updateButtonStates() // Update button visibility
                Log.d("AlarmMessage", repeatMessage)
            }
            R.id.btn_cancel_repeating_alarm -> {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
                isAlarmSet = false // Clear the alarm state
                // Save the alarm state change
                sharedPreferences.edit().putBoolean(ALARM_STATE_KEY, isAlarmSet).apply()
                updateButtonStates() // Update button visibility
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        // Siapkan time formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        // Set text dari textview berdasarkan tag
        when (tag) {
            TIME_PICKER_REPEAT_TAG -> {
                val formattedTime = dateFormat.format(calendar.time)
                binding?.tvRepeatingTime?.text = formattedTime
                // Save the selected time in SharedPreferences
                sharedPreferences.edit().putString(REPEATING_TIME_KEY, formattedTime).apply()
            }
            else -> {
            }
        }
    }

    // Function to update the button states based on whether the alarm is set or not
    private fun updateButtonStates() {
        if (isAlarmSet) {
            binding?.btnSetRepeatingAlarm?.isEnabled = false // Disable "Set Alarm" button
            binding?.btnCancelRepeatingAlarm?.isEnabled = true // Enable "Cancel Alarm" button
        } else {
            binding?.btnSetRepeatingAlarm?.isEnabled = true // Enable "Set Alarm" button
            binding?.btnCancelRepeatingAlarm?.isEnabled = false // Disable "Cancel Alarm" button
        }
    }

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }
}
