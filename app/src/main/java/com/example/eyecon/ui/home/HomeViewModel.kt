package com.example.eyecon.ui.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class HomeViewModel : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _profilePhotoUrl = MutableLiveData<Uri?>()
    val profilePhotoUrl: LiveData<Uri?> = _profilePhotoUrl


    private val auth = FirebaseAuth.getInstance()


    fun showusername() {
        val user = auth.currentUser
        _username.value = user?.displayName // Set the username or default to "User"
    }


    fun showphoto() {
        val user = auth.currentUser
        _profilePhotoUrl.value = user?.photoUrl
    }


    fun signOut() {
        auth.signOut()
    }
}