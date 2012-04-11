import os
import datetime
from google.appengine.ext.webapp import template
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from paypal.adaptive.payments import Requests
from paypal.adaptive.payments import Constants
from paypal.adaptive.payments import CommonElements
from paypal.adaptive.payments import FnAPI
from paypal.adaptive.payments import Exceptions

class MainPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }

        path = os.path.join(os.path.dirname(__file__), 'index.html')
        self.response.out.write(template.render(path, template_values))
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        convertCurrency = Requests.ConvertCurrencyRequest()
        baseAmount1 = CommonElements.CurrencyType(Constants.CurrencyCodes.USD, 10.0);
        baseAmount2 = CommonElements.CurrencyType(Constants.CurrencyCodes.EUR, 10.0);
        convertCurrency.set_baseAmountList([baseAmount1, baseAmount2])
        
        convertCurrency.set_convertToCurrencyList([Constants.CurrencyCodes.AUD, Constants.CurrencyCodes.BRL, Constants.CurrencyCodes.CAD])
        
        response = convertCurrency.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'index.html')
        self.response.out.write(template.render(path, template_values))
     
     
class ConvertCurrencyPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }

        path = os.path.join(os.path.dirname(__file__), 'convertCurrency.html')
        self.response.out.write(template.render(path, template_values))
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        convertCurrency = Requests.ConvertCurrencyRequest()
        baseAmount1 = CommonElements.CurrencyType(self.request.get('baseCurrencyCode'), self.request.get('baseAmount'));
        convertCurrency.set_baseAmountList([baseAmount1])
        
        convertCurrency.set_convertToCurrencyList([self.request.get('convertCurrency1'), self.request.get('convertCurrency2'), self.request.get('convertCurrency3')])
        
        response = convertCurrency.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'convertCurrency.html')
        self.response.out.write(template.render(path, template_values))
           
class PayPage(webapp.RequestHandler):
    
    
    def get(self):
        template_values = {
            'response': '',
            }

        path = os.path.join(os.path.dirname(__file__), 'pay.html')
        self.response.out.write(template.render(path, template_values))
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        
        #ToDo: self.request.META['HTTP_X_FORWARDED_FOR']
        client_details = CommonElements.ClientDetails('APP-80W284485P519543T', self.request.remote_addr )
        
        payment_details = CommonElements.PaymentDetails(self.request.get('actionType'))
        payment_details.set_cancelUrl('http://localhost:8080/Pay?cancel=1')
        payment_details.set_currencyCode(self.request.get('currencyCode'))
        payment_details.set_feesPayer(self.request.get('feesPayer'))
        payment_details.set_memo('testing 123')
        payment_details.set_trackingId('12345678')
        recv1 = CommonElements.Receiver(self.request.get('receiver_email1'))
        recv1.set_amount(self.request.get('receiver_amount1'))
        recv1.set_paymentType(self.request.get('paymentType'))
        recv2 = CommonElements.Receiver(self.request.get('receiver_email2'))
        recv2.set_amount(self.request.get('receiver_amount2'))
        recv2.set_paymentType(self.request.get('paymentType'))
        if ( self.request.get('paymentModel') == 'Chained'):
            if(self.request.get('primary') == 'receiver1'):
                recv1.set_primary('true')
            elif(self.request.get('primary') == 'receiver2'):
                recv2.set_primary('true')
                
        payment_details.set_receiverList([recv1, recv2])
        payment_details.set_returnUrl('http://localhost:8080/PaymentDetails?payKey=${payKey}')
        payment_details.set_senderEmail(self.request.get('senderEmail'))
        payment_details.set_trackingId('12345678')
        if(self.request.get('preapprovalKey') and len(self.request.get('preapprovalKey')) > 0):
            payment_details.set_preapprovalKey(self.request.get('preapprovalKey'))
        payRequest = Requests.PayRequest()
        payRequest.set_clientDetails(client_details)
        payRequest.set_paymentDetails(payment_details)
        response = payRequest.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        
        template_values = {
            'response': response
            }
        if hasattr(response, 'payKey'):
            template_values['paymentDetailsLink'] = '/PaymentDetails?payKey=' + response.payKey
            if response.paymentExecStatus != Constants.PaymentExecStatus.COMPLETED :
                template_values['paymentAuthZUrl'] = CommonElements.ServiceEndPoint.getAuthorizationUrl(Constants.ServiceEnvironment.SANDBOX) + '?cmd=_ap-payment&paykey=' + response.payKey
        path = os.path.join(os.path.dirname(__file__), 'pay.html')
        self.response.out.write(template.render(path, template_values))
         
class PaymentDetailsPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }


        if (self.request.get('payKey') is None or len(self.request.get('payKey')) <= 0):
            path = os.path.join(os.path.dirname(__file__), 'paymentDetails.html')
            self.response.out.write(template.render(path, template_values))
        else:
            self.post()
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        paymentdetails = Requests.PaymentDetailsRequest()
        paymentdetails.set_payKey(self.request.get('payKey'))
        paymentdetails.set_trackingId('12345678')
        response = paymentdetails.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'paymentDetails.html')
        self.response.out.write(template.render(path, template_values))
         
class ExecutePaymentPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }


        path = os.path.join(os.path.dirname(__file__), 'executePayment.html')
        self.response.out.write(template.render(path, template_values))
        
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        execPayment = Requests.ExecutePaymentRequest()
        execPayment.set_payKey(self.request.get('payKey'))
        execPayment.set_actionType(self.request.get('actionType'))
        response = execPayment.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'executePayment.html')
        self.response.out.write(template.render(path, template_values))
        
class PreapprovalPage(webapp.RequestHandler):
    
    
    def get(self):
        
        curr_date = datetime.datetime.now().strftime("%Y-%m-%d")
        template_values = {
            'response': '',
            'curr_date' : curr_date,
            
            }

        path = os.path.join(os.path.dirname(__file__), 'preapproval.html')
        self.response.out.write(template.render(path, template_values))
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        
        #ToDo: self.request.META['HTTP_X_FORWARDED_FOR']
        client_details = CommonElements.ClientDetails('APP-80W284485P519543T', self.request.remote_addr )
        preapproval_details = CommonElements.PreapprovalDetails()
        preapproval_details.set_senderEmail(self.request.get('senderEmail'))
        preapproval_details.set_endingDate(self.request.get('endingDate'))
        preapproval_details.set_startingDate(self.request.get('startingDate'))
        preapproval_details.set_maxTotalAmountOfAllPayments(self.request.get('maxTotalAmountOfAllPayments'))
        preapproval_details.set_currencyCode(self.request.get('currencyCode'))
        preapproval_details.set_cancelUrl('http://localhost:8080/Preapproval?cancel=1')
        preapproval_details.set_returnUrl('http://localhost:8080/PreapprovalDetails?preapprovalKey=${preapprovalKey}')
        
        preapprovalRequest = Requests.PreapprovalRequest()
        preapprovalRequest.set_clientDetails(client_details)
        preapprovalRequest.set_preapprovalDetails(preapproval_details)
        response = preapprovalRequest.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        
        template_values = {
            'response': response
            }
        if hasattr(response, 'preapprovalKey'):
            template_values['paymentDetailsLink'] = '/PreapprovalDetails?preapprovalKey=' + response.preapprovalKey
            template_values['paymentAuthZUrl'] = CommonElements.ServiceEndPoint.getAuthorizationUrl(Constants.ServiceEnvironment.SANDBOX) + '?cmd=_ap-preapproval&preapprovalkey=' + response.preapprovalKey
        path = os.path.join(os.path.dirname(__file__), 'preapproval.html')
        self.response.out.write(template.render(path, template_values))
   
class CancelPreapprovalPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }


        path = os.path.join(os.path.dirname(__file__), 'cancelPreapproval.html')
        self.response.out.write(template.render(path, template_values))
        
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        cancelPreapproval = Requests.CancelPreapprovalRequest()
        cancelPreapproval.set_preapprovalKey(self.request.get('preapprovalKey'))
        response = cancelPreapproval.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'cancelPreapproval.html')
        self.response.out.write(template.render(path, template_values))
        
class PreapprovalDetailsPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }

        if (self.request.get('preapprovalKey') is None or len(self.request.get('preapprovalKey')) <= 0):
            path = os.path.join(os.path.dirname(__file__), 'preapprovalDetails.html')
            self.response.out.write(template.render(path, template_values))
        else:
            self.post()
        
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        preapprovalDetails = Requests.PreapprovalDetailsRequest()
        preapprovalDetails.set_preapprovalKey(self.request.get('preapprovalKey'))
        response = preapprovalDetails.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'preapprovalDetails.html')
        self.response.out.write(template.render(path, template_values))
        
class RefundPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }


        path = os.path.join(os.path.dirname(__file__), 'refund.html')
        self.response.out.write(template.render(path, template_values))
        
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        refund = Requests.RefundRequest()
        refund.set_payKey(self.request.get('payKey'))
        refund.set_currencyCode(self.request.get('currencyCode'))
        refund.set_trackingId(self.request.get('trackingId'))
        response = refund.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'refund.html')
        self.response.out.write(template.render(path, template_values))
        
