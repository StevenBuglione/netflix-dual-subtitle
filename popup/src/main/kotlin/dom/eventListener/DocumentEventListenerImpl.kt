package dom.eventListener

import kotlinx.coroutines.*
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.events.Event

class DOMEventListenerImpl : DOMEventListener {
  override suspend fun waitForEvent(eventName: String, elementId: String?): Unit {
    return suspendCancellableCoroutine { continuation ->
      val target: Element = if (elementId != null) {
        document.getElementById(elementId) ?: throw IllegalArgumentException("Element with ID $elementId not found")
      } else {
        document
      }

      val listener = { _: Event ->
        continuation.resume(Unit)
      }

      target.addEventListener(eventName, listener)

      continuation.invokeOnCancellation {
        target.removeEventListener(eventName, listener)
      }
    }
  }
}
