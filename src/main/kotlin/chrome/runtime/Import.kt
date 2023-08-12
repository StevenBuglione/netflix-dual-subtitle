@file:JsQualifier("chrome.runtime")
package chrome.runtime

import chrome.tabs.Tab
import chrome.events.Event

external var onMessage: ExtensionMessageEvent

external var onInstalled: RuntimeInstalledEvent

external fun getURL(path: String): String

external interface RuntimeInstalledEvent : Event<(details: InstalledDetails) -> Unit>

external interface ExtensionMessageEvent : Event<(message: dynamic, sender: MessageSender, sendResponse: (response: Any) -> Unit) -> Unit>


external interface MessageSender {
    var id: String?
        get() = definedExternally
        set(value) = definedExternally
    var tab: Tab?
        get() = definedExternally
        set(value) = definedExternally
    var nativeApplication: String?
        get() = definedExternally
        set(value) = definedExternally
    var frameId: Number?
        get() = definedExternally
        set(value) = definedExternally
    var url: String?
        get() = definedExternally
        set(value) = definedExternally
    var tlsChannelId: String?
        get() = definedExternally
        set(value) = definedExternally
    var origin: String?
        get() = definedExternally
        set(value) = definedExternally
    var documentLifecycle: String? /* "prerender" | "active" | "cached" | "pending_deletion" */
        get() = definedExternally
        set(value) = definedExternally
    var documentId: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InstalledDetails {
    var reason: OnInstalledReason
    var previousVersion: String?
        get() = definedExternally
        set(value) = definedExternally
    var id: String?
        get() = definedExternally
        set(value) = definedExternally
}

external enum class OnInstalledReason {
    INSTALL /* = 'install' */,
    UPDATE /* = 'update' */,
    CHROME_UPDATE /* = 'chrome_update' */,
    SHARED_MODULE_UPDATE /* = 'shared_module_update' */
}