package org.bangkit.dicodingevent.ui.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.bangkit.dicodingevent.data.response.DicodingEventResponse
import org.bangkit.dicodingevent.data.response.ListEventsItem
import org.bangkit.dicodingevent.data.retrofit.NetworkModule
import org.bangkit.dicodingevent.util.SingleEventWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class HomeViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents : LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents : LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<SingleEventWrapper<String>>()
    val errorMessage: LiveData<SingleEventWrapper<String>> = _errorMessage

    companion object{
        private const val TAG = "HomeActivityViewModel"
    }

    init{
        getUpcomingEvents()
        getFinishedEvents()
    }

    private fun getUpcomingEvents(){
        _isLoading.value = true
        val client = NetworkModule.getApiService().getEvents(1)
        client.enqueue(object : Callback<DicodingEventResponse> {
            override fun onResponse(
                call: Call<DicodingEventResponse>,
                response: Response<DicodingEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _upcomingEvents.value = response.body()?.listEvents?.take(5)
                    }
                }else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                    _errorMessage.value = SingleEventWrapper("Gagal mengambil data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")

                when (t) {
                    is SocketTimeoutException -> {
                        _errorMessage.value = SingleEventWrapper("Request timeout. Silakan coba lagi.")
                    }
                    is IOException -> {
                        _errorMessage.value = SingleEventWrapper("Tidak dapat terhubung ke server. Periksa koneksi internet Anda.")
                    }
                    else -> {
                        _errorMessage.value = SingleEventWrapper("Terjadi kesalahan. ${t.message}")
                    }
                }
            }
        })
    }

    private fun getFinishedEvents(){
        _isLoading.value = true
        val client = NetworkModule.getApiService().getEvents(0)
        client.enqueue(object : Callback<DicodingEventResponse> {
            override fun onResponse(
                call: Call<DicodingEventResponse>,
                response: Response<DicodingEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _finishedEvents.value = response.body()?.listEvents?.take(5)
                    }
                }else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                    _errorMessage.value = SingleEventWrapper("Gagal mengambil data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")

                when (t) {
                    is SocketTimeoutException -> {
                        _errorMessage.value = SingleEventWrapper("Request timeout. Silakan coba lagi.")
                    }
                    is IOException -> {
                        _errorMessage.value = SingleEventWrapper("Tidak dapat terhubung ke server. Periksa koneksi internet Anda.")
                    }
                    else -> {
                        _errorMessage.value = SingleEventWrapper("Terjadi kesalahan. ${t.message}")
                    }
                }
            }
        })
    }
}