Ext.define('MyCms.view.channel.Form',{
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
				fieldLabel: '栏目名称',
		        name: 'name',
		        allowBlank: false
			},{
				fieldLabel: '栏目显示名称',
		        name: 'descr',
		        allowBlank: false
			},{
				fieldLabel: '栏目发布目录',
		        name: 'folder',
		        allowBlank: false
			},{
				xtype:'fieldcontainer',
    			fieldLabel:'概览模板',
    			defaultType: 'textfield',
    			layout:'hbox',
    			items:[{
    		        name: 'otempIds',
    		        allowBlank: true,
    		        flex:4,
    		        readOnly:true
    			},{
    				xtype:'button',
    				flex:1,
    				text:'选择',
    				handler:'chooseOTemp',
    				scope:me
    			}]
			},{
				xtype:'fieldcontainer',
    			fieldLabel:'细缆模板',
    			defaultType: 'textfield',
    			layout:'hbox',
    			items:[{
    		        name: 'dtempIds',
    		        allowBlank: true,
    		        flex:4,
    		        readOnly:true
    			},{
    				xtype:'button',
    				flex:1,
    				text:'选择',
    				handler:'chooseDTemp',
    				scope:me
    			}]
			}]
		});
		me.callParent();
	},
	chooseOTemp:function(){
		this.chooseTemp("outline");
	},
	chooseDTemp:function(){
		this.chooseTemp("detail");
	},
	chooseTemp:function(t){
		var me = this,fieldName = t=='outline'?'otempIds':'dtempIds',win = me.up('window'),site = win.parent?new MyCms.model.Site(win.parent.get('site')):win.site;
    	me.tempWin = Ext.create('MyCms.view.template.Window',{
    		site : site,
    		modal : true,
    		from : me,
    		showType : t,
    		buttons : [{
    			text : '确定',
    			handler : 'doChooseTemp',
    			scope : me
    		}],
    		initChooseData : me.getForm().findField(fieldName).getValue()
    	});
    	me.tempWin.show();
	},
	doChooseTemp:function(){
		var me = this,fieldName = me.tempWin.showType=='outline'?'otempIds':'dtempIds',rs = me.tempWin.down('grid').getSelectionModel().getSelection();;
		if(rs.length==0){
    		Ext.Msg.alert('错误','请选择模板！');
    		return;
    	}
    	var ids = [];
    	Ext.Array.each(rs,function(r){
    		ids.push(r.get('id'));
    	});
    	
    	var tempField = me.getForm().findField(fieldName);
    	tempField.setValue(ids.join(','));
    	
    	me.tempWin.close();
	}
});