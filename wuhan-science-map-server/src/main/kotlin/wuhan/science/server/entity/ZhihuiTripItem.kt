package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 智慧行item
 * </p>
 *
 * @author edoclin
 * @since 2023-03-29
 */
@TableName("t_zhihui_trip_item")
@WebView
class ZhihuiTripItem : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("子路线编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("所属智慧行编号")
    @Condition
    var tripId: String? = null

    @View
    @Label("路线名称")
    @Condition
    var itemName: String? = null

    @View
    @Label("关联课程编号")
    @Ignored
    @Condition
    var courseId: String? = null

    @View
    @Label("访问建")
    @Ignored
    @Condition
    var accessKey: String? = null

    @View
    @Label("显示顺序")
    @Ignored
    var displayIndex: Int? = null

    @View
    @Label("简短描述")
    @Ignored
    @Condition
    var descSimple: String? = null

    @View
    @Label("路线详情")
    @Ignored
    @Condition
    var descRichText: String? = null

    @View
    @Label("发布时间")
    @Sortable
    @TableField(fill = FieldFill.INSERT)
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
