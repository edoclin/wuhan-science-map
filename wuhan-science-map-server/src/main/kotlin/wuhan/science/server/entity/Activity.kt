package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 活动通知
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_activity")
@WebView
class Activity : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("活动编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("活动名称")
    @Condition
    var activityName: String? = null

    @View
    @Label("活动详情")
    @Ignored
    var descRichText: String? = null

    @View
    @Label("封面图片")
    @Ignored
    var coverAccessKey: String? = null

    @View
    @Label("所属基地编号")
    @Ignored
    var baseId: String? = null

    @View
    @Label("置顶显示")
    @TableField("is_top")
    @Ignored
    var top: Boolean? = null

    @View
    @Label("轮播图显示")
    @TableField("is_carousel")
    @Ignored
    var carousel: Boolean? = null

    @View
    @Label("置顶显示排列顺序")
    @Ignored
    var displayIndex: Int? = null

    @View
    @Label("发布时间")
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
