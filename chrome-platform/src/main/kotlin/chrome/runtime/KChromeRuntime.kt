package chrome.runtime

import kotlin.js.Promise

interface KChromeRuntime {
  fun sendMessage(message : String, value :Boolean)
  fun sendMessage(message : String, value :String)
}
