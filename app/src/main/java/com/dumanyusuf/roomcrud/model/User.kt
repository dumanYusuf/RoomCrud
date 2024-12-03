package com.dumanyusuf.roomcrud.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User(
    @ColumnInfo("name")
    var userName:String,
    @ColumnInfo("lastName")
    var userLastname:String,
    @ColumnInfo("phone")
    var userPhone:Int?,


    ){
    @PrimaryKey(autoGenerate = true)
    var id :Int?=null
}