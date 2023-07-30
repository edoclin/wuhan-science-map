package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebLecturer {
    @Label("讲师编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("讲师姓名")
    @Condition
    var lecturerName: String? = null

    @Label("讲师照片")
    @Ignored
    var lecturerPicture: String? = null


    @Label("讲师照片访问路径")
    @Ignored
    var lecturerPictureUrl: String? = null

    @Label("所属基地编号")
    @Condition
    @Ignored
    var baseId: String? = null

    @Label("所属基地")
    @Condition
    var baseName: String? = null

    @Label("讲师详情")
    @Condition
    @Ignored
    var descRichText: String? = null

    @Label("创建时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
