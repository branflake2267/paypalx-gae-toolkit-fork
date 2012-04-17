package com.paypal.adaptive.core.accounts;

import java.util.HashMap;

public class UserInfoType {
    private HashMap<String, String> responseParams;

    /**
     * (Required) The type of account. Allowable values are:
     */
    private AccountType accountType;

    /**
     * (Required) The name of the person for whom the PayPal account is created.
     */
    private String name;

    /**
     * Business name of the PayPal account holder.
     */
    private String businessName;

    /**
     * Identifies the PayPal account. NOTE: A call to this API must use either
     * emailAddress or accountId as the unique identifier for the account, but
     * must never include both in the same call.
     */
    private String accountId;

    /**
     * (Required)Email address associated with the PayPal account: one of the
     * unique identifiers for the account. NOTE: A call to this API must use
     * either emailAddress or accountId as the unique identifier for the
     * account, but must never include both in the same call.
     */
    private String emailAddress;

    public UserInfoType(HashMap<String, String> responseParams) {
        this.responseParams = responseParams;

        parseResponse();
    }

    private void parseResponse() {
        parseAccountType();
        parseName();
        parseBusinessName();
        parseAccountId();
        parseEmailAddress();
    }

    private void parseAccountType() {
        String value = responseParams.get("accountType");
        accountType = AccountType.fromValue(value);
    }

    private void parseName() {
        name = responseParams.get("name");
    }

    private void parseBusinessName() {
        businessName = responseParams.get("businessName");
    }

    private void parseAccountId() {
        accountId = responseParams.get("accountId");
    }

    private void parseEmailAddress() {
        emailAddress = responseParams.get("emailAddress");
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getName() {
        return name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
