package wuhan.science.server.view

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年03月29日
*/

class WebPutZhihuiTripItem {
    @Label("子路线编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("所属智慧行编号")
    @Condition
    @Ignored
    var tripId: String? = null

    @Label("路线名称")
    @Condition
    var itemName: String? = null

    @Label("关联课程编号")
    @Condition
    @Ignored
    var courseId: String? = null

    @Label("访问建")
    @Condition
    @Ignored
    var accessKey: String? = null

    @Label("显示顺序")
    @Ignored
    var displayIndex: String? = null

    @Label("简短描述")
    @Condition
    @Ignored
    var descSimple: String? = null

    @Label("路线详情")
    @Condition
    @Ignored
    var descRichText: String? = null

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
