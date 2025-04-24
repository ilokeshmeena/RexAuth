package com.rexvit.rexauth.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "auth_attempts")
data class AuthAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authType: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val success: Boolean
)