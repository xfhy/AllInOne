package com.xfhy.allinone.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * @author : xfhy
 * Create time : 2024/7/4 7:33 上午
 * Description :
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insertUser(user: User)

    @Insert
    suspend fun insertUserBySuspend(user: User)

    // 带了suspend,会自动切换到子线程
    @Query("SELECT * FROM user")
    fun getAllBySuspendFlow(): Flow<List<User>>

}