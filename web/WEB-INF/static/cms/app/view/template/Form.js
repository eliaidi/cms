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
		xtype : 'fieldcontainer',
		defaultType: 'textfield',
		layout : 'hbox',
		items:[{
			fieldLabel: '前缀',
	        name: 'prefix',
	        flex :1,
	        allowBlank: false,
	        value : 'index'
		},{
			fieldLabel: '后缀',
	        name: 'ext',
	        flex :1,
	        allowBlank: false,
	        value : 'html'
		}]
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