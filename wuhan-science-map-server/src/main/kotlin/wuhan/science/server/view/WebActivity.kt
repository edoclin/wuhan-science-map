package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebActivity {
    @Label("活动编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("活动名称")
    @Condition
    var activityName: String? = null


    @Label("活动封面")
    @Ignored
    var coverAccessKey: String? = null

    @Label("活动封面")
    @Ignored
    var coverAccessKeyUrl: String? = null
    @Label("活动详情")
    @Ignored
    var descRichText: String? = null

    @Label("所属基地编号")
    @Ignored
    var baseId: String? = null

    @Label("所属基地")
    @Ignored
    var baseName: String? = null
    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
