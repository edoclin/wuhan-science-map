package wuhan.science.server.util.tencent.cdn

import cn.hutool.setting.Setting
import com.qcloud.cos.utils.Md5Utils
import com.qcloud.cos.utils.UrlEncoderUtils
import org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME


object CDNUtil {
    var url: String? = null
    var keyPrimary: String? = null
    var keySecond: String? = null
    var signKey: String? = null
    var timestampKey: String? = null

    init {
        val activeProfile = wuhan.science.server.WuHanScienceServerApplication.ac?.environment?.getProperty(ACTIVE_PROFILES_PROPERTY_NAME)
        val setting = Setting("./$activeProfile/tencent.cdn.config")
        // todo
//        StrUtil.equals(activeProfile, "prod").takeIf { it }?.let {
//            val key = PinHuiTripServerApplication.ac?.environment?.getProperty("mpw.key")
//            url = AES.decrypt(setting["url"], key)
//            keyPrimary = AES.decrypt(setting["key-primary"], key)
//            keySecond = AES.decrypt(setting["key-second"], key)
//            signKey = AES.decrypt(setting["sign-key"], key)
//            timestampKey = AES.decrypt(setting["timestamp-key"], key)
//        }?: let {
//
//        }

        url = setting["url"]
        keyPrimary = setting["key-primary"]
        keySecond = setting["key-second"]
        signKey = setting["sign-key"]
        timestampKey = setting["timestamp-key"]

    }

    fun generateUrl(filePath: String, duration: Long = 60 * 60 * 24): String {
        val encodeUrlPath = UrlEncoderUtils.encodeUrlPath(filePath.startsWith("/").takeIf { it } ?.let { filePath } ?: "/${filePath}")
        val ts = (System.currentTimeMillis() / 1000).plus(duration)
        val md5Hex = Md5Utils.md5Hex("${keyPrimary}${encodeUrlPath}${ts}")
        return  "$url$encodeUrlPath?$signKey=$md5Hex&$timestampKey=${ts}"
    }
}
