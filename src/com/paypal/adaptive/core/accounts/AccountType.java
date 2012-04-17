package com.paypal.adaptive.core.accounts;

public enum AccountType {

    PERSONAL,
    PREMIER,
    BUSINESS;
    
    public static AccountType fromValue(String value) {
        return valueOf(value.toUpperCase());
    }
}
