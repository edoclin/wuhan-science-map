package wuhan.science.server.service.impl;

import wuhan.science.server.entity.User;
import wuhan.science.server.mapper.UserMapper;
import wuhan.science.server.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class UserServiceImpl : ServiceImpl<UserMapper, User>(), IUserService {

}
