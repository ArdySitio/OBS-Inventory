package com.ardy.test.inventory.constants;

public enum TypeEnum {
	T("Top Up"),
    W("Withdrawal");

    private String key;

    private TypeEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
    
	 public static boolean isValidType(String value) {
        for (TypeEnum type : TypeEnum.values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
