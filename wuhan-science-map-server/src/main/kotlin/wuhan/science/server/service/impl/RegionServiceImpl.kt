package wuhan.science.server.service.impl;

import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import net.postgis.jdbc.PGgeometry
import org.springframework.stereotype.Service
import wuhan.science.server.entity.Region
import wuhan.science.server.mapper.RegionMapper
import wuhan.science.server.service.IRegionService
import wuhan.science.server.util.postgis.GISDataUtil
import javax.annotation.Resource

/**
 * <p>
 * 地区 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class RegionServiceImpl : ServiceImpl<RegionMapper, Region>(), IRegionService {

    @Resource
    lateinit var regionMapper: RegionMapper
    override fun listRegionAndChildren(code: Int): List<wuhan.science.server.view.common.Region> {
        val listRegionAndChildren = regionMapper.listRegionAndChildren(code)
        takeIf { listRegionAndChildren.isNotEmpty() }?.let {
            listRegionAndChildren.forEach { region: wuhan.science.server.view.common.Region ->
                region.polygonGeometry = GISDataUtil.ewkb2MultiPolygon(region.polygonGeometry as String)
                recursionProcess(region.children)
            }
        }
        return listRegionAndChildren
    }

    fun recursionProcess(children: List<wuhan.science.server.view.common.Region>?) {
        takeIf { CollUtil.isNotEmpty(children) }?.let {
            children?.forEach { region: wuhan.science.server.view.common.Region ->
                region.polygonGeometry = GISDataUtil.ewkb2MultiPolygon(region.polygonGeometry as String)
                recursionProcess(region.children)
            }
        }
    }
}
