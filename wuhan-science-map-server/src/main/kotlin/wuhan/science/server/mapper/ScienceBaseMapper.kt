package wuhan.science.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import wuhan.science.server.entity.ScienceBase

/**
 * <p>
 * 科普基地 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface ScienceBaseMapper : BaseMapper<ScienceBase> {
    fun text2Geometry(text: String): String
}
