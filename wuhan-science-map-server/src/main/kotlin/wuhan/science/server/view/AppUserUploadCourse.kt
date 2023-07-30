package wuhan.science.server.entity

import com.fasterxml.jackson.annotation.JsonInclude
import wuhan.science.server.util.view.annotation.*
import wuhan.science.server.view.common.AppUserUpload
import javax.validation.constraints.NotEmpty

/**
* @author edoclin
* @since 2023年02月21日
*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppUserUploadCourse {

    @Label("文件访问路径, 现用作 content 字段")
    @NotEmpty
    var accessKey: String? = null

    @Label("课程编号")
    @NotEmpty
    var courseId: String? = null
    var courseName: String? = null

    @Label("心得体会, 现用做 upload keys 字段")
    var descRichText: List<AppUserUpload>? = null

    var updatedTime: String? = null

    var baseName: String? = null
}
