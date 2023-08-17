package chrome.tabs



class KChromeTabsImpl : KChromeTabs {

  override fun sendMessageToActiveTab(message: Any, value: Any){
    val queryInfo = QueryInfo {
      active = true
      currentWindow = true
    }

    query(queryInfo) { tabs ->
      val activeTab = tabs[0].id
      val messageObject = js("{}")
      messageObject.message = message
      messageObject.value = value

      if (activeTab != null) {
        console.log("Sending message to tab $activeTab: ${JSON.stringify(messageObject)}")
        sendMessage<Any,Unit>(activeTab,messageObject)
      }
    }
  }
}
