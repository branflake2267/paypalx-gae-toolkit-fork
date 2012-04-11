'''
Created on Aug 25, 2010

@author: palavilli
'''
import Exceptions
import CommonElements
import Constants
import Requests

class PayBase():
    ''' Base class for all Pay Methods '''
    def __init__(self, env, credentialObj, language = 'en_US'):
        '''
        Constructor
        '''
        self.language = language
        
        if credentialObj is None:
            raise Exceptions.MissingAPICredentialsError('Please provide Credentials')
        
        self.credentialObj = credentialObj
        
        if env is None:
            raise Exceptions.MissingParameterError('Please specify the \'env\'')
        else:
            self.env = env

    def set_memo(self, memo):
        if memo is None or len(memo) <= 0:
            raise Exceptions.InvalidParameterValue('Invalid value for memo')
        self.memo = memo
        
    def set_currencyCode(self, currencyCode):
        if currencyCode is None:
            raise Exceptions.InvalidParameterValue('Invalid value for currencyCode')
        self.currencyCode = currencyCode
        
    def set_cancelUrl(self, cancelUrl):
        if cancelUrl is None:
            raise Exceptions.InvalidParameterValue('Invalid value for cancelUrl')
        self.cancelUrl = cancelUrl
    
    def set_returnUrl(self, returnUrl):
        if returnUrl is None:
            raise Exceptions.InvalidParameterValue('Invalid value for returnUrl')
        self.returnUrl = returnUrl
    
    def set_userIp(self, userIp):
        if userIp is None:
            raise Exceptions.InvalidParameterValue('Invalid value for userIp')
        self.userIp = userIp
        
    def set_ipnURL(self, ipnURL):
        if ipnURL is None:
            raise Exceptions.InvalidParameterValue('Invalid value for ipnURL')
        self.ipnURL = ipnURL
        
    def set_senderEmail(self, senderEmail):
        if senderEmail is None:
            raise Exceptions.InvalidParameterValue('Invalid value for senderEmail')
        self.senderEmail = senderEmail
        
    def set_trackingId(self, trackingId):
        if trackingId is None:
            raise Exceptions.InvalidParameterValue('Invalid value for trackingId')
        self.trackingId = trackingId

    def validate(self):
        ''' make sure all required params are provided'''
        if not hasattr(self, 'currencyCode'):
            raise Exceptions.MissingParameterError('CurrencyCode')
        
        if not hasattr(self, 'returnUrl'):
            raise Exceptions.MissingParameterError('returnUrl')
        
        if not hasattr(self, 'cancelUrl'):
            raise Exceptions.MissingParameterError('cancelUrl')
        
        if not hasattr(self, 'memo'):
            raise Exceptions.MissingParameterError('memo')
        
        if not hasattr(self, 'userIp'):
            raise Exceptions.MissingParameterError('userIp')

