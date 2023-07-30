package wuhan.science.server.util.system

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix="custom")
class Configuration {
    lateinit var roles: List<String>
    lateinit var baseTypes: List<String>
    lateinit var newsTypes: List<String>
}