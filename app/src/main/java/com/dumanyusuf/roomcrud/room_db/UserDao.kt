package com.dumanyusuf.roomcrud.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dumanyusuf.roomcrud.model.User


@Dao
interface UserDao {


    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)


    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM User")
    suspend fun getUser():List<User>

    @Query("SELECT * FROM User WHERE name LIKE :name")
    suspend fun searchUser(name: String):List<User>
}