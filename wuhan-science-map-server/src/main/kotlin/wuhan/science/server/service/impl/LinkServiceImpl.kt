package wuhan.science.server.service.impl;

import wuhan.science.server.entity.Link;
import wuhan.science.server.mapper.LinkMapper;
import wuhan.science.server.service.ILinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 友情链接 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class LinkServiceImpl : ServiceImpl<LinkMapper, Link>(), ILinkService {

}
