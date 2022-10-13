package com.example.demo.mapper;

import com.example.demo.bean.AcctAssetData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AcctAssetDataMapper {

    List<AcctAssetData> list();
}
