package preferences.updaters

import chrome.storage.ChromeStorageSync
import preferences.wrappers.ElementWrapper

class PreferenceUpdater(private val elements: ElementWrapper) {

  suspend fun init() {
    listOf(
      ::updateFontMultiplier,
      ::updateOpacity,
      ::updateTextColor,
      ::updateOriginalTextColor,
      ::updateOriginalTextOpacity,
      ::updateOnOff,
      ::updateOnOffButton,
      ::updateButtonUpDownMode
    ).forEach { it.invoke() }
  }

  private suspend fun <T> updatePreference(key: String, updateAction: (T) -> Unit) {
    val storedValue = ChromeStorageSync.get(key)
    @Suppress("UNCHECKED_CAST")
    console.log("Updating Value to : " + storedValue[key])
    updateAction(storedValue[key] as T)
  }

  suspend fun updateFontMultiplier() = updatePreference<String>("font_multiplier") {
    elements.slideValue.innerHTML = it
    elements.slider.value = it
  }

  suspend fun updateOpacity() = updatePreference<String>("opacity") {
    elements.opacitySliderValue.innerHTML = it
    elements.opacitySlider.value = it
  }

  suspend fun updateOriginalTextOpacity() = updatePreference<String>("originaltext_opacity") {
    elements.originalOpacitySliderValue.innerHTML = it
    elements.originalOpacitySlider.value = it
  }

  suspend fun updateTextColor() = updatePreference<String>("text_color") {
    elements.colorPicker.value = it
  }

  suspend fun updateOriginalTextColor() = updatePreference<String>("originaltext_color") {
    elements.originalColorPicker.value = it
  }

  suspend fun updateOnOff() = updatePreference<Boolean>("on_off") {
    elements.onSwitch.checked = it
  }

  suspend fun updateOnOffButton() = updatePreference<Boolean>("button_on_off") {
    elements.buttonOnSwitch.checked = it
  }

  suspend fun updateButtonUpDownMode() = updatePreference<Boolean>("button_up_down_mode") {
    elements.buttonUpDownMode.checked = it
  }
}
