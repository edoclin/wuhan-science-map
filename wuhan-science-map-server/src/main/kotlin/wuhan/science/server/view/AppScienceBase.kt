package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.postgis.AppPoint
import wuhan.science.server.util.view.annotation.*
import wuhan.science.server.view.*

/**
 * @author edoclin
 * @since 2023年02月21日
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppScienceBase {

    @Label("科普基地编号")
    var id: String? = null

    @Label("基地名称")
    var baseName: String? = null

    var title: String? = null

    @Label("基地Logo")
    var baseLogo: String? = null

    var baseLogoUrl: String? = null

    @Label("基地类型")
    var baseType: String? = null

    @Label("基地简介")
    var descRichText: String? = null

    @Label("参观指南")
    var visitTutorial: String? = null

    @Label("行政区域")
    var polygonGeometry: List<AppPoint>? = null

    var centroid: AppPoint? = null

    @Label("行政区域编号")
    var regionCode: Int? = null

    @Label("详细地址")
    var detailAddress: String? = null

    @Label("是否置顶显示")
    var top: String? = null

    var carouselImageAccessKey: String? = null

    var carouselImageAccessKeyUrl: String? = null

    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

    var courses: List<AppBaseCourse>? = null

    var lecturers: List<AppLecturer>? = null

    var halls: List<AppBaseHall>? = null

    var links: List<AppLink>? = null

    var activities: List<AppActivity>? = null


}
