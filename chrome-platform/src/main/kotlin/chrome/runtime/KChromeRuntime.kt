package chrome.runtime

import kotlin.js.Promise

interface KChromeRuntime {
  suspend fun sendMessage(message : String, value :Boolean):Promise<Boolean>
  suspend fun sendMessage(message : String, value :String):Promise<String>
}
