package wuhan.science.server.controller


import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.tencent.cos.AllowActionsBuilder
import wuhan.science.server.util.tencent.cos.COSUtil

/**
 * <p>
 * 基地信息表 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2022-09-21
 */
@RestController
@RequestMapping("/server/tencentCos")
class TencentController {

    @RequestMapping(method = [RequestMethod.GET], path = ["/access"])
    fun getAccessUrl(
        @RequestParam(required = true) duration: Number, @RequestParam(required = true) key: String
    ): ResultBean {
        COSUtil.temporaryToken(duration, arrayOf(key), AllowActionsBuilder.builder().forGetObject().build())?.let {
            return ResultUtil.ok(COSUtil.generatePresignedUrl(COSUtil.createCOSClient(it), key, duration))
        } ?: let {
            return ResultUtil.error("服务器繁忙", key)
        }
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/temporaryToken"])
    fun getTemporaryToken(
        @RequestParam(required = true) duration: Number, @RequestParam(required = true) prefix: String
    ): ResultBean {
        COSUtil.temporaryToken(duration, arrayOf(prefix))?.let {
            return ResultUtil.ok(it)
        } ?: let {
            return ResultUtil.error("服务器繁忙", prefix)
        }
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/config"])
    fun getCOSConfig(): ResultBean {
        return COSUtil.COSConfig()
    }

}
