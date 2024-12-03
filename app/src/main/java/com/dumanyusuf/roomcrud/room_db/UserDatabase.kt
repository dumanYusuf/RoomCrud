package com.dumanyusuf.roomcrud.room_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dumanyusuf.roomcrud.model.User


@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}