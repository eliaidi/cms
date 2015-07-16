Ext.define('MyCms.view.channel.Form',{
	extend : 'Ext.form.Panel',
	uses:['Ext.form.Panel'],
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
				fieldLabel: '栏目名称',
		        name: 'name',
		        allowBlank: false
			},{
				fieldLabel: '栏目显示名称',
		        name: 'descr',
		        allowBlank: false
			},{
				fieldLabel: '栏目发布目录',
		        name: 'folder',
		        allowBlank: false
			}]
		});
		me.callParent();
	}
});