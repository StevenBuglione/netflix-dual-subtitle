import chrome.runtime.ChromeRuntime
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class ChromeRuntimeImpl : ChromeRuntime {
  override suspend fun sendMessage(type: String, value: Any): dynamic {
    val messageObject = js("{}")
    messageObject.message = type
    messageObject.value = value

    return suspendCoroutine<dynamic> { continuation ->
      chrome.runtime.sendMessage<dynamic, dynamic>(messageObject).then { response: dynamic ->
        continuation.resume(response)
      }.catch { error: Throwable ->
        console.error("Error sending message: $error")
        continuation.resumeWithException(error)
      }
    }
  }
}
