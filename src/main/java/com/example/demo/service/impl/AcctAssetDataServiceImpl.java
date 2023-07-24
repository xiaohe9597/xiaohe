package com.example.demo.service.impl;

import com.example.demo.bean.AcctAssetData;
import com.example.demo.mapper.AcctAssetDataMapper;
import com.example.demo.service.AcctAssetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MG02004
 * @createTime 2023/3/3 16:53
 * @description
 */
@Service
public class AcctAssetDataServiceImpl implements AcctAssetDataService {

    @Autowired
    private AcctAssetDataMapper acctAssetDataMapper;

    @Override
    public List<AcctAssetData> list() {
        return acctAssetDataMapper.list();
    }
}
