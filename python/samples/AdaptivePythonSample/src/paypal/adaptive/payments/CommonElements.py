'''
Created on Aug 3, 2010

@author: palavilli
'''
import urllib
urllib.getproxies_macosx_sysconf = lambda: {}
import Utils
import Constants
import Exceptions

class APICredential():
    
    def __init__(self, applicationId, APIUserName, APIPassword, Signature, applicationName):
        '''
        Constructor
        '''
        if applicationId is None:
            raise Exceptions.InvalidAPICredentialsError, 'Missing applicationId'
        
        if APIUserName is None:
            raise Exceptions.InvalidAPICredentialsError, 'Missing APIUserName'
        
        if APIPassword is None:
            raise Exceptions.InvalidAPICredentialsError, 'Missing APIPassword'
        
        if Signature is None:
            raise Exceptions.InvalidAPICredentialsError, 'Missing Signature'
        
        if applicationName is None:
            raise Exceptions.InvalidAPICredentialsError, 'Missing applicationName'
        
        self.applicationId = applicationId
        self.APIUserName = APIUserName
        self.APIPassword = APIPassword
        self.Signature = Signature
        self.applicationName = applicationName
        
    def get_headers(self):
        headers = dict()
        headers['X-Paypal-SECURITY-USERID'] = self.APIUserName
        headers['X-PAYPAL-SECURITY-PASSWORD'] = self.APIPassword
        headers['X-PAYPAL-SECURITY-SIGNATURE'] = self.Signature
        headers['X-Paypal-Application-Id'] = self.applicationId
        headers['X-PAYPAL-REQUEST-DATA-FORMAT'] = 'NV'
        headers['X-PAYPAL-RESPONSE-DATA-FORMAT'] = 'NV'
        headers['X-PAYPAL-REQUEST-SOURCE'] = 'GAE-Python_Toolkit'
        return headers
    
            
class BaseAddress():
    def __init__(self, type = None):
        if type is not None:
            self.type = type
    
    def set_line1(self, line1):
        self.line1 = line1
    def set_line2(self, line2):
        self.line2 = line2
    def set_city(self, city):
        self.city = city
    def set_state(self, state):
        self.state = state
    def set_postalCode(self, postalCode):
        self.postalCode = postalCode
    def set_countryCode(self, countryCode):
        self.countryCode = countryCode
    def set_type(self, type):
        self.type = type
    
    def deserialze(self, index, params):
        if('addressList.address(' + str(index) + ').baseAddress.type' in params):
            self.type = Utils.getValue(params['addressList.address(' + str(index) + ').baseAddress.type'])
        if('addressList.address(' + str(index) + ').baseAddress.line1' in params):
            self.line1 = Utils.getValue(params['addressList.address(' + str(index) + ').baseAddress.line1'])
        if('addressList.address(' + str(index) + ').baseAddress.line2' in params):
            self.line2 = Utils.getValue(params['addressList.address(' + str(index) + ').baseAddress.line2'])
        if('addressList.address(' + str(index) + ').baseAddress.city' in params):
            self.city = Utils.getValue(params['addressList.address(' + str(index) + ').baseAddress.city'])
        if('addressList.address(' + str(index) + ').baseAddress.state' in params):
            self.state = Utils.getValue(params['addressList.address(' + str(index) + ').baseAddress.state'])
        if('addressList.address(' + str(index) + ').baseAddress.postalCode' in params):
            self.postalCode = Utils.getValue(params['addressList.address(' + str(index) + ').baseAddress.postalCode'])
        if('addressList.address(' + str(index) + ').baseAddress.countryCode' in params):
            self.countryCode = Utils.getValue(params['addressList.address(' + str(index) + ').baseAddress.countryCode'])
        
        
    
