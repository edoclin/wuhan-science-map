package wuhan.science.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import wuhan.science.server.entity.ZhihuiTripItem

/**
 * <p>
 * 智慧行item Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-03-29
 */
@Mapper
interface ZhihuiTripItemMapper : BaseMapper<ZhihuiTripItem>
