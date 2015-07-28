Ext.define('MyCms.view.template.Window', {
	extend : 'Ext.Window',
	requires:['MyCms.view.template.Grid'],
	width : 927,
	height : 478,
	title : '模板管理',
	initComponent : function() {
		var me = this;
		me.title = "模板管理【站点："+me.site.get('name')+"】";
		
		var grid = Ext.create('MyCms.view.template.Grid',{
			_view : me
		});
		Ext.apply(me,{
			layout:'fit',
			items:[grid]
		});

		grid.getStore().on('load',me.doAfterLoad,me);
		me.callParent();
	},
	doAfterLoad : function(){
		var me = this;
		
		if(me.initChooseData){
			var tidArr = me.initChooseData.split(','),grid = me.down('grid'),sModel = grid.getSelectionModel(),store = grid.getStore(),initRs = [];
			Ext.Array.each(tidArr,function(tid){
				initRs.push(store.getById(tid));
			});
			sModel.select(initRs);
		}
	}
});