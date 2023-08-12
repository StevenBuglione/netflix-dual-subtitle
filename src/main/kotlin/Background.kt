import chrome.runtime.OnInstalledReason
import chrome.runtime.getURL
import chrome.runtime.onInstalled
import chrome.runtime.onMessage
import chrome.storage.ChromeStorageSync
import chrome.tabs.CreateProperties
import chrome.tabs.create

class Background {

    private fun checkInstalled(){
       onInstalled.addListener { details ->
           if (details.reason == OnInstalledReason.INSTALL){
               val createProperties = CreateProperties {
                    url = "tutorial.html"
               }
               create(createProperties)
           }
       }
    }

    private suspend fun checkFontMultiplier(){
        val key = "font_multiplier"
        val fontMultiplier = ChromeStorageSync.get(key)
        if(fontMultiplier[key] != null){
            console.log("Preferences: Font_multiplier found: ");
            console.log(fontMultiplier[key]);
        }else{
            console.log("No Font Multiplier stored");
            ChromeStorageSync.set(key,1)
        }
    }

    private suspend fun checkTextColor(){
        val key = "text_color"
        val textColor = ChromeStorageSync.get(key)
        if(textColor[key] != null){
            console.log("Preferences: Text Color : ${textColor[key]}");
        }else{
            console.log("No Color Preference Found - Setting YELLOW");
            ChromeStorageSync.set(key,"#FFFFFF")
        }
    }

    private suspend fun checkOpacity(){
        val key = "opacity"
        val opacity = ChromeStorageSync.get(key)
        if(opacity[key] != null){
            console.log("Preferences: Opacity : ${opacity[key]}");
        }else{
            console.log("No Opacity Preference Found - Setting to .8");
            ChromeStorageSync.set(key,.8)
        }
    }

    private suspend fun enableDisable(){
        val key = "on_off"
        val onOff = ChromeStorageSync.get(key)
        if(onOff[key] != null){
            console.log("Preferences: on_off : ${onOff[key]}");
        }else{
            console.log("No Opacity Preference Found - Setting to ON");
            ChromeStorageSync.set(key,1)
        }
    }

    private suspend fun onOffButton(){
        val key = "button_on_off"
        val onOffButton = ChromeStorageSync.get(key)
        if(onOffButton[key] != null){
            console.log("Preferences: button_on_off : ${onOffButton[key]}");
        }else{
            console.log("No Opacity Preference Found - Setting to ON");
            ChromeStorageSync.set(key,1)
        }
    }

    private suspend fun checkOriginalTextOpacity(){
        val key = "originaltext_opacity"
        val originalTextOpacity = ChromeStorageSync.get(key)
        if(originalTextOpacity[key] != null){
            console.log("Preferences: Original Text Opacity : ${originalTextOpacity[key]}");
        }else{
            console.log("No Original Text Opacity Preference Found - Setting to 1");
            ChromeStorageSync.set(key,1)
        }
    }

    private suspend fun checkOriginalTextColor(){
        val key = "originaltext_color"
        val originalTextColor = ChromeStorageSync.get(key)
        if(originalTextColor[key] != null){
            console.log("Preferences: OriginalText Color : ${originalTextColor[key]}");
        }else{
            console.log("No OriginalText Color Preference Found - Setting YELLOW");
            ChromeStorageSync.set(key,"#fff000")
        }
    }

    private suspend fun checkUpDownMode(){
        val key = "button_up_down_mode"
        val upDownMode = ChromeStorageSync.get(key)
        if(upDownMode[key] != null){
            console.log("Preferences: up_down_mode: ${upDownMode[key]}")
        }else{
            console.log("No up_down_mode Preference Found - Setting 1");
            ChromeStorageSync.set(key,1)
        }
    }

    suspend fun init() {
        // Initialize the methods
        checkInstalled()
        checkFontMultiplier()
        checkTextColor()
        checkOpacity()
        enableDisable()
        onOffButton()
        checkOriginalTextOpacity()
        checkOriginalTextColor()
        checkUpDownMode()

        // Add the chrome.runtime.onMessage listener
            onMessage.addListener { request, _, _ ->
            when (request.message) {
                "open_popup" -> {
                    val createProperties = CreateProperties {
                        url = getURL("tutorial.html")
                    }
                    create(createProperties)
                }
                "update_on_off" -> {
                    console.log("Background.kt received message from SLIDER to update on_off to ${request.value}")
                    ChromeStorageSync.set("on_off", request.value as Any)
                }
                "update_button_on_off" -> {
                    console.log("Background.kt received message from SLIDER to update button_on_off to ${request.value}")
                    ChromeStorageSync.set("button_on_off", request.value as Any)
                }
                "update_button_up_down_mode" -> {
                    console.log("Background.kt received a message from SLIDER to update up_down_mode to ${request.value}")
                    ChromeStorageSync.set("button_up_down_mode", request.value as Any)
                }
                "update_font_multiplier" -> {
                    console.log("Background.kt received message from SLIDER to update font multiplier to ${request.value}")
                    ChromeStorageSync.set("font_multiplier", request.value as Any)
                }
                "update_text_color" -> {
                    console.log("Background.kt received a message from COLORSELECTOR to update TEXT_COLOR to ${request.value}")
                    ChromeStorageSync.set("text_color", request.value as Any)
                }
                "update_opacity" -> {
                    console.log("BACKGROUND.kt received a message from SIDESELECTOR to update TEXT_SIDE to ${request.value}")
                    ChromeStorageSync.set("opacity", request.value as Any)
                }
                "update_originaltext_opacity" -> {
                    console.log("BACKGROUND.kt received a message from SIDESELECTOR to update ORIGINALTEXT_OPACITY to ${request.value}")
                    ChromeStorageSync.set("originaltext_opacity", request.value as Any)
                }
                "update_originaltext_color" -> {
                    console.log("Background.kt received a message from COLORSELECTOR to update ORIGINALTEXT_COLOR to ${request.value}")
                    ChromeStorageSync.set("originaltext_color", request.value as Any)
                }
            }
        }
    }




}

suspend fun main() = run {
    val backGround = Background()
    backGround.init()
}