class Address():
    def __init__(self, addresseeName = None, baseAddress= None):
        if(addresseeName is not None):
            self.addresseeName = addresseeName
        if(baseAddress is not None):
            self.baseAddress = baseAddress
    
    def set_addresseeName(self, addresseeName):
        self.addresseeName = addresseeName
    
    def set_baseAddress(self, baseAddress):
        self.baseAddress = baseAddress
        
    def deserialize(self, index, params):
        if('addressList.address(' + str(index) + ').addresseeName' in params):
            self.addresseeName = Utils.getValue(params['addressList.address(' + str(index) + ').addresseeName'])
        self.baseAddress = BaseAddress()
        self.baseAddress.deserialze(index, params)
        
class ClientDetails():
    '''
    classdocs
    '''

    def __init__(self, applicationId, ipAddress, deviceId = 'AppEngine'):
        '''
        Constructor
        '''
        self.applicationId = applicationId
        self.ipAddress = ipAddress
        self.deviceId = deviceId

    def get_params(self):
        urlParams = dict()
        urlParams['clientDetails.applicationId'] = self.applicationId
        urlParams['clientDetails.ipAddress'] = self.ipAddress
        urlParams['clientDetails.deviceId'] = 'AppEngine'
        return urlParams
    
    def serialize(self):
        out_str = ''
        out_str += 'clientDetails.applicationId='
        out_str += urllib.quote(self.applicationId)
        out_str += '&clientDetails.ipAddress='
        out_str += urllib.quote(self.ipAddress)
        out_str += '&clientDetails.deviceId='
        out_str += urllib.quote(self.deviceId)
        return out_str
    
class CurrencyType():
    def __init__(self, code, amount):
        self.code = code
        self.amount = amount
    def serialize(self, index):
        out_str = ''
        out_str += 'baseAmountList.currency(' + str(index) + ').amount='
        out_str += str(self.amount)
        out_str += '&baseAmountList.currency(' + str(index) + ').code='
        out_str += self.code
        return out_str

class CurrencyConversionList():
    def __init__(self, index, params):
        
        self.baseAmount = CurrencyType(Utils.getValue(params['estimatedAmountTable.currencyConversionList(' + str(index) + ').baseAmount.code']),
                                       Utils.getValue(params['estimatedAmountTable.currencyConversionList(' + str(index) + ').baseAmount.amount']))
        self.currencyList = []
        for i in range(10):
            if 'estimatedAmountTable.currencyConversionList(' + str(index) + ').currencyList.currency(' + str(i) + ').code' in params:
                currencyTypeObj = CurrencyType(Utils.getValue(params['estimatedAmountTable.currencyConversionList(' + str(index) + ').currencyList.currency(' + str(i) + ').code']),
                                               Utils.getValue(params['estimatedAmountTable.currencyConversionList(' + str(index) + ').currencyList.currency(' + str(i) + ').amount']))
                self.currencyList.append(currencyTypeObj)
            else:
                break
            
        
class ErrorData():
    def __init__(self, key_str, params):
        self.errorId = Utils.getValue(params[key_str +'.errorId'])
        if key_str + '.domain' in params:
            self.domain = Utils.getValue(params[key_str + '.domain'])
        if key_str +'.subdomain' in params:
            self.subdomain = Utils.getValue(params[ key_str + '.subdomain'])
        if key_str + '.severity' in params:
            self.severity = Utils.getValue(params[ key_str + '.severity'])
        if key_str + '.category' in params:
            self.category = Utils.getValue(params[key_str + '.category'])
        if key_str + '.message' in params:
            self.message = Utils.getValue(params[ key_str + '.message'])
        if key_str + '.exceptionId' in params:
            self.exceptionId = Utils.getValue(params[ key_str + '.exceptionId'])
        self.parameter = []
        #magic number - we don't expect to see more than 10 error parameter in a single request anyway
        count = 0
        while (count < 10):
            if key_str + '.parameter(' + str(count) + ')' in params and key_str + '.parameter(' + str(count+1) + ')' in params:
                error_parameter = ErrorParameter(Utils.getValue(params[ key_str + '.parameter(' + str(count) + ')']), Utils.getValue(params[ key_str + '.parameter(' + str(count+1) + ')']) )
                self.parameter.append(error_parameter)
                count += 2
            elif key_str + '.parameter(' + str(count) + ')' in params:
                error_parameter = ErrorParameter(Utils.getValue(params[ key_str + '.parameter(' + str(count) + ')']))
                self.parameter.append(error_parameter)
                break
            else:
                break
        
        
