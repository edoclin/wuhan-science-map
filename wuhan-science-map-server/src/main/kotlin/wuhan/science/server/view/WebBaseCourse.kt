package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebBaseCourse {
    @Label("课程编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("所属基地编号")
    @Condition
    @Ignored
    var baseId: String? = null

    @Label("所属基地名称")
    var baseName: String? = null

    @Label("课程名称")
    @Condition
    var courseName: String? = null

    @Label("所属区域")
    @Condition
    var regions: String? = null

    @Label("所属区域编号")
    @Ignored
    var regionCodes: List<Int>? = null

    @Label("详细地址")
    @Condition
    var detailAddress: String? = null

    @Label("课程详情")
    @Ignored
    var descRichText: String? = null

    @Label("开课时间")
    @Condition
    var startingTime: String? = null

    @Label("适宜年龄")
    @Ignored
    var targetAge: String? = null

    @Label("适宜年龄")
    var targetAgeFormat: String? = null

    @Label("适宜年龄时间戳")
    @Ignored
    var startingTimestamp: Long? = null

    @Label("是否置顶")
    @Ignored
    var top: Boolean? = null

    @Label("是否设为精彩课程")
    @Ignored
    var wonderful: Boolean? = null

    var wonderfulImageAccessKey: String? = null

    var wonderfulImageAccessKeyUrl: String? = null

    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
