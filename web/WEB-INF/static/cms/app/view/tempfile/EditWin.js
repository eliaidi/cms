Ext.define('MyCms.view.tempfile.EditWin',{
	extend:'MyCms.view.ux.MyWindow',
	modal:true,
	width:738,
	height:746,
	initComponent:function(){
		var me = this;
		
		me.title = "修改模板附件【"+me.tempFile.get('fileName')+"】";
		me.form = Ext.create('MyCms.view.tempfile.Form',{
			
		});
		
		var buttons = [{
			text : '确定',
			handler : 'doSure',
			scope : me
		},{
			text : '重置',
			handler : 'doReset',
			scope : me
		}];
		
		Ext.apply(me,{
			items:[me.form],
			buttons:buttons
		});
		
		if(me.tempFile){
			me.renderForm();
		}
		
		me.callParent();
	},
	onSuccess : function() {
		var me = this;
		me.from.fireEvent('refresh', me.from, me);
		me.close();
	},
	doSure:function(){
		var me = this, form = me.down('form');

		form.getForm().submit(
				{
					clientValidation : true,
					url : tempfile_save,
					params : me.tempFile?{id:me.tempFile.get('id')}:null,
					success : 'onSuccess',
					failure : function(form, action) {
						Ext.Msg.alert('失败',
								action.result ? action.result.message
										: 'No response');
					},
					scope : me
				});
	},
	doReset:function(){
		var me = this;
		
		me.form.getForm().reset();
		if(me.tempFile){
			me.renderForm();
		}
	},
	renderForm:function(){
		this.form.getForm().findField('file.content').setValue(this.tempFile.get('content'));
	}
});