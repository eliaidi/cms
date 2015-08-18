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
    doExtFieldComplete:function(me,fieldWin,rs){
    	var hfs = [];
    	for(var i=0;i<rs.length;i++){
    		var r = rs[i];
    		for(var k in r){
    			hfs.push({
    				xtype:'hidden',
			        name: 'fieldValues['+i+'].'+k,
			        value:r[k]
    			})
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
    	
    	Ext.Ajax.request({
			url : document_detail,
			params : {
				docId : me.document.get('id')
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if (!obj.success) {
					Ext.Msg.alert('错误', obj.message);
					return;
				}
				me.document = new MyCms.model.Document(obj.obj);
				me.form.getForm().loadRecord(me.document);
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
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