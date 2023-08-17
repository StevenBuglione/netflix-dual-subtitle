package chrome.tabs

interface KChromeTabs {
  fun sendMessageToActiveTab(message: Any, value: Any)
}
