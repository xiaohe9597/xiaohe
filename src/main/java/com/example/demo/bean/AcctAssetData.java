package com.example.demo.bean;

import com.example.demo.annotation.DateFormat;
import com.example.demo.annotation.Desensitizate;
import com.example.demo.annotation.Dict;
import com.example.demo.constants.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MG02004
 * @createTime 2022/10/8 16:58
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcctAssetData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String applySerialNo;

    private String loanSerialNo;

    @Dict(dictType = CommonConstants.DictType.TABLE, tableCode = {"SLXD", "世联小贷", "BYBB", "八亿宝宝"})
    private String owner;

    private String ownerDesc;

    private Date assignDate;

    private Double balance;

    @Dict(dictNo = "StatusDesc")
    private String status;

    private String statusDesc;

    private String ownerName;

    private Date inputDate;

    private String inputUser;

    private String remark;

    @Dict(dictType = CommonConstants.DictType.ENUM, dictNo = "EnumColumnEnum")
    private String enumColumn;

    private String enumColumnDesc;

    @DateFormat
    private String nowTime;

    @Desensitizate
    private String phone;
}
