package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 智慧行
 * </p>
 *
 * @author edoclin
 * @since 2023-03-29
 */
@TableName("t_zhihui_trip")
@WebView
class ZhihuiTrip : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("智慧行编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("智慧行名称")
    @Condition
    var tripName: String? = null

    @View
    @Label("访问键")
    @Ignored
    @Condition
    var accessKey: String? = null

    @View
    @Label("活动开始时间")
    @Sortable
    var startingTime: LocalDateTime? = null

    @View
    @Label("显示顺序")
    @Ignored
    @Condition
    var displayIndex: Int? = null

    @View
    @Label("简短显示")
    @Condition
    var descSimple: String? = null

    @View
    @Label("智慧行详情")
    @Condition
    var descRichText: String? = null


    @TableField(fill = FieldFill.INSERT)
    @View
    @Label("创建时间")
    @Ignored
    @Condition
    var createdTime: LocalDateTime? = null


    @TableField(fill = FieldFill.UPDATE)
    @View
    @Label("更新时间")
    @Ignored
    @Condition
    var updatedTime: LocalDateTime? = null

    @View
    @Label("状态")
    @Ignored
    @Condition
    var status: wuhan.science.server.util.database.Status? = null

    @View
    @Label("创建者")
    @Ignored
    @Condition
    var creator: String? = null

    @TableLogic
    @Ignored
    var deleted: Boolean? = null


}
