package com.example.eyecon.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _profilePhotoUrl = MutableLiveData<Uri?>()
    val profilePhotoUrl: LiveData<Uri?> = _profilePhotoUrl

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val auth = FirebaseAuth.getInstance()


    fun showusername() {
        val user = auth.currentUser
        _username.value = user?.displayName ?: "User"
    }

    fun showphoto() {
        val user = auth.currentUser
        Log.d("ProfileViewModel", "Photo URL: ${user?.photoUrl}")
        _profilePhotoUrl.value = user?.photoUrl
    }

    fun updateProfilePhoto(uri: Uri) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _profilePhotoUrl.value = uri
            }
        }
    }

    fun updateUsername(newUsername: String) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newUsername)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _username.value = newUsername
            }
        }
    }


    fun showemail(){
        val user = auth.currentUser
        _email.value = user?.email ?: "User"
    }

}