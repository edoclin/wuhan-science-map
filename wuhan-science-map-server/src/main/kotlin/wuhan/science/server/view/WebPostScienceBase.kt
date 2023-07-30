package wuhan.science.server.entity

import wuhan.science.server.util.view.annotation.Condition
import wuhan.science.server.util.view.annotation.Fixed
import wuhan.science.server.util.view.annotation.Ignored
import wuhan.science.server.util.view.annotation.Label
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPostScienceBase {
    @Label("科普基地编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("基地名称")
    @Condition
    var baseName: String? = null

    @Label("基地Logo")
    @Condition
    @Ignored
    var baseLogo: String? = null

    @Label("友情链接编号")
    var linkIds: List<String>? = null

    @Label("基地类型")
    @Fixed
    @Condition
    var baseType: String? = null

    @Label("基地简介")
    @Condition
    var descRichText: String? = null

    @Label("参观指南")
    var visitTutorial: String? = null

    @Label("行政区域")
    @Ignored
    var polygonGeometry: List<List<Double>>? = null

    @Label("行政区域编号")
    @Ignored
    var regionCode: Int? = null

    @Label("详细地址")
    var detailAddress: String? = null

    @Label("是否置顶展示")
    @Fixed
    @Condition
    var top: Boolean? = null

    @Label("是否轮播图展示")
    @Fixed
    @Condition
    var carousel: Boolean? = null

    var carouselImageAccessKey: String? = null

    @Label("当前状态")
    @Fixed
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
