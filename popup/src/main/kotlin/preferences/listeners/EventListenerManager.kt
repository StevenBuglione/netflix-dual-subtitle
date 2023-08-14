package preferences.listeners

import chrome.runtime.ChromeRuntime
import chrome.tabs.CreateProperties
import kotlinx.browser.window
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    console.log("Setting up event listeners...")
    waitForDOMContentLoaded()
    setupGlobalClickHandler()
    setupChangeListenerSlider(elements.slider, elements.slideValue) {
      console.log("Slider value changed to: $it")
      ChromeRuntime.sendMessage("update_font_multiplier", it)
    }
    setupChangeListenerSlider(elements.opacitySlider, elements.opacitySliderValue) {
      console.log("Opacity slider value changed to: $it")
      ChromeRuntime.sendMessage("update_opacity", it)
    }
    setupChangeListenerSlider(elements.originalOpacitySlider, elements.originalOpacitySliderValue) {
      console.log("Original opacity slider value changed to: $it")
      ChromeRuntime.sendMessage("update_originaltext_opacity", it)
    }
    setupInputListener(elements.colorPicker) {
      console.log("Color picker value changed to: $it")
      ChromeRuntime.sendMessage("update_text_color", it)
    }
    setupInputListener(elements.originalColorPicker) {
      console.log("Original color picker value changed to: $it")
      ChromeRuntime.sendMessage("update_originaltext_color", it)
    }
    setupCheckboxChangeListener(elements.onSwitch) {
      console.log("OnSwitch checked status: $it")
      ChromeRuntime.sendMessage("update_on_off", it)
    }
    setupCheckboxChangeListener(elements.buttonOnSwitch) {
      console.log("ButtonOnSwitch checked status: $it")
      ChromeRuntime.sendMessage("update_button_on_off", it)
    }
    setupCheckboxChangeListener(elements.buttonUpDownMode) {
      console.log("ButtonUpDownMode checked status: $it")
      ChromeRuntime.sendMessage("update_button_up_down_mode", it)
    }
    setupResetButtonListener()
    console.log("Event listeners setup complete.")
  }

  private fun setupChangeListenerSlider(slider: HTMLInputElement, sliderValue: HTMLDivElement, action: suspend (String) -> Unit) {
    slider.addEventListener("change", {
      sliderValue.innerHTML = slider.value
      console.log("Change listener triggered for slider: ${slider.id}")
      GlobalScope.launch { action(slider.value) }
    })
  }

  private fun setupCheckboxChangeListener(element: HTMLInputElement, action: suspend (Boolean) -> Unit) {
    element.addEventListener("change", {
      console.log("Change listener triggered for checkbox: ${element.id}")
      GlobalScope.launch { action(element.checked) }
    })
  }

  private fun setupInputListener(element: HTMLInputElement, action: suspend (String) -> Unit) {
    element.addEventListener("input", {
      console.log("Input listener triggered for element: ${element.id}")
      GlobalScope.launch { action(element.value) }
    })
  }

  private fun waitForDOMContentLoaded(): Promise<Unit> = GlobalScope.promise {
    window.document.addEventListener("DOMContentLoaded", EventListener {
      if (window.location.pathname == "/update.html") {
        console.log("Cancellation due to pathname being /update.html")
        throw CancellationException("Pathname is /update.html")
      }
    })
  }

  private fun setupGlobalClickHandler() {
    window.addEventListener("click", EventListener { event ->
      val target = event.target
      if (target is HTMLAnchorElement && target.href.isNotEmpty()) {
        console.log("Global click handler triggered for anchor element with href: ${target.href}")
        chrome.tabs.create(CreateProperties { url = target.href })
      }
    })
  }

  private fun setupResetButtonListener() {
    elements.resetButton.addEventListener("click", EventListener {
      console.log("Reset button clicked.")
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

