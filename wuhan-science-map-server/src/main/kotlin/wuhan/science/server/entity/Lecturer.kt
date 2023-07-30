package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 基地科普讲师
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_lecturer")
@WebView
class Lecturer : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("讲师编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("讲师姓名")
    @Condition
    var lecturerName: String? = null


    @View
    @Label("讲师照片")
    @Ignored
    var lecturerPicture: String? = null

    @View
    @Label("所属基地编号")
    @Ignored
    @Condition
    var baseId: String? = null

    @View
    @Label("讲师详情")
    @Ignored
    var descRichText: String? = null

    @View
    @Label("创建时间")
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
