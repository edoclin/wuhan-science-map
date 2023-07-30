package wuhan.science.server.controller;


import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.http.HtmlUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.*
import wuhan.science.server.service.*
import wuhan.science.server.util.*
import wuhan.science.server.util.database.*
import wuhan.science.server.util.date.*
import wuhan.science.server.util.region.RegionUtil
import wuhan.science.server.util.result.*
import wuhan.science.server.util.tencent.cdn.CDNUtil
import wuhan.science.server.util.view.*
import wuhan.science.server.view.*
import java.time.ZoneOffset
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 科普基地课程 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/baseCourse")
class BaseCourseController {
    @Resource
    lateinit var iBaseCourseService: IBaseCourseService

    @Resource
    lateinit var iScienceBaseService: IScienceBaseService

    @Resource
    lateinit var iUserRegisterCourseService: IUserRegisterCourseService

    @Resource
    lateinit var iUserFavoriteService: IUserFavoriteCourseService

    @Resource
    lateinit var iUSerService: IUserService

    @Resource
    lateinit var regionUtil: RegionUtil

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostBaseCourse): ResultBean {
        iBaseCourseService.getOne(KtQueryWrapper(BaseCourse::class.java).eq(BaseCourse::courseName, form.courseName))
            ?.let {
                // todo: message
                return ResultUtil.error("课程名称重复")
            } ?: let {
            val insert = BaseCourse()
            BeanUtil.copyProperties(form, insert)
            insert.startingTime = insert.startingTime?.plusHours(8)
            iBaseCourseService.save(insert).takeIf { it }?.let {
                return ResultUtil.ok("课程添加成功")
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
        val result = mutableListOf<WebBaseCourse>()
        val page = Page<BaseCourse>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<BaseCourse>(queryParam)
        val count = iBaseCourseService.count(wrapper)

        iBaseCourseService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebBaseCourse()
            BeanUtil.copyProperties(it, item, "top", "wonderful")
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item.startingTime = DateUtil.yyyyMMddHHmm(it.startingTime)
            item.startingTimestamp = it.startingTime?.toInstant(ZoneOffset.ofHours(8))?.toEpochMilli()
            item.top = it.top
            item.wonderful = it.wonderful
            item.targetAgeFormat = "${it.targetAge}岁以上"
            item.baseName = iScienceBaseService.getById(it.baseId)?.baseName ?: "关联基地失效"
            item.wonderfulImageAccessKeyUrl = it.wonderfulImageAccessKey?.let { key -> CDNUtil.generateUrl(key) }

            it.regionCode?.let { code ->
                val regionInfo = regionUtil.getRegionCodesByCode(code)
                item.regionCodes = regionInfo.regionCodes
                item.regions = regionInfo.name
            }
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iBaseCourseService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutBaseCourse): ResultBean {
        val update = BaseCourse()
        BeanUtil.copyProperties(form, update)
        update.startingTime = update.startingTime?.plusHours(8)
        iBaseCourseService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebBaseCourse>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebBaseCourse>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebBaseCourse>()
        iBaseCourseService.list(KtQueryWrapper(BaseCourse::class.java).select()).mapTo(result) {
            val item = WebBaseCourse()
            // todo: action
            item.id = it.id
            item.courseName = it.courseName
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/detail/{id}"])
    fun courseDetailById4App(@PathVariable id: String): ResultBean {
        val byId = iBaseCourseService.getById(id)
        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val appBaseCourse = AppBaseCourse()
            BeanUtil.copyProperties(byId, appBaseCourse, "top")
            appBaseCourse.startingTime = DateUtil.yyyyMMddHHmm(byId.startingTime)
            appBaseCourse.targetAge = "${byId.targetAge}岁以上"
            appBaseCourse.updatedTime = DateUtil.yyyyMMdd(byId.updatedTime)

            val user =
                iUSerService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))

            takeIf { BeanUtil.isNotEmpty(user) }?.let {
                val userRegisterCourse = iUserRegisterCourseService.getOne(
                    KtQueryWrapper(UserRegisterCourse::class.java).eq(
                        UserRegisterCourse::courseId, id
                    ).eq(UserRegisterCourse::userId, user.id)
                )

                val userFavoriteCourse = iUserFavoriteService.getOne(
                    KtQueryWrapper(UserFavoriteCourse::class.java).eq(
                        UserFavoriteCourse::courseId, id
                    ).eq(UserFavoriteCourse::userId, user.id)
                )
                appBaseCourse.registered = BeanUtil.isNotEmpty(userRegisterCourse)
                appBaseCourse.favored = BeanUtil.isNotEmpty(userFavoriteCourse)
            }
            return ResultUtil.ok(appBaseCourse)
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/wonderful"])
    fun wonderfulCourse4App(): ResultBean {
        val result = mutableListOf<AppBaseCourse>()

        iBaseCourseService.list(KtQueryWrapper(BaseCourse::class.java).eq(BaseCourse::wonderful, true).orderByDesc(BaseCourse::updatedTime)).mapTo(result) {baseCourse ->
            val appBaseCourse = AppBaseCourse()
            BeanUtil.copyProperties(
                baseCourse, appBaseCourse, "descRichText", "status", "createdTime", "updatedTime", "top"
            )
            val byId = iScienceBaseService.getById(baseCourse.baseId)
            takeIf { BeanUtil.isNotEmpty(byId) }?.let {
                appBaseCourse.baseName = byId.baseName
            }
            appBaseCourse.targetAge = "${baseCourse.targetAge}岁以上"
            appBaseCourse.startingTime = DateUtil.yyyyMMddHHmm(baseCourse.startingTime)
            appBaseCourse.descRichText = HtmlUtil.cleanHtmlTag(baseCourse.descRichText)
            appBaseCourse.wonderfulImageAccessKeyUrl = baseCourse.wonderfulImageAccessKey?.let { key -> CDNUtil.generateUrl(key) }
            appBaseCourse
        }

        return ResultUtil.ok(result)
    }


}
