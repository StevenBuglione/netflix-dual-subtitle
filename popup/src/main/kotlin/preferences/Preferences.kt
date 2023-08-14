package preferences

import chrome.runtime.ChromeRuntime
import chrome.storage.ChromeStorageSync
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

class Preferences {

  val slider = document.getElementById("mySlider")as HTMLInputElement;
  val slideValue = document.getElementById("mySliderValue")as HTMLDivElement;

  val opacitySlider = document.getElementById("opacitySlider")as HTMLInputElement;
  val opacitySliderValue = document.getElementById("opacitySliderValue")as HTMLDivElement;

  val originalOpacitySlider = document.getElementById("originalOpacitySlider") as HTMLInputElement
  val originalOpacitySliderValue = document.getElementById("originalOpacitySliderValue") as HTMLDivElement

  val colorPicker = document.getElementById("myColorPicker")as HTMLInputElement;
  val originalColorPicker = document.getElementById("myOriginalColorPicker")as HTMLInputElement;

  val resetButton = document.getElementById("resetButton")as HTMLInputElement;
  val onSwitch = document.getElementById("switchValue")as HTMLInputElement;
  val buttonOnSwitch = document.getElementById("button_switchValue")as HTMLInputElement;
  val buttonUpDownMode = document.getElementById("button_upDownValue")as HTMLInputElement;

  suspend fun init(){
    updateFontMultiplier()
    updateOpacity()
    updateTextColor()
    updateOriginalTextColor()
    updateOriginalTextOpacity()
    updateOnOff()
    updateOnOffButton()
    updateButtonUpDownMode()
    setupEventListeners()
    setupResetButtonListener()
  }


  suspend fun updateFontMultiplier(){
    val key = "font_multiplier"
    val storedVal = ChromeStorageSync.get(key)
    @Suppress("UnsafeCastFromDynamic")
    console.log(storedVal[key])
    slider.value = storedVal[key]
    slideValue.innerHTML = storedVal[key]

  }

  suspend fun updateOpacity(){
    val key = "opacity"
    val storedVal = ChromeStorageSync.get(key)
    console.log(storedVal[key])
    opacitySlider.value = storedVal[key]
    opacitySliderValue.innerHTML = storedVal[key]
  }


  suspend fun updateOriginalTextOpacity(){
    val key = "originaltext_opacity"
    val storedVal = ChromeStorageSync.get(key)
    console.log(storedVal[key])
    originalOpacitySlider.value = storedVal[key]
    originalOpacitySliderValue.innerHTML = storedVal[key]
  }


  suspend fun updateTextColor(){
    val key = "text_color"
    val storedVal = ChromeStorageSync.get(key)
    console.log(storedVal[key])
    colorPicker.value = storedVal[key]
  }

  suspend fun updateOriginalTextColor(){
    val key = "originaltext_color"
    val storedVal = ChromeStorageSync.get(key)
    console.log(storedVal[key])
    originalColorPicker.value = storedVal[key]
  }

  suspend fun updateOnOff(){
    val key = "on_off"
    val storedVal = ChromeStorageSync.get(key)
    console.log(storedVal[key])
    onSwitch.checked = storedVal[key]
  }


  suspend fun updateOnOffButton(){
    val key = "button_on_off"
    val storedVal = ChromeStorageSync.get(key)
    console.log(storedVal[key])
    buttonOnSwitch.checked = storedVal[key]
  }


  suspend fun updateButtonUpDownMode(){
    val key = "button_up_down_mode"
    val storedVal = ChromeStorageSync.get(key)
    console.log(storedVal[key])
    buttonUpDownMode.checked = storedVal[key]
  }

  private fun setupEventListeners() {
    slider.addEventListener("change", {
      slideValue.innerHTML = slider.value
      ChromeRuntime.sendMessage("update_opacity", slider.value)
    })

    opacitySlider.addEventListener("change", {
      opacitySliderValue.innerHTML = opacitySlider.value
      ChromeRuntime.sendMessage("update_opacity", opacitySlider.value)
    })

    originalOpacitySlider.addEventListener("change", {
      originalOpacitySliderValue.innerHTML = originalOpacitySlider.value
      ChromeRuntime.sendMessage("update_originaltext_opacity", originalOpacitySlider.value)
    })

    colorPicker.addEventListener("input", {
      ChromeRuntime.sendMessage("update_text_color", colorPicker.value)
    })

    originalColorPicker.addEventListener("input", {
      ChromeRuntime.sendMessage("update_originaltext_color", originalColorPicker.value)
    })

    onSwitch.addEventListener("change", {
      ChromeRuntime.sendMessage("update_on_off", onSwitch.checked)
    })

    buttonOnSwitch.addEventListener("change", {
      ChromeRuntime.sendMessage("update_button_on_off", buttonOnSwitch.checked)
    })

    buttonUpDownMode.addEventListener("change", {
      ChromeRuntime.sendMessage("update_button_up_down_mode", buttonUpDownMode.checked)
    })

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

}
