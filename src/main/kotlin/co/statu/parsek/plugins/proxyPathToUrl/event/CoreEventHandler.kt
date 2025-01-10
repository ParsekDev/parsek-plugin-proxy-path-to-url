package co.statu.parsek.plugins.proxyPathToUrl.event

import co.statu.parsek.api.annotation.EventListener
import co.statu.parsek.api.config.PluginConfigManager
import co.statu.parsek.api.event.CoreEventListener
import co.statu.parsek.config.ConfigManager
import co.statu.parsek.plugins.proxyPathToUrl.ProxyPathToUrlConfig
import co.statu.parsek.plugins.proxyPathToUrl.ProxyPathToUrlPlugin
import org.slf4j.Logger

@EventListener
class CoreEventHandler(
    private val logger: Logger,
    private val proxyPathToUrlPlugin: ProxyPathToUrlPlugin
) : CoreEventListener {
    override suspend fun onConfigManagerReady(configManager: ConfigManager) {
        val pluginConfigManager = PluginConfigManager(
            proxyPathToUrlPlugin,
            ProxyPathToUrlConfig::class.java,
        )

        proxyPathToUrlPlugin.pluginBeanContext.beanFactory.registerSingleton(
            pluginConfigManager.javaClass.name,
            pluginConfigManager
        )

        logger.info("Initialized plugin config")
    }
}