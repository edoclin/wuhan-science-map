package wuhan.science.server.controller;

import org.springframework.web.bind.annotation.*
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.system.Configuration
import wuhan.science.server.view.common.Role
import wuhan.science.server.view.common.WebStatus
import javax.annotation.Resource

/**
 * <p>
 * 活动通知 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/common")
class CommonController {
    @Resource
    lateinit var customConfiguration: Configuration

    companion object {
        var roles: MutableList<Role> = mutableListOf()
        var baseTypes: MutableList<String> = mutableListOf()
        var newsTypes: MutableList<String> = mutableListOf()
        var status: MutableList<WebStatus> = mutableListOf()
    }


    @RequestMapping(method = [RequestMethod.GET], path = ["/roles"])
    fun roles(): ResultBean {
        takeIf { roles.isEmpty() }?.let {
            synchronized(Role::class.java) {
                takeIf { roles.isEmpty() }?.let {
                    customConfiguration.roles.forEach {
                        val role = Role()
                        role.roleKey = it.split(".")[0]
                        role.roleName = it.split(".")[1]
                        roles.add(role)
                    }
                }
            }
        }
        return ResultUtil.ok(roles)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/baseTypes"])
    fun baseTypes(): ResultBean {
        takeIf { baseTypes.isEmpty() }?.let {
            synchronized(baseTypes) {
                takeIf { baseTypes.isEmpty() }?.let {
                    customConfiguration.baseTypes.forEach {
                        baseTypes.add(it)
                    }
                }
            }
        }
        return ResultUtil.ok(baseTypes)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/newsTypes"])
    fun newsTypes(): ResultBean {
        takeIf { newsTypes.isEmpty() }?.let {
            synchronized(newsTypes) {
                takeIf { newsTypes.isEmpty() }?.let {
                    customConfiguration.newsTypes.forEach {
                        newsTypes.add(it)
                    }
                }
            }
        }
        return ResultUtil.ok(newsTypes)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/status"])
    fun status(): ResultBean {

        takeIf { status.isEmpty() }?.let {
            synchronized(status) {
                takeIf { status.isEmpty() }?.let {
                    Status.values().forEach {item ->
                        val webStatus = WebStatus()
                        webStatus.key = item.name
                        webStatus.value = item.desc
                        status.add(webStatus)
                    }
                }
            }
        }
        return ResultUtil.ok(status)
    }


}
