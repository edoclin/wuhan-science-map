package wuhan.science.server.controller;

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import wuhan.science.server.entity.User
import wuhan.science.server.entity.UserFavoriteCourse
import wuhan.science.server.service.IBaseCourseService
import wuhan.science.server.service.IScienceBaseService
import wuhan.science.server.service.IUserFavoriteCourseService
import wuhan.science.server.service.IUserService
import wuhan.science.server.util.date.DateUtil
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.view.AppBaseCourse
import javax.annotation.Resource

/**
 * <p>
 * 用户收藏课程 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/userFavoriteCourse")
class UserFavoriteCourseController {

    @Resource
    lateinit var iUserService: IUserService

    @Resource
    lateinit var iBaseCourseService: IBaseCourseService

    @Resource
    lateinit var iUserFavoriteCourseService: IUserFavoriteCourseService

    @Resource
    lateinit var iScienceBaseService: IScienceBaseService


    @RequestMapping(method = [RequestMethod.GET], path = ["/app/favor/{id}"])
    fun favorCourse4App(@PathVariable id: String): ResultBean {
        val user =
            iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))
        takeIf { BeanUtil.isNotEmpty(user) }?.let {
            val one = iUserFavoriteCourseService.getOne(
                KtQueryWrapper(UserFavoriteCourse::class.java).eq(
                    UserFavoriteCourse::courseId, id
                ).eq(UserFavoriteCourse::userId, user.id)
            )
            takeIf { BeanUtil.isNotEmpty(one) }?.let {
                iUserFavoriteCourseService.removeById(one).takeIf { it }?.let {
                    return ResultUtil.ok(false)
                }
            }
            val byId = iBaseCourseService.getById(id)
            takeIf { BeanUtil.isNotEmpty(byId) }?.let {
                val userFavoriteCourse = UserFavoriteCourse()
                userFavoriteCourse.userId = user.id
                userFavoriteCourse.courseId = byId.id
                iUserFavoriteCourseService.save(userFavoriteCourse).takeIf { it }?.let {
                    return ResultUtil.ok(true)
                }
            }
        }

        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app"])
    fun favoriteCourseByUserId(): ResultBean {
        val user =
            iUserService.getOne(KtQueryWrapper(User::class.java).eq(User::wechatUid, StpUtil.getLoginIdAsString()))
        takeIf { BeanUtil.isNotEmpty(user) }?.let {
            val result = mutableListOf<AppBaseCourse>()
            iUserFavoriteCourseService.list(KtQueryWrapper(UserFavoriteCourse::class.java).eq(UserFavoriteCourse::userId, user.id)).mapTo(result) {userFavoriteCourse ->
                val appBaseCourse = AppBaseCourse()
                val byId = iBaseCourseService.getById(userFavoriteCourse.courseId)
                takeIf { BeanUtil.isNotEmpty(byId) }?.let {
                    val base = iScienceBaseService.getById(byId.baseId)
                    takeIf { BeanUtil.isNotEmpty(base) }?.let {
                        appBaseCourse.baseName = base.baseName
                    }
                    appBaseCourse.id = byId.id
                    appBaseCourse.courseName = byId.courseName
                    appBaseCourse.startingTime = DateUtil.yyyyMMddHHmm(byId.startingTime)
                    appBaseCourse.createdTime = DateUtil.yyyyMMddHHmm(userFavoriteCourse.updatedTime)
                }
                appBaseCourse
            }
            return ResultUtil.ok(result)
        }
        return ResultUtil.error()
    }

}