class ErrorParameter():
    def __init__(self, name, value = None):
        self.name = name
        if value is not None:
            self.value = value

class FundingConstraint():
    def __init__(self, fundingType):
        self.fundingType = fundingType

class PayError():
    def __init__(self, error = None, receiver = None):
        #contains Error data 
        if( error is not None):
            self.error = error
        self.error = error
        if (receiver is not None):
            self.receiver = receiver
        
    def set_error(self, error):
        self.error = error
    
    def set_receiver(self, receiver):
        self.receiver = receiver
class PaymentDetails():
    
    def __init__(self, actionType):
        self.actionType = actionType
    
    def set_cancelUrl(self, cancelUrl):
        self.cancelUrl = cancelUrl
    
    def set_currencyCode(self, currencyCode):
        self.currencyCode = currencyCode
    
    def set_ipnNotificationUrl(self, ipnNotificationUrl):
        self.ipnNotificationUrl = ipnNotificationUrl
    
    def set_memo(self, memo):
        self.memo = memo
    
    def set_returnUrl(self, returnUrl):
        self.returnUrl = returnUrl
       
    def set_senderEmail(self, senderEmail):
        self.senderEmail = senderEmail
    
    def set_status(self, status):
        self.status = status
    
    def set_trackingId(self, trackingId):
        self.trackingId = trackingId
    
    def set_payKey(self, payKey):
        self.payKey = payKey
    
    def set_paymentInfoList(self, paymentInfoList):
        self.paymentInfoList = paymentInfoList
    
    def set_feesPayer(self, feesPayer):
        self.feesPayer = feesPayer
    
    def set_reverseAllParallelPaymentsOnError(self, reverseAllParallelPaymentsOnError):
        self.reverseAllParallelPaymentsOnError = reverseAllParallelPaymentsOnError
    
    def set_preapprovalKey(self, preapprovalKey):
        self.preapprovalKey = preapprovalKey
        
    def set_pin(self, pin):
        self.pin = pin
        
    def set_fundingConstraintList(self, fundingConstraintList):
        self.fundingConstraintList = fundingConstraintList
    
    def set_receiverList(self, receiverList):
        self.receiverList = receiverList
    
    def serialize(self):
        out_str = ''
        out_str += 'actionType=' + self.actionType
        out_str += '&cancelUrl=' + urllib.quote(self.cancelUrl)
        out_str += '&currencyCode=' + self.currencyCode
        if hasattr(self, 'feesPayer'):
            out_str += '&feesPayer=' + self.feesPayer
            
        #add fundingConstraint Type 
        if hasattr(self, 'fundingConstraintList'):
            index = 0
            for fundingType in self.fundingConstraintList:
                out_str += '&fundingConstraint.allowedFundingType.fundingTypeInfo(' + str(index) + ').fundingType='
                out_str += fundingType
                index += 1
         
        if hasattr(self, 'ipnNotificationUrl'):
            out_str += '&ipnNotificationUrl=' + urllib.quote(self.ipnNotificationUrl)
        if hasattr(self, 'memo'):
            out_str += '&memo=' + urllib.quote(self.memo)
        if hasattr(self, 'pin'):
            out_str += '&pin=' + self.pin
        if hasattr(self, 'preapprovalKey'):
            out_str += '&preapprovalKey=' + urllib.quote(self.preapprovalKey)
        # add receivers 
        if hasattr(self, 'receiverList'):
            index = 0
            for receiver in self.receiverList:
                out_str += '&'  
                out_str += receiver.serialize(index)
                index += 1
            
        out_str += '&returnUrl=' + self.returnUrl
        if hasattr(self, 'reverseAllParallelPaymentsOnError'):
            out_str += '&reverseAllParallelPaymentsOnError=' + self.reverseAllParallelPaymentsOnError
        if hasattr(self, 'senderEmail'):
            out_str += '&senderEmail=' + urllib.quote(self.senderEmail)
        
        return out_str
        
