package installation

import chrome.runtime.OnInstalledReason
import chrome.runtime.onInstalled
import chrome.tabs.CreateProperties
import chrome.tabs.create

class InstallationHandler {

    fun checkInstalled() {
        onInstalled.addListener { details ->
            if (details.reason == OnInstalledReason.INSTALL) {
                val createProperties = CreateProperties {
                    url = "tutorial.html"
                }
                create(createProperties)
            }
        }
    }
}