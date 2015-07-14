Ext.define('MyCms.view.ux.MyMenu',{
	extend:'Ext.menu.Menu',
	initComponent:function(){
		var me = this;
		
		me.on('mouseleave',me.onMouseLeave);
		me.callParent();
	},
	onMouseLeave:function(){
		var me = this;
		
		Ext.destroy(me);
	}
});