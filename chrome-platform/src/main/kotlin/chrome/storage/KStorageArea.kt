package chrome.storage

import kotlin.js.Promise

interface KStorageArea {
    suspend fun get(keys: dynamic): dynamic
    fun set (keys: dynamic) : Promise<Unit>
    fun set (key: String, value: Any) : Promise<Unit>
}