package com.example.demo.service;

import com.example.demo.bean.PubSystemCode;

public interface PubSystemCodeService {

    PubSystemCode selectValueByCodeType(String codeType, String codeId);
}
