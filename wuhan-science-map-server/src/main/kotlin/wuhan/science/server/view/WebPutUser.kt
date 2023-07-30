package wuhan.science.server.entity

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPutUser {
    @Label("用户编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("真实姓名")
    @Condition
    var realName: String? = null

    @Label("性别")
    @Fixed
    @Condition
    var male: String? = null

    @Label("所属行政区域")
    @Condition
    var regionCode: String? = null

    @Label("详细地址")
    @Condition
    var addressDetail: String? = null

    @Label("手机")
    @Condition
    var mobile: String? = null

    @Label("微信小程序唯一编号")
    var wechatUid: String? = null

    @Label("居民身份证号")
    @Condition
    var prcRid: String? = null

    @Label("是否为孩子账号")
    @Fixed
    @Condition
    var child: String? = null

    @Label("监护人账号")
    @Condition
    @Ignored
    var guardianId: String? = null

    @Label("用户角色")
    @Condition
    var roleKey: String? = null

    @Label("注册时间")
    @Fixed
    var createdTime: String? = null

    @Label("更新时间")
    @Fixed
    var updatedTime: String? = null

    @Label("当前状态")
    @Fixed
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
