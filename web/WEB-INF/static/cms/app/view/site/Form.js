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
		var allFields = [{
				fieldLabel: '站点名称',
		        name: 'name',
		        allowBlank: false
			},{
				fieldLabel: '站点显示名称',
		        name: 'descr',
		        allowBlank: false
			},{
				fieldLabel: '站点发布目录',
		        name: 'folder',
		        allowBlank: false
			},{
				fieldLabel: '站点发布地址',
		        name: 'url',
		        allowBlank: false
			}];
		var pubStaFields = [];
		for(var k in MyCms.model.Document.StatusMapping){
			pubStaFields.push({
				 boxLabel  : MyCms.model.Document.StatusMapping[k],
                 name      : 'canPubSta',
                 inputValue: k,
                 id        : 'status'+k,
                 flex:1,
                 checked:MyCms.model.Site.DefaultPubSta[k]
			});
		}
		pubStaFields = {
				xtype: 'fieldcontainer',
	            fieldLabel: '可发布状态',
	            defaultType: 'checkboxfield',
	            layout:'hbox',
	            items: pubStaFields
		};
		allFields.push(pubStaFields);
		
		Ext.apply(me,{
			items:allFields
		});
		me.callParent();
	}
});