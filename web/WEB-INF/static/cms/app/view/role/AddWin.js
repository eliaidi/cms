Ext.define('MyCms.view.role.AddWin',{
	extend:'MyCms.view.ux.MyWindow',
	width:625,
	height:409,
	modal:true,
	initComponent:function(){
		var me = this;
		me.title = '添加角色';
		if(me.user){
			me.title = '修改角色['+me.role.get('name')+']';
		}
		var form = Ext.create('MyCms.view.role.Form',{});
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
		if(me.role){
			me.doReset();
		}
		if(me.from.allRes){
			me.drawCheckbox();
		}else{
			me.loadAllRes();
		}
	},
	doSave:function(){
		var me = this,form = me.down('form');
		
		form.getForm().submit({
			clientValidation:true,
    		url:role_save,
    		params:me.role?{id:me.role.get('id')}:{},
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
		if(me.role){
			form.getForm().loadRecord(me.role);
		}
		me.loadAllRes();
	},
	loadAllRes:function(){
		var me = this;
		Ext.Ajax.request({
			url : resource_list,
			params : {
				start:0,
				limit:200
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if(obj.list){
					me.from.allRes = obj.list;
					me.drawCheckbox();
				}
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
	},
	drawCheckbox:function(){
		
		var me = this,form = me.down('form'),citems = [],l = me.from.allRes;
		for(var i=0;i<l.length;i++){
			citems.push({
				boxLabel  : l[i].name,
                name      : 'resources['+i+'].id',
                inputValue: l[i].id,
                checked : isChecked(l[i].id)
			});
		}
		form.remove('res-checkboxs');
		form.add({
			id:'res-checkboxs',
			xtype: 'fieldcontainer',
			layout:'fit',
            fieldLabel: '选择权限',
            defaultType: 'checkboxfield',
            items:citems
		});
		
		function isChecked(id){
			if(me.role){
				var res = me.role.get('resources');
				for(var i=0;i<res.length;i++){
					if(res[i].id==id){
						return true;
					}
				}
			}
			return false;
		}
	}
});