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
}