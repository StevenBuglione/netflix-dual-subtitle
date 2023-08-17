package preferences

import Preference
import chrome.runtime.getURL
import chrome.storage.ChromeStorageSync
import chrome.tabs.*


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

    fun updatePreference(updateSetting: String, setting: String, value: dynamic) {
      console.log("background.js received message to update $setting to $value")
      ChromeStorageSync.set(setting, value as Any);
      ChromeTabs.sendMessageToActiveTab(updateSetting,value)
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
