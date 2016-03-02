/**
 * The main application class. An instance of this class is created by app.js when it calls
 * Ext.application(). This is the ideal place to handle application launch and initialization
 * details.
 */
Ext.define('MyCms.Application', {
    extend: 'Ext.app.Application',
    
    name: 'MyCms',
    stores: [
        // TODO: add global / shared stores here
    ],
    
    launch: function () {
       
    	Ext.apply(Ext.form.field.VTypes, {
    	    'int': function(val, field) {
    	        return /^\d+$/g.test(val);
    	    },
    	    intText: '必须是正整数！',
    	    intMask: '干啥的',
    	    'float': function(val, field) {
    	        return !isNaN(val);
    	    },
    	    floatText: '必须是数字！',
    	    floatMask: '干啥的',
    	    customField:function(val,field){
    	    	if(field.maxLength){
    	    		if(val&&val.length>field.maxLength){
    	    			return false;
    	    		}
    	    	}
    	    	return true;
    	    },
    	    customFieldText:'字段不合法！'
    	});
    	
    	Ext.apply(Ext.menu.Menu, {
    		listeners:{
    			'mouseleave' : function(){
    				this.hide();
    			}
    		}
    	});
    	
    	Ext.apply(Ext.Ajax,{
    		listeners:{
    			'requestcomplete':function(conn, response, options, eOpts){
    				console.log(response);
    			}
    		}
    	});
    	
    	Ext.apply(Ext.data.Store,{
    		listeners:{
    			'load':function(_this, records, successful, e){
    				//console.log(_this,records,successful,eOpts);
    				var result = Ext.JSON.decode(e._response.responseText);
    				if(!result.success&&result.message==='unauthorized'){
    					unauthorized(e.request._url);
    				}
    			}
    		}
    	});
    	
    	function unauthorized(addr){
    		Ext.Msg.alert('error','抱歉，您没有【'+addr+'】的权限！请联系管理员~~');
    	}
    },
    statics:{
    	copy:function(record){
    		MyCms.Application.clipBoard = record;
    	}
    }
});
