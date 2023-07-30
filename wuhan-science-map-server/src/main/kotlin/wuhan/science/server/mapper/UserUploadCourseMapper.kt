package wuhan.science.server.mapper;

import wuhan.science.server.entity.UserUploadCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户课程上传资料 Mapper 接口
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Mapper
interface UserUploadCourseMapper : BaseMapper<UserUploadCourse>
