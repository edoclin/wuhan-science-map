package wuhan.science.server.util.code

import cn.hutool.cache.CacheUtil
import cn.hutool.cache.impl.TimedCache
import cn.hutool.core.util.RandomUtil
import wuhan.science.server.util.slf4j.Slf4j
import wuhan.science.server.util.slf4j.Slf4j.Companion.log
import java.lang.RuntimeException

@Slf4j
object CodeUtil {
    private val codeCache: TimedCache<String, String> = CacheUtil.newTimedCache(1000 * 60 * 5)
    private val timeCache: TimedCache<String, String> = CacheUtil.newTimedCache(1000 * 60)


    fun sendCode(mobile: String): String {
        timeCache[mobile]?.let {
            throw RuntimeException("1分钟内仅能获取一次验证码")
        } ?: let {
            val code = RandomUtil.randomInt(100000, 1000000).toString()
            timeCache.put(mobile, code)
            codeCache.put(mobile, code)
            log.info("验证码<$mobile, $code>")
            return code
        }
    }

    fun getCode(mobile: String): String {

        codeCache[mobile]?.let {
            codeCache.remove(mobile)
            return it
        } ?: let {
            throw RuntimeException("无效的手机号")
        }
    }
}