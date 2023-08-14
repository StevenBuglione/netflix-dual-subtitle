import chrome.tabs.CreateProperties
import kotlinx.browser.window
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.events.EventListener
import preferences.Preferences
import preferences.PreferencesManger
import kotlin.js.Promise

suspend fun main() {
  waitForDOMContentLoaded()
  setupGlobalClickHandler()
  PreferencesManger().init()
}

private fun waitForDOMContentLoaded(): Promise<Unit> = GlobalScope.promise {
  window.document.addEventListener("DOMContentLoaded", EventListener {
    if (window.location.pathname == "/update.html") {
      throw CancellationException("Pathname is /update.html")
    }
  })
}


private fun setupGlobalClickHandler() {
  window.addEventListener("click", EventListener { event ->
    val target = event.target
    if (target is HTMLAnchorElement && target.href.isNotEmpty()) {
      chrome.tabs.create(CreateProperties { url = target.href })
    }
  })
}




