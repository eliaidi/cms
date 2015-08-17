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
    	    int: function(val, field) {
    	        return /^\d+$/g.test(val);
    	    },
    	    intText: '必须是正整数！',
    	    intMask: '干啥的',
    	    float: function(val, field) {
    	        return !isNaN(val);
    	    },
    	    floatText: '必须是数字！',
    	    floatMask: '干啥的'
    	});
    },
    statics:{
    	copy:function(record){
    		MyCms.Application.clipBoard = record;
    	}
    }
});
