package wuhan.science.server.controller;

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.web.bind.annotation.*
import wuhan.science.server.entity.*
import wuhan.science.server.service.*
import wuhan.science.server.util.database.Batch
import wuhan.science.server.util.database.QueryParam
import wuhan.science.server.util.database.QueryUtil
import wuhan.science.server.util.database.Status
import wuhan.science.server.util.date.DateUtil
import wuhan.science.server.util.postgis.GISDataUtil
import wuhan.science.server.util.region.RegionUtil
import wuhan.science.server.util.result.ResultBean
import wuhan.science.server.util.result.ResultUtil
import wuhan.science.server.util.system.Configuration
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
 * 科普基地 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/scienceBase")
class ScienceBaseController {
    @Resource
    lateinit var iScienceBaseService: IScienceBaseService

    @Resource
    lateinit var iRegionService: IRegionService

    @Resource
    lateinit var iLecturerService: ILecturerService

    @Resource
    lateinit var iBaseHallService: IBaseHallService

    @Resource
    lateinit var iLinkService: ILinkService

    @Resource
    lateinit var iBaseCourseService: IBaseCourseService

    @Resource
    lateinit var iBaseLinkService: IBaseLinkService

    @Resource
    lateinit var regionUtil: RegionUtil

    @Resource
    lateinit var customConfiguration: Configuration

    @Resource
    lateinit var iActivityService: IActivityService

