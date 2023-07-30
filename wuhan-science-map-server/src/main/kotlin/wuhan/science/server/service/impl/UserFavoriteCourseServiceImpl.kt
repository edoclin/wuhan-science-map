package wuhan.science.server.service.impl;

import wuhan.science.server.entity.UserFavoriteCourse;
import wuhan.science.server.mapper.UserFavoriteCourseMapper;
import wuhan.science.server.service.IUserFavoriteCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏课程 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class UserFavoriteCourseServiceImpl : ServiceImpl<UserFavoriteCourseMapper, UserFavoriteCourse>(), IUserFavoriteCourseService {

}
