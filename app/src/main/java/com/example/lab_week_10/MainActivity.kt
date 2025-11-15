package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    // Create an instance of the TotalDatabase (lazy)
    private val db: TotalDatabase by lazy { prepareDatabase() }

    // Create an instance of the TotalViewModel (lazy)
    private val viewModel: TotalViewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the value of the total from the database
        initializeValueFromDatabase()

        // Prepare the ViewModel (observe LiveData & set button listener)
        prepareViewModel()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        // Observe the LiveData object
        viewModel.total.observe(this) {
            // whenever the value changes, updateText is called
            updateText(it)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // Create and build the TotalDatabase with the name 'total-database'
    // allowMainThreadQueries() digunakan biar gampang (sesuai modul)
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries()
            .build()
    }

    // Initialize the value of the total from the database
    private fun initializeValueFromDatabase() {
        val totalList = db.totalDao().getTotal(ID)

        if (totalList.isEmpty()) {
            // jika belum ada, insert object Total dengan nilai 0
            db.totalDao().insert(Total(id = ID, total = 0))
        } else {
            // kalau sudah ada, ambil nilainya dan set ke ViewModel
            viewModel.setTotal(totalList.first().total)
        }
    }

    // Update value di database setiap Activity di-pause (step 15)
    override fun onPause() {
        super.onPause()
        // pastikan value tidak null, modul pakai !!
        db.totalDao().update(Total(ID, viewModel.total.value!!))
    }

    companion object {
        // ID dari satu-satunya row Total di DB
        const val ID: Long = 1
    }
}
