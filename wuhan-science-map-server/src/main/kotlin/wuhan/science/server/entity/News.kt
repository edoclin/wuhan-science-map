package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 热点资讯
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_news")
@WebView
class News : Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @View
    @Label("热点资讯编号")
    @Ignored
    @Condition
    var id: String? = null

    @View
    @Label("新闻标题")
    @Condition
    var newsTitle: String? = null

    @Label("新闻类型")
    @Condition
    var newsType: String? = null
    @View
    @Label("封面图片")
    @Condition
    var coverAccessKey: String? = null

    @View
    @Label("简单描述")
    @Condition
    var descSimple: String? = null

    @View
    @Label("新闻详情")
    @Ignored
    @Condition
    var contentRichText: String? = null

    @View
    @Label("点击量")
    @Sortable
    var hits: Int? = null

    @View
    @Label("置顶显示")
    @TableField("is_top")
    @Ignored
    var top: Boolean? = null

    @View
    @Label("轮播图显示")
    @TableField("is_carousel")
    @Ignored
    var carousel: Boolean? = null

    @View
    @Label("置顶显示排列顺序")
    @Ignored
    var displayIndex: Int? = null

    @View
    @Label("发布时间")
    @TableField(fill = FieldFill.INSERT)
    @Sortable
    var createdTime: LocalDateTime? = null

    @View
    @Label("更新时间")
    @Sortable
    @TableField(fill = FieldFill.UPDATE)
    var updatedTime: LocalDateTime? = null

    @View
    @Label("当前状态")
    @Sortable
    var status: Status? = null

    @View
    @Label("操作者")
    var creator: String? = null

    @TableLogic
    @Ignored
    var deleted: Boolean? = null
}
