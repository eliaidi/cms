Ext.define('MyCms.view.template.Window', {
	extend : 'Ext.Window',
	requires:['MyCms.view.template.Grid'],
	width : 927,
	height : 478,
	title : '模板管理',
	initComponent : function() {
		var me = this;
		
		var grid = Ext.create('MyCms.view.template.Grid',{
		});
		Ext.apply(me,{
			layout:'fit',
			items:[grid]
		});

		me.callParent();
	}
});