class PaymentInfo():
    
    def __init__(self, index, params):
        
        if 'paymentInfoList.paymentInfo(' + str(index) + ').pendingReason' in params:
            self.pendingReason = Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').pendingReason'])
        if 'paymentInfoList.paymentInfo(' + str(index) + ').transactionId' in params:
            self.transactionId = Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').transactionId'])
        if 'paymentInfoList.paymentInfo(' + str(index) + ').transactionStatus' in params:
            self.transactionStatus = Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').transactionStatus'])
        if 'paymentInfoList.paymentInfo(' + str(index) + ').refundedAmount' in params:
            self.refundedAmount = Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').refundedAmount'])
        if 'paymentInfoList.paymentInfo(' + str(index) + ').pendingRefund' in params:
            self.pendingRefund = Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').pendingRefund'])
        if 'paymentInfoList.paymentInfo(' + str(index) + ').senderTransactionId' in params:
            self.senderTransactionId = Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').senderTransactionId'])
        if 'paymentInfoList.paymentInfo(' + str(index) + ').senderTransactionStatus' in params:
            self.senderTransactionStatus = Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').senderTransactionStatus'])
        if 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.amount' or 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.email' in params:
            self.receiver = Receiver()
            if 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.amount' in params:
                self.receiver.set_amount(Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').receiver.amount']))
            if 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.email' in params:
                self.receiver.set_email(Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').receiver.email']))
            if 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.invoiceId' in params:
                self.receiver.set_invoiceId(Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').receiver.invoiceId']))
            if 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.paymentType' in params:
                self.receiver.set_paymentType(Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').receiver.paymentType']))
            if 'paymentInfoList.paymentInfo(' + str(index) + ').receiver.primary' in params:
                self.receiver.set_primary(Utils.getValue(params['paymentInfoList.paymentInfo(' + str(index) + ').receiver.primary']))

    
    def set_transactionId(self, transactionId):
        self.transactionId = transactionId
        
    def set_transactionStatus(self, transactionStatus):
        self.transactionStatus = transactionStatus
    
    def set_receiver(self, receiver):
        self.receiver = receiver
    
    def set_refundedAmount(self, refundedAmount):
        self.refundedAmount = refundedAmount
    
    def set_pendingReason(self, pendingReason):
        self.pendingReason = pendingReason
        
    def set_pendingRefund(self, pendingRefund):
        self.pendingRefund = pendingRefund
    
    def set_senderTransactionId(self, senderTransactionId):
        self.senderTransactionId = senderTransactionId
    
    def set_senderTransactionStatus(self, senderTransactionStatus):
        self.senderTransactionStatus = senderTransactionStatus
        
class PhoneNumberType():
    
    def __init__(self, countryCode, phonenumber, extension):
        self.countryCode = countryCode
        self.extension = extension
        self.phonenumber = phonenumber
    def serialize(self, index):
        out_str = ''
        if hasattr(self, 'countryCode'):
            out_str += 'receiverList.receiver(' + str(index) + ').phone.countryCode='
            out_str += self.countryCode
        if hasattr(self, 'extension'):
            out_str += '&receiverList.receiver(' + str(index) + ').phone.extension='
            out_str += self.extension
        if hasattr(self, 'phonenumber'):
            out_str += '&receiverList.receiver(' + str(index) + ').phone.phonenumber='
            out_str += self.phonenumber
        return out_str
    
