package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 科普基地展厅
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_base_hall")
@WebView
class BaseHall : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("展厅编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("所属基地编号")
    @Ignored
    @Condition
    var baseId: String? = null

    @View
    @Label("展厅名称")
    @Condition
    var hallName: String? = null

    @View
    @Label("展厅Logo")
    var hallLogo: String? = null


    /**
     * 展厅简介
     */
    @View
    @Label("展厅简介")
    @Condition
    var descSimple: String? = null

    /**
     * 图片、语音、视频等信息
     */
    @View
    @Label("展厅详情")
    @Ignored
    @Condition
    var descRichText: String? = null

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
