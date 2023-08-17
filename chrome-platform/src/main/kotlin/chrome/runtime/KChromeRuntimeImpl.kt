package chrome.runtime


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.js.Promise

class KChromeRuntimeImpl : KChromeRuntime {


  override suspend fun sendMessage(message: String, value: Boolean){
    val messageObject = js("{}")
    messageObject.message = message
    messageObject.value = value

    sendMessage<String,Boolean>(messageObject).catch {
      GlobalScope.launch {
        delay(3000)
        sendMessage<String,Boolean>(messageObject)
      }
    }
  }

  override suspend fun sendMessage(message: String, value: String){
    val messageObject = js("{}")
    messageObject.message = message
    messageObject.value = value
    sendMessage<String,String>(messageObject).catch {
      GlobalScope.launch {
        delay(3000)
        sendMessage<String,String>(messageObject)
      }
    }
  }
}
