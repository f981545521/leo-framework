package cn.acyou.leo.tool.service.impl;

import cn.acyou.leo.tool.entity.TOrder;
import cn.acyou.leo.tool.mapper.TOrderMapper;
import cn.acyou.leo.tool.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

}
