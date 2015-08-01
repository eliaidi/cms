Ext.define('MyCms.view.ux.MyWindow',{
	extend : 'Ext.window.Window',
	initComponent:function(){
		
		var me = this,maxWidth = ScreenWidth,maxHeight = ScreenHeight - 100;
		
		me.width = me.width>maxWidth?maxWidth:me.width;
		me.height = me.height>maxHeight?maxHeight:me.height;
		
		me.callParent();
	}
});