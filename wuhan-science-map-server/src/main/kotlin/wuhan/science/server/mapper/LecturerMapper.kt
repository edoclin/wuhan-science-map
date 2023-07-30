package wuhan.science.server.mapper;

import wuhan.science.server.entity.Lecturer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 基地科普讲师 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface LecturerMapper : BaseMapper<Lecturer>
