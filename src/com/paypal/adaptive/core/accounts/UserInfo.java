package com.paypal.adaptive.core.accounts;

import java.util.HashMap;

public class UserInfo {
    private HashMap<String, String> responseParams;

    private AccountType accountType;
    private String businessName;
    private String accountId;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String middleName;
    private String nameSuffix;
    private String salutation;

    public UserInfo(HashMap<String, String> responseParams) {
        this.responseParams = responseParams;

        parseResponse();
    }

    private void parseResponse() {
        accountType = AccountType.fromValue(responseParams.get("userInfo.accountType"));
        businessName = responseParams.get("userInfo.businessName");
        accountId = responseParams.get("userInfo.accountId");
        emailAddress = responseParams.get("userInfo.emailAddress");
        firstName = responseParams.get("userInfo.name.firstName");
        lastName = responseParams.get("userInfo.name.lastName");
        middleName = responseParams.get("userInfo.name.middleName");
        nameSuffix = responseParams.get("userInfo.name.suffix");
        salutation = responseParams.get("userInfo.name.salutation");
    }

    public HashMap<String, String> getResponseParams() {
        return responseParams;
    }
    
    public String getAccountId() {
        return accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public String getSalutation() {
        return salutation;
    }
}
