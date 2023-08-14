package preferences

import chrome.runtime.ChromeRuntime
import chrome.storage.ChromeStorageSync
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

class PreferencesManger {

  private val slider = document.getElementById("mySlider")as HTMLInputElement;
  private val slideValue = document.getElementById("mySliderValue")as HTMLDivElement;
  private val opacitySlider = document.getElementById("opacitySlider")as HTMLInputElement;
  private val opacitySliderValue = document.getElementById("opacitySliderValue")as HTMLDivElement;
  private val originalOpacitySlider = document.getElementById("originalOpacitySlider") as HTMLInputElement
  private val originalOpacitySliderValue = document.getElementById("originalOpacitySliderValue") as HTMLDivElement
  private val colorPicker = document.getElementById("myColorPicker")as HTMLInputElement;
  private val originalColorPicker = document.getElementById("myOriginalColorPicker")as HTMLInputElement;
  private val resetButton = document.getElementById("resetButton")as HTMLInputElement;
  private val onSwitch = document.getElementById("switchValue")as HTMLInputElement;
  private val buttonOnSwitch = document.getElementById("button_switchValue")as HTMLInputElement;
  private val buttonUpDownMode = document.getElementById("button_upDownValue")as HTMLInputElement;

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

    setupEventListeners()
    setupResetButtonListener()
  }

  private suspend fun <T> updatePreference(key: String, updateAction: (T) -> Unit) {
    val storedValue = ChromeStorageSync.get(key)
    @Suppress("UNCHECKED_CAST")
    console.log("Saved Vale" + storedValue[key])
    updateAction(storedValue[key] as T)
  }


  suspend fun updateFontMultiplier() = updatePreference<String>("font_multiplier") {
    slideValue.innerHTML = it
    slider.value = it
  }

  suspend fun updateOpacity() = updatePreference<String>("opacity") {
    opacitySliderValue.innerHTML = it
    opacitySlider.value = it
  }

  suspend fun updateOriginalTextOpacity() = updatePreference<String>("originaltext_opacity") {
    originalOpacitySliderValue.innerHTML = it
    originalOpacitySlider.value = it
  }

  suspend fun updateTextColor() = updatePreference<String>("text_color") {
    colorPicker.value = it
  }

  suspend fun updateOriginalTextColor() = updatePreference<String>("originaltext_color") {
    originalColorPicker.value = it
  }

  suspend fun updateOnOff() = updatePreference<Boolean>("on_off") {
    onSwitch.checked = it
  }

  suspend fun updateOnOffButton() = updatePreference<Boolean>("button_on_off") {
    buttonOnSwitch.checked = it
  }

  suspend fun updateButtonUpDownMode() = updatePreference<Boolean>("button_up_down_mode") {
    buttonUpDownMode.checked = it
  }

  private fun setupEventListeners() {
    setupChangeListenerSlider(slider, slideValue){ ChromeRuntime.sendMessage("update_font_multiplier",it)}
    setupChangeListenerSlider(opacitySlider,opacitySliderValue) { ChromeRuntime.sendMessage("update_opacity", it) }
    setupChangeListenerSlider(originalOpacitySlider,originalOpacitySliderValue) { ChromeRuntime.sendMessage("update_originaltext_opacity", it) }
    setupInputListener(colorPicker) { ChromeRuntime.sendMessage("update_text_color", it) }
    setupInputListener(originalColorPicker) { ChromeRuntime.sendMessage("update_originaltext_color", it) }
    setupCheckboxChangeListener(onSwitch) { ChromeRuntime.sendMessage("update_on_off", it) }
    setupCheckboxChangeListener(buttonOnSwitch) { ChromeRuntime.sendMessage("update_button_on_off", it) }
    setupCheckboxChangeListener(buttonUpDownMode) { ChromeRuntime.sendMessage("update_button_up_down_mode", it) }
  }

  private fun setupChangeListenerSlider(slider: HTMLInputElement,sliderValue: HTMLDivElement, action: (String) -> Unit) {
    slider.addEventListener("change",{
      sliderValue.innerHTML = slider.value
    })
  }

  private fun setupCheckboxChangeListener(element: HTMLInputElement, action: (Boolean) -> Unit) {
    element.addEventListener("change", {
      action(element.checked)
    })
  }


  private fun setupInputListener(element: HTMLInputElement, action: (String) -> Unit) {
    element.addEventListener("input", { action(element.value) })
  }

  private fun setupResetButtonListener() {
    resetButton.addEventListener("click", EventListener {
      colorPicker.value = "#FFFFFF"
      colorPicker.dispatchEvent(Event("input"))

      originalColorPicker.value = "#FFF000"
      originalColorPicker.dispatchEvent(Event("input"))

      opacitySlider.value = "0.8"
      opacitySlider.dispatchEvent(Event("change"))

      originalOpacitySlider.value = "1"
      originalOpacitySlider.dispatchEvent(Event("change"))

      slider.value = "1"
      slider.dispatchEvent(Event("change"))
    })
  }

  private fun HTMLInputElement.resetTo(value: String) {
    this.value = value
    this.dispatchEvent(org.w3c.dom.events.Event("change"))
  }


}

