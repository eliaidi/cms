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
	doSure:function(){
		
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