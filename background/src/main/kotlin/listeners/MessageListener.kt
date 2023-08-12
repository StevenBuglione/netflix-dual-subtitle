package listeners

import chrome.runtime.onMessage
import preferences.PreferenceService

class MessageListener {

    private val preferenceService = PreferenceService()

    fun setupMessageListeners() {
        onMessage.addListener { request, _, _ ->
            when (request.message) {
                "open_popup" -> preferenceService.openPopup()
                "update_on_off" -> preferenceService.updatePreference("on_off", request.value)
                "update_button_on_off" -> preferenceService.updatePreference("button_on_off", request.value)
                "update_button_up_down_mode" -> preferenceService.updatePreference("button_up_down_mode", request.value)
                "update_font_multiplier" -> preferenceService.updatePreference("font_multiplier", request.value)
                "update_text_color" -> preferenceService.updatePreference("text_color", request.value)
                "update_opacity" -> preferenceService.updatePreference("opacity", request.value)
                "update_originaltext_opacity" -> preferenceService.updatePreference("originaltext_opacity", request.value)
                "update_originaltext_color" -> preferenceService.updatePreference("originaltext_color", request.value)
            }
        }
    }

}