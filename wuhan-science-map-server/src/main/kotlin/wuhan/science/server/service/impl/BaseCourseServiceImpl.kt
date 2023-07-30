package wuhan.science.server.service.impl;

import wuhan.science.server.entity.BaseCourse;
import wuhan.science.server.mapper.BaseCourseMapper;
import wuhan.science.server.service.IBaseCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 科普基地课程 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class BaseCourseServiceImpl : ServiceImpl<BaseCourseMapper, BaseCourse>(), IBaseCourseService {

}
