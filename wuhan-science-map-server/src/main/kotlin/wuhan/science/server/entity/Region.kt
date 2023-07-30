package wuhan.science.server.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.view.annotation.*
import java.io.Serializable

/**
 * <p>
 * 地区
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@TableName("t_region")
@WebView
class Region : Serializable {

    @TableId(value = "code", type = IdType.ASSIGN_UUID)
    @View
    @Label("地区编号")
    @Sortable
    @Condition
    var code: Int? = null

    @View
    @Label("地区名称")
    @Condition
    var regionName: String? = null

    @View
    @Label("父级编号")
    @Sortable
    @Condition
    var parentCode: Int? = null

    @View
    @Label("行政区域")
    @Ignored
    var polygonGeometry: String? = null

    @TableLogic
    var deleted: Boolean? = null

    @View
    @Label("操作者")
    var creator: String? = null

    @View
    @Label("当前状态")
    @Sortable
    var status: Status? = null
}
