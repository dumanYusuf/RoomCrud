package com.dumanyusuf.roomcrud.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.dumanyusuf.roomcrud.model.User
import com.dumanyusuf.roomcrud.room_db.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserViewModel (application: Application):AndroidViewModel(application) {

    private val roomDatabase= Room.databaseBuilder(
        getApplication(),UserDatabase::class.java,"User"
    ).build()

    private val userDao=roomDatabase.userDao()

    val itemList = mutableStateOf<List<User>>(listOf())


    init {
        getUser()
    }



    fun getUser(){
        viewModelScope.launch(Dispatchers.IO) {
          itemList.value=  userDao.getUser()
            Log.e("basarılı","user getirme işlemi basarılık")
        }
    }


    fun saveUser(user:User){
        viewModelScope.launch (Dispatchers.IO){
            userDao.insert(user)
            getUser()
            Log.e("basarılı","user kaydetme işlemi basarılık")
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            userDao.delete(user)
            getUser()
            Log.e("basarılı","user silme işlemi basarılık")
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch (Dispatchers.IO){
            userDao.update(user)
            getUser()
        }
    }


    fun searchUser(searchingName:String){
        viewModelScope.launch(Dispatchers.IO) {
            val searchingList=if (searchingName.isNotEmpty()){
                userDao.searchUser(name = searchingName)
            }
            else{
                userDao.getUser()
            }
            itemList.value=searchingList

            Log.e("basarılı","user arama işlemi basarılık")
        }
    }


}