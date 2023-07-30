@RequestMapping(method = [RequestMethod.POST], path = ["/cms"])
fun post4Cms(@RequestBody @Valid form: WebPost$entity$): ResultBean {
    $service_bean$.getOne(KtQueryWrapper($entity$::class.java).eq($entity$::$field$, form.$field$))?.let {
        // todo: message
        return ResultUtil.error("")
    } ?: let {
        val insert = $entity$()
        BeanUtil.copyProperties(form, insert)
        $service_bean$.save(insert).takeIf { it }?.let {
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
    val result = mutableListOf<Web$entity$>()
    val page = Page<$entity$>(current.toLong(), limit.toLong())
    val wrapper = QueryUtil.buildQueryWrapper<$entity$>(queryParam)
    val count = $service_bean$.count(wrapper)

    $service_bean$.page(
        page, QueryUtil.orderBy(wrapper, queryParam)
    ).records.mapTo(result) {
        val item = Web$entity$()
        BeanUtil.copyProperties(it, item)
        item.status = it.status?.desc
        item.createdTime = DateUtil.yyyyMMdd(it.createdTime)
        item.updatedTime = DateUtil.yyyyMMddHHmm(it.updatedTime)
        item
    }
    return ResultUtil.ok(result, count)
}

@RequestMapping(method = [RequestMethod.DELETE], path = ["/cms/batch"])
fun deleteBatch4Cms(@RequestBody @NotBlank batch: Batch): ResultBean {
    $service_bean$.removeBatchByIds(batch.ids).takeIf { it }?.let {
        batch.ids?.forEach { id ->
         // todo: 级联删除
        }
        return ResultUtil.ok("删除成功")
    }
    return ResultUtil.error("参数无效")
}

@RequestMapping(method = [RequestMethod.PUT], path = ["cms"])
fun update4Cms(@RequestBody @Valid form: WebPut$entity$): ResultBean {
    val update = $entity$()
    BeanUtil.copyProperties(form, update)
    $service_bean$.updateById(update).takeIf { it }?.let {
        return ResultUtil.ok("更新成功")
    }
    return ResultUtil.error("对象不存在")
}

@RequestMapping(method = [RequestMethod.GET], path = ["/cms/conditions"])
fun conditions4Cms(): ResultBean {
    return ResultUtil.ok(ViewUtil.queryCondition<Web$entity$>())
}

@RequestMapping(method = [RequestMethod.GET], path = ["/cms/table-columns"])
fun tableColumns4Cms(): ResultBean {
    return ResultUtil.ok(ViewUtil.tableColumns<Web$entity$>())
}

@RequestMapping(method = [RequestMethod.GET], path = ["/cms/selector"])
fun listSelector4Cms(): ResultBean {
    val result = mutableListOf<Web$entity$>()
    $service_bean$.list(KtQueryWrapper($entity$::class.java).select()).mapTo(result) {
        val item = Web$entity$()
        // todo: action
        item
    }
    return ResultUtil.ok(result)
}