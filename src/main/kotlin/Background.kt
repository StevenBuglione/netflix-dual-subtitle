import chrome.runtime.OnInstalledReason
import chrome.runtime.onInstalled
import chrome.tabs.CreateProperties
import chrome.tabs.create
import installation.InstallationHandler
import listeners.MessageListener
import preferences.PreferenceService

class Background {

    private val preferenceService = PreferenceService()
    private val messageListener = MessageListener()
    private val installationHandler = InstallationHandler()

    suspend fun init() {
        installationHandler.checkInstalled()
        preferenceService.checkPreferences()
        messageListener.setupMessageListeners()
    }
}