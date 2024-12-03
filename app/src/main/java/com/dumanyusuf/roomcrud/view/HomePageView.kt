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

    val isAlertDialogShow = remember { mutableStateOf(false) }
    val userName = remember { mutableStateOf("") }
    val userLastName = remember { mutableStateOf("") }
    val userPhone = remember { mutableStateOf("") }
    val search = remember { mutableStateOf("") }
    val selectedUser = remember { mutableStateOf<User?>(null) }  // Seçilen kullanıcı

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            value = search.value,
                            onValueChange = {
                                search.value = it
                                viewModel.searchUser(it)
                            },
                            maxLines = 1,
                            label = { Text(text = "kişi ara") },
                            modifier = Modifier.padding(10.dp).fillMaxWidth(),
                            leadingIcon = {
                                Icon(painter = painterResource(R.drawable.search), contentDescription = "")
                            },
                            trailingIcon = {
                                if (search.value.isNotEmpty()) {
                                    IconButton(onClick = {
                                        search.value = ""
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
            Column(modifier = Modifier.fillMaxSize().padding(it)) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.itemList.value) { user ->
                        UserCard(user = user, viewModel = viewModel, onEditClick = {
                            // Seçilen kullanıcıyı edit işlemine alıyoruz
                            selectedUser.value = user
                            userName.value = user.userName
                            userLastName.value = user.userLastname
                            userPhone.value = user.userPhone?.toString() ?: ""
                            isAlertDialogShow.value = true
                        })
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Yeni kullanıcı eklemek için
                    isAlertDialogShow.value = true
                }
            ) {
                Icon(painter = painterResource(R.drawable.add), contentDescription = "")
            }

            if (isAlertDialogShow.value) {
                AlertDialog(
                    onDismissRequest = {
                        isAlertDialogShow.value = false
                    },
                    title = { Text(text = if (selectedUser.value == null) "Kişi Kaydet" else "Kişi Düzenle") },
                    text = {
                        Column {
                            // 1. TextField
                            TextField(
                                value = userName.value,
                                onValueChange = { userName.value = it },
                                label = { Text("Ad") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 2. TextField
                            TextField(
                                value = userLastName.value,
                                onValueChange = { userLastName.value = it },
                                label = { Text("Soyad") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 3. TextField
                            TextField(
                                value = userPhone.value,
                                onValueChange = { userPhone.value = it },
                                label = { Text("Telefon") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val user = User(
                                    userName.value,
                                    userLastName.value,
                                    userPhone.value.toIntOrNull()
                                )
                                if (selectedUser.value == null) {
                                    viewModel.saveUser(user)
                                } else {
                                    user.id = selectedUser.value?.id
                                    viewModel.updateUser(user)
                                }
                                isAlertDialogShow.value = false
                                userName.value = ""
                                userLastName.value = ""
                                userPhone.value = ""
                            }
                        ) {
                            Text("Kaydet")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                isAlertDialogShow.value = false
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
fun UserCard(user: User, viewModel: UserViewModel, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Ad: ${user.userName}")
                Icon(
                    modifier = Modifier.clickable {
                        viewModel.deleteUser(user)
                    },
                    painter = painterResource(R.drawable.delete), contentDescription = "")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Soyad: ${user.userLastname}")
                Icon(
                    modifier = Modifier.clickable {
                        onEditClick()
                    },
                    painter = painterResource(R.drawable.edit), contentDescription = "")
            }
        }
    }
}
