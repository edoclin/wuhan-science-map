package wuhan.science.server.service.impl;

import wuhan.science.server.entity.Lecturer;
import wuhan.science.server.mapper.LecturerMapper;
import wuhan.science.server.service.ILecturerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基地科普讲师 服务实现类
 * </p>
 *
 * @author edoclin
 * @since 2023-02-18
 */
@Service
open class LecturerServiceImpl : ServiceImpl<LecturerMapper, Lecturer>(), ILecturerService {

}
