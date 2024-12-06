package com.example.eyecon.ui.detail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel: ViewModel() {
    private val _cekphoto = MutableLiveData<Uri?>()
    val cekphoto: LiveData<Uri?> = _cekphoto

    fun cekphoto(uri: Uri?) {
        _cekphoto.value = uri
    }
}