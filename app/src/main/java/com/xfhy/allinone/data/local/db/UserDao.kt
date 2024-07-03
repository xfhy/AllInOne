package com.xfhy.allinone.data.local.db

import androidx.room.Dao
import androidx.room.Query

/**
 * @author : xfhy
 * Create time : 2024/7/4 7:33 上午
 * Description :
 */
@Dao
interface UserDao {

    // https://developer.android.com/training/data-storage/room
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

}