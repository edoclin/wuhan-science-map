package wuhan.science.server.entity

import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPutBaseHall {
    @Label("展厅编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("所属基地编号")
    @Condition
    @Ignored
    var baseId: String? = null

    @Label("展厅名称")
    @Condition
    var hallName: String? = null

    @View
    @Label("展厅Logo")
    var hallLogo: String? = null


    @Label("展厅简介")
    @Condition
    var descSimple: String? = null

    @Label("展厅详情")
    @Condition
    @Ignored
    var descRichText: String? = null

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
