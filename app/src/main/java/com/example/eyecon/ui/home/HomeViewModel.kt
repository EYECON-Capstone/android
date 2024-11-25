package com.example.eyecon.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _profilePhotoUrl = MutableLiveData<String>()
    val profilePhotoUrl: LiveData<String> get() = _profilePhotoUrl

    private val auth = FirebaseAuth.getInstance()


    fun showprofile(){
        val user = auth.currentUser
        _username.value = user?.displayName ?: "User" // Set the username or default to "User"
        _profilePhotoUrl.value = user?.photoUrl?.toString() ?: " "
    }
    fun signOut() {
        auth.signOut()
    }
}