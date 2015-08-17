Ext.define('MyCms.view.extfield.AddEdit',{
	extend:'MyCms.view.ux.MyWindow',
	modal:true,
	width:626,
	height:379,
	initComponent:function(){
		var me = this;
		me.title = '新增栏目【'+me.from.from.channel.get('name')+'】扩展字段';
		var form = Ext.create('MyCms.view.extfield.Form');
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
		
		if(me.extField){
			form.getForm().findField('name').setReadOnly(true);
		}
		
		me.callParent();
	},
	doSave:function(){
		var me = this,form = me.down('form');
		form.getForm().submit({
    		clientValidation: true,
    	    url: extfield_save,
    	    params:me.extField?{id:me.extField.get('id')}:{'channel.id' : me.from.from.channel.get('id') },
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
		
		var me = this,form = me.down('form');
		form.getForm().reset();
		if(me.extField){
			form.getForm().findField('name').setReadOnly(true);
		}
	}
});