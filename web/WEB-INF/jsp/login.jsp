<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base >

<title>登陆</title>
<style type="text/css">
#loginForm{
}
</style>
<link rel='stylesheet' type="text/css" href="<%=path%>/static/cms/bootstrap.css" />

<script type="text/javascript" src="<%=path%>/static/cms/ext/build/bootstrap.js"></script>
<script type="text/javascript">
var ScreenWidth = window.screen.availWidth,ScreenHeight = window.screen.availHeight;
var boxWidth = 469,boxHeight = 258;
var LOGIN_URL = '<%=path%>/login';
Ext.onReady(function(){
	
	var loginPanel = Ext.create('Ext.form.Panel',{
		renderTo:'loginForm',
		title:'用户登录',
		width:boxWidth,
		height:boxHeight,
		margin:'50px '+(ScreenWidth - boxWidth)/2+'px',
		bodyPadding:'30px 50px',
		items: [{
	        xtype: 'textfield',
	        name: 'username',
	        fieldLabel: '用户名',
	        allowBlank: false
	    }, {
	        xtype: 'textfield',
	        name: 'password',
	        inputType: 'password',
	        fieldLabel: '密码',
	        allowBlank: false
	    },{
	    	xtype: 'checkboxfield',
	    	boxLabel  : '记住我',
            name      : 'rememberMe',
            inputValue: 'true'
	    }],
	    buttons: [{
	        text: '登录',
	        formBind: true,
	        listeners: {
	            click: 'onLoginClick'
	        },
	        onLoginClick:function(){
		    	var me = this.up('form');
		    	var uf = me.getForm().findField('username'),pf = me.getForm().findField('password');
		    	
		    	console.log(uf,pf);
		    	
		    	me.getForm().submit({
		    		clientValidation:true,
		    		url:LOGIN_URL,
		    		success: function(form, action) {
		    		       if(action.result.success){
		    		    	   window.location.assign('<%=path%>/');
		    		       }else{
		    		    	   Ext.Msg.alert('Failure', action.result.message);
		    		       }
		    		},
		    		failure: function(form, action) {
	    		        switch (action.failureType) {
	    		            case Ext.form.action.Action.CLIENT_INVALID:
	    		                Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
	    		                break;
	    		            case Ext.form.action.Action.CONNECT_FAILURE:
	    		                Ext.Msg.alert('Failure', 'Ajax communication failed');
	    		                break;
	    		            case Ext.form.action.Action.SERVER_INVALID:
	    		               	Ext.Msg.alert('Failure', action.result.message);
	    		       }
	    		    }
		    	});
		    }
	    }]
	    
	});
	
	if(window.attachEvent){
		window.attachEvent('onkeypress',keyPressFn);
		window.onkeypress = keyPressFn;
	}else{
		window.addEventListener('keypress',keyPressFn);
	}
	
	
	
	function keyPressFn(e){
		e = e||window.event;
		if(e.charCode!=13) return;
		//console.log(e,loginPanel,loginPanel.down('button'));
		var btn = loginPanel.down('button');
		btn.fireEvent('click');
	}
});


</script>
</head>

<body>

	<div id="loginForm"></div>
</body>
</html>
