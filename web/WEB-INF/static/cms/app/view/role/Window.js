Ext.define('MyCms.view.role.Window',{
	extend:'MyCms.view.ux.MyWindow',
	width:680,
	height:330,
	modal:true,
	initComponent:function(){
		var me = this;
		me.title = '配置角色';
		if(me.user){
			me.title += '【'+me.user.get('username')+'】';
		}
		
		var grid = Ext.create('MyCms.view.role.Grid',{user:me.user});
		var buttons = [{
			text : '确定',
			handler : 'onOk',
			scope : me
		},{
			text : '重置',
			handler : 'onReset',
			scope : me
		}];
		Ext.apply(me,{
			layout:'fit',
			items:[grid],
			buttons:buttons
		});
		
		me.callParent();
	},
	onOk:function(){},
	onReset:function(){}
});