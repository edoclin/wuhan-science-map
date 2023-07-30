package wuhan.science.server.controller;

import cn.hutool.core.bean.BeanUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.*
import wuhan.science.server.service.IZhihuiTripService
import wuhan.science.server.util.database.Batch
import wuhan.science.server.util.database.QueryParam
import wuhan.science.server.util.database.QueryUtil
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.date.DateUtil
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.tencent.cdn.CDNUtil
import wuhan.science.server.util.view.ViewUtil
import wuhan.science.server.view.*
import java.time.ZoneOffset
import java.util.*
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 智慧行 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-03-29
 */
@RestController
@RequestMapping("/server/zhihuiTrip")
class ZhihuiTripController {
    @Resource
    lateinit var iZhihuiTripService: IZhihuiTripService

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostZhihuiTrip): ResultBean {
        iZhihuiTripService.getOne(KtQueryWrapper(ZhihuiTrip::class.java).eq(ZhihuiTrip::tripName, form.tripName))?.let {
            // todo: message
            return ResultUtil.error("添加失败，请联系管理员！")
        } ?: let {
            val insert = ZhihuiTrip()
            BeanUtil.copyProperties(form, insert)
            iZhihuiTripService.save(insert).takeIf { it }?.let {
                // todo: message
                return ResultUtil.ok("添加成功")
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
        val result = mutableListOf<WebZhihuiTrip>()
        val page = Page<ZhihuiTrip>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<ZhihuiTrip>(queryParam)
        val count = iZhihuiTripService.count(wrapper)

        iZhihuiTripService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebZhihuiTrip()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item.startingTime = DateUtil.yyyyMMddHHmm(it.startingTime)
            item.startingTimestamp = it.startingTime?.toInstant(ZoneOffset.ofHours(8))?.toEpochMilli()
            item.accessUrl = it.accessKey?.let { key -> CDNUtil.generateUrl(key) }
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iZhihuiTripService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
             // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutZhihuiTrip): ResultBean {
        val update = ZhihuiTrip()
        BeanUtil.copyProperties(form, update)
        iZhihuiTripService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebZhihuiTrip>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebZhihuiTrip>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebZhihuiTrip>()
        iZhihuiTripService.list(KtQueryWrapper(ZhihuiTrip::class.java).select()).mapTo(result) {
            val item = WebZhihuiTrip()
            // todo: action
            item.id = it.id
            item.tripName = it.tripName
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/trip/{top}"])
    fun zhihuiTrip4App(@PathVariable top: Boolean): ResultBean {
        val result = mutableListOf<AppZhihuiTrip>()

        iZhihuiTripService.list(KtQueryWrapper(ZhihuiTrip::class.java).orderByAsc(ZhihuiTrip::displayIndex)
            .ne(ZhihuiTrip::status,Status.BANNED)).mapTo(result) {zhihuiTrip ->
            val appZhihuiTrip = AppZhihuiTrip()
            BeanUtil.copyProperties(
                zhihuiTrip, appZhihuiTrip, "descRichText", "status", "createdTime", "updatedTime", "creator"
            )
            appZhihuiTrip.tripName = zhihuiTrip.tripName
            appZhihuiTrip.accessKeyUrl = zhihuiTrip.accessKey?.let { key -> CDNUtil.generateUrl(key) }
            appZhihuiTrip.descSimple = zhihuiTrip.descSimple
            appZhihuiTrip.startingTime = DateUtil.yyyyMMdd(zhihuiTrip.startingTime)
            appZhihuiTrip
        }
        if (top && result.isNotEmpty()){
            return ResultUtil.ok(result.take(1).toTypedArray())
        }
        return ResultUtil.ok(result)
    }

}


