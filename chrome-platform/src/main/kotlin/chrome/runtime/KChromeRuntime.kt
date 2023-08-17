package chrome.runtime

interface KChromeRuntime {

  suspend fun sendMessage(message : String, value :Boolean)
  suspend fun sendMessage(message : String, value :String)
}
