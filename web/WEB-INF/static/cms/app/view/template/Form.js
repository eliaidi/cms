Ext.define('MyCms.view.template.Form',{
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
				xtype : 'fieldcontainer',
				defaultType: 'textfield',
				layout : 'hbox',
				items:[{
					fieldLabel: '模板名称',
			        name: 'name',
			        allowBlank: false,
			        flex :1
				},{
					xtype:'combobox',
					fieldLabel: '模板类型',
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
				}]
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
			}]
		});
		me.callParent();
	},
	getTypeDate:function(){
		
		var me = this,typeObj = MyCms.model.Template.Type,data = [];
		
		for(var k in typeObj){
			data.push({
				id : k,
				name : typeObj[k]
			});
		}
		return data;
	}
});