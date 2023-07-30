package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebLink {
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
    @Ignored
    var linkLogo: String? = null

    @Ignored
    var linkLogoUrl: String? = null

    @Label("创建时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
