package wuhan.science.server.service.impl;

import wuhan.science.server.entity.UserRegisterCourse;
import wuhan.science.server.mapper.UserRegisterCourseMapper;
import wuhan.science.server.service.IUserRegisterCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户报名课程 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class UserRegisterCourseServiceImpl : ServiceImpl<UserRegisterCourseMapper, UserRegisterCourse>(), IUserRegisterCourseService {

}
