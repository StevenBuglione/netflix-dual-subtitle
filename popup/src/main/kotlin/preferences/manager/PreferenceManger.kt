package preferences.manager

import preferences.listeners.EventListenerManager
import preferences.updaters.PreferenceUpdater
import preferences.wrappers.ElementWrapper

class PreferencesManager {

  private val elements = ElementWrapper()
  private val updater = PreferenceUpdater(elements)
  private val eventListenerManager = EventListenerManager(elements)

  suspend fun init() {
    updater.init()
    eventListenerManager.setupEventListeners()
  }
}

