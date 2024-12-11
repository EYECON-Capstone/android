package com.example.eyecon.ui.addphoto

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eyecon.data.photo.local.entity.HistoryEntity
import com.example.eyecon.data.photo.remote.PhotoRepository
import com.example.eyecon.data.photo.remote.response.Data
import com.example.eyecon.data.photo.remote.response.DataItem
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddPhotoViewModel(private val photoRepository: PhotoRepository): ViewModel() {
    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _resultData = MutableLiveData<DataItem?>()
    val resultData: LiveData<DataItem?> = _resultData

    private val _listHistory = MutableLiveData<List<HistoryEntity>>()
    val listHistory: LiveData<List<HistoryEntity>> = _listHistory



    private val _detailphoto = MutableLiveData<Result<Data>?>(null)
    val detailphoto: LiveData<Result<Data>?> get() = _detailphoto


    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }
    fun predictphoto(
        image: MultipartBody.Part,
        idUser: RequestBody
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Mengaktifkan indikator loading sebelum proses dimulai
                Log.d("AddPhotoViewModel", "Sending image for prediction...")

                val result = photoRepository.predictphoto(image, idUser)
                result.onSuccess {
                    Log.d("AddPhotoViewModel", "Prediction successful")
                    _resultData.value = it // DataItem
                }.onFailure {
                    Log.e("AddPhotoViewModel", "Prediction failed: ${it.message}")
                }
            } catch (e: Exception) {
                Log.e("AddPhotoViewModel", "An error occurred: ${e.message}")
            } finally {
                _isLoading.value = false // Mematikan indikator loading setelah proses selesai
            }
        }
    }
    fun getDetailPhoto(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Turn on loading indicator before the network call
                val result = photoRepository.getDetailPhoto(id)
                _detailphoto.value = result
            } catch (e: Exception) {
                Log.e("AddPhotoViewModel", "Error occurred while fetching photo details: ${e.message}")
            } finally {
                _isLoading.value = false // Turn off loading indicator after the network call completes
            }
        }
    }

    fun deleteHistory(idUser: String) {
        viewModelScope.launch {
            try {
                photoRepository.deleteHistory(idUser)
                _isDeleted.value = true
            } catch (e: Exception) {
                _isDeleted.value = false
            }
        }
    }

    fun saveImageDetectionHistory(history: HistoryEntity) {
        viewModelScope.launch {
            photoRepository.insertHistory(history)
        }
    }


    fun getHistoryByUserId(idUser: String) {
        viewModelScope.launch {
            photoRepository.getHistoryByUserId(idUser).observeForever { histories ->
                _listHistory.value = histories
            }
        }
    }

}
class AddPhotoViewModelFactory(private val photoRepository: PhotoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddPhotoViewModel::class.java)) {
            AddPhotoViewModel(photoRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    companion object {
        fun getInstance(context: Context) = AddPhotoViewModelFactory(Injection.provideRepository(context))
    }
}