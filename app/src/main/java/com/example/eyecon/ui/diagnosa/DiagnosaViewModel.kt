package com.example.eyecon.ui.diagnosa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiagnosaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Diagnosa Fragment"
    }
    val text: LiveData<String> = _text
}