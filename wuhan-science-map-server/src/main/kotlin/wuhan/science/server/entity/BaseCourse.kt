package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 科普基地课程
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_base_course")
@WebView
class  BaseCourse : Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("课程编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("所属基地编号")
    @Ignored
    @Condition
    var baseId: String? = null

    @View
    @Label("课程名称")
    @Condition
    var courseName: String? = null

    @View
    @Label("所属区域")
    @Condition
    @Ignored
    var regionCode: Int? = null

    @View
    @Label("详细地址")
    @Condition
    var detailAddress: String? = null

    @View
    @Label("课程详情")
    @Ignored
    var descRichText: String? = null

    @View
    @Label("开课时间")
    @Sortable
    var startingTime: LocalDateTime? = null

    @View
    @Label("适宜年龄")
    @Condition
    var targetAge: Int? = null

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

    @TableField("is_wonderful")
    @View
    @Label("是否设为精彩课程")
    @Sortable
    @Condition
    var wonderful: Boolean? = null

    var wonderfulImageAccessKey: String? = null

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
    @Condition
    @Sortable
    var status: Status? = null

    @View
    @Label("操作者")
    var creator: String? = null

    @TableLogic
    @Ignored
    var deleted: Boolean? = null
}
