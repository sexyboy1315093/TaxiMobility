package com.teameverywhere.taximobility.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teameverywhere.taximobility.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }

    val loginComplete = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()
    var isRemember = MutableLiveData<Boolean>()

    var nameViewModel = MutableLiveData<String>()
    var passwordViewModel = MutableLiveData<String>()

    fun sendName(name: String){
        nameViewModel.value = name
    }

    fun sendPassword(password: String){
        passwordViewModel.value = password
    }

    fun login(name: String, password: String){
        loading.value = true
        job = CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
            if(name == MyApplication.prefs.getName("name", "0") && password == MyApplication.prefs.getPassword("password", "12038120-")){
                loginComplete.value = true
                error.value = null
                loading.value = false
            }else {
                loginComplete.value = false
                onError("로그인실패")
            }
        }
    }

    fun remember(name: String, password: String){
        job = CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
            isRemember.value = name == MyApplication.prefs.getName("name", "0") && password == MyApplication.prefs.getPassword("password", "12038120-")
        }
    }

    private fun onError(message: String){
        error.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}