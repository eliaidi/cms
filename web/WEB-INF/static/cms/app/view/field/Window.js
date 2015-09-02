Ext.define('MyCms.view.field.Window',{
	extend:'MyCms.view.ux.MyWindow',
	modal:true,
	width:926,
	height:511,
	initComponent:function(){
		var me = this;
		
		me.title = "站点【"+me.site.get('name')+"】的自定义字段";
		var grid = Ext.create('MyCms.view.field.Grid',{
			from : me
		});
		
		Ext.apply(me,{
			layout:'fit',
			items:[grid]
		});
		//me.on('close','doAfterClose',me);
		me.callParent();
	},
	doAfterClose:function(){
		this.from.fireEvent('refresh',this.from,this);
	}
});