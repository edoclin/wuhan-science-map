package wuhan.science.server.controller;

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.*
import wuhan.science.server.service.*
import wuhan.science.server.util.*
import wuhan.science.server.util.database.*
import wuhan.science.server.util.date.*
import wuhan.science.server.util.result.*
import wuhan.science.server.util.tencent.cdn.CDNUtil
import wuhan.science.server.util.view.*
import wuhan.science.server.view.*
import wuhan.science.server.view.common.AppUserUpload
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 用户课程上传资料 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/userUploadCourse")
class UserUploadCourseController {
    @Resource
    lateinit var iUserUploadCourseService: IUserUploadCourseService

    @Resource
    lateinit var iUserService: IUserService

    @Resource
    lateinit var iBaseCourseService: IBaseCourseService

    @Resource
    lateinit var iScienceBaseService: IScienceBaseService

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostUserUploadCourse): ResultBean {
        iUserUploadCourseService.getOne(KtQueryWrapper(UserUploadCourse::class.java).eq(UserUploadCourse::id, form.id))
            ?.let {
                // todo: message
                return ResultUtil.error("")
            } ?: let {
            val insert = UserUploadCourse()
            BeanUtil.copyProperties(form, insert)
            iUserUploadCourseService.save(insert).takeIf { it }?.let {
                // todo: message
                return ResultUtil.ok("")
            }
            return ResultUtil.error("参数错误")
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms/{current}/{limit}"])
    fun list4Cms(
        @PathVariable @DecimalMin("1") current: Number,
        @PathVariable @DecimalMin("1") limit: Number,
        @RequestBody queryParam: QueryParam
    ): ResultBean {
        val result = mutableListOf<WebUserUploadCourse>()
        val page = Page<UserUploadCourse>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<UserUploadCourse>(queryParam)
        val count = iUserUploadCourseService.count(wrapper)

        iUserUploadCourseService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebUserUploadCourse()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iUserUploadCourseService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutUserUploadCourse): ResultBean {
        val update = UserUploadCourse()
        BeanUtil.copyProperties(form, update)
        iUserUploadCourseService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebUserUploadCourse>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebUserUploadCourse>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebUserUploadCourse>()
        iUserUploadCourseService.list(KtQueryWrapper(UserUploadCourse::class.java).select()).mapTo(result) {
            val item = WebUserUploadCourse()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/app"])
    fun postUserCourseResult4App(@RequestBody @Valid form: AppUserUploadCourse): ResultBean {
        val user =
            iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))

        takeIf { BeanUtil.isNotEmpty(user) }?.let {
            val userUploadCourse = UserUploadCourse()
            userUploadCourse.courseId = form.courseId
            userUploadCourse.accessKey = form.accessKey
            userUploadCourse.descRichText = JSONUtil.toJsonStr(form.descRichText)
            userUploadCourse.userId = user.id

            iUserUploadCourseService.save(userUploadCourse).takeIf { it }?.let {
                return ResultUtil.ok("发布成功")
            }
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/{id}"])
    fun listUserCourseResult4App(@PathVariable id: String): ResultBean {
        val user =
            iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))



        takeIf { BeanUtil.isNotEmpty(user) }?.let {

            val result = mutableListOf<AppUserUploadCourse>()

            var wrapper = KtQueryWrapper(UserUploadCourse::class.java).eq(UserUploadCourse::userId, user.id)

            if (!StrUtil.equals("-1", id)) {
                wrapper = wrapper.eq(
                    UserUploadCourse::courseId, id
                )
            }
            wrapper = wrapper.orderByDesc(UserUploadCourse::updatedTime)

            iUserUploadCourseService.list(wrapper).mapTo(result) { userUploadCourse ->
                val appUserUploadCourse = AppUserUploadCourse()

                val byId = iBaseCourseService.getById(userUploadCourse.courseId)

                takeIf { BeanUtil.isNotEmpty(byId) }?.let {
                    appUserUploadCourse.courseName = byId.courseName
                    val base = iScienceBaseService.getById(byId.baseId)
                    appUserUploadCourse.baseName = base?.baseName ?: ""
                }
                val uploads = JSONUtil.toList(userUploadCourse.descRichText, AppUserUpload::class.java)
                val descRichText = mutableListOf<AppUserUpload>()
                for (i in 0 until uploads.size) {
                    val appUserUpload = AppUserUpload()
                    appUserUpload.key = uploads[i].key?.let { key -> CDNUtil.generateUrl(key) }
                    descRichText.add(appUserUpload)
                }
                appUserUploadCourse.descRichText = descRichText
                appUserUploadCourse.accessKey = userUploadCourse.accessKey
                appUserUploadCourse.updatedTime = DateUtil.yyyyMMddHHmm(userUploadCourse.updatedTime)
                appUserUploadCourse
            }
            return ResultUtil.ok(result)
        }
        return ResultUtil.error()
    }

}
