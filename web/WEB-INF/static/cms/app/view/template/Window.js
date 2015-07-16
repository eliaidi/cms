Ext.define('MyCms.view.template.Window', {
	extend : 'Ext.Window',
	width : 927,
	height : 478,
	title : '模板管理',
	initComponent : function() {
		var me = this;
		
		var grid = Ext.create('MyCms.view.template.Grid',{
			
		});
		Ext.apply(me,{
			items:[grid]
		});

		me.callParent();
	}
});