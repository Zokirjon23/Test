package com.example.test

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.test.databinding.ActivityMainBinding
import com.example.test.databinding.TimePickerDialogBinding


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ActivityMainBinding::bind)
    private var isRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.timer.stop()
        binding.first.setOnClickListener { viewModel.onOneClicked(binding.first.text.toString()) }
        binding.second.setOnClickListener { viewModel.onTwoClicked(binding.second.text.toString()) }

        viewModel.resultDialog.observe(this) {
            AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(it)
                .setPositiveButton("restart") { _, _ -> viewModel.restartClicked() }
                .setCancelable(false)
                .show()
        }

        viewModel.beginGame.observe(this) { time ->
            binding.timer.base = SystemClock.elapsedRealtime()
            binding.timer.start()
            isRun = true
            Log.d("VVV", "timer")
            binding.timer.setOnChronometerTickListener {
                val elapsedMillis = (SystemClock.elapsedRealtime() - binding.timer.base)/1000
                Log.d("VVV", "onCreate: $elapsedMillis vs $time")
                if (elapsedMillis == time){
                    it.stop()
                    isRun = false
                    viewModel.onGameFinished(binding.first.text.toString(),binding.second.text.toString())
                }
            }
        }
        viewModel.increaseOne.observe(this) {
            binding.first.text = it.toString()
        }
        viewModel.increaseTwo.observe(this) {
            binding.second.text = it.toString()
        }
        viewModel.restart.observe(this) {
            binding.apply {
                first.text = "0"
                second.text = "0"
            }
        }
        viewModel.openDialog.observe(this) {
            if (!isRun){
                val dBinding = TimePickerDialogBinding.inflate(layoutInflater)
                AlertDialog.Builder(this)
                    .setTitle("Select game time")
                    .setView(dBinding.root)
                    .setPositiveButton("set") { p0, _ ->
                        viewModel.dialogOnOkClicked((dBinding.second.value + dBinding.minute.value * 60).toLong())
                        p0.dismiss()
                    }.setCancelable(false).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("time", binding.timer.base)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey("time"))
            binding.timer.base = savedInstanceState.getLong("time")
        super.onRestoreInstanceState(savedInstanceState)
    }
}