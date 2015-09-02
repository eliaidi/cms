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
		        name: 'name',
		        allowBlank: false
			},{
				fieldLabel: '字段名称',
		        name: 'field.name',
		        allowBlank: false
			},{
				xtype:'combobox',
				fieldLabel: '字段类型',
				name:'field.type',
				forceSelection:true,
				store: Ext.create('Ext.data.Store', {
                	fields: [
                	         {name: 'id', type: 'string'},
                	         {name: 'name',  type: 'string'}
                	     ],
                    proxy: {
                        type: 'ajax',
                        url: field_list_type,
                        extraParams:{
                        	siteId : me.from.from.from.channel.get('site').id
                        },
                        reader: {
                            type: 'json',
                            rootProperty: 'types'
                        }
                    }
                }),
                queryMode: 'remote',
                displayField: 'name',
                valueField: 'id',
                allowBlank: false
			},{
				fieldLabel: '字段长度',
		        name: 'field.length',
		        allowBlank: true
			}]
		});
		me.callParent();
	}
});