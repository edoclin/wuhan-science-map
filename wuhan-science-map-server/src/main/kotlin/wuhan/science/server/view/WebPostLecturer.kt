package wuhan.science.server.entity

import wuhan.science.server.util.view.annotation.Condition
import wuhan.science.server.util.view.annotation.Fixed
import wuhan.science.server.util.view.annotation.Ignored
import wuhan.science.server.util.view.annotation.Label
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPostLecturer {
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

    @Label("所属基地编号")
    @Condition
    @Ignored
    var baseId: String? = null

    @Label("讲师详情")
    @Condition
    @Ignored
    var descRichText: String? = null

    @Label("创建时间")
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
