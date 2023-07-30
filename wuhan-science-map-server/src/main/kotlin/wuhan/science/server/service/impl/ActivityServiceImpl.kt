package wuhan.science.server.service.impl;

import wuhan.science.server.entity.Activity;
import wuhan.science.server.mapper.ActivityMapper;
import wuhan.science.server.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动通知 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class ActivityServiceImpl : ServiceImpl<ActivityMapper, Activity>(), IActivityService {

}
