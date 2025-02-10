package com.helisur.helisurapp.domain.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class InternetViewModel(private val connectivityRepository: ConnectivityRepository) : ViewModel() {
    val isOnline = connectivityRepository.isConnected.asLiveData()
}