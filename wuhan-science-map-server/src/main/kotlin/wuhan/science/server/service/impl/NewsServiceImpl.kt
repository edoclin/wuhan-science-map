package wuhan.science.server.service.impl;

import wuhan.science.server.entity.News;
import wuhan.science.server.mapper.NewsMapper;
import wuhan.science.server.service.INewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 热点资讯 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class NewsServiceImpl : ServiceImpl<NewsMapper, News>(), INewsService {

}
