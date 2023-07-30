package wuhan.science.server.mapper;

import wuhan.science.server.entity.UserRegisterCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户报名课程 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface UserRegisterCourseMapper : BaseMapper<UserRegisterCourse>
