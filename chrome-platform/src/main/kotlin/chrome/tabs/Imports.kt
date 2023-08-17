@file:JsQualifier("chrome.tabs")

package chrome.tabs

import chrome.events.Event
import kotlin.js.Promise

external fun query(queryInfo: QueryInfo): Promise<Array<Tab>>

external fun create(createProperties: CreateProperties): Promise<Tab>

external fun query(queryInfo: QueryInfo, callback: (result: Array<Tab>) -> Unit)

external var onUpdated: TabUpdatedEvent

external interface TabUpdatedEvent : Event<(tabId: Number, changeInfo: TabChangeInfo, tab: Tab) -> Unit>

external fun <M, R> sendMessage(tabId: Number, message: M): Promise<R>

external fun <M, R> sendMessage(tabId: Number, message: M, options: MessageSendOptions): Promise<R>

external interface TabChangeInfo {
  var status: String?
    get() = definedExternally
    set(value) = definedExternally
  var pinned: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var url: String?
    get() = definedExternally
    set(value) = definedExternally
  var audible: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var discarded: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var autoDiscardable: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var groupId: Number?
    get() = definedExternally
    set(value) = definedExternally
  var mutedInfo: MutedInfo?
    get() = definedExternally
    set(value) = definedExternally
  var favIconUrl: String?
    get() = definedExternally
    set(value) = definedExternally
  var title: String?
    get() = definedExternally
    set(value) = definedExternally
}


