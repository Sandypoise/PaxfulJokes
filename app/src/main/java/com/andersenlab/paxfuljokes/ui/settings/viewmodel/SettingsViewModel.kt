package com.andersenlab.paxfuljokes.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersenlab.paxfuljokes.model.local.PrefManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefManager: PrefManager) : ViewModel() {

    private var searchFirst = ""
    private var searchSecond = ""

    fun handleFirstNameEdit(searchText: String) {
        if (searchText == searchFirst) return
        searchFirst = searchText
        viewModelScope.launch {
            delay(500)
            if (searchText != searchFirst) {
                return@launch
            }

            prefManager.firstName = searchText
        }
    }

    fun handleLastNameEdit(searchText: String) {
        if (searchText == searchSecond) return
        searchSecond = searchText
        viewModelScope.launch {
            delay(500)
            if (searchText != searchSecond)
                return@launch

            prefManager.lastName = searchText
        }
    }
}