package wuhan.science.server.entity

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPostBaseLink {
    @Label("基地友情链接编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("所属基地编号")
    @Condition
    @Ignored
    var baseId: String? = null

    @Label("友情链接编号")
    @Condition
    @Ignored
    var linkId: String? = null

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
