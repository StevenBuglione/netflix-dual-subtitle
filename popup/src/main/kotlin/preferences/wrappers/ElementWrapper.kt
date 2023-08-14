package preferences.wrappers

import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement

class ElementWrapper {
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
}
