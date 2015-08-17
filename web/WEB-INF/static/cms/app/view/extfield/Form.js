Ext.define('MyCms.view.extfield.Form',{
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
				fieldLabel: '字段中文名称',
		        name: 'label',
		        allowBlank: false
			},{
				fieldLabel: '字段名称',
		        name: 'name',
		        allowBlank: false
			},{
				xtype:'combobox',
				fieldLabel: '字段类型',
				name:'type',
				forceSelection:true,
			    store: Ext.create('Ext.data.Store', {
			        fields: [ 'id','name'],
			        data : me.getTypeDate()
			    }),
			    queryMode: 'local',
			    displayField: 'name',
			    allowBlank: false,
			    valueField: 'id'
			},{
				fieldLabel: '字段长度',
		        name: 'length',
		        allowBlank: true
			}]
		});
		me.callParent();
	},
	getTypeDate:function(){
		var ds = [];
		for(var k in MyCms.model.ExtField.Type){
			ds.push({
				id:k,
				name:MyCms.model.ExtField.Type[k]
			});
		}
		return ds;
	}
});