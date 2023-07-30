package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WebBaseHall {
    @Label("展厅编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("所属基地编号")
    @Condition
    @Ignored
    var baseId: String? = null

    @Label("所属基地")
    var baseName: String? = null


    @View
    @Label("展厅Logo")
    @Ignored
    var hallLogo: String? = null

    @View
    @Ignored
    var hallLogoUrl: String? = null

    @Label("展厅名称")
    @Condition
    var hallName: String? = null

    @Label("展厅简介")
    var descSimple: String? = null

    @Label("展厅详情")
    @Ignored
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
