package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
import javax.validation.constraints.NotEmpty

/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppUserInfoForm {

    @Label("真实姓名")
    @NotEmpty
    var realName: String? = null

    @Label("性别")
    var isMale: Boolean? = null

    @Label("详细地址")
    @NotEmpty
    var addressDetail: String? = null

    var latitude: Double? = null

    var longitude: Double? = null

    @Label("手机")
    @NotEmpty
    var mobile: String? = null

    @Label("微信小程序唯一编号")
    var wechatUid: String? = null

    @Label("居民身份证号")
    @NotEmpty
    var prcRid: String? = null

    @NotEmpty
    var childRealName: String? = null

    @NotEmpty
    var childPrcRid: String? = null

    var childIsMale: Boolean? = null

    var childMobile: String? = null
}
