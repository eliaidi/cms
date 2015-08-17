Ext.define('MyCms.view.extfield.Window',{
	extend:'MyCms.view.ux.MyWindow',
	modal:true,
	width:926,
	height:511,
	initComponent:function(){
		var me = this;
		
		me.title = "栏目【"+me.channel.get('descr')+"】的扩展字段";
		var grid = Ext.create('MyCms.view.extfield.Grid',{
			from : me
		});
		
		Ext.apply(me,{
			layout:'fit',
			items:[grid]
		});
		me.callParent();
	}
});