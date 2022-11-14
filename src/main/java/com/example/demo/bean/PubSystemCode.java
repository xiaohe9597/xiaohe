package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MG02004
 * @createTime 2022/10/11 11:50
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PubSystemCode {

    private String codeType;

    private String codeId;

    private String codeName;

    private Integer seq;

    private String remark;

    private Integer level;

    private String repairDefinition;

}
