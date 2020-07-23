package com.andersenlab.paxfuljokes.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andersenlab.paxfuljokes.model.local.PrefManager

class SettingsModelFactory(private val prefManager: PrefManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            prefManager
        ) as T
    }
}