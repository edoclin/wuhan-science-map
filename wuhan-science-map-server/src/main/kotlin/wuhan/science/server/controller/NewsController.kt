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
import wuhan.science.server.util.system.Configuration
import wuhan.science.server.util.tencent.cdn.CDNUtil
import wuhan.science.server.util.view.*
import wuhan.science.server.view.*
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 热点资讯 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/news")
class NewsController {
    @Resource
    lateinit var iNewsService: INewsService

    @Resource
    lateinit var customConfiguration: Configuration

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostNews): ResultBean {
        iNewsService.getOne(KtQueryWrapper(News::class.java).eq(News::newsTitle, form.newsTitle))?.let {
            return ResultUtil.error("新闻标题重复")
        } ?: let {
            val insert = News()
            BeanUtil.copyProperties(form, insert)
            iNewsService.save(insert).takeIf { it }?.let {
                return ResultUtil.ok("发布资讯成功")
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
        val result = mutableListOf<WebNews>()
        val page = Page<News>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<News>(queryParam)
        val count = iNewsService.count(wrapper)

        iNewsService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebNews()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            it.coverAccessKey?.let { key ->
                item.coverAccessKeyUrl = CDNUtil.generateUrl(key)
            }
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iNewsService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutNews): ResultBean {
        val update = News()
        BeanUtil.copyProperties(form, update)
        iNewsService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebNews>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebNews>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebNews>()
        iNewsService.list(KtQueryWrapper(News::class.java).select()).mapTo(result) {
            val item = WebNews()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/displayIndex"])
    fun listDisplayIndex4Cms(): ResultBean {
        val result = mutableListOf<WebNews>()
        iNewsService.list(
            KtQueryWrapper(News::class.java).eq(News::top, true).orderByAsc(News::newsType)
                .orderByAsc(News::displayIndex)
        ).mapTo(result) {
            val item = WebNews()
            item.id = it.id
            item.newsTitle = it.newsTitle
            item.newsType = it.newsType
            item.displayIndex = it.displayIndex
            item.createdTime = DateUtil.yyyyMMddHHmm(it.createdTime)
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["/cms/changeIndex"])
    fun changeIndex4Cms(@RequestBody batch: List<News>): ResultBean {
        takeIf { iNewsService.updateBatchById(batch) }?.let {
            return ResultUtil.ok("展示顺序修改成功")
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/swiper"])
    fun listSwiper4App(): ResultBean {
        val result = mutableListOf<AppNews>()
        iNewsService.list(KtQueryWrapper(News::class.java).eq(News::carousel, true).ne(News::status, Status.BANNED))
            .mapTo(result) {
                val appNews = AppNews()
                appNews.newsTitle = it.newsTitle
                appNews.title = it.newsTitle
                appNews.id = it.id
                appNews.coverAccessKeyUrl = it.coverAccessKey?.let { key -> CDNUtil.generateUrl(key) }
                appNews.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
                appNews
            }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/list/info/{type}/{current}/{limit}"])
    fun listNewsInfo4App(@PathVariable type: Int, @PathVariable current: Long, @PathVariable limit: Long): ResultBean {
        val result = mutableListOf<AppNews>()
        val page = Page<News>(current, limit)
        var wrapper = KtQueryWrapper(News::class.java).eq(News::top, false).orderByDesc(News::updatedTime)

        iNewsService.page(page, wrapper).records.mapTo(result) {
            val appNews = AppNews()
            BeanUtil.copyProperties(it, appNews)
            appNews.coverAccessKeyUrl = it.coverAccessKey?.let { key -> CDNUtil.generateUrl(key) }
            appNews.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            appNews
        }
        return ResultUtil.ok(result, result.size)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/list/info/top/{type}"])
    fun listNewsInfoTop4App(@PathVariable type: Int): ResultBean {
        val result = mutableListOf<AppNews>()
        var wrapper = KtQueryWrapper(News::class.java).eq(News::top, true)

        if (type != -1) {
            wrapper = wrapper.eq(News::newsType, customConfiguration.newsTypes[type])
        }

        wrapper = wrapper.orderByAsc(News::displayIndex)
        iNewsService.list(wrapper).mapTo(result) {
            val item = AppNews()
            BeanUtil.copyProperties(it, item)
            item.coverAccessKeyUrl = it.coverAccessKey?.let { key -> CDNUtil.generateUrl(key) }
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/detail/{id}"])
    fun newsDetailById4App(@PathVariable id: String): ResultBean {
        val byId = iNewsService.getById(id)

        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val appNews = AppNews()
            BeanUtil.copyProperties(byId, appNews)
            appNews.coverAccessKeyUrl = byId.coverAccessKey?.let { key -> CDNUtil.generateUrl(key) }
            appNews.updatedTime = DateUtil.yyyyMMddHHmm(byId.updatedTime)
            return ResultUtil.ok(appNews)
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/search/{keyword}"])
    fun searchNewsByKeyword4App(@PathVariable keyword: String): ResultBean {
        val result = mutableListOf<AppNews>()
        iNewsService.list(KtQueryWrapper(News::class.java).like(News::newsTitle, keyword)).mapTo(result) {
            val appNews = AppNews()
            appNews.newsTitle = it.newsTitle
            appNews.title = it.newsTitle
            appNews.id = it.id
            appNews.coverAccessKeyUrl = it.coverAccessKey?.let { key -> CDNUtil.generateUrl(key) }
            appNews.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            appNews.newsType = it.newsType
            appNews
        }
        return ResultUtil.ok(result)
    }

}