class PreapprovalDetails():
    
    def __init__(self, status = None):
        if status is not None:
            self.status = status
        
    def set_cancelUrl(self,cancelUrl):
        self.cancelUrl = cancelUrl
    
    def set_ipnNotificationUrl(self, ipnNotificationUrl):
        self.ipnNotificationUrl = ipnNotificationUrl
    
    def set_returnUrl(self, returnUrl):
        self.returnUrl = returnUrl
    
    def set_currencyCode(self,currencyCode):
        self.currencyCode = currencyCode
    
    def set_dateOfMonth(self, dateOfMonth):
        self.dateOfMonth = dateOfMonth
    
    def set_dayOfWeek(self, dayOfWeek):
        self.dayOfWeek = dayOfWeek
    def set_endingDate(self, endingDate):
        self.endingDate = endingDate
    
    def set_maxAmountPerPayment(self, maxAmountPerPayment):
        self.maxAmountPerPayment = maxAmountPerPayment
    
    def set_maxNumberOfPayments(self, maxNumberOfPayments):
        self.maxNumberOfPayments = maxNumberOfPayments
    
    def set_maxNumberOfPaymentsPerPeriod(self, maxNumberOfPaymentsPerPeriod):
        self.maxNumberOfPaymentsPerPeriod = maxNumberOfPaymentsPerPeriod
        
    def set_maxTotalAmountOfAllPayments(self, maxTotalAmountOfAllPayments):
        self.maxTotalAmountOfAllPayments = maxTotalAmountOfAllPayments
    
    def set_paymentPeriod(self, paymentPeriod):
        self.paymentPeriod = paymentPeriod
    
    def set_memo(self, memo):
        self.memo = memo
    
    def set_senderEmail(self, senderEmail):
        self.senderEmail = senderEmail
    
    def set_startingDate(self, startingDate):
        self.startingDate = startingDate
    
    def set_pinType(self, pinType):
        self.pinType = pinType
    
    def set_status(self, status):
        self.status = status
    
    def set_approved(self, approved):
        self.approved = approved
    
    def set_curPayments(self, curPayments):
        self.curPayments = curPayments
    
    def set_curPaymentsAmount(self, curPaymentsAmount):
        self.curPaymentsAmount = curPaymentsAmount
    
    def set_curPeriodAttempts(self, curPeriodAttempts):
        self.curPeriodAttempts = curPeriodAttempts
    
    def set_curPeriodEndingDate(self, curPeriodEndingDate):
        self.curPeriodEndingDate = curPeriodEndingDate
    
    def serialize(self):
        out_str = ''
        if hasattr(self, 'dateOfMonth'):
            out_str += '&dateOfMonth=' + self.dateOfMonth
        if hasattr(self, 'dayOfWeek'):
            out_str += '&dayOfWeek=' + self.dayOfWeek
        if hasattr(self, 'endingDate'):
            out_str += '&endingDate=' + self.endingDate
        if hasattr(self, 'startingDate'):
            out_str += '&startingDate=' + self.startingDate
        if hasattr(self, 'senderEmail'):
            out_str += '&senderEmail=' + self.senderEmail
        if hasattr(self, 'maxAmountPerPayment'):
            out_str += '&maxAmountPerPayment=' + self.maxAmountPerPayment
        if hasattr(self, 'maxTotalAmountOfAllPayments'):
            out_str += '&maxTotalAmountOfAllPayments=' + self.maxTotalAmountOfAllPayments
        if hasattr(self, 'maxNumberOfPayments'):
            out_str += '&maxNumberOfPayments=' + self.maxNumberOfPayments
        if hasattr(self, 'maxNumberOfPaymentsPerPeriod'):
            out_str += '&maxNumberOfPaymentsPerPeriod=' + self.maxNumberOfPaymentsPerPeriod
        if hasattr(self, 'paymentPeriod'):
            out_str += '&paymentPeriod=' + self.paymentPeriod
        if hasattr(self, 'pinType'):
            out_str += '&pinType=' + self.pinType
        if hasattr(self, 'currencyCode'):
            out_str += '&currencyCode=' + self.currencyCode
        if hasattr(self, 'memo'):
            out_str += '&memo=' + self.memo
        if hasattr(self, 'cancelUrl'):
            out_str += '&cancelUrl=' + self.cancelUrl
        if hasattr(self, 'returnUrl'):
            out_str += '&returnUrl=' + self.returnUrl
        if hasattr(self, 'ipnNotificationUrl'):
            out_str += '&ipnNotificationUrl=' + self.ipnNotificationUrl
        return out_str
    
    def deserialize(self, params):
        if 'dateOfMonth' in params:
            self.dateOfMonth = Utils.getValue(self.raw['dateOfMonth'])
        if 'dayOfWeek' in params:
            self.dateOfMonth = Utils.getValue(self.raw['dayOfWeek'])
        if 'endingDate' in params:
            self.dateOfMonth = Utils.getValue(self.raw['endingDate'])
        if 'startingDate' in params:
            self.dateOfMonth = Utils.getValue(self.raw['startingDate'])
        if 'senderEmail' in params:
            self.dateOfMonth = Utils.getValue(self.raw['senderEmail'])
        if 'maxAmountPerPayment' in params:
            self.dateOfMonth = Utils.getValue(self.raw['maxAmountPerPayment'])
        if 'maxTotalAmountOfAllPayments' in params:
            self.dateOfMonth = Utils.getValue(self.raw['maxTotalAmountOfAllPayments'])
        if 'maxNumberOfPayments' in params:
            self.dateOfMonth = Utils.getValue(self.raw['maxNumberOfPayments'])
        if 'maxNumberOfPaymentsPerPeriod' in params:
            self.dateOfMonth = Utils.getValue(self.raw['maxNumberOfPaymentsPerPeriod'])
        if 'paymentPeriod' in params:
            self.dateOfMonth = Utils.getValue(self.raw['paymentPeriod'])
        if 'pinType' in params:
            self.dateOfMonth = Utils.getValue(self.raw['pinType'])
        if 'currencyCode' in params:
            self.dateOfMonth = Utils.getValue(self.raw['currencyCode'])
        if 'memo' in params:
            self.dateOfMonth = Utils.getValue(self.raw['memo'])
        if 'cancelUrl' in params:
            self.dateOfMonth = Utils.getValue(self.raw['cancelUrl'])
        if 'returnUrl' in params:
            self.dateOfMonth = Utils.getValue(self.raw['returnUrl'])
        if 'ipnNotificationUrl' in params:
            self.dateOfMonth = Utils.getValue(self.raw['ipnNotificationUrl'])
        if 'approved' in params:
            self.approved = Utils.getValue(self.raw['approved'])
        if 'curPayments' in params:
            self.curPayments = Utils.getValue(self.raw['curPayments'])
        if 'curPaymentsAmount' in params:
            self.curPaymentsAmount = Utils.getValue(self.raw['curPaymentsAmount'])
        if 'curPeriodAttempts' in params:
            self.curPeriodAttempts = Utils.getValue(self.raw['curPeriodAttempts'])
        if 'curPeriodEndingDate' in params:
            self.curPeriodEndingDate = Utils.getValue(self.raw['curPeriodEndingDate'])
        if 'status' in params:
            self.status = Utils.getValue(self.raw['status'])
        #for now we will support only upto 10
        self.addressList = []
        for index in range(10):
            if 'addressList.address(' + str(index) + ').baseAddress.type' in params:
                address = Address()
                address.deserialize(index, params)
                self.addressList.append(address)
            else:
                break
            
            
            
