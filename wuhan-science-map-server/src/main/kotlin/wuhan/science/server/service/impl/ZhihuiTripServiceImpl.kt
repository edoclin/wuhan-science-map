package wuhan.science.server.service.impl;

import wuhan.science.server.entity.ZhihuiTrip;
import wuhan.science.server.mapper.ZhihuiTripMapper;
import wuhan.science.server.service.IZhihuiTripService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 智慧行 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-03-29
 */
@Service
open class ZhihuiTripServiceImpl : ServiceImpl<ZhihuiTripMapper, ZhihuiTrip>(), IZhihuiTripService {

}
