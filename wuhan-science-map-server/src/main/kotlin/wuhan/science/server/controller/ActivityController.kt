package wuhan.science.server.controller;

import cn.hutool.core.bean.BeanUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.Activity
import wuhan.science.server.entity.WebActivity
import wuhan.science.server.entity.WebPostActivity
import wuhan.science.server.entity.WebPutActivity
import wuhan.science.server.service.IActivityService
import wuhan.science.server.service.IScienceBaseService
import wuhan.science.server.util.database.Batch
import wuhan.science.server.util.database.QueryParam
import wuhan.science.server.util.database.QueryUtil
import wuhan.science.server.util.date.DateUtil
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.tencent.cdn.CDNUtil
import wuhan.science.server.util.view.ViewUtil
import wuhan.science.server.view.AppActivity
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 活动通知 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/activity")
class ActivityController {
    @Resource
    lateinit var iActivityService: IActivityService

    @Resource
    lateinit var iScienceBaseService: IScienceBaseService

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostActivity): ResultBean {
        iActivityService.getOne(KtQueryWrapper(Activity::class.java).eq(Activity::activityName, form.activityName))
            ?.let {
                return ResultUtil.error("活动名称重复")
            } ?: let {
            val insert = Activity()
            BeanUtil.copyProperties(form, insert)
            iActivityService.save(insert).takeIf { it }?.let {
                return ResultUtil.ok("新增活动成功")
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
        val result = mutableListOf<WebActivity>()
        val page = Page<Activity>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<Activity>(queryParam)
        val count = iActivityService.count(wrapper)

        iActivityService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebActivity()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            iScienceBaseService.getById(it.baseId)?.let { base ->
                item.baseName = base.baseName
            }
            it.coverAccessKey?.let { key ->
                item.coverAccessKeyUrl = CDNUtil.generateUrl(key)
            }
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iActivityService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutActivity): ResultBean {
        val update = Activity()
        BeanUtil.copyProperties(form, update)
        iActivityService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebActivity>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebActivity>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebActivity>()
        iActivityService.list(KtQueryWrapper(Activity::class.java).select()).mapTo(result) {
            val item = WebActivity()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/detail/{id}"])
    fun newsDetailById4App(@PathVariable id: String): ResultBean {
        val byId = iActivityService.getById(id)

        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val appActivity = AppActivity()
            BeanUtil.copyProperties(byId, appActivity)
            appActivity.updatedTime = DateUtil.yyyyMMddHHmm(byId.updatedTime)
            appActivity.coverAccessKeyUrl = byId.coverAccessKey?.let { key -> CDNUtil.generateUrl(key) }

            return ResultUtil.ok(appActivity)
        }
        return ResultUtil.error()
    }

}