external interface QueryInfo {

    /**
     * Optional. Whether the tabs have completed loading.
     * One of: "loading", or "complete"
     */
    var status: String? /* "loading" | "complete" */
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. Whether the tabs are in the last focused window.
     * @since Chrome 19.
     */
    var lastFocusedWindow: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. The ID of the parent window, or windows.WINDOW_ID_CURRENT for the current window. */
    var windowId: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. The type of window the tabs are in.
     * One of: "normal", "popup", "panel", "app", or "devtools"
     */
    var windowType: String? /* "normal" | "popup" | "panel" | "app" | "devtools" */
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. Whether the tabs are active in their windows. */
    var active: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. The position of the tabs within their windows.
     * @since Chrome 18.
     */
    var index: Number?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. Match page titles against a pattern. */
    var title: String?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. Match tabs against one or more URL patterns. Note that fragment identifiers are not matched. */
    var url: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. Whether the tabs are in the current window.
     * @since Chrome 19.
     */
    var currentWindow: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. Whether the tabs are highlighted. */
    var highlighted: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * Whether the tabs are discarded. A discarded tab is one whose content has been unloaded from memory, but is still visible in the tab strip. Its content gets reloaded the next time it's activated.
     * @since Chrome 54.
     */
    var discarded: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * Whether the tabs can be discarded automatically by the browser when resources are low.
     * @since Chrome 54.
     */
    var autoDiscardable: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. Whether the tabs are pinned. */
    var pinned: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. Whether the tabs are audible.
     * @since Chrome 45.
     */
    var audible: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. Whether the tabs are muted.
     * @since Chrome 45.
     */
    var muted: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. The ID of the group that the tabs are in, or chrome.tabGroups.TAB_GROUP_ID_NONE for ungrouped tabs.
     * @since Chrome 88
     */
    var groupId: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Tab {
    /** Optional. Either loading or complete. */
    var status: String?
        get() = definedExternally
        set(value) = definedExternally

    /** The zero-based index of the tab within its window. */
    var index: Number

    /**
     * Optional.
     * The ID of the tab that opened this tab, if any. This property is only present if the opener tab still exists.
     * @since Chrome 18.
     */
    var openerTabId: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * The title of the tab. This property is only present if the extension's manifest includes the "tabs" permission.
     */
    var title: String?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * The URL the tab is displaying. This property is only present if the extension's manifest includes the "tabs" permission.
     */
    var url: String?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The URL the tab is navigating to, before it has committed.
     * This property is only present if the extension's manifest includes the "tabs" permission and there is a pending navigation.
     * @since Chrome 79.
     */
    var pendingUrl: String?
        get() = definedExternally
        set(value) = definedExternally

    /** Whether the tab is pinned. @since Chrome 9. */
    var pinned: Boolean

    /** Whether the tab is highlighted. @since Chrome 16. */
    var highlighted: Boolean

    /** The ID of the window the tab is contained within. */
    var windowId: Number

    /** Whether the tab is active in its window. (Does not necessarily mean the window is focused.) @since Chrome 16. */
    var active: Boolean

    /**
     * Optional.
     * The URL of the tab's favicon. This property is only present if the extension's manifest includes the "tabs" permission.
     * It may also be an empty string if the tab is loading.
     */
    var favIconUrl: String?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * The ID of the tab. Tab IDs are unique within a browser session. Under some circumstances a Tab may not be assigned an ID,
     * for example when querying foreign tabs using the sessions API, in which case a session ID may be present.
     * Tab ID can also be set to chrome.tabs.TAB_ID_NONE for apps and devtools windows.
     */
    var id: Number?
        get() = definedExternally
        set(value) = definedExternally

    /** Whether the tab is in an incognito window. */
    var incognito: Boolean

    /** Whether the tab is selected. @deprecated since Chrome 33. Please use tabs.Tab.highlighted. */
    var selected: Boolean

    /**
     * Optional.
     * Whether the tab has produced sound over the past couple of seconds (but it might not be heard if also muted).
     * Equivalent to whether the speaker audio indicator is showing.
     * @since Chrome 45.
     */
    var audible: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether the tab is discarded. A discarded tab is one whose content has been unloaded from memory,
     * but is still visible in the tab strip. Its content gets reloaded the next time it's activated.
     * @since Chrome 54.
     */
    var discarded: Boolean

    /** Whether the tab can be discarded automatically by the browser when resources are low. @since Chrome 54. */
    var autoDiscardable: Boolean

    /**
     * Optional.
     * Current tab muted state and the reason for the last state change.
     * @since Chrome 46. Warning: this is the current Beta channel.
     */
    var mutedInfo: MutedInfo?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. The width of the tab in pixels. @since Chrome 31. */
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. The height of the tab in pixels. @since Chrome 31. */
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. The session ID used to uniquely identify a Tab obtained from the sessions API.
     * @since Chrome 31.
     */
    var sessionId: String?
        get() = definedExternally
        set(value) = definedExternally

    /** The ID of the group that the tab belongs to. @since Chrome 88 */
    var groupId: Number
}

external interface MutedInfo {
    /**
     * Whether the tab is prevented from playing sound
     * (but hasn't necessarily recently produced sound).
     * Equivalent to whether the muted audio indicator is showing.
     */
    var muted: Boolean

    /**
     * Optional. The reason the tab was muted or unmuted.
     * Not set if the tab's mute state has never been changed.
     * “user”: A user input action has set/overridden the muted state.
     * “capture”: Tab capture started, forcing a muted state change.
     * “extension”: An extension, identified by the extensionId field,
     * set the muted state.
     */
    var reason: String?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. The ID of the extension that changed the muted state.
     * Not set if an extension was not the reason the muted state last changed.
     */
    var extensionId: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CreateProperties {

    /**
     * Optional. The position the tab should take in the window. The provided value will be clamped to between zero and the number of tabs in the window.
     */
    var index: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * The ID of the tab that opened this tab. If specified, the opener tab must be in the same window as the newly created tab.
     * @since Chrome 18.
     */
    var openerTabId: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * The URL to navigate the tab to initially. Fully-qualified URLs must include a scheme (i.e. 'http://www.google.com', not 'www.google.com'). Relative URLs will be relative to the current page within the extension. Defaults to the New Tab Page.
     */
    var url: String?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. Whether the tab should be pinned. Defaults to false
     * @since Chrome 9.
     */
    var pinned: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /** Optional. The window to create the new tab in. Defaults to the current window. */
    var windowId: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional.
     * Whether the tab should become the active tab in the window. Does not affect whether the window is focused (see windows.update). Defaults to true.
     * @since Chrome 16.
     */
    var active: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. Whether the tab should become the selected tab in the window. Defaults to true
     * @deprecated since Chrome 33. Please use active.
     */
    var selected: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MessageSendOptions {
    /**
     * Optional. Send a message to a specific frame identified
     * by frameId instead of all frames in the tab.
     */
    var frameId: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Optional. Send a message to a specific document identified by documentId instead of all frames in the tab.
     * Since:
     * Chrome 106.
     */
    var documentId: String?
        get() = definedExternally
        set(value) = definedExternally
}


