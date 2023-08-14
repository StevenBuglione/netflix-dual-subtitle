package chrome.runtime

import chrome.tabs.Tab
import chrome.tabs.TabChangeInfo
import kotlin.js.Promise

interface KChromeRuntime {

  suspend fun sendMessage(message : String, value :Boolean)
  suspend fun sendMessage(message : String, value :String)
}
