Ext.define('MyCms.view.resource.Form',{
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
				fieldLabel: '名称',
		        name: 'name',
		        allowBlank: false
			},{
				fieldLabel: '值',
		        name: 'value',
		        allowBlank: false
			},{
				xtype:'combobox',
				fieldLabel: '类型',
				name:'type',
				forceSelection:true,
			    store: Ext.create('Ext.data.Store', {
			        fields: [ 'id','name'],
			        data : me.getTypeData()
			    }),
			    queryMode: 'local',
			    displayField: 'name',
			    allowBlank: false,
			    valueField: 'id'
			}]
		});
		me.callParent();
	},
	getTypeData:function(){
		
		var data = [];
		
		for(var i in MyCms.model.Resource.Types){
			data.push({
				id:i,
				name:MyCms.model.Resource.Types[i]
			});
		}
		return data;
	}
});