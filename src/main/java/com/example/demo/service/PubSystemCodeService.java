package com.example.demo.service;

import com.example.demo.bean.PubSystemCode;
import org.springframework.cache.annotation.Cacheable;

public interface PubSystemCodeService {

    //@Cacheable(value = "CodeLibrary", key = "#p0+#p1")
    PubSystemCode selectValueByCodeType(String codeType, String codeId);
}
