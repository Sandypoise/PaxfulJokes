package com.andersenlab.paxfuljokes.model.local

import android.content.Context
import android.content.SharedPreferences

private const val PREF_STORAGE_NAME = "default_prefs"
private const val SHARED_PREF_FIRSTNAME = "SHARED_PREF_FIRSTNAME"
private const val SHARED_PREF_LASTNAME = "SHARED_PREF_LASTNAME"
private const val SHARED_PREF_OFFLINE_MODE = "SHARED_PREF_OFFLINE_MODE"
private const val DEFAULT_FIRST_NAME = "Chuck"
private const val DEFAULT_LAST_NAME = "Norris"
private const val FIRST_NAME_PLACEHOLDER = "%1\$s"
private const val LAST_NAME_PLACEHOLDER = "%2\$s"

class PrefManager(appContext: Context) {

    private val sharedPref: SharedPreferences =
        appContext.getSharedPreferences(PREF_STORAGE_NAME, Context.MODE_PRIVATE)

    val firstNameNullable: String?
        get() {
            val name = sharedPref.getString(SHARED_PREF_FIRSTNAME, "")
            return if (name.isNullOrBlank()) {
                null
            } else {
                name
            }
        }

    val lastNameNullable: String?
        get() {
            val name = sharedPref.getString(SHARED_PREF_LASTNAME, "")
            return if (name.isNullOrBlank()) {
                null
            } else {
                name
            }
        }

    var firstName: String
        get() {
            val name = sharedPref.getString(
                SHARED_PREF_FIRSTNAME,
                DEFAULT_FIRST_NAME
            )
            return if (name.isNullOrBlank()) {
                DEFAULT_FIRST_NAME
            } else {
                name
            }
        }
        set(value) {
            with(sharedPref.edit()) {
                putString(
                    SHARED_PREF_FIRSTNAME,
                    if (value.trim() == DEFAULT_FIRST_NAME) {
                        ""
                    } else {
                        value
                    }
                )
                apply()
            }
        }

    var lastName: String
        get() {
            val name = sharedPref.getString(
                SHARED_PREF_LASTNAME,
                DEFAULT_LAST_NAME
            )
            return if (name.isNullOrBlank()) {
                DEFAULT_LAST_NAME
            } else {
                name
            }
        }
        set(value) {
            with(sharedPref.edit()) {
                putString(
                    SHARED_PREF_LASTNAME,
                    if (value.trim() == DEFAULT_LAST_NAME) {
                        ""
                    } else {
                        value
                    }
                )
                apply()
            }
        }

    var isOffline
        get() = sharedPref.getBoolean((SHARED_PREF_OFFLINE_MODE), false)
        set(value) {
            with(sharedPref.edit()) {
                putBoolean(SHARED_PREF_OFFLINE_MODE, value)
                apply()
            }
        }


    fun replaceNameWithPlaceholder(
        input: String
    ) =
        input
            .replace(
                firstName,
                FIRST_NAME_PLACEHOLDER
            )
            .replace(
                lastName,
                LAST_NAME_PLACEHOLDER
            )
}