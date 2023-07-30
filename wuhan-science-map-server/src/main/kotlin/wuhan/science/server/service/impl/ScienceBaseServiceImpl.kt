package wuhan.science.server.service.impl;

import wuhan.science.server.entity.ScienceBase;
import wuhan.science.server.mapper.ScienceBaseMapper;
import wuhan.science.server.service.IScienceBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 科普基地 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class ScienceBaseServiceImpl : ServiceImpl<ScienceBaseMapper, ScienceBase>(), IScienceBaseService {

}
