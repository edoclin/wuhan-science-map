package wuhan.science.server.entity

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPostLink {
    @Label("友情链接编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("访问路径")
    @Condition
    var url: String? = null

    @Label("链接名称")
    @Condition
    var linkName: String? = null

    @Label("链接Logo")
    var linkLogo: String? = null

    @Label("创建时间")
    @Fixed
    var createdTime: String? = null

    @Label("更新时间")
    @Fixed
    var updatedTime: String? = null

    @Label("当前状态")
    @Fixed
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
