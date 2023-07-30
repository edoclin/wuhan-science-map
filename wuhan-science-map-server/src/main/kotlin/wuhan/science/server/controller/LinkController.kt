package wuhan.science.server.controller;

import cn.hutool.core.bean.BeanUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.Link
import wuhan.science.server.entity.WebLink
import wuhan.science.server.entity.WebPostLink
import wuhan.science.server.entity.WebPutLink
import wuhan.science.server.service.ILinkService
import wuhan.science.server.util.database.Batch
import wuhan.science.server.util.database.QueryParam
import wuhan.science.server.util.database.QueryUtil
import wuhan.science.server.util.date.DateUtil
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.tencent.cdn.CDNUtil
import wuhan.science.server.util.view.ViewUtil
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
/**
 * <p>
 * 友情链接 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/link")
class LinkController {
    @Resource
    lateinit var iLinkService: ILinkService

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostLink): ResultBean {
        iLinkService.getOne(KtQueryWrapper(Link::class.java).eq(Link::linkName, form.linkName))?.let {
            // todo: message
            return ResultUtil.error("链接名称重复")
        } ?: let {
            val insert = Link()
            BeanUtil.copyProperties(form, insert)
            iLinkService.save(insert).takeIf { it }?.let {
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
        val result = mutableListOf<WebLink>()
        val page = Page<Link>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<Link>(queryParam)
        val count = iLinkService.count(wrapper)

        iLinkService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebLink()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item.linkLogoUrl = it.linkLogo?.let { key -> CDNUtil.generateUrl(key) }
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iLinkService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
             // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutLink): ResultBean {
        val update = Link()
        BeanUtil.copyProperties(form, update)
        iLinkService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebLink>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebLink>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebLink>()
        iLinkService.list(KtQueryWrapper(Link::class.java).select()).mapTo(result) {
            val item = WebLink()
            item.linkName = it.linkName
            item.id = it.id
            item
        }
        return ResultUtil.ok(result)
    }
}
