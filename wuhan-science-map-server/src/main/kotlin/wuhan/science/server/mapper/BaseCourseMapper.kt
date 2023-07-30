package wuhan.science.server.mapper;

import wuhan.science.server.entity.BaseCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 科普基地课程 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface BaseCourseMapper : BaseMapper<BaseCourse>
