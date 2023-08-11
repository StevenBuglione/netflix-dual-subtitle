import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget

inline fun EventTarget.onEvent(type: String, noinline listener: (Event) -> Unit) =
    addEventListener(type, listener)

inline fun EventTarget.onEventAsync(scope: CoroutineScope, type: String, noinline listener: suspend (Event) -> Unit) =
    onEvent(type = type, listener = { event: Event -> scope.launch { listener(event) }})

inline fun EventTarget.onChangeEvent(noinline listener: (Event) -> Unit) =
    onEvent("change", listener)

inline fun EventTarget.onContentLoadedEventAsync(scope: CoroutineScope, noinline listener: suspend (Event) -> Unit) =
    onEventAsync(scope, "DOMContentLoaded", listener)
