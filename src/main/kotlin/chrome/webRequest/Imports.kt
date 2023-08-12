@file:JsQualifier("chrome.webRequest")

package chrome.webRequest
external interface RequestFilter {
    var tabId: Number?
        get() = definedExternally
        set(value) = definedExternally
    var types: Array<String? /* "main_frame" | "sub_frame" | "stylesheet" | "script" | "image" | "font" | "object" | "xmlhttprequest" | "ping" | "csp_report" | "media" | "websocket" | "other" */>?
        get() = definedExternally
        set(value) = definedExternally
    var urls: Array<String>
    var windowId: Number?
        get() = definedExternally
        set(value) = definedExternally
}