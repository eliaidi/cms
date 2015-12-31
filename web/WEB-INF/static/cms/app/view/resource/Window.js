Ext.define('MyCms.view.resource.Window',{
	extend:'MyCms.view.ux.MyWindow',
	width:680,
	height:330,
	modal:true,
	title:'资源管理',
	initComponent:function(){
		var me = this;
		
		var grid = Ext.create('MyCms.view.resource.Grid',{});
		Ext.apply(me,{
			layout:'fit',
			items:[grid]
		});
		
		me.callParent();
	}
});