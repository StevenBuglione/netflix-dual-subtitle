package chrome.tabs

external interface TabMessage<T> {
  var updateSetting: String
  var value: T
}
