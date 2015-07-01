Ext.define('MyCms.view.site.Form',{
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
				fieldLabel: '站点名称',
		        name: 'name',
		        allowBlank: false
			},{
				fieldLabel: '站点显示名称',
		        name: 'descr',
		        allowBlank: false
			},{
				fieldLabel: '站点发布地址',
		        name: 'url',
		        allowBlank: false
			}]
		});
		me.callParent();
	}
});