package wuhan.science.server.service.impl;

import wuhan.science.server.entity.ZhihuiTripItem;
import wuhan.science.server.mapper.ZhihuiTripItemMapper;
import wuhan.science.server.service.IZhihuiTripItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 智慧行item 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-03-29
 */
@Service
open class ZhihuiTripItemServiceImpl : ServiceImpl<ZhihuiTripItemMapper, ZhihuiTripItem>(), IZhihuiTripItemService {

}
