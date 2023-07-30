package wuhan.science.server.config

import cn.dev33.satoken.exception.NotLoginException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.ResponseBody
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.slf4j.Slf4j

import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
@Slf4j
class GlobalException {
    @ModelAttribute
    @Throws(IOException::class)
    operator fun get(request: HttpServletRequest?) {
    }

    @Scheduled(cron = "0 0 4 1/1 * ? ")
    fun scheduled() {
    }

    @ResponseBody
    @ExceptionHandler
    fun handlerException(e: Exception, request: HttpServletRequest?, response: HttpServletResponse?): ResultBean {
        e.printStackTrace()
        return when (e) {
            is NotLoginException -> ResultUtil.notLogin()
            else -> ResultUtil.exception(e.message!!, 0)
        }
    }
}