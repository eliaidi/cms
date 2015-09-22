Ext.define('MyCms.view.document.Window',{
	extend: 'MyCms.view.ux.MyWindow',
	id:'document-window',
	title : '新增文档',
	layout: 'anchor',
	width : 896,
	height : 690,
	border: false,
    requires: [
        'MyCms.view.ux.MyWindow',
        'Ext.form.Panel',
        'MyCms.view.document.Form',
        'MyCms.model.Document',
        'MyCms.view.appendix.Window',
        'MyCms.view.ux.RemoteDocWin'
    ],

    initComponent : function(){
    	
    	var me = this;
    	if(me.channel){
    		me.title = '新增栏目【'+me.channel.get('name')+'】下的文档';
    	}
    	
    	me.form = Ext.create('MyCms.view.document.Form');
    	
    	Ext.apply(me,{
    		items:[me.form,{
    			xtype:'button',
    			text: '扩展字段',
    			margin:'5px 10px',
    			width:237,
	            handler:'fieldValueMgt',
	            scope:me
    		},{
    			xtype:'button',
    			text: '附件管理',
    			margin:'5px 10px',
    			width:237,
	            handler:'onAppendixMgt',
	            scope:me
    		},{
    			xtype:'button',
    			text: '加载远程文档',
    			margin:'5px 10px',
    			width:237,
	            handler:'loadRemoteDoc',
	            scope:me
    		}],
    		buttons:[{
    			text:'确定',
    			handler:'onOk',
    			scope:me
    		},{
    			text:'重置',
    			handler:'okReset',
    			scope:me
    		}]
    	});
    	
    	if(me.document){//编辑
    		me.title = '修改文档【'+me.document.get('title')+'】';
    		me.loadDoc();
    	}
    	
    	me.on('remoteComplete',me.remoteDocComplete);
    	me.on('extFieldComplete','doExtFieldComplete',me);
    	
    	this.callParent();
    },
    clearFieldValues:function(){
    	var me = this,form = me.form.getForm(),fields = form.getFields();
    	
    	fields.each(function(f){
    		if(f.name.indexOf('fieldValues')>=0&&f.hidden){
    			me.form.remove(f);
    		}
    	});
    },
    doExtFieldComplete:function(me,fieldWin,rs){
    	
    	me.clearFieldValues();
    	var hfs = [],index = 0;
    	for(var i=0;i<rs.length;i++,index++){
    		var r = rs[i];
    		for(var k in r){
    			hfs.push({
    				xtype:'hidden',
			        name: 'fieldValues['+(index)+'].'+k,
			        value:r[k]
    			})
    		}
    	}
    	if(fieldWin.extraValue){
    		for(var k in fieldWin.extraValue){
    			for(var i=0;i<fieldWin.extraValue[k].length;i++,index++){
    	    		var r = fieldWin.extraValue[k][i];
    	    		for(var k1 in r){
    	    			hfs.push({
    	    				xtype:'hidden',
    				        name: 'fieldValues['+(index)+'].'+k1,
    				        value:r[k1]
    	    			});
    	    		}
    	    	}
    		}
    	}
    	
    	me.form.add(hfs);
    	
    	fieldWin.close();
    },
    fieldValueMgt:function(){
    	var me = this;
    	
    	Ext.create('MyCms.view.fieldvalue.Window',{
    		from:me,
    		channel:me.channel,
    		document:me.document
    	}).show();
    },
    loadDoc:function(){
    	var me = this;
    	
    	me.form.loadRecord(me.document);
    	
    	if(me.document){
    		var fvs = me.document.get('fieldValues');
    		console.log('fvs::',fvs);
    		if(fvs&&fvs.length>0){
    			for(var i=0;i<fvs.length;i++){
    				var fv = fvs[i];
    				
    				me.form.add({
    					xtype:'hidden',
				        name: 'fieldValues['+(i)+'].value',
				        value:fv.value
    				});
    				me.form.add({
    					xtype:'hidden',
				        name: 'fieldValues['+(i)+'].field.id',
				        value:fv.field.id
    				});
    				me.form.add({
    					xtype:'hidden',
				        name: 'fieldValues['+(i)+'].extField.id',
				        value:fv.extField.id
    				});
    				if(fv.extField.field.custom){
    					me.form.add({
        					xtype:'hidden',
    				        name: 'fieldValues['+(i)+'].group',
    				        value:fv.group
        				});
    				}
    			}
    		}
    	}
    	
    },
    remoteDocComplete:function(me,doc){
    	me.form.getForm().loadRecord(new MyCms.model.Document(doc));
    },
    loadRemoteDoc:function(){
    	var me = this;
    	var win = Ext.create('MyCms.view.ux.RemoteDocWin',{
    		view:me
    	});
    	win.show();
    },
    onAppendixMgt:function(){
    	var me = this;
    	
    	if(!me.appWin){
    		me.appWin = Ext.create('MyCms.view.appendix.Window',{
    			view:me,
    			document:me.document
    		});
    	}
    	me.appWin.show();
    },
    onOk : function(){
    	var me = this,pms;
    	pms = me.document?{id:me.document.get('id')}:(me.channel?{channelId:me.channel.get('id')}:{});
    	me.form.getForm().submit({
    		clientValidation: true,
    	    url: document_save,
    	    params:pms,
            success: 'onSuccess',
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            },
            scope:me
        });
    },
    okReset : function(){
    	var me = this;
    	
    	if(me.document){
    		me.form.getForm().loadRecord(me.document);
    	}else{
    		me.form.getForm().reset();
    	}
    },
    onSuccess : function(){
    	var me = this;
    	
    	me.view.fireEvent('refresh',me.view,me);
    	me.close();
    }
});