class SimplePayPage(webapp.RequestHandler):
    
    
    def get(self):
        template_values = {
            'response': '',
            }

        path = os.path.join(os.path.dirname(__file__), 'simplePay.html')
        self.response.out.write(template.render(path, template_values))
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        
        simplePay = FnAPI.SimplePay(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        
        #set receiverEmail
        simplePay.set_receiverEmail(self.request.get('receiver_email'))
        #set amount
        simplePay.set_amount(self.request.get('amount'))
        #set currencyCode
        simplePay.set_currencyCode(self.request.get('currencyCode'))        
        #set returnUrl
        simplePay.set_returnUrl('http://localhost:8080/PaymentDetails?payKey=${payKey}')        
        #set cancelUrl
        simplePay.set_cancelUrl('http://localhost:8080/SimplePay?cancel=1')
        #set memo
        simplePay.set_memo(self.request.get('memo'))
        #set userIp
        simplePay.set_userIp(self.request.remote_addr)
        #set senderEmail if exists
        simplePay.set_senderEmail(self.request.get('senderEmail'))
        
        template_values = { }
        
        try:
            response = simplePay.make_request()
        except Exceptions.AuthorizationRequiredError, e:
            template_values['paymentAuthZUrl'] = e.url
            self.redirect(e.url)
        except Exceptions.PayPalError, e:
            str = ''
            for errorData in e.errorList:
                str += 'errorId:' + errorData.errorId
                str += ' message:' + errorData.message
            template_values['errorOccured'] = str
        except Exceptions.PayPalBaseError, e:
            template_values['errorOccured'] = e.value
        except Exception, e:
            template_values['errorOccured'] = e.value
        else:
            template_values = { 
            'response': response
            }
            if hasattr(response, 'payKey'):
                template_values['paymentDetailsLink'] = '/PaymentDetails?payKey=' + response.payKey
            
        path = os.path.join(os.path.dirname(__file__), 'simplePay.html')
        self.response.out.write(template.render(path, template_values))
        
class ChainedPayPage(webapp.RequestHandler):
    
    
    def get(self):
        template_values = {
            'response': '',
            }

        path = os.path.join(os.path.dirname(__file__), 'chainedPay.html')
        self.response.out.write(template.render(path, template_values))
        
    def post(self):
        
        template_values = { }
        try:
            apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        
            chainedPay = FnAPI.ChainedPay(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        
            recv1 = CommonElements.Receiver(self.request.get('receiver_email1'))
            recv1.set_amount(self.request.get('receiver_amount1'))
            recv1.set_paymentType(self.request.get('paymentType'))
            recv1.set_primary('true')
            chainedPay.set_primaryReceiver(recv1)
        
            recv2 = CommonElements.Receiver(self.request.get('receiver_email2'))
            recv2.set_amount(self.request.get('receiver_amount2'))
            recv2.set_paymentType(self.request.get('paymentType'))
            chainedPay.set_secondaryReceiver(recv2)
        
            #set currencyCode
            chainedPay.set_currencyCode(self.request.get('currencyCode'))        
            #set returnUrl
            chainedPay.set_returnUrl('http://localhost:8080/PaymentDetails?payKey=${payKey}')        
            #set cancelUrl
            chainedPay.set_cancelUrl('http://localhost:8080/SimplePay?cancel=1')
            #set memo
            chainedPay.set_memo(self.request.get('memo'))
            #set userIp
            chainedPay.set_userIp(self.request.remote_addr)
            #set senderEmail if exists
            chainedPay.set_senderEmail(self.request.get('senderEmail'))
        
            response = chainedPay.make_request()
        except Exceptions.AuthorizationRequiredError, e:
            template_values['paymentAuthZUrl'] = e.url
            self.redirect(e.url)
        except Exceptions.PayPalError, e:
            str = ''
            for errorData in e.errorList:
                str += 'errorId:' + errorData.errorId
                str += ' message:' + errorData.message
            template_values['errorOccured'] = str
        except Exceptions.PayPalBaseError, e:
            template_values['errorOccured'] = e.value
        except Exception, e:
            template_values['errorOccured'] = e.value
        else:
            template_values = { 
            'response': response
            }
            if hasattr(response, 'payKey'):
                template_values['paymentDetailsLink'] = '/PaymentDetails?payKey=' + response.payKey
            
        path = os.path.join(os.path.dirname(__file__), 'chainedPay.html')
        self.response.out.write(template.render(path, template_values))
        
class TransactionPage(webapp.RequestHandler):
    def get(self):
        template_values = {
            'response': '',
            }

        #if (self.request.get('id') is None or len(self.request.get('payKey')) <= 0):
        path = os.path.join(os.path.dirname(__file__), 'transaction.html')
        self.response.out.write(template.render(path, template_values))
        #else:
        #    self.post()
        
    def post(self):
        apiCredentials = CommonElements.APICredential('APP-80W284485P519543T', 
                                                           'pd_1265515509_biz_api1.yahoo.com', 
                                                           '1265515515', 
                                                           'AFcWxV21C7fd0v3bYYYRCpSSRl31AC1woL3k8kA7-43yk77UeVap4cwO',
                                                           'PythonSampleApp')
        paymentdetails = Requests.PaymentDetailsRequest()
        paymentdetails.set_payKey(self.request.get('payKey'))
        paymentdetails.set_trackingId('12345678')
        response = paymentdetails.make_request(Constants.ServiceEnvironment.SANDBOX, apiCredentials)
        template_values = {
            'response': response,
            }

        path = os.path.join(os.path.dirname(__file__), 'transaction.html')
        self.response.out.write(template.render(path, template_values))
        
        
        
application = webapp.WSGIApplication(
                                     [('/', MainPage),
                                     ('/ConvertCurrency', ConvertCurrencyPage),
                                     ('/Pay', PayPage),
                                     ('/PaymentDetails', PaymentDetailsPage),
                                     ('/ExecutePayment', ExecutePaymentPage),
                                     ('/Preapproval', PreapprovalPage),
                                     ('/CancelPreapproval', CancelPreapprovalPage),
                                     ('/PreapprovalDetails', PreapprovalDetailsPage),
                                     ('/Refund', RefundPage),
                                     ('/SimplePay', SimplePayPage),
                                     ('/ChainedPay', ChainedPayPage),
                                     ('/Transaction', TransactionPage)],
                                     debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()