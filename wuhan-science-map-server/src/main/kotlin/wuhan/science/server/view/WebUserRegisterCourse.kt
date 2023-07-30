package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebUserRegisterCourse {
    @Label("用户报名课程")
    @Condition
    @Ignored
    var id: String? = null

    @Label("用户编号")
    @Condition
    @Ignored
    var userId: String? = null

    @Label("联系方式")
    var mobile: String? = null

    @Label("真实姓名")
    var realName: String? = null


    @Label("课程编号")
    @Condition
    @Ignored
    var courseId: String? = null

    @Label("报名时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
