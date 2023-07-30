package wuhan.science.server.entity

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppUserRegisterCourse {
    @Label("用户报名课程")
    @Condition
    @Ignored
    var id: String? = null

    @Label("用户编号")
    @Condition
    var userId: String? = null

    @Label("课程编号")
    @Condition
    @Ignored
    var courseId: String? = null

    @Label("报名时间")
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
