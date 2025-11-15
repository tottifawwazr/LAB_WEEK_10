package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    // Room database
    private val db: TotalDatabase by lazy { prepareDatabase() }

    // ViewModel
    private val viewModel: TotalViewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil nilai awal total (+ insert kalau DB masih kosong)
        initializeValueFromDatabase()

        // Siapkan LiveData & button
        prepareViewModel()
    }

    // Dipanggil setiap aplikasi START (termasuk setelah kill lalu buka lagi)
    override fun onStart() {
        super.onStart()
        showLastUpdateDate()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        // Observe LiveData
        viewModel.total.observe(this) {
            updateText(it)
        }

        // Button increment
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // Build Room DB
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .fallbackToDestructiveMigration() // aman kalau schema berubah
            .allowMainThreadQueries()
            .build()
    }

    // Inisialisasi nilai total dari DB (dan isi kalau kosong)
    private fun initializeValueFromDatabase() {
        val list = db.totalDao().getTotal(ID)

        if (list.isEmpty()) {
            // pertama kali: insert total 0 dengan tanggal sekarang
            val now = Date().toString()
            val totalObj = TotalObject(
                value = 0,
                date = now
            )
            db.totalDao().insert(Total(id = ID, total = totalObj))
            viewModel.setTotal(0)
        } else {
            val entity = list.first()
            // set nilai total ke ViewModel
            viewModel.setTotal(entity.total.value)
        }
    }

    // Baca total.date dari DB lalu tampilkan sebagai Toast
    private fun showLastUpdateDate() {
        val list = db.totalDao().getTotal(ID)
        if (list.isNotEmpty()) {
            val dateString = list.first().total.date
            Toast.makeText(this, dateString, Toast.LENGTH_LONG).show()
        }
    }

    // Simpan nilai total + tanggal terbaru setiap Activity pause
    override fun onPause() {
        super.onPause()

        val currentTotal = viewModel.total.value ?: 0
        val now = Date().toString()

        val totalObj = TotalObject(
            value = currentTotal,
            date = now
        )
        db.totalDao().update(Total(ID, totalObj))
    }

    companion object {
        const val ID: Long = 1
    }
}
