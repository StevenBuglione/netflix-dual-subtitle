package preferences.updaters

import chrome.storage.ChromeStorageSync
import preferences.wrappers.ElementWrapper

class PreferenceUpdater(private val elements: ElementWrapper) {

  suspend fun init() {
    console.log("Initializing PreferenceUpdater...")
    listOf(
      ::updateFontMultiplier,
      ::updateOpacity,
      ::updateTextColor,
      ::updateOriginalTextColor,
      ::updateOriginalTextOpacity,
      ::updateOnOff,
      ::updateOnOffButton,
      ::updateButtonUpDownMode
    ).forEach {
      console.log("Invoking function: ${it.name}")
      it.invoke()
    }
    console.log("PreferenceUpdater initialization complete.")
  }

  private suspend fun <T> updatePreference(key: String, updateAction: (T) -> Unit) {
    console.log("Updating preference for key: $key")
    val storedValue = ChromeStorageSync.get(key)
    @Suppress("UNCHECKED_CAST")
    val value = storedValue[key] as T
    console.log("Retrieved value for key $key: $value")
    updateAction(value)
    console.log("Updated preference for key $key successfully.")
  }

  suspend fun updateFontMultiplier() {
    console.log("Updating font multiplier...")
    updatePreference<String>("font_multiplier") {
      elements.slideValue.innerHTML = it
      elements.slider.value = it
    }
    console.log("Font multiplier updated.")
  }

  suspend fun updateOpacity() {
    console.log("Updating opacity...")
    updatePreference<String>("opacity") {
      elements.opacitySliderValue.innerHTML = it
      elements.opacitySlider.value = it
    }
    console.log("Opacity updated.")
  }

  suspend fun updateOriginalTextOpacity() {
    console.log("Updating original text opacity...")
    updatePreference<String>("originaltext_opacity") {
      elements.originalOpacitySliderValue.innerHTML = it
      elements.originalOpacitySlider.value = it
    }
    console.log("Original text opacity updated.")
  }

  suspend fun updateTextColor() {
    console.log("Updating text color...")
    updatePreference<String>("text_color") {
      elements.colorPicker.value = it
    }
    console.log("Text color updated.")
  }

  suspend fun updateOriginalTextColor() {
    console.log("Updating original text color...")
    updatePreference<String>("originaltext_color") {
      elements.originalColorPicker.value = it
    }
    console.log("Original text color updated.")
  }

  suspend fun updateOnOff() {
    console.log("Updating on/off switch...")
    updatePreference<Boolean>("on_off") {
      elements.onSwitch.checked = it
    }
    console.log("On/off switch updated.")
  }

  suspend fun updateOnOffButton() {
    console.log("Updating on/off button...")
    updatePreference<Boolean>("button_on_off") {
      elements.buttonOnSwitch.checked = it
    }
    console.log("On/off button updated.")
  }

  suspend fun updateButtonUpDownMode() {
    console.log("Updating button up-down mode...")
    updatePreference<Boolean>("button_up_down_mode") {
      elements.buttonUpDownMode.checked = it
    }
    console.log("Button up-down mode updated.")
  }
}

