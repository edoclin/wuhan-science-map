package wuhan.science.server.view

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppBaseCourse {
    @Label("课程编号")
    var id: String? = null

    @Label("所属基地编号")
    var baseId: String? = null

    var baseName: String? = null

    @Label("课程名称")
    var courseName: String? = null

    var finished: Boolean? = null

    @Label("所属区域")
    var regionCode: String? = null

    @Label("详细地址")
    var detailAddress: String? = null

    @Label("课程详情")
    var descRichText: String? = null

    @Label("开课时间")
    var startingTime: String? = null

    @Label("适宜年龄")
    var targetAge: String? = null

    var registered: Boolean? = null

    var favored: Boolean? = null

    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null


    var wonderfulImageAccessKeyUrl: String? = null

}
