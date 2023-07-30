package wuhan.science.server.controller;

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.db.nosql.redis.RedisDS
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import redis.clients.jedis.Jedis
import wuhan.science.server.entity.*
import wuhan.science.server.service.*
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
import java.util.*
import javax.annotation.Resource
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

/**
 * <p>
 * 智慧行item 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-03-29
 */
@RestController
@RequestMapping("/server/zhihuiTripItem")
class ZhihuiTripItemController {
    @Resource
    lateinit var iZhihuiTripItemService: IZhihuiTripItemService

    @Resource
    lateinit var iZhihuiTripService: IZhihuiTripService

    @Resource
    lateinit var iBaseCourseService: IBaseCourseService


    @Resource
    lateinit var iUserRegisterCourseService: IUserRegisterCourseService

    @Resource
    lateinit var iUserFavoriteService: IUserFavoriteCourseService

    @Resource
    lateinit var iUSerService: IUserService

    val jedis: Jedis = RedisDS.create().jedis

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostZhihuiTripItem): ResultBean {
        iZhihuiTripItemService.getOne(
            KtQueryWrapper(ZhihuiTripItem::class.java).eq(
                ZhihuiTripItem::itemName,
                form.itemName
            )
        )?.let {
            // todo: message
            return ResultUtil.error("添加失败，请联系管理员！")
        } ?: let {
            val insert = ZhihuiTripItem()
            BeanUtil.copyProperties(form, insert)

            iZhihuiTripItemService.save(insert).takeIf { it }?.let {
                // todo: message
                jedis.set(insert.id, "40")
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
        val result = mutableListOf<WebZhihuiTripItem>()
        val page = Page<ZhihuiTripItem>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<ZhihuiTripItem>(queryParam)
        val count = iZhihuiTripItemService.count(wrapper)

        iZhihuiTripItemService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebZhihuiTripItem()
            BeanUtil.copyProperties(it, item)
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item.accessUrl = it.accessKey?.let { key -> CDNUtil.generateUrl(key) }
            item.tripName = iZhihuiTripService.getById(it.tripId)?.tripName ?: "关联基地失效"
            item.courseName = iBaseCourseService.getById(it.courseId)?.courseName ?: "关联课程失效"
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iZhihuiTripItemService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutZhihuiTripItem): ResultBean {
        val update = ZhihuiTripItem()
        BeanUtil.copyProperties(form, update)
        iZhihuiTripItemService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebZhihuiTripItem>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebZhihuiTripItem>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebZhihuiTripItem>()
        iZhihuiTripItemService.list(KtQueryWrapper(ZhihuiTripItem::class.java).select()).mapTo(result) {
            val item = WebZhihuiTripItem()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }


    @RequestMapping(method = [RequestMethod.GET], path = ["/app/tripList/{id}"])
    fun zhihuiTripList4App(@PathVariable id: String): ResultBean {
        val byId = iZhihuiTripService.getById(id)
        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val result = mutableListOf<AppZhihuiTripItem>()
            iZhihuiTripItemService.list(
                KtQueryWrapper(ZhihuiTripItem::class.java).eq(ZhihuiTripItem::tripId, id)
                    .orderByAsc(ZhihuiTripItem::displayIndex)
                    .ne(ZhihuiTripItem::status, Status.BANNED)
            ).mapTo(result) { zhihuiTripItem ->
                val appZhihuiTripItem = AppZhihuiTripItem()
                BeanUtil.copyProperties(
                    zhihuiTripItem, appZhihuiTripItem, "descRichText", "status", "createdTime", "updatedTime", "creator"
                )
                appZhihuiTripItem.itemName = zhihuiTripItem.itemName
                appZhihuiTripItem.accessKeyUrl = zhihuiTripItem.accessKey?.let { key -> CDNUtil.generateUrl(key) }
                appZhihuiTripItem.descSimple = zhihuiTripItem.descSimple
                appZhihuiTripItem
            }
            return ResultUtil.ok(result)
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/detailItem/{id}"])
    fun zhihuiTripItemDetail4App(@PathVariable id: String): ResultBean {
        val byId = iZhihuiTripItemService.getById(id)
        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val appZhihuiTripItem = AppZhihuiTripItem()
            BeanUtil.copyProperties(
                byId, appZhihuiTripItem, "status", "createdTime", "updatedTime", "creator"
            )
            appZhihuiTripItem.itemName = byId.itemName
            appZhihuiTripItem.accessKeyUrl = byId.accessKey?.let { key -> CDNUtil.generateUrl(key) }
            appZhihuiTripItem.descSimple = byId.descSimple

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
                appZhihuiTripItem.registered = BeanUtil.isNotEmpty(userRegisterCourse)
                appZhihuiTripItem.favored = BeanUtil.isNotEmpty(userFavoriteCourse)
            }
            return ResultUtil.ok(appZhihuiTripItem)
        }
        return ResultUtil.error()
    }
    
    @RequestMapping(method = [RequestMethod.GET], path = ["/app/{itemId}"])
    fun checkStock(@PathVariable itemId: String): ResultBean {
        val key = StpUtil.getLoginIdAsString() + "-" + itemId
        if (jedis.exists(key)){
            jedis.expire(key, 60*10)
            return ResultUtil.ok()
        }

        // 检查库存
        val stock = jedis.get(itemId)
        if (stock.toInt() == 0){
            return ResultUtil.error("活动名额已满,敬请期待下次！")
        }
        // 修改库存
        jedis.decr(itemId)
        // 过期释放库存
        jedis.set(key,"usingTimeLine")
        jedis.expire(key, 60*10)

        return ResultUtil.ok()
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms/registerActive"])
    fun registerActive(@RequestBody @Valid form: WebPostZhihuiTripItemRegister ): ResultBean {

        iZhihuiTripItemService.getOne(
            KtQueryWrapper(ZhihuiTripItem::class.java).eq(
                ZhihuiTripItem::itemName,
                form.itemName
            )
        )?.let {
            // todo: message
            return ResultUtil.error("添加失败，请联系管理员！")
        } ?: let {
            val insert = ZhihuiTripItem()
            BeanUtil.copyProperties(form, insert)

            iZhihuiTripItemService.save(insert).takeIf { it }?.let {
                // todo: message
                jedis.set(insert.id, "40")
                return ResultUtil.ok("添加成功")
            }
            return ResultUtil.error("参数错误")
        }

        return ResultUtil.ok()
    }

        
}
