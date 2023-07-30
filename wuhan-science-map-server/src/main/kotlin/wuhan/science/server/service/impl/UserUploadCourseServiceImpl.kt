package wuhan.science.server.service.impl;

import wuhan.science.server.entity.UserUploadCourse;
import wuhan.science.server.mapper.UserUploadCourseMapper;
import wuhan.science.server.service.IUserUploadCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户课程上传资料 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class UserUploadCourseServiceImpl : ServiceImpl<UserUploadCourseMapper, UserUploadCourse>(), IUserUploadCourseService {

}
