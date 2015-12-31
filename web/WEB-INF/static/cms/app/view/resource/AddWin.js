Ext.define('MyCms.view.resource.AddWin',{
	extend:'MyCms.view.ux.MyWindow',
	width:352,
	height:250,
	modal:true,
	initComponent:function(){
		var me = this;
		me.title = '添加资源';
		if(me.resource){
			me.title = '修改资源['+me.resource.get('name')+']';
		}
		var form = Ext.create('MyCms.view.resource.Form',{});
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
		if(me.resource){
			me.doReset();
		}
	},
	doSave:function(){
		var me = this,form = me.down('form');
		
		form.getForm().submit({
			clientValidation:true,
    		url:resource_save,
    		params:me.resource?{id:me.resource.get('id')}:{},
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
		if(me.resource){
			form.getForm().loadRecord(me.resource);
		}
	}
});