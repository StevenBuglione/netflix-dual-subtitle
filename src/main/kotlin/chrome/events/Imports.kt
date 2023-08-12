@file:JsQualifier("chrome.events")

package chrome.events

import chrome.webRequest.RequestFilter

external interface BaseEvent<T : Function<*>> {
    fun addListener(callback: T, filter: RequestFilter = definedExternally)
    fun getRules(callback: (rules: Array<Rule>) -> Unit)
    fun getRules(ruleIdentifiers: Array<String>, callback: (rules: Array<Rule>) -> Unit)
    fun hasListener(callback: T): Boolean
    fun removeRules(ruleIdentifiers: Array<String> = definedExternally, callback: () -> Unit = definedExternally)
    fun removeRules()
    fun removeRules(ruleIdentifiers: Array<String> = definedExternally)
    fun removeRules(callback: () -> Unit = definedExternally)
    fun addRules(rules: Array<Rule>, callback: (rules: Array<Rule>) -> Unit = definedExternally)
    fun removeListener(callback: T)
    fun hasListeners(): Boolean
}

external interface Event<T : Function<*>> : BaseEvent<T> {
    fun addListener(callback: T)
}

external interface Rule {
    var priority: Number?
        get() = definedExternally
        set(value) = definedExternally
    var conditions: Array<Any>
    var id: String?
        get() = definedExternally
        set(value) = definedExternally
    var actions: Array<Any>
    var tags: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
}