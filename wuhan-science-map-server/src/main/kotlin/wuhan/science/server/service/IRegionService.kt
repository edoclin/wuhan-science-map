package wuhan.science.server.service;

import com.baomidou.mybatisplus.extension.service.IService
import wuhan.science.server.entity.Region

/**
 * <p>
 * 地区 服务类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
interface IRegionService : IService<Region> {
    fun listRegionAndChildren(code: Int): List<wuhan.science.server.view.common.Region>
}
