package wuhan.science.server.mapper;

import wuhan.science.server.entity.BaseLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 基地关联友情链接 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface BaseLinkMapper : BaseMapper<BaseLink>
