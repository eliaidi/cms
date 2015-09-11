Ext.define('MyCms.view.extfield.AddEdit',{
	extend:'MyCms.view.ux.MyWindow',
	modal:true,
	width:626,
	height:379,
	initComponent:function(){
		var me = this;
		me.title = '新增栏目【'+me.from.from.channel.get('name')+'】扩展字段';
		if(me.extField){
			me.title = '修改栏目【'+me.from.from.channel.get('name')+'】扩展字段【'+me.extField.get('name')+'】';
		}
		var form = Ext.create('MyCms.view.extfield.Form',{
			from : me
		});
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
			items:[form],
			buttons:btns
		});
		
		me.on('afterrender','doAfterRender',me);
		me.callParent();
	},
	doAfterRender:function(){
		
		this.doReset();
	},
	doSave:function(){
		var me = this,form = me.down('form');
		form.getForm().submit({
    		clientValidation: true,
    	    url: extfield_save,
    	    params:me.extField?{id:me.extField.get('id'),'field.id':me.extField.get('field').id}:{'channel.id' : me.from.from.channel.get('id') },
            success: 'onSuccess',
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            },
            scope:me
        });
	},
	onSuccess:function(){
		var me = this;
		me.close();
		me.from.fireEvent('refresh',me.from);
	},
	doReset:function(){
		
		var me = this,form = me.down('form').getForm();
		form.reset();
		if(me.extField){
			form.loadRecord(me.extField);
			form.findField('field.name').setValue(me.extField.get('fieldName'));
//			form.getForm().findField('field.name').setReadOnly(true);
			form.findField('field.type').setConfig('hidden',true).setDisabled(true);
			form.findField('field.length').setConfig('hidden',true).setDisabled(true);
		}
	}
});