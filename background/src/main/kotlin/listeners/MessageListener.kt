package listeners

import chrome.runtime.onMessage
import preferences.PreferenceService

class MessageListener {

    private val preferenceService = PreferenceService()

    fun setupMessageListeners() {
        onMessage.addListener { request, _, _ ->
            when (request.message) {
                "open_popup" -> preferenceService.openPopup()
                "update_on_off" -> preferenceService.updatePreference("update_on_off","on_off", request.value)
                "update_button_on_off" -> preferenceService.updatePreference("update_button_on_off","button_on_off", request.value)
                "update_button_up_down_mode" -> preferenceService.updatePreference("update_button_up_down_mode","button_up_down_mode", request.value)
                "update_font_multiplier" -> preferenceService.updatePreference("update_font_multiplier","font_multiplier", request.value)
                "update_text_color" -> preferenceService.updatePreference("update_text_color","text_color", request.value)
                "update_opacity" -> preferenceService.updatePreference("update_opacity","opacity", request.value)
                "update_originaltext_opacity" -> preferenceService.updatePreference("update_originaltext_opacity","originaltext_opacity", request.value)
                "update_originaltext_color" -> preferenceService.updatePreference("update_originaltext_color","originaltext_color", request.value)
            }
        }
    }

}
