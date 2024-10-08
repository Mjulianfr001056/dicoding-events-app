package org.bangkit.dicodingevent.ui.ui.upcoming

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

class UpcomingViewModel : ViewModel() {
    private val _eventList = MutableLiveData<List<ListEventsItem>>()
    val eventlist : LiveData<List<ListEventsItem>> = _eventList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> = _searchResults

    private val _errorMessage = MutableLiveData<SingleEventWrapper<String>>()
    val errorMessage: LiveData<SingleEventWrapper<String>> = _errorMessage

    companion object{
        private const val TAG = "UpcomingActivityViewModel"
    }

    init{
        getEvents()
    }

    private fun getEvents(){
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
                        _eventList.value = response.body()?.listEvents
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

    fun searchEvent(
        query: String
    ){
        _isLoading.value = true
        val client = NetworkModule.getApiService().searchEvents(-1, query)

        client.enqueue(object : Callback<DicodingEventResponse> {
            override fun onResponse(
                call: Call<DicodingEventResponse>,
                response: Response<DicodingEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _searchResults.value = response.body()?.listEvents
                        _errorMessage.value = SingleEventWrapper("Data ditemukan")
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