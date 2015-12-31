Ext.define('MyCms.view.role.Form',{
	extend : 'MyCms.view.ux.MyForm',
	bodyPadding: 5,
	initComponent:function(){
		
		var me = this;
		
		Ext.apply(me,{
			layout: 'anchor',
		    defaults: {
		        anchor: '100%',
		        margin:'10px auto'
		    },
		    defaultType: 'textfield',
			items:[{
				fieldLabel: '角色名称',
		        name: 'name',
		        allowBlank: false
			},{
				xtype:'checkboxfield',
				boxLabel: '是否管理员角色',
		        name: 'isAdmin',
		        inputValue:'true',
		        allowBlank: true
			}]
		});
		me.callParent();
	}
});