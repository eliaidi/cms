Ext.define('MyCms.view.document.Form',{
	extend : 'MyCms.view.ux.MyForm',
	requires:['MyCms.view.ux.MyHtmlEditor','MyCms.view.ux.DatetimeField'],
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
//			        cls:'Wdate',
			        allowBlank: true,
			        listeners:{
			        	focus:function(_this,_e){
			        		//WdatePicker();
			        	}
			        }
				}]
			},{
				xtype: 'fckeditor',
				fieldLabel: '摘要',
		        name: 'abst',
		        height:150,
		        allowBlank: true
			},{
				xtype:'fckeditor',
				fieldLabel: '内容',
		        name: 'content',
		        height:300,
		        allowBlank: false
			},{
				xtype: 'hidden',
		        name: 'appIds',
		        height:350,
		        allowBlank: true
			}]
		});
		me.callParent();
	}
});