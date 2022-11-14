package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MG02004
 * @createTime 2022/10/20 16:46
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PubSystemParams {

    private String paramName;

    private String paramValue;

    private String remark;

    private String canModify;
}
