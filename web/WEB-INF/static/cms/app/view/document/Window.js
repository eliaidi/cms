Ext.define('MyCms.view.document.Window',{
	extend: 'Ext.Window',
	id:'document-window',
	title : '新增文档',
	layout: 'anchor',
	width : 896,
	height : 690,
	border: false,
    uses: [
        'Ext.Window',
        'Ext.form.Panel',
        'MyCms.view.document.Form',
        'MyCms.model.Document',
        'MyCms.view.appendix.Window'
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
    			text: '附件管理',
    			margin:'5px 10px',
    			width:237,
	            handler:'onAppendixMgt',
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
    		me.form.getForm().loadRecord(me.document);
    	}
    	
    	this.callParent();
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
    	var me = this;
    	me.form.getForm().submit({
    		clientValidation: true,
    	    url: document_save,
    	    params:me.document?{id:me.document.get('id')}:(me.channel?{channelId:me.channel.get('id')}:null),
            success: function(form, action) {
               Ext.Msg.alert('成功', action.result.message,'onSuccess',me);
            },
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            }
        });
    },
    okReset : function(){
    	var me = this;
    	
    	if(me.document){
    		me.form.getForm().loadRecord(me.channel);
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