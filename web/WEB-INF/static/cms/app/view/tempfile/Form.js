Ext.define('MyCms.view.tempfile.Form',{
	extend:'Ext.form.Panel',
	bodyPadding: 5,
	layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    defaultType: 'textfield',
	initComponent:function(){
		var me = this;
		
		Ext.apply(me,{
			items:[{
				xtype:'textarea',
				fieldLabel: '模板内容',
				height:600,
		        name: 'file.content',
		        allowBlank: false
			}]
		});
		me.callParent();
	}
});