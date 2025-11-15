package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import database.Total
import database.TotalDatabase
import database.TotalObject
import viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val ID = 1L

    private val db: TotalDatabase by lazy { prepareDatabase() }

    private val viewModel: TotalViewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()

        // Ambil date dari DB lalu tampilkan Toast
        val totalList = db.totalDao().getTotal(ID)
        if (totalList.isNotEmpty()) {
            val dateString = totalList.first().total.date
            Toast.makeText(this, "Last Opened: $dateString", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { total ->
            updateText(total)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    override fun onPause() {
        super.onPause()

        // Update total + date baru
        val updated = Total(
            id = ID,
            total = TotalObject(
                value = viewModel.total.value ?: 0,
                date = Date().toString()   // ✔ update date sesuai instruksi
            )
        )

        db.totalDao().update(updated)
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase() {
        val totalList = db.totalDao().getTotal(ID)

        if (totalList.isEmpty()) {
            // Insert pertama kali
            db.totalDao().insert(
                Total(
                    id = ID,
                    total = TotalObject(
                        value = 0,
                        date = Date().toString() // ✔ set date awal
                    )
                )
            )
        } else {
            // Load existing value ke ViewModel
            viewModel.setTotal(totalList.first().total.value)
        }
    }
}
