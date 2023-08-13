import chrome.tabs.CreateProperties
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLAnchorElement
import preferences.Preferences
import kotlin.js.Promise

suspend fun main() {

  waitForDOMContentLoaded()
  window.addEventListener("click", { event ->
    val target = event.target
    if (target is HTMLAnchorElement && target.href.isNotEmpty()) {
      val createProperties = CreateProperties {
        url = target.href
      }
      chrome.tabs.create(createProperties)
    }
  })

  val preferences = Preferences()
  preferences.init()
}

fun waitForDOMContentLoaded(): Promise<Unit> = GlobalScope.promise {
  document.addEventListener("DOMContentLoaded", {
    if (window.location.pathname == "/update.html") {
      // to stop popup js from running when update.html is opened and causing errors
      throw CancellationException("Pathname is /update.html")
    }
  })
}


