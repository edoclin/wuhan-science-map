package wuhan.science.server.entity

import wuhan.science.server.util.view.annotation.Condition
import wuhan.science.server.util.view.annotation.Fixed
import wuhan.science.server.util.view.annotation.Ignored
import wuhan.science.server.util.view.annotation.Label
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPutScienceBase {
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

    @Label("基地类型")
    @Fixed
    @Condition
    var baseType: String? = null

    @Label("基地简介")
    @Condition
    var descRichText: String? = null

    @Label("参观指南")
    var visitTutorial: String? = null

    @Label("友情链接编号")
    var linkIds: List<String>? = null

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
    var top: String? = null

    @Label("是否轮播图展示")
    var carousel: String? = null

    var carouselImageAccessKey: String? = null

    @Label("是否轮播图展示")
    @Fixed
    @Condition
    var displayIndex: Int? = null

    @Label("发布时间")
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
