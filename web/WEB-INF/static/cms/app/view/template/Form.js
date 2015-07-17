Ext.define('MyCms.view.template.Form',{
	extend:'Ext.form.Panel',
	bodyPadding: 5,
	layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    defaultType: 'textfield',
    items:[{
		fieldLabel: '模板名称',
        name: 'name',
        allowBlank: false
	},{
		xtype:'textarea',
		fieldLabel: '模板内容',
		height:600,
        name: 'file.content',
        allowBlank: false
	}],
	initComponent:function(){
		var me = this;
		
		me.callParent();
	}
});