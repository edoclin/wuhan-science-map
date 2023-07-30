package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_user")
@WebView
class User : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("用户编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("真实姓名")
    @Condition
    var realName: String? = null

    @TableField("is_male")
    @View
    @Label("性别")
    @Condition
    @Sortable
    var male: Boolean? = null

    @View
    @Label("所属行政区域")
    @Condition
    @Ignored
    var regionCode: Int? = null

    @View
    @Label("详细地址")
    @Condition
    var addressDetail: String? = null

    var password: String? = null

    @View
    @Label("手机")
    @Condition
    var mobile: String? = null

    @View
    @Label("微信小程序唯一编号")
    @Ignored
    var wechatUid: String? = null

    /**
     * 居民身份证号
     */
    @View
    @Label("居民身份证号")
    @Condition
    var prcRid: String? = null

    @TableField("is_child")
    @View
    @Label("是否为孩子账号")
    var child: Boolean? = null

    /**
     * 监护人ID
     */
    @View
    @Label("监护人账号")
    @Ignored
    var guardianId: String? = null

    @View
    @Label("用户角色")
    var roleKey: String? = null

    @View
    @Label("注册时间")
    @TableField(fill = FieldFill.INSERT)
    @Sortable
    var createdTime: LocalDateTime? = null

    @View
    @Label("更新时间")
    @Sortable
    @TableField(fill = FieldFill.UPDATE)
    var updatedTime: LocalDateTime? = null

    @View
    @Label("当前状态")
    @Sortable
    var status: Status? = null

    @View
    @Label("操作者")
    var creator: String? = null

    @TableLogic
    @Ignored
    var deleted: Boolean? = null
}
