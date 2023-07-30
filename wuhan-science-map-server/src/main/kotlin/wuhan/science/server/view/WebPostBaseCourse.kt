package wuhan.science.server.entity

import wuhan.science.server.util.view.annotation.Condition
import wuhan.science.server.util.view.annotation.Fixed
import wuhan.science.server.util.view.annotation.Ignored
import wuhan.science.server.util.view.annotation.Label
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPostBaseCourse {
    @Label("课程编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("所属基地编号")
    @Condition
    @Ignored
    var baseId: String? = null

    @Label("课程名称")
    @Condition
    var courseName: String? = null

    @Label("所属区域")
    @Condition
    var regionCode: String? = null

    @Label("详细地址")
    @Condition
    var detailAddress: String? = null

    @Label("课程详情")
    var descRichText: String? = null

    @Label("开课时间")
    @Fixed
    @Condition
    var startingTime: String? = null

    @Label("适宜年龄")
    @Condition
    var targetAge: String? = null

    @Label("是否置顶")
    @Fixed
    @Condition
    var top: Boolean? = null

    @Label("是否置顶")
    @Fixed
    @Condition
    var wonderful: Boolean? = null

    var wonderfulImageAccessKey: String? = null

    @Label("发布时间")
    @Fixed
    var createdTime: String? = null

    @Label("更新时间")
    @Fixed
    var updatedTime: String? = null

    @Label("当前状态")
    @Fixed
    @Condition
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
