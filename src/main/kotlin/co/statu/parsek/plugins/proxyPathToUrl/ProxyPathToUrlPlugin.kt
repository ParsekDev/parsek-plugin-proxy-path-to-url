package co.statu.parsek.plugins.proxyPathToUrl

import co.statu.parsek.api.ParsekPlugin

class ProxyPathToUrlPlugin : ParsekPlugin() {
    override suspend fun onLoad() {
        val httpClient = vertx.createHttpClient()

        pluginBeanContext.beanFactory.registerSingleton(httpClient.javaClass.name, httpClient)
    }

}

