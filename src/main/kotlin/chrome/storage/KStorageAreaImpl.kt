package chrome.storage

import kotlinx.coroutines.await
import kotlin.js.Promise

class KStorageAreaImpl(private val storageArea: StorageArea) : KStorageArea {

    override suspend fun get(keys: dynamic): dynamic {
        return storageArea.get(keys).await()
    }

    override fun set(keys: dynamic): Promise<Unit> {
        return storageArea.set(keys)
    }

    override fun set(key: String, value: Any): Promise<Unit> {
        val item = js("{}")
        item[key] = value
        return storageArea.set(item)
    }


}