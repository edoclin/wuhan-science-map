package wuhan.science.server.util.region

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import org.springframework.stereotype.Component
import wuhan.science.server.entity.Region
import wuhan.science.server.service.IRegionService
import wuhan.science.server.view.common.RegionOffspring
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.Resource

@Component
class RegionUtil {

    @Resource
    private lateinit var iRegionService: IRegionService

    companion object {
        val offspringCache: ConcurrentHashMap<Int, List<RegionOffspring>> = ConcurrentHashMap()
    }

    fun getRegionCodesByCode(regionCode: Int): RegionInfo {
        val result = RegionInfo()
        val regions = Stack<String>()
        val regionCodes = Stack<Int>()
        var regionNameStr = ""

        var region = iRegionService.getById(regionCode)

        while (BeanUtil.isNotEmpty(region)) {
            regions.push(region.regionName)
            regionCodes.push(region.code)
            region = iRegionService.getById(region.parentCode)
        }

        val codes = mutableListOf<Int>()
        while (regions.isNotEmpty()) {
            regionNameStr += regions.pop()
            codes.add(regionCodes.pop())
        }
        result.regionCodes = codes.toList()
        result.name = regionNameStr
        return result
    }


    fun queryOffspringCodes(code: Int?, name: String?): List<RegionOffspring> {
        takeIf { code != null }?.let {
            val regionOffsprings = offspringCache[code]

            takeIf { CollUtil.isNotEmpty(regionOffsprings) }?.let {
                if (regionOffsprings != null) {
                    return regionOffsprings
                }
            }

        }
        val children = iRegionService.list(KtQueryWrapper(Region::class.java).eq(Region::parentCode, code))
        val result = mutableListOf<RegionOffspring>()
        takeIf { CollUtil.isNotEmpty(children) }?.let {
            children.forEach { child ->
                result.addAll(recursionQueryOffspringCodes(child.code, child.regionName))
            }
        }
        val regionOffspring = RegionOffspring()
        regionOffspring.regionName = name
        regionOffspring.code = code
        result.add(regionOffspring)
        if (code != null) {
            offspringCache[code] = result
        }
        return result
    }

    fun recursionQueryOffspringCodes(code: Int?, name: String?): List<RegionOffspring> {
        val children = iRegionService.list(KtQueryWrapper(Region::class.java).eq(Region::parentCode, code))
        val result = mutableListOf<RegionOffspring>()
        takeIf { CollUtil.isNotEmpty(children) }?.let {
            children.forEach { child ->
                result.addAll(recursionQueryOffspringCodes(child.code, child.regionName))
            }
        }
        val regionOffspring = RegionOffspring()
        regionOffspring.regionName = name
        regionOffspring.code = code
        result.add(regionOffspring)
        return result
    }
}