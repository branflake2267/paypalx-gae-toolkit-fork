package com.paypal.adaptive.core.accounts;

/**
 * The criteria that must be matched in addition to emailAddress. Currently, only NAME is supported.  
 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_ACGetVerifiedStatusAPI
 */
public enum MatchCriteria {
    NAME,
    /**
     * To use ConfirmationType NONE you must request and be granted advanced permission levels.
     */
    NONE;
}
