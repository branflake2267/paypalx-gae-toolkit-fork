'''
Created on Aug 3, 2010

@author: palavilli
'''

class AckCode():
    '''
    AckCode returned in Response Envelop
    '''
    Success = 'Success'
    Failure = 'Failure'
    Warning = 'Warning'
    SuccessWithWarning = 'SuccessWithWarning'
    FailureWithWarning = 'FailureWithWarning'
    
class ActionType():
    '''
    ActionType set in the Pay/ExecutePayment operations
    '''
    PAY = 'PAY'
    CREATE = 'CREATE'
    PAY_PRIMARY = 'PAY_PRIMARY'
    
class CurrencyCodes():
    '''
    CurrencyCodes supported by Adaptive Payments APIs
    '''
    AUD = 'AUD'
    BRL = 'BRL'
    CAD = 'CAD'
    CZK = 'CZK'
    DKK = 'DKK'
    EUR = 'EUR'
    HKD = 'HKD'
    HUF = 'HUF'
    ILS = 'ILS'
    JPY = 'JPY'
    MXR = 'MXR'
    MXN = 'MXN'
    NOK = 'NOK'
    NZD = 'NZD'
    PHP = 'PHP'
    PLN = 'PLN'
    GBP = 'GBP'
    SGD = 'SGD'
    SEK = 'SEK'
    CHF = 'CHF'
    TWD = 'TWD'
    THB = 'THB'
    USD = 'USD'
    
class DayOfWeek():
    NO_DAY_SPECIFIED = 'NO_DAY_SPECIFIED'
    SUNDAY = 'SUNDAY'
    MONDAY = 'MONDAY'
    TUESDAY = 'TUESDAY'
    WEDNESDAY = 'WEDNESDAY'
    THURSDAY = 'THURSDAY'
    FRIDAY = 'FRIDAY'
    SATURDAY = 'SATURDAY'
    
class ErrorCategory():
    System = 'System'
    Application = 'Application'
    Request = 'Request'

class ErrorSeverity():
    Error = 'Error'
    Warning = 'Warning'
    
class FeesPayerType():
    SENDER = 'SENDER'
    PRIMARYRECEIVER = 'PRIMARYRECEIVER'
    EACHRECEIVER = 'EACHRECEIVER'
    SECONDARYONLY = 'SECONDARYONLY'
    
class FundingType():
    ECHECK = 'ECHECK'
    BALANCE = 'BALANCE'
    CREDITCARD = 'CREDITCARD'

class PaymentExecStatus():
    CREATED = 'CREATED'
    COMPLETED = 'COMPLETED'
    INCOMPLETE = 'INCOMPLETE'
    ERROR = 'ERROR'
    REVERSALERROR = 'REVERSALERROR'
    PROCESSING = 'PROCESSING'
    PENDING = 'PENDING'

class PaymentPeriodType():
    NO_PERIOD_SPECIFIED = 'NO_PERIOD_SPECIFIED'
    DAILY = 'DAILY'
    WEEKLY = 'WEEKLY'
    BIWEEKLY = 'BIWEEKLY'
    SEMIMONTHLY = 'SEMIMONTHLY'
    MONTHLY = 'MONTHLY'
    ANNUALLY = 'ANNUALLY'

class PaymentType():
    GOODS = 'GOODS'
    SERVICE = 'SERVICE'
    PERSONAL = 'PERSONAL'
    CASHADVANCE = 'CASHADVANCE'
    
class PinType():
    NOT_REQUIRED = 'NOT_REQUIRED'
    REQUIRED = 'REQUIRED'

class ReasonCodeType():
    Chargeback_Settlement = 'Chargeback Settlement'
    Admin_reversal = 'Admin reversal'
    Refund = 'Refund'
    Unknown = 'Unknown Reason'
    
class ServiceEnvironment():
    PRODUCTION = 'PRODUCTION'
    SANDBOX = 'SANDBOX'
    BETA_SANDBOX = 'BETA_SANDBOX'
    
class StatusType():
    SUCCESS = 'SUCCESS'
    PENDING = 'PENDING'
    CREATED = 'CREATED'
    PARTIALLY_REFUNDED = 'PARTIALLY_REFUNDED'
    DENIED = 'DENIED'
    PROCESSING = 'PROCESSING'
    REVERSED = 'REVERSED'

class TransactionType():
    Adaptive_Payment_PAY = 'Adaptive Payment PAY'
    Adaptive_Payment_CREATE = 'Adaptive Payment CREATE'
    Adjustment = 'Adjustment'
    Adaptive_Payment_PREAPPROVAL = 'Adaptive Payment PREAPPROVAL'
    UNEXPECTED = 'Unexpected Transaction Type'
    