package com.rexvit.rexauth.storage.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.rexvit.rexauth.storage.dao.AuthAttemptDao
import com.rexvit.rexauth.storage.entity.AuthAttemptEntity

@Database(entities = [AuthAttemptEntity::class], version = 1)
abstract class AuthDatabase : RoomDatabase() {
    abstract fun authAttemptDao(): AuthAttemptDao
}