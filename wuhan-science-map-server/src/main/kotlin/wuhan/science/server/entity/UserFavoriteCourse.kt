package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 用户收藏课程
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_user_favorite_course")
@WebView
class UserFavoriteCourse : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("用户收藏课程编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("用户编号")
    @Ignored
    @Condition
    var userId: String? = null

    @View
    @Label("课程编号")
    @Ignored
    @Condition
    var courseId: String? = null

    @View
    @Label("收藏时间")
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
