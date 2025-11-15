package com.example.lab_week_10.database

import androidx.room.*

@Dao
interface TotalDao {

    // Insert row baru, replace kalau id sudah ada
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(total: Total)

    // Update row yang sudah ada
    @Update
    fun update(total: Total)

    // Hapus row
    @Delete
    fun delete(total: Total)

    // Query untuk get Total berdasarkan id
    @Query("SELECT * FROM total WHERE id = :id")
    fun getTotal(id: Long): List<Total>
}