class Receiver():
    
    def __init__(self, email = None):
        if(email is not None):
            self.email = email
    
    def set_email(self, email):
        self.email = email
        
    def set_amount(self, amount):
        self.amount = amount
    
    def set_invoiceId(self, invoiceId):
        self.invoiceId = invoiceId
    
    def set_paymentType(self, paymentType):
        self.paymentType = paymentType
        
    def set_primary(self, primary):
        self.primary = primary
    
    def set_phone(self, phone):
        """phone is of type PhoneNumberType
        """
        self.phone = phone
    
    def serialize(self, index):
        out_str = ''
        out_str += 'receiverList.receiver(' + str(index) + ').amount=' + str(self.amount)
        if hasattr(self, 'email'):
            out_str += '&receiverList.receiver(' + str(index) + ').email=' + urllib.quote(self.email)
        if hasattr(self, 'invoiceId'):
            out_str += '&receiverList.receiver(' + str(index) + ').invoiceId=' + urllib.quote(self.invoiceId)
        if hasattr(self, 'paymentType'):
            out_str += '&receiverList.receiver(' + str(index) + ').paymentType=' + self.paymentType
        if hasattr(self, 'phone'):
            out_str += '&' + self.phone.serialize(index)
        if hasattr(self, 'primary'):
            out_str += '&receiverList.receiver(' + str(index) + ').primary=' + str(self.primary)
        return out_str
        
        
