package wuhan.science.server.entity

import wuhan.science.server.util.view.annotation.Condition
import wuhan.science.server.util.view.annotation.Fixed
import wuhan.science.server.util.view.annotation.Ignored
import wuhan.science.server.util.view.annotation.Label
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPostActivity {
    @Label("活动编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("活动名称")
    @Condition
    var activityName: String? = null

    @Label("活动详情")
    var descRichText: String? = null
    @Label("封面图片")
    var coverAccessKey: String? = null
    @Label("所属基地编号")
    var baseId: String? = null

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
