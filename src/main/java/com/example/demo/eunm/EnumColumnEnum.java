package com.example.demo.eunm;

public enum EnumColumnEnum {

    ONE("1", "ONE"),
    TWO("2", "TWO");

    String code;
    String value;

    EnumColumnEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValue(String code) {
        for (EnumColumnEnum e : values()
        ) {
            if (e.code.equals(code)) {
                return e.value;
            }
        }
        return "";
    }
}
