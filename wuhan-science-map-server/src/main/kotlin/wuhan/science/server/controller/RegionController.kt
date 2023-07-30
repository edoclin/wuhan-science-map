package wuhan.science.server.controller;

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.Region
import wuhan.science.server.entity.WebPostRegion
import wuhan.science.server.entity.WebPutRegion
import wuhan.science.server.entity.WebRegion
import wuhan.science.server.service.IRegionService
import wuhan.science.server.util.database.Batch
import wuhan.science.server.util.database.QueryParam
import wuhan.science.server.util.database.QueryUtil
import wuhan.science.server.util.postgis.GISDataUtil
import wuhan.science.server.util.region.RegionUtil
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.view.ViewUtil
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 地区 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/region")
class RegionController {
    @Resource
    lateinit var iRegionService: IRegionService

    @Resource
    lateinit var regionUtil: RegionUtil

    @RequestMapping(method = [RequestMethod.GET], path = ["/listRegionAndChildren/{code}"])
    fun listRegionAndChildren(@PathVariable code: Int): ResultBean {
        return ResultUtil.ok(iRegionService.listRegionAndChildren(code))
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/centroid/{code}"])
    fun getPolygonCentroid(@PathVariable code: Int): ResultBean {
        val region = iRegionService.getById(code)
        takeIf { BeanUtil.isNotEmpty(region) }?.let {
            return ResultUtil.ok(GISDataUtil.ewkb2Point(region.polygonGeometry as String, true))
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostRegion): ResultBean {
        iRegionService.getOne(KtQueryWrapper(Region::class.java).eq(Region::code, form.code))?.let {
            // todo: message
            return ResultUtil.error("")
        } ?: let {
            val insert = Region()
            BeanUtil.copyProperties(form, insert)
            iRegionService.save(insert).takeIf { it }?.let {
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
        val result = mutableListOf<WebRegion>()
        val page = Page<Region>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<Region>(queryParam)
        val count = iRegionService.count(wrapper)

        iRegionService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebRegion()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iRegionService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutRegion): ResultBean {
        val update = Region()
        BeanUtil.copyProperties(form, update)
        iRegionService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebRegion>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebRegion>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebRegion>()
        iRegionService.list(KtQueryWrapper(Region::class.java).select()).mapTo(result) {
            val item = WebRegion()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }


    @RequestMapping(method = [RequestMethod.GET], path = ["/app/offspringCodeByParentCode/{code}/{isMap}"])
    fun listOffspringCodeByCode4App(@PathVariable code: Int?, @PathVariable isMap: Boolean): ResultBean {
        val region = iRegionService.getById(code)
        takeIf { BeanUtil.isNotEmpty(region) }?.let {

            var offspring = regionUtil.queryOffspringCodes(region.code, region.regionName)

            if (isMap) {
                val keys = mutableListOf<Int?>()
                val values = mutableListOf<String?>()

                offspring.forEach { item ->
                    keys.add(item.code)
                    values.add(item.regionName)
                }
                return ResultUtil.ok(CollUtil.zip(keys, values))
            }

            offspring = offspring.sortedBy { item -> item.code }
            return ResultUtil.ok(offspring)
        }
        return ResultUtil.error()
    }
}
