package wuhan.science.server.service.impl;

import wuhan.science.server.entity.BaseLink;
import wuhan.science.server.mapper.BaseLinkMapper;
import wuhan.science.server.service.IBaseLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基地关联友情链接 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class BaseLinkServiceImpl : ServiceImpl<BaseLinkMapper, BaseLink>(), IBaseLinkService {

}
