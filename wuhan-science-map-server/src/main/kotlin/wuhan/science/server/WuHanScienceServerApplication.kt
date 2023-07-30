package wuhan.science.server

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext


@SpringBootApplication
@MapperScan(basePackages = ["wuhan.science.server.mapper"])
class WuHanScienceServerApplication {
    companion object {
        var ac: ConfigurableApplicationContext? = null
        @JvmStatic
        fun main(args: Array<String>) {
            ac = runApplication<WuHanScienceServerApplication>(*args)
        }
    }

}



