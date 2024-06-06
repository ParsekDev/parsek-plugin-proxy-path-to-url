package co.statu.parsek.plugins.proxyPathToUrl.event

import co.statu.parsek.api.annotation.EventListener
import co.statu.parsek.api.config.PluginConfigManager
import co.statu.parsek.api.event.RouterEventListener
import co.statu.parsek.plugins.proxyPathToUrl.ProxyPathToUrlConfig
import co.statu.parsek.plugins.proxyPathToUrl.ProxyPathToUrlPlugin
import io.vertx.core.http.HttpClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.proxy.handler.ProxyHandler
import io.vertx.httpproxy.HttpProxy
import io.vertx.httpproxy.ProxyOptions

@EventListener
class RouterEventHandler(
    private val proxyPathToUrlPlugin: ProxyPathToUrlPlugin
): RouterEventListener {
    private val pluginConfigManager by lazy {
        proxyPathToUrlPlugin.pluginBeanContext.getBean(PluginConfigManager::class.java) as PluginConfigManager<ProxyPathToUrlConfig>
    }

    private val httpClient by lazy {
        proxyPathToUrlPlugin.pluginBeanContext.getBean(HttpClient::class.java)
    }

    override fun onBeforeCreateRoutes(router: Router) {
        val config = pluginConfigManager.config

        config.proxyConfigs.forEach { proxyConfig ->
            val proxyOptions = ProxyOptions()
            proxyOptions.setSupportWebSocket(proxyConfig.supportWebSocket ?: false)

            val reverseProxy = HttpProxy.reverseProxy(proxyOptions, httpClient)

            reverseProxy.origin(proxyConfig.port, proxyConfig.host)

            val proxyHandler = ProxyHandler.create(reverseProxy)

            val route = router.route(proxyConfig.path)

            route.order(proxyConfig.order ?: 2)

            proxyConfig.metadata?.forEach { (key, value) ->
                route.putMetadata(key, value)
            }

            route.handler(proxyHandler)
        }
    }
}