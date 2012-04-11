'''
Created on Aug 25, 2010

@author: palavilli
'''

class PayPalBaseError(Exception):
    '''
    classdocs
    '''
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)

        
class AuthorizationRequiredError(PayPalBaseError):
    
    def __init__(self, url):
        PayPalBaseError.__init__(self, 'AuthorizationRequired')
        self.url = url
        
class InvalidAPICredentialsError(PayPalBaseError):
    ''' nothing to add really'''

class InvalidReceiverError(PayPalBaseError):
    '''nothing to add'''

class InvalidPrimaryReceiverAmountError(PayPalBaseError):
    ''' raised in case of chained payment scenario '''
    def set_primaryReceiverAmount(self, primaryReceiverAmount):
        self.primaryReceiverAmount = primaryReceiverAmount
    
    def set_sumOfSecondaryReceiversAmount(self, sumOfSecondaryReceiversAmount):
        self.sumOfSecondaryReceiversAmount = sumOfSecondaryReceiversAmount
        
class InvalidResponseDataError(PayPalBaseError):
    def set_responseData(self, responseData):
        self.responseData = responseData
    
class InvalidFeesPayerError(PayPalBaseError):
    ''' nothing to add really'''
    
class InvalidPaymentTypeError(PayPalBaseError):
    ''' nothing to add really'''
    
class InvalidParameterValue(PayPalBaseError):
    '''nothing to add really'''
    
class MissingAPICredentialsError(PayPalBaseError):
    '''nothing to add really'''

class MissingParameterError(PayPalBaseError):
    
    def set_parameterName(self, parameterName):
        self.parameterName = parameterName
    
class NotEnoughReceiversError(PayPalBaseError):
    
    def set_actualNumber(self, actualNumber):
        self.actualNumber = actualNumber
    
    def set_minimumRequired(self, minimumRequired):
        self.minimumRequired = minimumRequired
    
class PaymentExecError(PayPalBaseError):
    
    def set_paymentExecStatus(self, paymentExecStatus):
        self.paymentExecStatus = paymentExecStatus
    
    def set_payErrorList(self, payErrorList):
        self.payErrorList = payErrorList
    
class PaymentInCompleteError(PaymentExecError):
    
    def set_responseEnvelope(self, responseEnvelope):
        self.responseEnvelope = responseEnvelope
    
    def set_payKey(self, payKey):
        self.payKey = payKey
    
class PaymentTypeNotAllowedError(PayPalBaseError):
    
    def set_paymentType(self, paymentType):
        self.paymentType = paymentType
    
class PayPalError(PayPalBaseError):
    
    def set_paymentExecStatus(self, paymentExecStatus):
        self.paymentExecStatus = paymentExecStatus
    
    def set_errorList(self, errorList):
        self.errorList = errorList 
    
    def set_responseEnvelope(self, responseEnvelope):
        self.responseEnvelope = responseEnvelope
        
class ReceiversCountMismatchError(PayPalBaseError):
    
    def set_expectedNumberOfReceivers(self, expectedNumberOfReceivers):
        self.expectedNumberOfReceivers = expectedNumberOfReceivers
    
    def set_actualNumberOfReceivers(self, actualNumberOfReceivers):
        self.actualNumberOfReceivers = actualNumberOfReceivers
        
class RequestAlreadyMadeError(PayPalBaseError):
    ''' nothing really here '''

class RequestFailureError(PayPalBaseError):
    
    def set_HTTP_RESPONSE_CODE(self, HTTP_RESPONSE_CODE):
        self.HTTP_RESPONSE_CODE = HTTP_RESPONSE_CODE    
    def set_reason(self, reason):
        self.reason = reason    