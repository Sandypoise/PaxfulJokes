package com.andersenlab.paxfuljokes.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.andersenlab.paxfuljokes.R
import com.andersenlab.paxfuljokes.model.local.PrefManager
import com.andersenlab.paxfuljokes.ui.settings.viewmodel.SettingsModelFactory
import com.andersenlab.paxfuljokes.ui.settings.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            prefManager = PrefManager(it.applicationContext)
            settingsViewModel =
                ViewModelProviders.of(
                    this,
                    SettingsModelFactory(
                        prefManager
                    )
                )
                    .get(SettingsViewModel::class.java)
        }
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        firstnameEdit.doOnTextChanged { text, _, _, _ ->
            val searchText = text.toString().trim()
            settingsViewModel.handleFirstNameEdit(searchText)
        }

        lastnameEdit.doOnTextChanged { text, _, _, _ ->
            val searchText = text.toString().trim()
            settingsViewModel.handleLastNameEdit(searchText)
        }

        settingsSwitch.isChecked = prefManager.isOffline
        settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefManager.isOffline = isChecked
        }
    }
}