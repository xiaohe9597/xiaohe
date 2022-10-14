package com.example.demo.service.impl;

import com.example.demo.bean.PubSystemCode;
import com.example.demo.mapper.PubSystemCodeMapper;
import com.example.demo.service.PubSystemCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MG02004
 * @createTime 2022/10/11 11:47
 * @description
 */
@Service
public class PubSystemCodeServiceImpl implements PubSystemCodeService {

    @Autowired
    private PubSystemCodeMapper pubSystemCodeMapper;

    @Override
    public PubSystemCode selectValueByCodeType(String codeType, String codeId) {
        return pubSystemCodeMapper.selectValueByCodeType(codeType, codeId);
    }
}
