package wuhan.science.server.entity

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPostRegion {
    @Label("地区编号")
    @Fixed
    @Condition
    var code: String? = null

    @Label("地区名称")
    @Condition
    var regionName: String? = null

    @Label("父级编号")
    @Fixed
    @Condition
    var parentCode: String? = null

    @Label("行政区域")
    @Ignored
    var polygonGeometry: String? = null

    @Label("操作者")
    var creator: String? = null

    @Label("当前状态")
    @Fixed
    var status: String? = null

}
