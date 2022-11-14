package com.example.demo.service.impl;

import com.example.demo.bean.PubSystemCode;
import com.example.demo.bean.PubSystemParams;
import com.example.demo.mapper.PubSystemCodeMapper;
import com.example.demo.service.PubSystemCodeService;
import com.example.demo.service.PubSystemParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MG02004
 * @createTime 2022/10/11 11:47
 * @description
 */
@Service
public class PubSystemCodeServiceImpl implements PubSystemCodeService {

    private static final Logger logger = LoggerFactory.getLogger(PubSystemCodeServiceImpl.class);

    @Autowired
    private PubSystemCodeMapper pubSystemCodeMapper;

    @Autowired
    PubSystemParamsService pubSystemParamsService;

    @Override
    public PubSystemCode selectValueByCodeType(String codeType, String codeId) {
        return pubSystemCodeMapper.selectValueByCodeType(codeType, codeId);
    }

    @Transactional
    @Override
    public void addPubSystemCode(PubSystemCode pubSystemCode) {
        pubSystemCodeMapper.add(pubSystemCode);
        //int i = 10/0; //代码执行异常，事务是否回滚？ 回滚
//        try {
            pubSystemParamsService.addPubSystemParams(new PubSystemParams("xiaohe", "27", "未婚", "1"));
//        } catch (Exception e) {
//            logger.info("REQUIRED事务中调用不同此事务传播行为异常：{}", e);
//        }
    }
}
