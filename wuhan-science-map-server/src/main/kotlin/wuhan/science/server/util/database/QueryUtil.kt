package wuhan.science.server.util.database

import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.date.LocalDateTimeUtil
import cn.hutool.core.map.MapUtil
import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.toolkit.Wrappers

object QueryUtil {

    fun <T> buildQueryWrapper(queryParam: QueryParam): QueryWrapper<T> {
        var wrapper = Wrappers.query<T>()

        queryParam.conditions.takeIf { CollectionUtil.isNotEmpty(it) }?.onEach { item ->

            if (StrUtil.equals(item.column, "status").and(StrUtil.isNotEmpty(item.value))) {
                wrapper = wrapper.eq(item.column, Status.valueOf(item.value).status)
            } else {
                if (!item.isAnd) {
                    wrapper = wrapper.or()
                }
                item.value.trim().lowercase().let { value ->
                    wrapper = if (StrUtil.equals(value, "true")) {
                        wrapper.eq(StrUtil.toUnderlineCase(item.column), true)
                    } else if (StrUtil.equals(value, "false")) {
                        wrapper.eq(StrUtil.toUnderlineCase(item.column), false)
                    } else {
                        wrapper.like(StrUtil.toUnderlineCase(item.column), item.value.trim())
                    }
                }
            }
        }
        val now = LocalDateTimeUtil.now()
        queryParam.createdBetween.takeIf { MapUtil.isNotEmpty(it) }?.let { item ->
            wrapper = wrapper.between(
                "created_time", item["start"]?.plusHours(8) ?: now, item["end"]?.plusHours(8) ?: now
            )
        }

        queryParam.updatedBetween.takeIf { MapUtil.isNotEmpty(it) }?.let { item ->
            wrapper = wrapper.between(
                "updated_time", item["start"]?.plusHours(8) ?: now, item["end"]?.plusHours(8) ?: now
            )
        }
        return wrapper
    }

    fun <T> orderBy(wrapper: QueryWrapper<T>, queryParam: QueryParam): QueryWrapper<T> {
        val columns = mutableListOf<String>()
        queryParam.orderColumns?.mapTo(columns) {
            StrUtil.toUnderlineCase(it)
        }
        return wrapper.orderBy(true, queryParam.isAsc, columns)
    }
}