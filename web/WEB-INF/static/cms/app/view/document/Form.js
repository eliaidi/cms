Ext.define('MyCms.view.document.Form',{
	extend : 'Ext.form.Panel',
	requires:['MyCms.view.ux.MyHtmlEditor'],
	bodyPadding: 5,
	initComponent:function(){
		
		var me = this;
		
		var htmleditor = Ext.create('MyCms.view.ux.MyHtmlEditor',{
			fieldLabel: '内容',
	        name: 'content',
	        height:350,
	        allowBlank: false
		});
		
		Ext.apply(me,{
			layout: 'anchor',
		    defaults: {
		        anchor: '100%',
		        margin:'10px auto'
		    },
		    defaultType: 'textfield',
			items:[{
				fieldLabel: '标题',
		        name: 'title',
		        allowBlank: false
			},{
				xtype:'container',
				defaultType: 'textfield',
				layout:{
					type: 'hbox',
	    	        align: 'stretch'
				},
				border:false,
				items:[{
					fieldLabel: '作者',
			        name: 'author',
			        allowBlank: true,
			        flex:1
				},{
					xtype:'datefield',
					format:'Y-m-d',
					fieldLabel: '撰写时间',
			        name: 'writeTime',
			        allowBlank: true
				}]
			},{
				xtype: 'htmleditor',
				fieldLabel: '摘要',
		        name: 'abst',
		        height:118,
		        allowBlank: true
			},htmleditor,{
				xtype: 'hidden',
		        name: 'appIds',
		        height:350,
		        allowBlank: true
			}]
		});
		me.callParent();
	}
});