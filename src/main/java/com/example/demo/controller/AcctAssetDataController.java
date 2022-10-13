package com.example.demo.controller;

import com.example.demo.annotation.DataDictionary;
import com.example.demo.bean.AcctAssetData;
import com.example.demo.eunm.ResultEnum;
import com.example.demo.mapper.AcctAssetDataMapper;
import com.example.demo.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MG02004
 * @createTime 2022/10/8 17:02
 * @description
 */
@RequestMapping(value = "/acctAssetData")
@RestController
public class AcctAssetDataController {

    @Autowired
    private AcctAssetDataMapper acctAssetDataMapper;

    @DataDictionary
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result list() {
        List<AcctAssetData> acctAssetDataList = acctAssetDataMapper.list();
        return Result.BuildResponseResult(ResultEnum.SUCESS.getCode(), ResultEnum.SUCESS.getMsg(), acctAssetDataList);
    }
}
