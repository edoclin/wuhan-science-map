package wuhan.science.server.view

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年03月29日
*/

class WebPostZhihuiTrip {
    @Label("智慧行编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("智慧行名称")
    @Condition
    var tripName: String? = null

    @Label("访问键")
    @Condition
    @Ignored
    var accessKey: String? = null

    @Label("活动开始时间")
    @Fixed
    var startingTime: String? = null

    @Label("显示顺序")
    @Condition
    @Ignored
    var displayIndex: String? = null

    @Label("简短显示")
    @Condition
    var descSimple: String? = null

    @Label("智慧行详情")
    @Condition
    var descRichText: String? = null

    @Label("创建时间")
    @Condition
    @Ignored
    var createdTime: String? = null

    @Label("更新时间")
    @Condition
    @Ignored
    var updatedTime: String? = null

    @Label("状态")
    @Condition
    @Ignored
    var status: String? = null

    @Label("创建者")
    @Condition
    @Ignored
    var creator: String? = null

}
