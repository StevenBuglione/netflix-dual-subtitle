package chrome.runtime

import kotlin.js.Promise

class KChromeRuntimeImpl : KChromeRuntime {


  override suspend fun sendMessage(message: String, value: Boolean): Promise<Boolean> {
    val messageObject = js("{}")
    messageObject.message = message
    messageObject.value = value
    return sendMessage<String,Boolean>(messageObject)
  }

  override suspend fun sendMessage(message: String, value: String): Promise<String> {
    val messageObject = js("{}")
    messageObject.message = message
    messageObject.value = value
    return sendMessage<String,String>(messageObject)
  }

}