    @Resource
    lateinit var regionController: RegionController

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostScienceBase): ResultBean {
        iScienceBaseService.getOne(KtQueryWrapper(ScienceBase::class.java).eq(ScienceBase::baseName, form.baseName))
            ?.let {
                return ResultUtil.error("基地名称重复")
            } ?: let {
            val insert = ScienceBase()
            BeanUtil.copyProperties(form, insert, "status")
            insert.status = form.status?.let { it1 -> Status.valueOf(it1) }
            insert.polygonGeometry = form.polygonGeometry?.let { list -> GISDataUtil.list2PolygonString(list) }
            iScienceBaseService.save(insert).takeIf { it }?.let {
                CollUtil.isNotEmpty(form.linkIds).takeIf { it }?.let {
                    val batch = mutableListOf<BaseLink>()
                    form.linkIds?.mapTo(batch) {
                        val baseLink = BaseLink()
                        baseLink.baseId = insert.id
                        baseLink.linkId = it
                        baseLink
                    }
                    iBaseLinkService.saveBatch(batch)
                }
                return ResultUtil.ok("新增基地成功")
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
        val result = mutableListOf<WebScienceBase>()
        val page = Page<ScienceBase>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<ScienceBase>(queryParam)
        val count = iScienceBaseService.count(wrapper)

        iScienceBaseService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebScienceBase()
            BeanUtil.copyProperties(it, item, "top", "carousel", "polygonGeometry", "regionCode")
            item.top = it.top
            item.carousel = it.carousel
            item.status = it.status?.desc
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item.baseLogoUrl = it.baseLogo?.let { key -> CDNUtil.generateUrl(key) }
            item.carouselImageAccessKeyUrl = it.carouselImageAccessKey?.let { key -> CDNUtil.generateUrl(key) }
            it.polygonGeometry?.let { polygon ->
                item.polygonGeometry = GISDataUtil.ewkb2Array(polygon)
                item.centroid = GISDataUtil.ewkb2Point(polygon, true)
            }
            val linkIds = mutableListOf<String?>()
            iBaseLinkService.list(KtQueryWrapper(BaseLink::class.java).eq(BaseLink::baseId, it.id))
                .mapTo(linkIds) { baseLink ->
                    baseLink.linkId
                }
            item.linkIds = linkIds.filterNotNull()

            it.regionCode?.let { code ->
                val regionInfo = regionUtil.getRegionCodesByCode(code)
                item.regionCodes = regionInfo.regionCodes
                item.regions = regionInfo.name
            }
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iScienceBaseService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutScienceBase): ResultBean {
        val update = ScienceBase()
        BeanUtil.copyProperties(form, update)

        update.polygonGeometry = form.polygonGeometry?.let { GISDataUtil.list2PolygonString(it) }

        iScienceBaseService.updateById(update).takeIf { it }?.let {
            iBaseLinkService.remove(KtQueryWrapper(BaseLink::class.java).eq(BaseLink::baseId, update.id))
            CollUtil.isNotEmpty(form.linkIds).takeIf { it }?.let {
                val batch = mutableListOf<BaseLink>()
                form.linkIds?.mapTo(batch) {
                    val baseLink = BaseLink()
                    baseLink.baseId = update.id
                    baseLink.linkId = it
                    baseLink
                }
                iBaseLinkService.saveBatch(batch)
            }
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebScienceBase>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebScienceBase>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebScienceBase>()
        iScienceBaseService.list(KtQueryWrapper(ScienceBase::class.java).select()).mapTo(result) {
            val item = WebScienceBase()
            item.id = it.id
            item.baseName = it.baseName
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/displayIndex"])
    fun listDisplayIndex4Cms(): ResultBean {
        val result = mutableListOf<WebScienceBase>()
        iScienceBaseService.list(
            KtQueryWrapper(ScienceBase::class.java).eq(ScienceBase::top, true).orderByAsc(ScienceBase::displayIndex)
        ).mapTo(result) {
            val webScienceBase = WebScienceBase()
            webScienceBase.id = it.id
            webScienceBase.baseName = it.baseName
            webScienceBase.displayIndex = it.displayIndex
            webScienceBase
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["/cms/changeIndex"])
    fun changeIndex4Cms(@RequestBody batch: List<ScienceBase>): ResultBean {
        takeIf { iScienceBaseService.updateBatchById(batch) }?.let {
            return ResultUtil.ok("展示顺序修改成功")
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/swiper"])
    fun listSwiper4App(): ResultBean {
        val result = mutableListOf<AppScienceBase>()
        iScienceBaseService.list(
            KtQueryWrapper(ScienceBase::class.java).eq(ScienceBase::carousel, true)
                .ne(ScienceBase::status, Status.BANNED)
        ).mapTo(result) {
            val item = AppScienceBase()
            item.baseName = it.baseName
            item.title = it.baseName
            item.baseType = it.baseType
            item.id = it.id
            item.carouselImageAccessKeyUrl = it.carouselImageAccessKey?.let { key -> CDNUtil.generateUrl(key) }
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/list/info/top/{type}/{regionCode}"])
    fun listBaseInfoTop4App(@PathVariable type: Int, @PathVariable regionCode: Int): ResultBean {
        val result = mutableListOf<AppScienceBase?>()
        var wrapper =
            KtQueryWrapper(ScienceBase::class.java).ne(ScienceBase::status, Status.BANNED).eq(ScienceBase::top, true)

        if (type != -1) {
            wrapper = wrapper.eq(ScienceBase::baseType, customConfiguration.baseTypes[type])
        }

        wrapper = wrapper.orderByAsc(ScienceBase::displayIndex)
        val region = iRegionService.getById(regionCode)
        val offspringResult = regionController.listOffspringCodeByCode4App(region.code, true)

        val offspring = offspringResult.data as Map<*, *>
        takeIf { BeanUtil.isNotEmpty(region) }?.let {
            iScienceBaseService.list(wrapper).mapTo(result) { base ->
                var item: AppScienceBase? = null
                takeIf { offspring.contains(base.regionCode) }?.let {
                    item = AppScienceBase()
                    item!!.baseName = base.baseName
                    item!!.baseType = base.baseType
                    item!!.id = base.id
                    item!!.regionCode = base.regionCode
                    item!!.detailAddress = base.detailAddress
                    item!!.baseLogoUrl = base.baseLogo?.let { key -> CDNUtil.generateUrl(key) }
                    item!!.updatedTime = DateUtil.yyyyMMddHHmm(base.updatedTime)
                }
                item
            }
            return ResultUtil.ok(result.filterNotNull())
        }
        return ResultUtil.error()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/list/info/{type}/{current}/{limit}"])
    fun listNewsInfo4App(@PathVariable type: Int, @PathVariable current: Long, @PathVariable limit: Long): ResultBean {
        val result = mutableListOf<AppScienceBase>()
        val page = Page<ScienceBase>(current, limit)
        var wrapper = KtQueryWrapper(ScienceBase::class.java).eq(ScienceBase::top, false)

        if (type != -1) {
            wrapper = wrapper.eq(ScienceBase::baseType, customConfiguration.baseTypes[type])

        }


        wrapper = wrapper.orderByDesc(ScienceBase::updatedTime)

        iScienceBaseService.page(page, wrapper).records.mapTo(result) {
            val item = AppScienceBase()
            val options = CopyOptions.create().ignoreError().ignoreNullValue()
            BeanUtil.copyProperties(it, item, options)
            item.baseLogoUrl = it.baseLogo?.let { key -> CDNUtil.generateUrl(key) }
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result, result.size)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/detail/{id}"])
    fun baseDetailById4App(@PathVariable id: String): ResultBean {
        val byId = iScienceBaseService.getById(id)
        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val appScienceBase = AppScienceBase()
            BeanUtil.copyProperties(
                byId, appScienceBase, "polygonGeometry", "status", "createdTime", "updatedTime"
            )
            appScienceBase.baseLogoUrl = byId.baseLogo?.let { key -> CDNUtil.generateUrl(key) }

            byId.polygonGeometry?.let { polygon ->
                appScienceBase.polygonGeometry = GISDataUtil.ewkb2Polygon4App(polygon)
                appScienceBase.centroid = GISDataUtil.ewkb2AppPoint(polygon, true)
            }
            val courses = mutableListOf<AppBaseCourse>()
            val lecturers = mutableListOf<AppLecturer>()
            val halls = mutableListOf<AppBaseHall>()
            val links = mutableListOf<AppLink?>()
            val activities = mutableListOf<AppActivity>()
            iBaseCourseService.list(KtQueryWrapper(BaseCourse::class.java).eq(BaseCourse::baseId, id))
                .mapTo(courses) { baseCourse ->
                    val appBaseCourse = AppBaseCourse()
                    BeanUtil.copyProperties(
                        baseCourse, appBaseCourse, "descRichText", "status", "createdTime", "updatedTime", "top"
                    )
                    appBaseCourse.targetAge = "${baseCourse.targetAge}岁以上"
                    appBaseCourse.startingTime = DateUtil.yyyyMMddHHmm(baseCourse.startingTime)
                    appBaseCourse
                }

            iLecturerService.list(KtQueryWrapper(Lecturer::class.java).eq(Lecturer::baseId, id))
                .mapTo(lecturers) { lecturer ->
                    val appLecturer = AppLecturer()
                    BeanUtil.copyProperties(
                        lecturer, appLecturer, "descRichText", "status", "createdTime", "updatedTime"
                    )
                    appLecturer.lecturerPicture = lecturer.lecturerPicture?.let { key -> CDNUtil.generateUrl(key) }
                    appLecturer
                }

            iBaseHallService.list(KtQueryWrapper(BaseHall::class.java).eq(BaseHall::baseId, id))
                .mapTo(halls) { baseHall ->
                    val appBaseHall = AppBaseHall()
                    BeanUtil.copyProperties(
                        baseHall, appBaseHall, "descRichText", "hallLogo", "status", "createdTime", "updatedTime"
                    )
                    appBaseHall.hallLogoUrl = baseHall.hallLogo?.let { key -> CDNUtil.generateUrl(key) }

                    appBaseHall
                }

            iBaseLinkService.list(KtQueryWrapper(BaseLink::class.java).eq(BaseLink::baseId, id))
                .mapTo(links) { baseLink ->
                    val appLink = AppLink()
                    val link = iLinkService.getById(baseLink.linkId)

                    takeIf { BeanUtil.isNotEmpty(link) }?.let {
                        BeanUtil.copyProperties(
                            link, appLink, "linkLogo", "status", "createdTime", "updatedTime"
                        )
                        appLink.linkLogoUrl = link.linkLogo?.let { key -> CDNUtil.generateUrl(key) }
                    }
                    appLink
                }

            iActivityService.list(KtQueryWrapper(Activity::class.java).eq(Activity::baseId, id))
                .mapTo(activities) { activity ->
                    val appActivity = AppActivity()
                    BeanUtil.copyProperties(
                        activity, appActivity,
                        "descRichText",
                        "createdTime",
                        "updatedTime",
                        "status",
                    )
                    appActivity.coverAccessKeyUrl = activity.coverAccessKey?.let { key -> CDNUtil.generateUrl(key) }
                    appActivity.updatedTime = DateUtil.yyyyMMddHHmm(activity.updatedTime)
                    appActivity
                }

            appScienceBase.courses = courses
            appScienceBase.lecturers = lecturers
            appScienceBase.halls = halls
            appScienceBase.links = links.filterNotNull()
            appScienceBase.activities = activities
            return ResultUtil.ok(appScienceBase)
        }
        return ResultUtil.error()
    }


    @RequestMapping(method = [RequestMethod.GET], path = ["/app/search/{keyword}/{regionCode}"])
    fun searchBaseByName4App(@PathVariable keyword: String, @PathVariable regionCode: Int): ResultBean {
        val region = iRegionService.getById(regionCode)
        val offspringResult = regionController.listOffspringCodeByCode4App(region.code, true)

        val offspring = offspringResult.data as Map<*, *>
        val result = mutableListOf<AppScienceBase?>()
        iScienceBaseService.list(KtQueryWrapper(ScienceBase::class.java).like(ScienceBase::baseName, keyword))
            .mapTo(result) { base ->
                var item: AppScienceBase? = null
                takeIf { offspring.contains(base.regionCode) }?.let {
                    item = AppScienceBase()
                    item!!.baseName = base.baseName
                    item!!.baseType = base.baseType
                    item!!.id = base.id
                    item!!.regionCode = base.regionCode
                    item!!.detailAddress = base.detailAddress
                    item!!.baseLogoUrl = base.baseLogo?.let { key -> CDNUtil.generateUrl(key) }
                    item!!.updatedTime = DateUtil.yyyyMMddHHmm(base.updatedTime)
                }
                item
            }
        return ResultUtil.ok(result.filterNotNull())
    }


}
