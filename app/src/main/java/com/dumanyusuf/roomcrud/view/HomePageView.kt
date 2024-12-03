package com.dumanyusuf.roomcrud.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dumanyusuf.roomcrud.R
import com.dumanyusuf.roomcrud.model.User
import com.dumanyusuf.roomcrud.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePageView(
    viewModel: UserViewModel,
) {

    val isAllerDiologShow = remember { mutableStateOf(false) }
    val userName= remember { mutableStateOf("") }
    val userLastName= remember { mutableStateOf("") }
    val userPhone= remember { mutableStateOf("") }
    val search= remember { mutableStateOf("") }





    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){

                        OutlinedTextField(
                            value =search.value,
                            onValueChange = {
                                search.value=it
                                viewModel.searchUser(it)

                                            },
                            maxLines = 1,
                            label = { Text(text = "kişi ara") },
                            modifier = Modifier.padding(10.dp).fillMaxWidth(),
                            leadingIcon = {
                                Icon(painter = painterResource(R.drawable.search), contentDescription = "")
                            },
                            trailingIcon = {
                                if (search.value.isNotEmpty()){
                                    IconButton(onClick = {
                                        search.value=""
                                        viewModel.getUser()
                                    }) {
                                        Icon(painter = painterResource(R.drawable.delete), contentDescription = "")
                                    }
                                }
                            }
                        )
                    }
                }
            )
        },
        content = {
            Column (modifier = Modifier.fillMaxSize().padding(it)){
                LazyColumn (modifier = Modifier.fillMaxSize()){
                    items(viewModel.itemList.value){user->
                        UserCard(user = user,viewModel)
                    }
                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // alertDialog
                    isAllerDiologShow.value=true
                }
            ) {
                Icon(painter = painterResource(R.drawable.add), contentDescription = "")
            }

            if (isAllerDiologShow.value) {
                AlertDialog(
                    onDismissRequest = {
                        isAllerDiologShow.value = true
                    },
                    title = { Text(text = "Kişi Kaydet") },
                    text = {
                        Column {
                            // 1. TextField
                            TextField(
                                value = userName.value,
                                onValueChange = { userName.value = it
                                                },
                                label = { Text("Ad") },
                                modifier = Modifier.fillMaxWidth()

                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 2. TextField
                            TextField(
                                value = userLastName.value,
                                onValueChange = { userLastName.value = it
                                                },
                                label = { Text("Soyad") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 3. TextField
                            TextField(
                                value = userPhone.value,
                                onValueChange = { userPhone.value = it
                                                },
                                label = { Text("Telefon") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val user=User(userName.value,userLastName.value,userPhone.value.toIntOrNull())
                                viewModel.saveUser(user)
                                isAllerDiologShow.value = false
                                userName.value=""
                                userLastName.value=""
                                userPhone.value=""
                            }
                        ) {
                            Text("Kaydet")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                isAllerDiologShow.value = false
                            }
                        ) {
                            Text("İptal")
                        }
                    }
                )
            }


        }


    )


}

@Composable
fun UserCard(user: User,viewModel: UserViewModel) {

        Card(
            modifier = Modifier.fillMaxWidth().padding(10.dp).size(80.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Ad: ${user.userName}")
                    Icon(
                        modifier = Modifier.clickable {
                            viewModel.deleteUser(user)

                        },
                        painter = painterResource(R.drawable.delete), contentDescription = "")
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Soyad: ${user.userLastname}")
                    Icon(
                        modifier = Modifier.clickable {
                            // burda hangi kullanıcının edit butonuna bastıysam o kullanıcın verileri alerdialogda cıksın ve ve update fonl calıstırabileyim


                        },
                        painter = painterResource(R.drawable.edit), contentDescription = "")
                }
               // Text(text = "Telefon: ${user.userPhone?: "Belirtilmemiş"}")
            }
        }

}
