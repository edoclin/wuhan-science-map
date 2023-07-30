package wuhan.science.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import wuhan.science.server.entity.Region

/**
 * <p>
 * 地区 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface RegionMapper : BaseMapper<Region> {
    fun listRegionAndChildren(code: Int): List<wuhan.science.server.view.common.Region>
}