class SimplePay(PayBase):
    '''
    classdocs
    '''
    def set_receiverEmail(self, receiverEmail):
        ''' one of receiverEmail or receiverPhone must be set'''
        if receiverEmail is None or len(receiverEmail) <= 0:
            raise Exceptions.InvalidParameterValue('Invalid value for receiverEmail')
        self.receiverEmail = receiverEmail
        
    def set_receiverPhone(self, receiverPhone):
        ''' one of receiverEmail or receiverPhone must be set'''
        if receiverPhone is None or len(receiverPhone) <= 0:
            raise Exceptions.InvalidParameterValue('Invalid value for receiverPhone')
        self.receiverPhone = receiverPhone
    
    def set_paymentType(self, paymentType):
        if paymentType is None or len(paymentType) <= 0:
            raise Exceptions.InvalidParameterValue('Invalid value for paymentType')
        self.paymentType = paymentType
    
    def set_amount(self, amount):
        if amount is None or amount <= 0:
            raise Exceptions.InvalidParameterValue('Invalid value for amount')
        self.amount = amount
    
    def set_receiverInvoiceId(self, receiverInvoiceId):
        if receiverInvoiceId is None or len(receiverInvoiceId) <= 0:
            raise Exceptions.InvalidParameterValue('Invalid value for receiverInvoiceId')
        self.receiverInvoiceId = receiverInvoiceId
 
    def set_feesPayer(self, feesPayer):
        ''' only SENDER and EASHRECEIVER (default) are valid'''
        if feesPayer != Constants.FeesPayerType.EACHRECEIVER and feesPayer != Constants.FeesPayerType.SENDER:
            raise Exceptions.InvalidFeesPayerError, 'Only SENDER or EACHRECEIVER allowed'
        self.feesPayer = feesPayer
 
    def make_request(self):
        
        PayBase.validate(self)
        
        client_details = CommonElements.ClientDetails(self.credentialObj.applicationName, self.userIp )
        
        payment_details = CommonElements.PaymentDetails(Constants.ActionType.PAY)
        payment_details.set_cancelUrl(self.cancelUrl)
        payment_details.set_currencyCode(self.currencyCode)
        payment_details.set_memo(self.memo)

        if hasattr(self,'feesPayer'):
            payment_details.set_feesPayer(self.feesPayer)
        
        if hasattr(self, 'trackingId'):
            payment_details.set_trackingId(self.trackingId)
            
        if hasattr(self, 'trackingId'):
            payment_details.set_trackingId(self.trackingId)
        if hasattr(self, 'ipnURL'):
            payment_details.set_ipnNotificationUrl(self.ipnURL)
            
        #receiver
        receiver = CommonElements.Receiver()
        if hasattr(self, 'receiverEmail'):
            receiver.set_email(self.receiverEmail)
        elif hasattr(self, 'receiverPhone'):
            receiver.set_phone(self.receiverPhone)
        else:
            raise Exceptions.MissingParameterError('Either receiverEmail or receiverPhone must be set')
        
        receiver.set_amount(self.amount)
        if hasattr(self, 'receiverInvoiceId'):
            receiver.set_invoiceId(self.receiverInvoiceId)
        if hasattr(self, 'paymentType'):
            receiver.set_paymentType(self.paymentType)
        
        payment_details.set_receiverList([receiver,])
        
        payment_details.set_returnUrl(self.returnUrl)
        if hasattr(self, 'senderEmail'):
            payment_details.set_senderEmail(self.senderEmail)
            
        payRequest = Requests.PayRequest()
        payRequest.set_clientDetails(client_details)
        payRequest.set_paymentDetails(payment_details)
        
        response = payRequest.make_request(self.env, self.credentialObj)
        
        if hasattr(response, 'payKey'):
            if response.paymentExecStatus != Constants.PaymentExecStatus.COMPLETED :
                raise Exceptions.AuthorizationRequiredError(CommonElements.ServiceEndPoint.getAuthorizationUrl(Constants.ServiceEnvironment.SANDBOX) + '?cmd=_ap-payment&paykey=' + response.payKey)
            else:
                '''payment complete'''
        else:
            ''' no payKey! '''
            exp = Exceptions.PayPalError('PayRequest returned error')
            if hasattr(response, 'errorList'):
                exp.set_errorList(response.errorList)
            if hasattr(response, 'responseEnvelope'):
                exp.set_responseEnvelope(response.responseEnvelope)
            if hasattr(response, 'paymentExecStatus'):
                exp.set_paymentExecStatus(response.paymentExecStatus)
            raise exp
            
 
