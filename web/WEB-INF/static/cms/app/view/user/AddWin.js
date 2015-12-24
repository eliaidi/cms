Ext.define('MyCms.view.user.AddWin',{
	extend:'MyCms.view.ux.MyWindow',
	width:352,
	height:250,
	modal:true,
	initComponent:function(){
		var me = this;
		me.title = '添加用户';
		if(me.user){
			me.title = '修改用户';
		}
		var form = Ext.create('MyCms.view.user.Form',{});
		var btns = [{
			text : '保存',
			handler : 'doSave',
			scope : me
		},{
			text : '重置',
			handler : 'doReset',
			scope : me
		}];
		
		Ext.apply(me,{
			layout:'fit',
			items:[form],
			buttons:btns
		});
		
		me.on('afterrender',me.doAfterRender,me);
		me.callParent();
	},
	doAfterRender:function(){
		var me = this;
		if(me.user){
			var usernameField = me.down('form').getForm().findField('username');
			usernameField.setReadOnly(true);
			me.doReset();
		}
	},
	doSave:function(){
		var me = this,form = me.down('form');
		
		form.getForm().submit({
			clientValidation:true,
    		url:user_save,
    		params:me.user?{id:me.user.get('id')}:{},
    		success: function(form, action) {
    		       if(action.result.success){
    		    	   Ext.Msg.alert('Success', '保存成功！',function(){
    		    		   me.close();
    		    		   me.from.fireEvent('refresh');
    		    	   });
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
	},
	doReset:function(){
		var me = this,form = me.down('form');
		
		form.getForm().reset();
		if(me.user){
			form.getForm().loadRecord(me.user);
		}
	}
});