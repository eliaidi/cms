Ext.define('MyCms.view.user.Window',{
	
	extend:'MyCms.view.ux.MyWindow',
	width:800,
	height:430,
	title:'用户管理',
	initComponent:function(){
		var me = this;
		
		var grid = Ext.create('MyCms.view.user.Grid',{});
		Ext.apply(me,{
			layout:'fit',
			items:grid
		});
		
		me.callParent();
	}
});