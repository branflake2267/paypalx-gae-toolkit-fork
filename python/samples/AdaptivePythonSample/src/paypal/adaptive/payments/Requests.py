'''
Created on Aug 3, 2010

@author: palavilli
'''
import urllib
urllib.getproxies_macosx_sysconf = lambda: {}
from urllib2 import Request, urlopen, URLError, HTTPError
import CommonElements
import Constants
import Utils
import Exceptions
from cgi import parse_qs

class PayPalResponse(object):
    def __init__(self, query_string):
        self.raw = parse_qs(query_string)
        # first load up the response envelope
        self.responseEnvelope = CommonElements.ResponseEnvelope(self.raw)
        # parse errorData
        self.errorList = []
        for index in range(10):
            if 'error(' + str(index) +').errorId' in self.raw:
                key_str = 'error(' + str(index) +')'
                errorData = CommonElements.ErrorData( key_str, self.raw)
                self.errorList.append(errorData)
            else:
                break

    def __str__(self):
        return str(self.raw)

    def __getattr__(self, key):
        key = key.upper()
        try:
            value = self.raw[key]
            if len(value) == 1:
                return value[0]
            return value
        except KeyError:
            raise AttributeError(self)

    def success(self):
        return self.responseEnvelope.ack in Constants.AckCode.Success, Constants.AckCode.SuccessWithWarning
    
    success = property(success)
    
class PayPalRequest():
    '''
    classdocs
    '''
    def __init__(self, language = None):
        '''
        Constructor
        '''
        if language is None:
            self.requestEnvelope = CommonElements.RequestEnvelope()
        else:
            self.requestEnvelope = CommonElements.RequestEnvelope(language)
    
    def make_request(self, env, apiCredentials, method_name, api_data):
        
        if apiCredentials is None:
            raise Exceptions.MissingAPICredentialsError('Missing API Credentials')
        
        #serialize all the request 
        url = CommonElements.ServiceEndPoint.getUrl(env) + '' + method_name
        
        #add request envelope
        api_data += '&' + self.requestEnvelope.serialize()
        
        req = Request(url, api_data, apiCredentials.get_headers())
        try:
            theresp = urlopen(req).read()
        except HTTPError, e:
            raise Exceptions.RequestFailureError().set_HTTP_RESPONSE_CODE(e.code)
        except URLError, e:
            raise Exceptions.RequestFailureError().set_reason(e.reason)
        else:
            # everything is fine
            return theresp
        
class ConvertCurrencyRequest(PayPalRequest):
    '''
    classdocs
    '''
    
    def set_baseAmountList(self, baseAmountList):
        self.baseAmountList = baseAmountList
    
    def set_convertToCurrencyList(self, convertToCurrencyList):
        self.convertToCurrencyList = convertToCurrencyList
    
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        
        index = 0
        #parse through the baseAmountList which is a list of currentyType objects
        for currencyType in self.baseAmountList:
            if index > 0:
                data += '&'
            data += currencyType.serialize(index)
            index += 1
        
        #parse through the convertToCurrencyList
        index = 0
        for currCode in self.convertToCurrencyList:
            data += '&convertToCurrencyList.currencyCode('
            data += str(index)
            data += ')='
            data += currCode
            index += 1
        
        response = PayPalRequest.make_request(self, env, apiCredentials, 'ConvertCurrency', data)
        convertCurrencyResponseObj = ConvertCurrencyResponse(response)
        return convertCurrencyResponseObj
        
class ConvertCurrencyResponse(PayPalResponse):
    
    def __init__(self, query_string):
        PayPalResponse.__init__(self, query_string)
        self.estimatedAmountTable = []
        for i in range(10):
            if 'estimatedAmountTable.currencyConversionList(' + str(i) + ').baseAmount.code' in self.raw:
                currencyConversionObj = CommonElements.CurrencyConversionList(i, self.raw)
                self.estimatedAmountTable.append(currencyConversionObj)
            else:
                break

class PayRequest(PayPalRequest):
    '''
    classdocs
    '''
    def set_clientDetails(self, clientDetails):
        self.clientDetails = clientDetails
    
    def set_paymentDetails(self, paymentDetails):
        self.paymentDetails = paymentDetails
    
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        # add client details
        data += self.clientDetails.serialize()
        
        #add payment details
        data += '&'  
        data += self.paymentDetails.serialize()
        
        response = PayPalRequest.make_request(self, env, apiCredentials, 'Pay', data)
        payResponse = PayResponse(response)
        return payResponse
                
