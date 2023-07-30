package wuhan.science.server.entity

import wuhan.science.server.util.view.annotation.Condition
import wuhan.science.server.util.view.annotation.Fixed
import wuhan.science.server.util.view.annotation.Ignored
import wuhan.science.server.util.view.annotation.Label
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPutNews {
    @Label("热点资讯编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("新闻标题")
    @Condition
    var newsTitle: String? = null


    @Label("新闻类型")
    var newsType: String? = null


    @Label("封面图片")
    @Condition
    var coverAccessKey: String? = null

    @Label("简单描述")
    @Condition
    var descSimple: String? = null

    @Label("新闻详情")
    @Condition
    @Ignored
    var contentRichText: String? = null

    @Label("点击量")
    @Fixed
    var hits: String? = null

    @Label("置顶展示")
    @Ignored
    var top: Boolean? = null

    @Label("轮播图展示")
    @Ignored
    var carousel: Boolean? = null

    @Label("置顶展示顺序")
    @Ignored
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
