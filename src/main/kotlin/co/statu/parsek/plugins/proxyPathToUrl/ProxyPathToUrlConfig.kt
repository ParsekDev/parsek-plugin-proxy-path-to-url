package co.statu.parsek.plugins.proxyPathToUrl

import co.statu.parsek.api.config.PluginConfig

class ProxyPathToUrlConfig(
    val proxyConfigs: List<ProxyConfig> = listOf()
) : PluginConfig() {
    companion object {
        data class ProxyConfig(
            val path: String,
            val supportWebSocket: Boolean? = null,
            val host: String,
            val port: Int,
            val order: Int? = null,
            val metadata: Map<String, Any?>? = null
        )
    }
}