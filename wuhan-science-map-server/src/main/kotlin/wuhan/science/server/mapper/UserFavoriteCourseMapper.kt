package wuhan.science.server.mapper;

import wuhan.science.server.entity.UserFavoriteCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户收藏课程 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface UserFavoriteCourseMapper : BaseMapper<UserFavoriteCourse>
