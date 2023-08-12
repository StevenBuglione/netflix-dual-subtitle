import chrome.runtime.*
import chrome.storage.ChromeStorageSync

// Preference class to manage each preference's default value and retrieval
class Preference<T:Any>(private val key: String, private val defaultValue: T) {
    suspend fun fetch(): T {
        val storedValue = ChromeStorageSync.get(key)
        @Suppress("UnsafeCastFromDynamic")
        return storedValue[key] ?: setDefaultAndReturn()
    }

    private fun setDefaultAndReturn(): T {
        console.log("No $key Preference Found - Setting to $defaultValue")
        ChromeStorageSync.set(key, defaultValue)
        return defaultValue
    }
}
