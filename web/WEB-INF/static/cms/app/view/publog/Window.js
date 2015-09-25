Ext.define('MyCms.view.publog.Window',{
	extend:'MyCms.view.ux.MyWindow',
	requires:['MyCms.view.publog.Grid'],
	width:942,
	height:475,
	title:'发布监控',
	initComponent:function(){
		
		var me = this;
		
		var doneGrid = Ext.create('MyCms.view.publog.Grid',{
			title:'发布完成',
			url:publog_list+'?type=done'
		});
		var doingGrid = Ext.create('MyCms.view.publog.Grid',{
			title:'正在发布',
			url:publog_list+'?type=doing'
		});
		var failGrid = Ext.create('MyCms.view.publog.Grid',{
			title:'发布失败',
			url:publog_list+'?type=fail'
		});
		Ext.apply(me,{
			layout:'fit',
			items:[{
				xtype:'tabpanel',
				layout:'fit',
				items:[doneGrid,doingGrid,failGrid]
			}]
		});
		
		me.callParent();
	}
});