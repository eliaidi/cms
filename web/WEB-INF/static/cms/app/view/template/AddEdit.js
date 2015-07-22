Ext.define('MyCms.view.template.AddEdit', {
	extend : 'Ext.window.Window',
	width : 850,
	height : 764,
	modal : true,
	title : '新增模板',
	initComponent : function() {
		var me = this;

		me.form = Ext.create('MyCms.view.template.Form');
		var buttons = [ {
			text : '保存',
			handler : 'onSave',
			scope : me
		}, {
			text : '重置',
			handler : 'onReset',
			scope : me
		} ];
		Ext.apply(me, {
			layout : 'fit',
			items : [ me.form],
			buttons : buttons
		});

		if (me.template) {
			me.title = '编辑模板';
			me.loadForm();
		}
		me.callParent();
	},
	loadForm : function() {
		var me = this, form = me.form;

		form.getForm().loadRecord(me.template);
		form.getForm().findField('file.content').setValue(
				me.template.get('content'));
	},
	onSuccess : function() {
		var me = this;
		me.view.fireEvent('refresh', me.view, me);
		me.close();
	},
	onSave : function() {
		var me = this, form = me.down('form');

		form.getForm().submit(
				{
					clientValidation : true,
					url : template_save,
					params : me.template?{'id':me.template.get('id'),'site.id' : me.view._view.site.get('id')}:{'site.id' : me.view._view.site.get('id')},
					success : 'onSuccess',
					failure : function(form, action) {
						Ext.Msg.alert('失败',
								action.result ? action.result.message
										: 'No response');
					},
					scope : me
				});
	},
	onReset : function() {
		
		var me = this;
		me.form.getForm().reset();
		if(me.template){
			me.loadForm();
		}
	}
});