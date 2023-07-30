package wuhan.science.server.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.transaction.annotation.Transactional
import wuhan.science.server.util.execption.InitSystemException
import wuhan.science.server.util.slf4j.Slf4j

/**
 * SpringBoot启动后执行run()
 */
@Configuration
@EnableAsync
@Slf4j
class InitApplicationRunner : ApplicationRunner {


//    @Value("\${system.mobile}")
//    private val mobile: String? = null
//
//
//    @Value("\${system.password}")
//    private val password: String? = null

    @Async
    @Transactional(rollbackFor = [InitSystemException::class])
    override fun run(args: ApplicationArguments) {

    }
}