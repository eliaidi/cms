Ext.define('MyCms.view.user.Form',{
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
				fieldLabel: '用户名',
		        name: 'username',
		        allowBlank: false
			},{
				fieldLabel: '真实姓名',
		        name: 'truename',
		        allowBlank: true
			},{
				fieldLabel: 'Email',
		        name: 'email',
		        allowBlank: true
			}]
		});
		me.callParent();
	}
});