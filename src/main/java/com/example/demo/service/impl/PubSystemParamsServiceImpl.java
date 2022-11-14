package com.example.demo.service.impl;

import com.example.demo.bean.PubSystemParams;
import com.example.demo.mapper.PubSystemParamsMapper;
import com.example.demo.service.PubSystemParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MG02004
 * @createTime 2022/10/20 16:50
 * @description
 */
@Service
public class PubSystemParamsServiceImpl implements PubSystemParamsService {

    @Autowired
    private PubSystemParamsMapper pubSystemParamsMapper;

    /**
     * Propagation.NESTED
     * 如果当前存在事务，则嵌套在当前事务下作为一个子事务，没有当前没有事务，则开启个新的事务
     * @param pubSystemParams
     */
    @Transactional(propagation = Propagation.NESTED)
    //@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addPubSystemParams(PubSystemParams pubSystemParams) {
        pubSystemParamsMapper.add(pubSystemParams);
        int i = 10/0; //嵌套事务中抛异常
    }
}
