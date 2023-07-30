package wuhan.science.server.entity

import kotlin.String
import wuhan.science.server.util.view.annotation.*
import com.fasterxml.jackson.annotation.JsonInclude
/**
* @author edoclin
* @since 2023年02月21日
*/

class WebPutUserUploadCourse {
    @Label("用户课程上传资料编号")
    @Condition
    @Ignored
    var id: String? = null

    @Label("用户编号")
    @Condition
    @Ignored
    var userId: String? = null

    @Label("文件访问路径")
    @Condition
    @Ignored
    var accessKey: String? = null

    @Label("课程编号")
    @Condition
    @Ignored
    var courseId: String? = null

    @Label("心得体会")
    @Condition
    @Ignored
    var descRichText: String? = null

    @Label("上传时间")
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