class PayResponse(PayPalResponse):
    
    def __init__(self, query_string):
        PayPalResponse.__init__(self, query_string)
        if 'paymentExecStatus' in self.raw:
            self.paymentExecStatus = Utils.getValue((self.raw['paymentExecStatus']))
        if 'payKey' in self.raw:
            self.payKey = Utils.getValue(self.raw['payKey'])
        # parse payError if any
        # parse errorData
        self.payErrorList = []
        for index in range(10):
            if 'payErrorList.payError(' + str(index) +').error.errorId' or 'payErrorList.payError(' + str(index) +').receiver.amount' in self.raw:
                error_key_str = 'payErrorList.payError(' + str(index) +').error'
                recv_key_str = 'payErrorList.payError(' + str(index) +').receiver'
                payError = CommonElements.PayError()
                
                if error_key_str + '.errorId' in self.raw:
                    errorData = CommonElements.ErrorData( error_key_str, self.raw)
                    payError.set_error(errorData)
                    
                if recv_key_str + '.amount' in self.raw:
                    recv = CommonElements.Receiver('')
                    if recv_key_str + '.amount' in self.raw:
                        recv.set_amount(Utils.getValue(self.raw[ recv_key_str + '.amount']))
                    if recv_key_str + '.email' in self.raw:
                        recv.set_email(Utils.getValue(self.raw[ recv_key_str + '.email']))
                    if recv_key_str + '.invoiceId' in self.raw:
                        recv.set_invoiceId(Utils.getValue(self.raw[ recv_key_str + '.invoiceId']))
                    if recv_key_str + '.paymentType' in self.raw:
                        recv.set_paymentType(Utils.getValue(self.raw[ recv_key_str + '.paymentType']))
                    if recv_key_str + '.primary' in self.raw:
                        recv.set_primary(Utils.getValue(self.raw[ recv_key_str + '.primary']))
                    payError.set_receiver(recv)
            else:
                break
        
class PaymentDetailsRequest(PayPalRequest):
    '''
    classdocs
    '''
    def set_payKey(self, payKey):
        self.payKey = payKey
    
    def set_transactionId(self, transactionId):
        self.transactionId = transactionId
    
    def set_trackingId(self, trackingId):
        self.trackingId = trackingId
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        # add payKey
        data += 'payKey=' + urllib.quote(self.payKey)
        
        #add trackingId
        if hasattr(self, 'trackingId'):
            data += '&trackingId=' + urllib.quote(self.trackingId)  
            
        #add transactionId
        if hasattr(self, 'transactionId'):
            data += '&transactionId=' + urllib.quote(self.transactionId)  
        
        
        response = PayPalRequest.make_request(self, env, apiCredentials, 'PaymentDetails', data)
        paymentDetailsResponse = PaymentDetailsResponse(response)
        return paymentDetailsResponse

class PaymentDetailsResponse(PayPalResponse):
    
    def __init__(self, query_string):
        PayPalResponse.__init__(self, query_string)
        
        if 'actionType' in self.raw:
            self.actionType = Utils.getValue(self.raw['actionType'])
        if 'payKey' in self.raw:
            self.payKey = Utils.getValue(self.raw['payKey'])
        if 'cancelUrl' in self.raw:
            self.cancelUrl = Utils.getValue(self.raw['cancelUrl'])
        if 'currencyCode' in self.raw:
            self.currencyCode = Utils.getValue(self.raw['currencyCode'])
        if 'feePayer' in self.raw:
            self.feePayer = Utils.getValue(self.raw['feePayer'])
        if 'ipnNotificationUrl' in self.raw:
            self.ipnNotificationUrl = Utils.getValue(self.raw['ipnNotificationUrl'])
        if 'memo' in self.raw:
            self.memo = Utils.getValue(self.raw['memo'])
        if 'returnUrl' in self.raw:
            self.returnUrl = Utils.getValue(self.raw['returnUrl'])
        if 'reverseAllParallelPaymentsOnError' in self.raw:
            self.reverseAllParallelPaymentsOnError = Utils.getValue(self.raw['reverseAllParallelPaymentsOnError'])
        if 'senderEmail' in self.raw:
            self.senderEmail = Utils.getValue(self.raw['senderEmail'])
        if 'status' in self.raw:
            self.status = Utils.getValue(self.raw['status'])
        if 'trackingId' in self.raw:
            self.trackingId = Utils.getValue(self.raw['trackingId'])
        if 'preapprovalKey' in self.raw:
            self.preapprovalKey = Utils.getValue(self.raw['preapprovalKey'])
        
        self.fundingConstraint = []
        for index in range(5):
            if 'fundingConstraint.allowedFundingType(' + str(index) + ').fundingTypeInfo.fundingType' in self.raw:
                self.fundingConstraint.append(Utils.getValue(self.raw['fundingConstraint.allowedFundingType(' + str(index) + ').fundingTypeInfo.fundingType']))
            
            
        # parse paymentInfoList
        self.paymentInfoList = []
        for index in range(6):
            if 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.amount' in self.raw:
                payment_info = CommonElements.PaymentInfo(index, self.raw)
                self.paymentInfoList.append(payment_info)
            
