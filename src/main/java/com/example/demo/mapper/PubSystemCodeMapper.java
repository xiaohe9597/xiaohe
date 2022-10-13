package com.example.demo.mapper;

import com.example.demo.bean.PubSystemCode;
import org.springframework.stereotype.Component;

@Component
public interface PubSystemCodeMapper {

    PubSystemCode selectValueByCodeType(String codeType, String codeId);
}
