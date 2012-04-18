package com.paypal.adaptive.core.accounts;

/**
 * Get Verified Status API
 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_ACGetVerifiedStatusAPI
 */
public enum AccountStatus {
    VERIFIED,
    UNVERIFIED;
    
    public static AccountStatus fromValue(String value) {
        return valueOf(value.toUpperCase());
    }
}
