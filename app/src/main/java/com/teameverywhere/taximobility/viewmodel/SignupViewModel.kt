package com.teameverywhere.taximobility.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teameverywhere.taximobility.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*

class SignupViewModel: ViewModel() {
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }

    val loginComplete = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    var userName = MutableLiveData<String>()
    var userPhone = MutableLiveData<String>()
    var userLicense = MutableLiveData<String>()
    var userPassword = MutableLiveData<String>()

    fun sendUserName(name: String){
        userName.value = name
    }

    fun sendUserPhone(phone: String){
        userPhone.value = phone
    }

    fun sendUserLicense(license: String){
        userLicense.value = license
    }

    fun sendUserPassword(password: String){
        userPassword.value = password
    }

    fun signUp(name: String, phone: String, license: String, password: String){
        MyApplication.prefs.setName("name", name)
        MyApplication.prefs.setPhone("phone", phone)
        MyApplication.prefs.setLicense("license", license)
        MyApplication.prefs.setPassword("password", password)

        loading.value = true
        job = CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
            loginComplete.value = true
            error.value = null
            loading.value = false
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