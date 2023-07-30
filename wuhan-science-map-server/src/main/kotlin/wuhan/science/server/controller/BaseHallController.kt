package wuhan.science.server.controller;

import cn.hutool.core.bean.BeanUtil
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
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 科普基地展厅 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/baseHall")
class BaseHallController {
    @Resource
    lateinit var iBaseHallService: IBaseHallService

    @Resource
    lateinit var iScienceBaseService: IScienceBaseService

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostBaseHall): ResultBean {
        val insert = BaseHall()
        BeanUtil.copyProperties(form, insert)
        iBaseHallService.save(insert).takeIf { it }?.let {
            return ResultUtil.ok("新增展厅成功")
        }
        return ResultUtil.error("参数错误")
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms/{current}/{limit}"])
    fun list4Cms(
        @PathVariable @DecimalMin("1") current: Number,
        @PathVariable @DecimalMin("1") limit: Number,
        @RequestBody queryParam: QueryParam
    ): ResultBean {
        val result = mutableListOf<WebBaseHall>()
        val page = Page<BaseHall>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<BaseHall>(queryParam)
        val count = iBaseHallService.count(wrapper)

        iBaseHallService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebBaseHall()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item.hallLogoUrl = it.hallLogo?.let { key -> CDNUtil.generateUrl(key) }
            item.baseName = iScienceBaseService.getById(it.baseId)?.baseName ?: "对象不存在"
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iBaseHallService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutBaseHall): ResultBean {
        val update = BaseHall()
        BeanUtil.copyProperties(form, update)
        iBaseHallService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebBaseHall>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebBaseHall>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebBaseHall>()
        iBaseHallService.list(KtQueryWrapper(BaseHall::class.java).select()).mapTo(result) {
            val item = WebBaseHall()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/detail/{id}"])
    fun lecturerDetailById4App(@PathVariable id: String): ResultBean {
        val byId = iBaseHallService.getById(id)
        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val hall = AppBaseHall()
            BeanUtil.copyProperties(byId, hall,
                "baseId",
                "createdTime",
                "updatedTime",
                "status",
            )
            hall.hallLogoUrl = byId.hallLogo?.let { key -> CDNUtil.generateUrl(key) }
            return ResultUtil.ok(hall)
        }
        return ResultUtil.error()
    }
}
