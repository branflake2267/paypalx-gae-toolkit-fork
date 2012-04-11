Ext.setup({
    tabletStartupScreen: 'tablet_startup.png',
    phoneStartupScreen: 'phone_startup.png',
    icon: 'icon.png',
    glossOnIcon: false,

    onReady: function() {
        var tpl = Ext.XTemplate.from('gallery');

        var getInnovatePhotos = function() {
        	makeAjaxRequest('ppx.devnet@gmail.com');
        }
        var getCESPhotos = function() {
        	makeAjaxRequest('ppalavilli');
        }
        var makeAjaxRequest = function(userid) {
            Ext.getBody().mask('Loading...', 'x-mask-loading', false);
            Ext.Ajax.request({
                url: 'photodata',
                params: {
                	userid: userid
                },
                success: function(response, opts) {
                	var json = eval('(' + response.responseText +')'); 
            		var gallery = json.gallery;
                    if (gallery) {
                        var html = tpl.applyTemplate(gallery);
                        Ext.getCmp('content').update(html);    
                    }
                    else {
                        alert('There was an error retrieving the gallery.');
                    }
                    Ext.getCmp('status').setTitle('Select the images to order prints!');
                    Ext.getBody().unmask();
                    
                }
            });
        };
        
        var orderPrints = function(){
        	var counter = 0;
        	for(key in myArray)
        	{
        		if(key.indexOf("http")==0) {
        			if(myArray[key] == 1) {
        				counter++;
        			}
        		}
        	}
        	if(counter == 0){
        		Ext.Msg.alert('Error', 'Please select pictures that you want to print!', Ext.emptyFn);
        	} else {
        		Ext.Msg.confirm("Confirmation", "Are you sure you want to order " + counter+ " prints?", obtainPayKey);
        	}
        }
        
        var obtainPayKey = function(){
        	var str = "";
        	var counter = 0;
        	for(key in myArray)
        	{
        		
        		if(key.indexOf("http")==0) {
        			if(myArray[key] == 1) {
        				counter++;
        				if(counter > 1) 
        					str += ",";
        				str += key;
        			}
        			// make call to server side to obtain payKey
        		}
        	}
        	
        	if(counter == 0){
        		Ext.Msg.alert('Error', 'Please select pictures that you want to print!', Ext.emptyFn);
        	} else {
	        	Ext.getBody().mask('Loading...', 'x-mask-loading', false);
	            Ext.Ajax.request({
	                url: 'getPayKey',
	                params: {
	                    ids: str
	                },
	                success: function(response, opts) {
	            		var json = eval('(' + response.responseText +')'); 
	            		var status = json.PayResponse.Status;
	                    if (status && status == "CREATED") {
	                        var PPAuthzUrl = json.PayResponse.PPAuthzUrl;
	                        // for iPhone/iPad do a redirect - for everything else use embedded flow
	                        if((navigator.userAgent.match(/iPhone/i)) || (navigator.userAgent.match(/iPad/i))
	                        		|| (navigator.userAgent.match(/Android/i)) ) {
	                        	window.location.href = PPAuthzUrl;
	                        } else {
	                        	myDgFlow.dgStart(PPAuthzUrl);
	                        }
	                    } else {
	                        alert('There was an error retrieving the PayKey.');
	                    }
	                    Ext.getCmp('status').setTitle('Redirecting to PayPal...');
	                }
	            });
        	}
        	
        }
        
        var getQueryParam = function (name) {
        	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
        	var regexS = "[\\?&]"+name+"=([^&#]*)";
        	var regex = new RegExp( regexS );
        	var results = regex.exec( window.location.href );
        	if( results == null )
        		return "";
        	else
        		return results[1];
        }
        function checkStatus() {
			if(paymentStatus != -1) {
				if(paymentStatus == 1) {
					Ext.Msg.alert('Order Success', 'Your prints are on the way!', Ext.emptyFn);
				} else {
					Ext.Msg.alert('Order Canceled', 'Please click on Order Prints to try ordering again.', Ext.emptyFn);
				}
				Ext.getCmp('status').setTitle('Select an album above.');
				Ext.getBody().unmask();
				paymentStatus = -1;
			}
			setTimeout(checkStatus,200);
        }
        
        new Ext.Panel({
            fullscreen: true,
            id: 'content',
            scroll: 'vertical',
            dockedItems: [{
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    text: 'Innovate 2010',
                    handler: getInnovatePhotos,
                },{
                    text: 'CES',
                    handler: getCESPhotos
                },
                {xtype: 'spacer'},{
                    text: 'Order Prints',
                    handler: orderPrints
                }]
            },{
                id: 'status',
                xtype: 'toolbar',
                dock: 'bottom',
                title: "Select an album above."
            }]
        });
        setTimeout(checkStatus,200);
        
        var status = getQueryParam("status");
        if(status && status == 'success')
        	Ext.Msg.alert('Order Success', 'Your prints are on the way!', Ext.emptyFn);
        else if(status && status == 'canceled')
        	Ext.Msg.alert('Order Canceled', 'Please click on Order Prints to try ordering again.', Ext.emptyFn);
        	
    }
});


var myArray = new Array();
function handleTouch(photodiv){
	var myDiv = Ext.get(photodiv.id);
	if(myArray[photodiv.id]) {
		if(myArray[photodiv.id] == 1) {
			myArray[photodiv.id] = 0;
			myDiv.setStyle("background-color", "#c5c5c5");
		}
		else {
			myArray[photodiv.id] = 1;
			myDiv.setStyle("background-color", "#0099CC");
		}
	} else {
		myArray[photodiv.id] = 1;
		myDiv.setStyle("background-color", "#0099CC");
	}
}


var dgFlow = new PAYPAL.apps.DGFlow();
function MyDGFlow(dgFlow) {
	    this.mainObj = dgFlow;
	    this.dgStart = function (ppurl) {
	    	this.mainObj.startFlow(ppurl);
	    }
	    this.dgSuccess = function () {
	    	this.mainObj.closeFlow();
	    	//alert("yey - success");
	    	paymentStatus = 1;
	    	//Ext.Msg.alert('Order Success', 'Your prints are on the way!', Ext.emptyFn);
            //Ext.getBody().unmask();
	    };
	    this.dgCancel = function () {
	    	this.mainObj.closeFlow();
	    	paymentStatus = 0;
	    	//alert("You've canceled your payment!");
	    	//Ext.Msg.alert('Order Canceled', 'Please click on Order Prints to try ordering again.', Ext.emptyFn);
            //Ext.getBody().unmask();
	    };   
}
var myDgFlow = new MyDGFlow(dgFlow);
var paymentStatus = -1;