class ChainedPay(PayBase):
    '''
    classdocs
    '''
    
    def __init__(self, env, credentialObj, language = 'en_US'):
        '''
        Constructor
        '''
        PayBase.__init__(self, env, credentialObj, language)
        self.receiverList = []
        
    def set_receiverList(self, receiverList):
        self.receiverList = receiverList
        
    def set_primaryReceiver(self, receiver):
        ''' if primary flag is not set raise an exception'''
        if hasattr(self, 'primaryReceiverSet'):
            raise Exceptions.InvalidReceiverError('Primary Receiver already set')
        
        if receiver.primary is not 'true':
            raise Exceptions.InvalidReceiverError('primary flag not set for primary receiver')
        
        if not hasattr(receiver, 'email') and not hasattr(receiver, 'phone'):
            raise Exceptions.MissingParameterError('Either receiverEmail or receiverPhone must be set')
        
        if not hasattr(receiver, 'amount'):
            raise Exceptions.MissingParameterError('amount must be set for primary receiver')
            
        self.receiverList.append(receiver)
        self.primaryReceiverSet = True
    
    def set_secondaryReceiver(self, receiver):
        if hasattr(receiver, 'primary'):
            if receiver.primary is 'true':
                raise Exceptions.InvalidReceiverError('primary flag set for secondary receiver')
        
        if not hasattr(receiver, 'email') and not hasattr(receiver, 'phone'):
            raise Exceptions.MissingParameterError('Either receiverEmail or receiverPhone must be set')
        
        if not hasattr(receiver, 'amount'):
            raise Exceptions.MissingParameterError('amount must be set for secondary receiver')
        
        self.receiverList.append(receiver)
    
    def set_feesPayer(self, feesPayer):
        ''' only SENDER and EASHRECEIVER (default) are valid'''
        if feesPayer != Constants.FeesPayerType.EACHRECEIVER and feesPayer != Constants.FeesPayerType.PRIMARYRECEIVER and feesPayer != Constants.FeesPayerType.SECONDARYONLY:
            raise Exceptions.InvalidFeesPayerError, 'Only PRIMARYRECEIVER or SECONDARYRECEIVER or EACHRECEIVER allowed'
        self.feesPayer = feesPayer
        
    def make_request(self):
        
        PayBase.validate(self)
        
        if not hasattr(self, 'primaryReceiverSet'):
            raise Exceptions.InvalidReceiverError('Primary Receiver not set')
        
        client_details = CommonElements.ClientDetails(self.credentialObj.applicationName, self.userIp )
        
        payment_details = CommonElements.PaymentDetails(Constants.ActionType.PAY)
        payment_details.set_cancelUrl(self.cancelUrl)
        payment_details.set_currencyCode(self.currencyCode)
        payment_details.set_memo(self.memo)

        if hasattr(self,'feesPayer'):
            payment_details.set_feesPayer(self.feesPayer)
        
        if hasattr(self, 'trackingId'):
            payment_details.set_trackingId(self.trackingId)
            
        if hasattr(self, 'trackingId'):
            payment_details.set_trackingId(self.trackingId)
        if hasattr(self, 'ipnURL'):
            payment_details.set_ipnNotificationUrl(self.ipnURL)
            
        ''' amount received by primary receiver should be >= sum of amounts received by secondary receivers'''
        primaryReceiverAmount = 0
        sumOfSecondaryReceiversAmount = 0
        for receiver in self.receiverList:
            if hasattr(receiver, 'primary') and receiver.primary == 'true':
                primaryReceiverAmount = receiver.amount
            else:
                sumOfSecondaryReceiversAmount += float(receiver.amount)
            #validate paymentType in receivers
            if receiver.paymentType == Constants.PaymentType.PERSONAL or receiver.paymentType == Constants.PaymentType.CASHADVANCE:
                raise Exceptions.InvalidPaymentTypeError('PERSONAL or CASHADVANCE paymentTypes not allowed for Chained Payments')
        
        if sumOfSecondaryReceiversAmount > primaryReceiverAmount:
            exp = Exceptions.InvalidPrimaryReceiverAmountError()
            exp.set_primaryReceiverAmount(primaryReceiverAmount)
            exp.set_sumOfSecondaryReceiversAmount(sumOfSecondaryReceiversAmount)
            raise exp
        
        payment_details.set_receiverList(self.receiverList)
        
        payment_details.set_returnUrl(self.returnUrl)
        if hasattr(self, 'senderEmail'):
            payment_details.set_senderEmail(self.senderEmail)
            
        payRequest = Requests.PayRequest()
        payRequest.set_clientDetails(client_details)
        payRequest.set_paymentDetails(payment_details)
        
        response = payRequest.make_request(self.env, self.credentialObj)
        
        if hasattr(response, 'payKey'):
            if response.paymentExecStatus != Constants.PaymentExecStatus.COMPLETED :
                raise Exceptions.AuthorizationRequiredError(CommonElements.ServiceEndPoint.getAuthorizationUrl(Constants.ServiceEnvironment.SANDBOX) + '?cmd=_ap-payment&paykey=' + response.payKey)
            else:
                '''payment complete'''
        else:
            ''' no payKey! '''
            exp = Exceptions.PayPalError('PayRequest returned error')
            if hasattr(response, 'errorList'):
                exp.set_errorList(response.errorList)
            if hasattr(response, 'responseEnvelope'):
                exp.set_responseEnvelope(response.responseEnvelope)
            if hasattr(response, 'paymentExecStatus'):
                exp.set_paymentExecStatus(response.paymentExecStatus)
            raise exp
            
                 
            