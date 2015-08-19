Ext.define('MyCms.view.ux.FCKWin', {
	extend : 'MyCms.view.ux.MyWindow',
	modal : true,
	width : 1000,
	height : 672,
	title : '富文本编辑器',
	initComponent : function() {
		var me = this;
		me.form = Ext.create('Ext.Component');
		var btns = [ {
			text : '确定',
			handler : 'onOk',
			scope : me
		}, {
			text : '取消',
			handler : 'onCancel',
			scope : me
		} ];
		
		Ext.apply(me,{
			layout:'fit',
			items:[me.form],
			buttons:btns
		});

		me.on('render','doAfterRender',me);
		me.callParent();
	},
	doAfterRender:function(){
		CKEDITOR.replace( this.form.id );
	},
	onOk : function() {

	},
	onCancel : function() {

	}
});