class ExecutePaymentRequest(PayPalRequest):
    '''
    classdocs
    '''
    def set_actionType(self, actionType):
        self.actionType = actionType
    
    def set_payKey(self, payKey):
        self.payKey = payKey
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        # add payKey
        data += 'payKey=' + urllib.quote(self.payKey)
        # add actiontype
        data += '&actionType=' + urllib.quote(self.actionType)
        
        response = PayPalRequest.make_request(self, env, apiCredentials, 'ExecutePayment', data)
        payResponse = ExecutePaymentResponse(response)
        return payResponse
                
class ExecutePaymentResponse(PayResponse):
    
    def __init__(self, query_string):
        PayResponse.__init__(self, query_string)
        
class PreapprovalRequest(PayPalRequest):
    '''
    classdocs
    '''
    def set_clientDetails(self, clientDetails):
        self.clientDetails = clientDetails
    
    def set_preapprovalDetails(self, preapprovalDetails):
        self.preapprovalDetails = preapprovalDetails
    
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        # add client details
        data += self.clientDetails.serialize()
        
        #add payment details
        data += '&'  
        data += self.preapprovalDetails.serialize()
        
        response = PayPalRequest.make_request(self, env, apiCredentials, 'Preapproval', data)
        payResponse = PreapprovalResponse(response)
        return payResponse
                
class PreapprovalResponse(PayPalResponse):
    
    def __init__(self, query_string):
        PayPalResponse.__init__(self, query_string)
        if 'preapprovalKey' in self.raw:
            self.preapprovalKey = Utils.getValue(self.raw['preapprovalKey'])

class CancelPreapprovalRequest(PayPalRequest):
    '''
    classdocs
    '''
    
    def set_preapprovalKey(self, preapprovalKey):
        self.preapprovalKey = preapprovalKey
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        # add payKey
        data += 'preapprovalKey=' + urllib.quote(self.preapprovalKey)
        
        response = PayPalRequest.make_request(self, env, apiCredentials, 'CancelPreapproval', data)
        payResponse = CancelPreapprovalResponse(response)
        return payResponse
                
class CancelPreapprovalResponse(PayResponse):
    
    def __init__(self, query_string):
        PayResponse.__init__(self, query_string)
        
class PreapprovalDetailsRequest(PayPalRequest):
    '''
    classdocs
    '''
    def set_preapprovalKey(self, preapprovalKey):
        self.preapprovalKey = preapprovalKey
    def set_getBillingAddress(self, getBillingAddress):
        self.getBillingAddress = getBillingAddress
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        # add payKey
        data += 'preapprovalKey=' + urllib.quote(self.preapprovalKey)
        
        if hasattr(self, 'getBillingAddress'):
            data += '&getBillingAddress=' + self.getBillingAddress
            
        response = PayPalRequest.make_request(self, env, apiCredentials, 'PreapprovalDetails', data)
        payResponse = CancelPreapprovalResponse(response)
        return payResponse
                
class PreapprovalDetailsResponse(PayResponse):
    
    def __init__(self, query_string):
        PayResponse.__init__(self, query_string)
        self.preapprovalDetails = CommonElements.PreapprovalDetails()
        self.preapprovalDetails.deserialize(self.raw)
    
class RefundRequest(PayPalRequest):
    '''
    classdocs
    '''
    def set_currencyCode(self, currencyCode):
        self.currencyCode = currencyCode
    
    def set_payKey(self, payKey):
        self.payKey = payKey
    
    def set_transactionId(self, transactionId):
        self.transactionId = transactionId
    
    def set_trackingId(self, trackingId):
        self.trackingId = trackingId
        
    def set_receiverList(self, receiverList):
        self.receiverList = receiverList
    
    def make_request(self, env, apiCredentials):
        # prepare the post data
        data = ''
        # add payKey
        data += 'payKey=' + urllib.quote(self.payKey)
        # add currencyCode
        data += '&currencyCode=' + urllib.quote(self.currencyCode)
        
        #add trackingId
        if hasattr(self, 'trackingId'):
            data += '&trackingId=' + urllib.quote(self.trackingId)  
            
        #add transactionId
        if hasattr(self, 'transactionId'):
            data += '&transactionId=' + urllib.quote(self.transactionId)  
        
        # add receivers 
        if hasattr(self, 'receiverList'):
            index = 0
            for receiver in self.receiverList:
                data += '&'  
                data += receiver.serialize(index)
                index += 1
        
        
        response = PayPalRequest.make_request(self, env, apiCredentials, 'Refund', data)
        payResponse = ExecutePaymentResponse(response)
        return payResponse
                
class RefundResponse(PayResponse):
    
    def __init__(self, query_string):
        PayResponse.__init__(self, query_string)   
        if 'currencyCode' in self.raw:
            self.currencyCode = Utils.getValue(self.raw['currencyCode'])
        #refundInfoList