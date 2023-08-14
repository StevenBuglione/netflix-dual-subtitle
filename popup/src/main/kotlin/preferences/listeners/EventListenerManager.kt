package preferences.listeners

import chrome.runtime.ChromeRuntime
import chrome.tabs.CreateProperties
import kotlinx.browser.window
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import preferences.wrappers.ElementWrapper
import kotlin.js.Promise

class EventListenerManager(private val elements: ElementWrapper) {
  fun setupEventListeners() {
    waitForDOMContentLoaded()
    setupGlobalClickHandler()
    setupChangeListenerSlider(elements.slider, elements.slideValue){ ChromeRuntime.sendMessage("update_font_multiplier",it)}
    setupChangeListenerSlider(elements.opacitySlider,elements.opacitySliderValue) { ChromeRuntime.sendMessage("update_opacity", it) }
    setupChangeListenerSlider(elements.originalOpacitySlider,elements.originalOpacitySliderValue) { ChromeRuntime.sendMessage("update_originaltext_opacity", it) }
    setupInputListener(elements.colorPicker) { ChromeRuntime.sendMessage("update_text_color", it) }
    setupInputListener(elements.originalColorPicker) { ChromeRuntime.sendMessage("update_originaltext_color", it) }
    setupCheckboxChangeListener(elements.onSwitch) { ChromeRuntime.sendMessage("update_on_off", it) }
    setupCheckboxChangeListener(elements.buttonOnSwitch) { ChromeRuntime.sendMessage("update_button_on_off", it) }
    setupCheckboxChangeListener(elements.buttonUpDownMode) { ChromeRuntime.sendMessage("update_button_up_down_mode", it) }
    setupResetButtonListener()
  }

  private fun setupChangeListenerSlider(slider: HTMLInputElement, sliderValue: HTMLDivElement, action: (String) -> Unit) {
    slider.addEventListener("change",{
      console.log("Updating Slider" + slider.value)
      sliderValue.innerHTML = slider.value
    })
  }

  private fun setupCheckboxChangeListener(element: HTMLInputElement, action: (Boolean) -> Unit) {
    element.addEventListener("change", {
      console.log("Updating CheckBox")
      action(element.checked)
    })
  }
  private fun setupInputListener(element: HTMLInputElement, action: (String) -> Unit) {
    console.log("Updating imput")
    element.addEventListener("input", { action(element.value) })
  }

  private fun waitForDOMContentLoaded(): Promise<Unit> = GlobalScope.promise {
    window.document.addEventListener("DOMContentLoaded", EventListener {
      if (window.location.pathname == "/update.html") {
        throw CancellationException("Pathname is /update.html")
      }
    })
  }


  private fun setupGlobalClickHandler() {
    window.addEventListener("click", EventListener { event ->
      val target = event.target
      if (target is HTMLAnchorElement && target.href.isNotEmpty()) {
        chrome.tabs.create(CreateProperties { url = target.href })
      }
    })
  }

  private fun setupResetButtonListener() {
    elements.resetButton.addEventListener("click", EventListener {
      elements.colorPicker.value = "#FFFFFF"
      elements.colorPicker.dispatchEvent(Event("input"))
      elements.originalColorPicker.value = "#FFF000"
      elements.originalColorPicker.dispatchEvent(Event("input"))
      elements.opacitySlider.value = "0.8"
      elements.opacitySlider.dispatchEvent(Event("change"))
      elements.originalOpacitySlider.value = "1"
      elements.originalOpacitySlider.dispatchEvent(Event("change"))
      elements.slider.value = "1"
      elements.slider.dispatchEvent(Event("change"))
    })
  }
}
