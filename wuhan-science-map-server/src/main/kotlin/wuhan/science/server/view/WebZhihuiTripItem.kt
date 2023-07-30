package wuhan.science.server.view

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年03月29日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebZhihuiTripItem {


    @Label("子路线编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("所属智慧行编号")
    @Condition
    @Ignored
    var tripId: String? = null

    @Label("所属智慧行名称")
    @Condition
    var tripName: String? = null


    @Label("路线名称")
    @Condition
    var itemName: String? = null

    @Label("关联课程编号")
    @Condition
    @Ignored
    var courseId: String? = null

    @Label("关联课程名称")
    @Condition
    var courseName: String? = null

    @Label("访问建")
    @Condition
    @Ignored
    var accessKey: String? = null

    @Ignored
    var accessUrl: String? = null

    @Label("显示顺序")
    var displayIndex: String? = null

    @Label("简短描述")
    @Condition
    var descSimple: String? = null

    @Label("路线详情")
    @Condition
    @Ignored
    var descRichText: String? = null

    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    @Condition
    @Ignored
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
