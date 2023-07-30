package wuhan.science.server.service.impl;

import wuhan.science.server.entity.BaseHall;
import wuhan.science.server.mapper.BaseHallMapper;
import wuhan.science.server.service.IBaseHallService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 科普基地展厅 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class BaseHallServiceImpl : ServiceImpl<BaseHallMapper, BaseHall>(), IBaseHallService {

}