class RefundInfo():
    
    def __init__(self, refundStatus = None):
        if refundStatus is not None:
            self.refundStatus = refundStatus
    
    def set_receiver(self, receiver):
        self.receiver = receiver
        
    def set_refundStatus(self, refundStatus):
        self.refundStatus = refundStatus
    
    def set_refundNetAmount(self, refundNetAmount):
        self.refundNetAmount = refundNetAmount
    
    def set_refundFeeAmount(self, refundFeeAmount):
        self.refundFeeAmount = refundFeeAmount
    
    def set_refundGrossAmount(self, refundGrossAmount):
        self.refundGrossAmount = refundGrossAmount
    
    def set_totalOfAllRefunds(self, totalOfAllRefunds):
        self.totalOfAllRefunds = totalOfAllRefunds
    
    def set_refundHasBecomeFull(self, refundHasBecomeFull):
        self.refundHasBecomeFull = refundHasBecomeFull
        
    def set_encryptedRefundTransactionId(self, encryptedRefundTransactionId):
        self.encryptedRefundTransactionId = encryptedRefundTransactionId
    
    def set_refundTransactionStatus(self, refundTransactionStatus):
        self.refundTransactionStatus = refundTransactionStatus
    
    def set_errorList(self, errorList):
        self.errorList = errorList
    
    #def deserialize(self, index, params):
        #TODO
        
    
class RequestEnvelope():
    def __init__(self, errorLanguage = 'en_US', detailLevel = 'ReturnAll'):
        self.errorLanguage = errorLanguage
        self.detailLevel = detailLevel
    def serialize(self):
        out_str = ''
        out_str += 'requestEnvelope.errorLanguage='
        out_str += str(self.errorLanguage)
        out_str += '&requestEnvelope.detailLevel='
        out_str += str(self.detailLevel)
        return out_str
        
class ResponseEnvelope():
    def __init__(self, params):
        self.ack =              Utils.getValue(params['responseEnvelope.ack'])
        self.build =            Utils.getValue(params['responseEnvelope.build'])
        self.correlationId =    Utils.getValue(params['responseEnvelope.correlationId'])
        self.timestamp =        Utils.getValue(params['responseEnvelope.timestamp'])
    
        
class ServiceEndPoint():
    
    @staticmethod
    def getUrl(env):
        if env == 'PRODUCTION':
            return 'https://svcs.paypal.com/AdaptivePayments/'
        elif env == 'BETA_SANDBOX':
            return 'https://svcs.beta-sandbox.paypal.com/AdaptivePayments/'
        else:
            return 'https://svcs.sandbox.paypal.com/AdaptivePayments/'
    @staticmethod
    def getAuthorizationUrl(env):
        if env == 'PRODUCTION':
            return 'https://www.paypal.com/cgi-bin/webscr'
        elif env == 'BETA_SANDBOX':
            return 'https://www.sandbox.paypal.com/cgi-bin/webscr'
        else:
            return 'https://www.sandbox.paypal.com/cgi-bin/webscr'
