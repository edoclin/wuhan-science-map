package wuhan.science.server.view

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppActivity {
    @Label("活动编号")
    var id: String? = null

    @Label("活动名称")
    var activityName: String? = null

    @Label("活动详情")
    var descRichText: String? = null

    var coverAccessKeyUrl: String? = null

    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null
}
