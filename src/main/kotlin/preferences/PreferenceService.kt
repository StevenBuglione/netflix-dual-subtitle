package preferences

import Preference
import chrome.runtime.getURL
import chrome.storage.ChromeStorageSync
import chrome.tabs.CreateProperties
import chrome.tabs.create


class PreferenceService {

    private val preferences = listOf(
        Preference("font_multiplier", 1),
        Preference("text_color", "#FFFFFF"),
        Preference("opacity", .8),
        Preference("on_off", 1),
        Preference("button_on_off", 1),
        Preference("originaltext_opacity", 1),
        Preference("originaltext_color", "#fff000"),
        Preference("button_up_down_mode", 1)
    )

    fun updatePreference(key: String, value: dynamic) {
        console.log("background.js received message to update $key to $value")
        ChromeStorageSync.set(key, value as Any)
    }

    fun openPopup() {
        val createProperties = CreateProperties {
            url = getURL("tutorial.html")
        }
        create(createProperties)
    }

    suspend fun checkPreferences(){
        preferences.forEach { it.fetch() }
    }

}