Ext.define('MyCms.view.ux.CmsHtmlEditor',{
	extend:'Ext.form.field.HtmlEditor',
	alias : 'widget.chtmleditor',
	initComponent:function(){
		var me = this;
		
		this.on('destroy','closeWindow',this);
		this.callParent();
		this.toolbar.add({
			xtype:'button',
			text:'高级编辑',
			handler:'openAdvEditWin',
			scope : me
		});
	},
	openAdvEditWin : function(){
		this.openWin = window.open(RootPath+'/document/fckeditor',this.id);
	},
	closeWindow : function(){
		
		if(this.openWin){
			this.openWin.close();
		}
	}
});