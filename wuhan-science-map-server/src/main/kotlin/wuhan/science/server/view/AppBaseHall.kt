package wuhan.science.server.view

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppBaseHall {
    @Label("展厅编号")
    var id: String? = null

    @Label("所属基地编号")
    var baseId: String? = null

    @Label("展厅名称")
    var hallName: String? = null

    @Label("展厅Logo")
    var hallLogo: String? = null

    var hallLogoUrl: String? = null

    @Label("展厅简介")
    var descSimple: String? = null

    @Label("展厅详情")
    var descRichText: String? = null

    @Label("发布时间")
    var createdTime: String? = null

    @Label("更新时间")
    var updatedTime: String? = null

    @Label("当前状态")
    var status: String? = null

    @Label("操作者")
    var creator: String? = null

}
