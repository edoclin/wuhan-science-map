package wuhan.science.server.controller;

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.date.LocalDateTimeUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.*
import wuhan.science.server.service.*
import wuhan.science.server.util.*
import wuhan.science.server.util.database.*
import wuhan.science.server.util.date.*
import wuhan.science.server.util.result.*
import wuhan.science.server.util.view.*
import wuhan.science.server.view.*
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 用户报名课程 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/userRegisterCourse")
class UserRegisterCourseController {
    @Resource
    lateinit var iUserRegisterCourseService: IUserRegisterCourseService

    @Resource
    lateinit var iUserService: IUserService

    @Resource
    lateinit var iBaseCourseService: IBaseCourseService

    @Resource
    lateinit var iScienceBaseService: IScienceBaseService

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostUserRegisterCourse): ResultBean {

        val insert = UserRegisterCourse()
        BeanUtil.copyProperties(form, insert)
        iUserRegisterCourseService.save(insert).takeIf { it }?.let {
            return ResultUtil.ok("课程报名成功")
        }
        return ResultUtil.error("参数错误")
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms/{current}/{limit}"])
    fun list4Cms(
        @PathVariable @DecimalMin("1") current: Number,
        @PathVariable @DecimalMin("1") limit: Number,
        @RequestBody queryParam: QueryParam
    ): ResultBean {
        val result = mutableListOf<WebUserRegisterCourse>()
        val page = Page<UserRegisterCourse>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<UserRegisterCourse>(queryParam)
        val count = iUserRegisterCourseService.count(wrapper)

        iUserRegisterCourseService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebUserRegisterCourse()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            it.userId?.let {
                iUserService.getById(it)?.let { user ->
                    item.mobile = user.mobile
                    item.realName = user.realName
                }
            }
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iUserRegisterCourseService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutUserRegisterCourse): ResultBean {
        val update = UserRegisterCourse()
        BeanUtil.copyProperties(form, update)
        iUserRegisterCourseService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebUserRegisterCourse>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebUserRegisterCourse>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebUserRegisterCourse>()
        iUserRegisterCourseService.list(KtQueryWrapper(UserRegisterCourse::class.java).select()).mapTo(result) {
            val item = WebUserRegisterCourse()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/register/{id}"])
    fun registerCourse4App(@PathVariable id: String): ResultBean {
        val user =
            iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))
        takeIf { BeanUtil.isNotEmpty(user) }?.let {

            val one = iUserRegisterCourseService.getOne(
                KtQueryWrapper(UserRegisterCourse::class.java).eq(
                    UserRegisterCourse::courseId, id
                ).eq(UserRegisterCourse::userId, user.id)
            )

            takeIf { BeanUtil.isNotEmpty(one) }?.let {
                iUserRegisterCourseService.removeById(one.id).takeIf { it }?.let {
                    return ResultUtil.ok(false)
                }
            }
            val byId = iBaseCourseService.getById(id)
            takeIf { BeanUtil.isNotEmpty(byId) }?.let {

                val userRegisterCourse = UserRegisterCourse()
                userRegisterCourse.userId = user.id
                userRegisterCourse.courseId = byId.id

                iUserRegisterCourseService.save(userRegisterCourse).takeIf { it }?.let {
                    return ResultUtil.ok(true)
                }
            }
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app"])
    fun registerCourseByUserId(): ResultBean {
        val user =
            iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))
        takeIf { BeanUtil.isNotEmpty(user) }?.let {
            val result = mutableListOf<AppBaseCourse>()
            iUserRegisterCourseService.list(
                KtQueryWrapper(UserRegisterCourse::class.java).eq(
                    UserRegisterCourse::userId, user.id
                )
            ).mapTo(result) { userRegisterCourse ->
                val appBaseCourse = AppBaseCourse()
                val byId = iBaseCourseService.getById(userRegisterCourse.courseId)
                takeIf { BeanUtil.isNotEmpty(byId) }?.let {
                    val base = iScienceBaseService.getById(byId.baseId)
                    takeIf { BeanUtil.isNotEmpty(base) }?.let {
                        appBaseCourse.baseName = base.baseName
                    }
                    appBaseCourse.id = byId.id
                    appBaseCourse.courseName = byId.courseName
                    appBaseCourse.startingTime = DateUtil.yyyyMMddHHmm(byId.startingTime)
                    appBaseCourse.createdTime = DateUtil.yyyyMMddHHmm(userRegisterCourse.updatedTime)
                    println()

                    appBaseCourse.finished = LocalDateTimeUtil.now() > byId.startingTime
                }
                appBaseCourse
            }
            return ResultUtil.ok(result)
        }
        return ResultUtil.error()
    }

}
