package com.rexvit.rexauth.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rexvit.rexauth.storage.entity.AuthAttemptEntity

@Dao
interface AuthAttemptDao {
    @Insert
    suspend fun insertAttempt(attempt: AuthAttemptEntity)

    @Query("SELECT COUNT(*) FROM auth_attempts WHERE timestamp > :since AND success = 0")
    suspend fun getFailedAttemptsSince(since: Long): Int

    @Query("DELETE FROM auth_attempts WHERE timestamp < :before")
    suspend fun deleteAttemptsOlderThan(before: Long)
}
