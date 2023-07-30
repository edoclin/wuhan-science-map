package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.postgis.Point
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebScienceBase {
    @Label("科普基地编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("基地名称")
    @Condition
    var baseName: String? = null

    @Label("基地Logo")
    @Ignored
    var baseLogo: String? = null

    @Label("基地Logo链接")
    @Ignored
    var baseLogoUrl: String? = null

    @Label("基地类型")
    @Fixed
    @Condition
    var baseType: String? = null

    @Label("基地简介")
    @Ignored
    var descRichText: String? = null

    @Label("友情链接编号")
    @Ignored
    var linkIds: List<String>? = null

    @Label("参观指南")
    @Ignored
    var visitTutorial: String? = null

    @Label("行政区域")
    @Ignored
    var polygonGeometry: List<List<Double>>? = null
    @Label("中心位置")
    @Ignored
    var centroid: Point? = null

    @Label("行政区域编号")
    @Ignored
    var regionCode: Int? = null

    @Label("所属区域")
    @Ignored
    var regions: String? = null
    @Label("所属区域编号")
    @Ignored
    var regionCodes: List<Int>? = null

    @Label("详细地址")
    var detailAddress: String? = null

    @Label("置顶展示")
    @Ignored
    @Fixed
    @Condition
    var top: Boolean? = null

    @Label("轮播图展示")
    @Ignored
    @Fixed
    @Condition
    var carousel: Boolean? = null

    @Ignored
    var carouselImageAccessKey: String? = null

    @Ignored
    var carouselImageAccessKeyUrl: String? = null

    @Label("置顶展示顺序")
    @Ignored
    var displayIndex: Int? = null

    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
