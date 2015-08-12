Ext.define('MyCms.view.tempfile.Window', {
	extend : 'MyCms.view.ux.MyWindow',
	modal : true,
	width : 1000,
	height : 575,
	initComponent : function() {
		var me = this;
		me.title = "站点【"+me.from._view.site.get('name')+"】下的模板附件";
		
		me.grid = Ext.create("MyCms.view.tempfile.Grid",{
			from : me
		});
		
		Ext.apply(me,{
			layout:'fit',
			items:[me.grid]
		});
		
		me.callParent();
	}
});