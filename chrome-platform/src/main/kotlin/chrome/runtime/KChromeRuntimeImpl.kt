package chrome.runtime

class KChromeRuntimeImpl : KChromeRuntime {


  override fun sendMessage(message: String, value: Boolean) {
    val messageObject = js("{}")
    messageObject.message = message
    messageObject.value = value
    sendMessage<String,Boolean>(messageObject)
  }

  override fun sendMessage(message: String, value: String) {
    val messageObject = js("{}")
    messageObject.message = message
    messageObject.value = value
    sendMessage<String,String>(messageObject)
  }

}
