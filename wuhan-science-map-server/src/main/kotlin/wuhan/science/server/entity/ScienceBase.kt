package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import pinhui.trip.server.entity.GeometryTypeHandler
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 科普基地
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_science_base", autoResultMap = true)
@WebView
class ScienceBase : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("科普基地编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("基地名称")
    @Condition
    var baseName: String? = null

    @View
    @Label("基地Logo")
    @Ignored
    @Condition
    var baseLogo: String? = null

    @View
    @Label("基地类型")
    @Condition
    @Sortable
    var baseType: String? = null

    /**
     * 单位简介
     */
    @View
    @Label("基地简介")
    @Condition
    var descRichText: String? = null

    /**
     * 参观指南
     */
    @View
    @Label("参观指南")
    var visitTutorial: String? = null

    @View
    @Label("行政区域")
    @Ignored
    @TableField(typeHandler = GeometryTypeHandler::class)
    var polygonGeometry: String? = null

    @View
    @Label("行政区域编号")
    @Ignored
    var regionCode: Int? = null

    @View
    @Label("详细地址")
    var detailAddress: String? = null

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

    var carouselImageAccessKey: String? = null

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
    var deleted: Boolean? = null
}
