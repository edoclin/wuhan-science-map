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
 * 基地科普讲师 前端控制器
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/server/lecturer")
class LecturerController {
    @Resource
    lateinit var iLecturerService: ILecturerService

    @Resource
    lateinit var iScienceBaseService: IScienceBaseService

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
    fun post4Cms(@RequestBody @Valid form: WebPostLecturer): ResultBean {

        val insert = Lecturer()
        BeanUtil.copyProperties(form, insert)
        iLecturerService.save(insert).takeIf { it }?.let {
            return ResultUtil.ok("新增讲师成功")
        }
        return ResultUtil.error("参数错误")
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/cms/{current}/{limit}"])
    fun list4Cms(
        @PathVariable @DecimalMin("1") current: Number,
        @PathVariable @DecimalMin("1") limit: Number,
        @RequestBody queryParam: QueryParam
    ): ResultBean {
        val result = mutableListOf<WebLecturer>()
        val page = Page<Lecturer>(current.toLong(), limit.toLong())
        val wrapper = QueryUtil.buildQueryWrapper<Lecturer>(queryParam)
        val count = iLecturerService.count(wrapper)

        iLecturerService.page(
            page, QueryUtil.orderBy(wrapper, queryParam)
        ).records.mapTo(result) {
            val item = WebLecturer()
            BeanUtil.copyProperties(it, item)
            iScienceBaseService.getById(it.baseId)?.let { scienceBase ->
                item.baseName = scienceBase.baseName
            }
            item.status = it.status?.desc
            item.lecturerPictureUrl = it.lecturerPicture?.let { key -> CDNUtil.generateUrl(key) }
            item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
            item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
            item
        }
        return ResultUtil.ok(result, count)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
    fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
        iLecturerService.removeBatchByIds(batch.ids).takeIf { it }?.let {
            batch.ids?.forEach { id ->
                // todo: 级联删除
            }
            return ResultUtil.ok("删除成功")
        }
        return ResultUtil.error("参数无效")
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
    fun update4Cms(@RequestBody @Valid form: WebPutLecturer): ResultBean {
        val update = Lecturer()
        BeanUtil.copyProperties(form, update)
        iLecturerService.updateById(update).takeIf { it }?.let {
            return ResultUtil.ok("更新成功")
        }
        return ResultUtil.error("对象不存在")
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
    fun conditions4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.queryCondition<WebLecturer>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
    fun tableColumns4Cms(): ResultBean {
        return ResultUtil.ok(ViewUtil.tableColumns<WebLecturer>())
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
    fun listSelector4Cms(): ResultBean {
        val result = mutableListOf<WebLecturer>()
        iLecturerService.list(KtQueryWrapper(Lecturer::class.java).select()).mapTo(result) {
            val item = WebLecturer()
            // todo: action
            item
        }
        return ResultUtil.ok(result)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/app/detail/{id}"])
    fun lecturerDetailById4App(@PathVariable id: String): ResultBean {
        val byId = iLecturerService.getById(id)
        takeIf { BeanUtil.isNotEmpty(byId) }?.let {
            val appLecturer = AppLecturer()
            BeanUtil.copyProperties(byId, appLecturer,
                "baseId",
                "createdTime",
                "updatedTime",
                "status",
            )
            appLecturer.lecturerPicture = byId.lecturerPicture?.let { key -> CDNUtil.generateUrl(key) }
            return ResultUtil.ok(appLecturer)
        }
        return ResultUtil.error()
    }


}
