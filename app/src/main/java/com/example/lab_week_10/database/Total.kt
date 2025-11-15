package com.example.lab_week_10.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Create an Entity with a table name of "total"
@Entity(tableName = "total")
data class Total(

    // Primary key, auto generate
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    // Kolom untuk menyimpan nilai total
    @ColumnInfo(name = "total")
    val total: Int = 0,
)
