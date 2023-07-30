package wuhan.science.server.util.tencent.cos

import cn.hutool.core.date.DateUtil
import cn.hutool.setting.Setting
import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicSessionCredentials
import com.qcloud.cos.auth.COSCredentials
import com.qcloud.cos.http.HttpMethodName
import com.qcloud.cos.http.HttpProtocol
import com.qcloud.cos.region.Region
import com.tencent.cloud.CosStsClient
import com.tencent.cloud.Response
import org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import java.net.URL
import java.util.*


object COSUtil {
    private var region: String? = null
    private var bucket: String? = null
    private var secretId: String? = null
    private var secretKey: String? = null

    init {
        val activeProfile = wuhan.science.server.WuHanScienceServerApplication.ac?.environment?.getProperty(ACTIVE_PROFILES_PROPERTY_NAME)
        val setting = Setting("./$activeProfile/tencent.cos.config")
        // todo
//        StrUtil.equals(activeProfile, "prod").takeIf { it }?.let {
//            val key = PinHuiTripServerApplication.ac?.environment?.getProperty("mpw.key")
//            region = AES.decrypt(setting["region"], key)
//            bucket = AES.decrypt(setting["bucket"], key)
//            secretId = AES.decrypt(setting["secret-id"], key)
//            secretKey = AES.decrypt(setting["secret-key"], key)
//        }?: let {
//
//        }

        region = setting["region"]
        bucket = setting["bucket"]
        secretId = setting["secret-id"]
        secretKey = setting["secret-key"]
    }

    fun temporaryToken(
        durationSeconds: Number = 1800,
        allowPrefixes: Array<String> = arrayOf("*"),
        allowActions: Array<String> = arrayOf("*")
    ): COSTemporaryToken? {
        val config = TreeMap<String, Any?>()
        try {
            config["secretId"] = secretId
            config["secretKey"] = secretKey
            // 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
            config["durationSeconds"] = durationSeconds.toInt()
            config["bucket"] = bucket
            config["region"] = region

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
            // 列举几种典型的前缀授权场景：
            // 1、允许访问所有对象："*"
            // 2、允许访问指定的对象："a/a1.txt", "b/b1.txt"
            // 3、允许访问指定前缀的对象："a*", "a/*", "b/*"
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config["allowPrefixes"] = allowPrefixes

            // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
            // 简单上传、表单上传和分块上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            config["allowActions"] = allowActions
            val startTime: Long = DateUtil.currentSeconds()
            val expiredTime: Long = startTime + durationSeconds.toLong()
            val response: Response = CosStsClient.getCredential(config)
            return COSTemporaryToken(
                response.credentials.tmpSecretId,
                response.credentials.tmpSecretKey,
                response.credentials.sessionToken,
                startTime,
                expiredTime
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun COSConfig(): ResultBean {
        return ResultUtil.ok(COSConfig(region, bucket))
    }

    // 创建 COSClient 实例，这个实例用来后续调用请求
    fun createCOSClient(temporaryToken: COSTemporaryToken): COSClient {

        val tmpSecretId = temporaryToken.temporarySecretId
        val tmpSecretKey = temporaryToken.temporarySecretKey
        val sessionToken = temporaryToken.sessionToken
        val cred: COSCredentials = BasicSessionCredentials(tmpSecretId, tmpSecretKey, sessionToken)

        // ClientConfig 中包含了后续请求 COS 的客户端设置：
        val clientConfig = ClientConfig()

        // 设置 bucket 的地域
        // COS_REGION 请参照 https://cloud.tencent.com/document/product/436/6224
        clientConfig.region = Region(region)

        // 设置请求协议, http 或者 https
        // 5.6.53 及更低的版本，建议设置使用 https 协议
        // 5.6.54 及更高版本，默认使用了 https
        clientConfig.httpProtocol = HttpProtocol.https

        // 以下的设置，是可选的：

        // 设置 socket 读取超时，默认 30s
        clientConfig.socketTimeout = 30 * 1000
        // 设置建立连接超时，默认 30s
        clientConfig.connectionTimeout = 30 * 1000

        // 如果需要的话，设置 http 代理，ip 以及 port
        clientConfig.httpProxyIp = "httpProxyIp"
        clientConfig.httpProxyPort = 80

        // 生成 cos 客户端。
        return COSClient(cred, clientConfig)
    }

    fun generatePresignedUrl(
        cosClient: COSClient,
        key: String,
        duration: Number,
        method: HttpMethodName = HttpMethodName.GET,
        headers: Map<String, String> = mapOf(),
        params: Map<String, String> = mapOf(),
    ): URL {
        val expirationDate = Date(System.currentTimeMillis() + duration.toLong() * 1000)
        val url = cosClient.generatePresignedUrl(bucket, key, expirationDate, method, headers, params)
        cosClient.shutdown()
        return url
    }
}