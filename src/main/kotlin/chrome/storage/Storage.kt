package chrome.storage


//Object Used To Interface With chrome.storage.sync javascript method
//ChromeStorageSync.get(keys: dynamic) -> is asynchronous and uses coroutines
//ChromeStorageSync.set(keys: dynamic) -> returns back promise only used for setting nothing really else
object ChromeStorageSync : KStorageArea by KStorageAreaImpl(sync)