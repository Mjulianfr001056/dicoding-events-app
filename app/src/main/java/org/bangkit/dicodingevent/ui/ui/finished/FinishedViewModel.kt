package org.bangkit.dicodingevent.ui.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.bangkit.dicodingevent.data.response.DicodingEventResponse
import org.bangkit.dicodingevent.data.response.ListEventsItem
import org.bangkit.dicodingevent.data.retrofit.NetworkModule
import org.bangkit.dicodingevent.ui.ui.upcoming.UpcomingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    private val _eventList = MutableLiveData<List<ListEventsItem>>()
    val eventlist : LiveData<List<ListEventsItem>> = _eventList

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "FinishedActivityViewModel"
    }

    init{
        getEvents()
    }

    private fun getEvents(){
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
                        _eventList.value = response.body()?.listEvents
                    }
                }else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
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
//                        Log.d(TAG, "onResponse: ${_searchResults.value}")
                    }
                }else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}