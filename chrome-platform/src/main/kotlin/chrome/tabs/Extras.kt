package chrome.tabs

@Suppress("UNCHECKED_CAST_TO_NATIVE_INTERFACE")
inline fun QueryInfo(block: QueryInfo.() -> Unit) = (js("{}") as QueryInfo).apply(block)

@Suppress("UNCHECKED_CAST_TO_NATIVE_INTERFACE")
inline fun CreateProperties(block: CreateProperties.() -> Unit) = (js("{}") as CreateProperties).apply(block)

@Suppress("UNCHECKED_CAST_TO_NATIVE_INTERFACE")
inline fun MessageSendOptions(block: MessageSendOptions.() -> Unit) = (js("{}") as MessageSendOptions).apply(block)

@Suppress("UNCHECKED_CAST_TO_NATIVE_INTERFACE")
inline fun <T> TabMessage(block: TabMessage<T>.() -> Unit) = (js("{}") as TabMessage<T>).apply(block)

