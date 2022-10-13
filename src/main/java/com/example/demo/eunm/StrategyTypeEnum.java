package com.example.demo.eunm;

import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.strategory.*;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

public enum StrategyTypeEnum {

    StrategyIdCard("IdCard", new StrategyCardId()),
    StrategyPhone("phone", new StrategyPhone()),
    StrategyPassword("password", new StrategyPassword()),
    StrategyEmail("email", new StrategyEmail()),
    StrategyChineseName("chineseName", new StrategyChineseName());

    private String strategyType;

    private IStrategy iStrategy;

    StrategyTypeEnum(String strategyType, IStrategy iStrategy) {
        this.strategyType = strategyType;
        this.iStrategy = iStrategy;
    }

    public String getStrateType() {
        return strategyType;
    }

    public void setStrateType(String strateType) {
        this.strategyType = strateType;
    }

    public IStrategy getiStrategy() {
        return iStrategy;
    }

    public void setiStrategy(IStrategy iStrategy) {
        this.iStrategy = iStrategy;
    }

    public static IStrategy getIStrategy(String strategyType) {
        return EnumSet.allOf(StrategyTypeEnum.class).stream()
                .filter(o -> StringUtils.equals(o.getStrateType(), strategyType))
                .findFirst().map(o -> o.iStrategy)
                .orElse(null);
    }

    public interface EnumType {
        String IdCard = "IdCard";
        String phone = "phone";
        String password = "password";
        String email = "email";
        String chineseName = "chineseName";
    }
}
