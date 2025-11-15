package com.example.lab_week_10.database

import androidx.room.Database
import androidx.room.RoomDatabase

// Create a database with the @Database annotation
// TotalDatabase.kt
@Database(entities = [Total::class], version = 2)   // penting: version > 1
abstract class TotalDatabase : RoomDatabase() {
    abstract fun totalDao(): TotalDao
}
