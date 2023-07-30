package wuhan.science.server.entity

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPutActivity {
    @Label("活动编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("活动名称")
    @Condition
    var activityName: String? = null

    @Label("活动详情")
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
