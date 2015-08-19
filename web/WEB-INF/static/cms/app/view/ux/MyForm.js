Ext.define('MyCms.view.ux.MyForm',{
	extend:'Ext.form.Panel',
	initComponent:function(){
		var me = this;
		
		me.callParent();
	},
	loadRecord:function(r){

		this.superclass.loadRecord.call(this.callParent(),r);
	}
});