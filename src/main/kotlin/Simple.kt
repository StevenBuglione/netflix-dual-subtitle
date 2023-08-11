import chrome.storage.ChromeStorageSync
import chrome.tabs.QueryInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.*
import kotlinx.coroutines.async
import kotlinx.coroutines.await
import org.w3c.dom.HTMLSelectElement
import kotlinx.browser.document

class BackgroundChanger {

    /**
     * Get the current URL.
     */
    private suspend fun getCurrentTabUrlAsync(): String {
        val queryInfo = QueryInfo {
            active = true
            currentWindow = true
        }
        val tabs = GlobalScope.async { chrome.tabs.query(queryInfo).await() }.await()
        val tab = tabs[0]
        console.log(tab.url)
        return tab.url ?: throw RuntimeException("tab.url should be a string")
    }
    /**
     * Gets the saved background color for url.
     *
     * @param url URL whose background color is to be retrieved.
     */
    private suspend fun getSavedBackgroundColorAsync(url: String): String? {
        val item = ChromeStorageSync.get(url)
        console.log("hello")
        return item[url] as? String
    }

    /**
     * Sets the given background color for url.
     *
     * @param url URL for which background color is to be saved.
     * @param color The background color to be saved.
     */
    private fun saveBackgroundColor(url: String, color: String) {
        val items = js("{}")
        items[url] = color
        ChromeStorageSync.set(items)
        console.log("hello 1")
    }

    /**
     * The main function for the extension to change background color.
     */
    fun init() {
        document.addEventListener("DOMContentLoaded", {
            GlobalScope.launch {
                val url = getCurrentTabUrlAsync()
                val dropdown = document.getElementById("dropdown") as HTMLSelectElement
                val savedColor = getSavedBackgroundColorAsync(url)
                if (savedColor != null) {
                    dropdown.value = savedColor
                }
                dropdown.onChangeEvent {
                    saveBackgroundColor(url, dropdown.value)
                }

            }
        })
    }
}

fun main() = run {
    val changer = BackgroundChanger()
    changer.init()
}
