package com.example.eyecon.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eyecon.R
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _profilePhotoUrl = MutableLiveData<Uri?>()
    val profilePhotoUrl: LiveData<Uri?> = _profilePhotoUrl


    private val auth = FirebaseAuth.getInstance()


    fun showusername(){
        val user = auth.currentUser
        _username.value = user?.displayName ?: "User" // Set the username or default to "User"
    }

    fun showphoto() {
        val user = auth.currentUser
        _profilePhotoUrl.value = user?.photoUrl
    }

    fun showemail(){
        val user = auth.currentUser
        _email.value = user?.email ?: "User"
    }
    fun signOut() {
        auth.signOut()
    }
}