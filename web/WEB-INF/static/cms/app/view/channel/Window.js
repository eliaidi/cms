Ext.define('MyCms.view.channel.Window',{
	extend: 'MyCms.view.ux.MyWindow',
	id:'channel-window',
	title : '新增栏目',
	layout: 'anchor',
	width : 601,
	height : 262,
	border: false,
    uses: [
        'MyCms.view.ux.MyWindow',
        'Ext.form.Panel',
        'MyCms.view.channel.Form',
        'MyCms.model.Channel'
    ],

    initComponent : function(){
    	
    	var me = this;
    	if(me.parent){
    		me.title = '新增栏目【'+me.parent.get('name')+'】下的栏目';
    	}else if(me.site){
    		me.title = '新增站点【'+me.site.get('name')+'】下的栏目';
    	}
    	
    	me.form = Ext.create('MyCms.view.channel.Form');
    	
    	Ext.apply(me,{
    		items:[me.form],
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
    	
    	if(me.channel){//编辑
    		me.title = '修改栏目【'+me.channel.get('name')+'】';
    		me.form.getForm().loadRecord(me.channel);
    	}
    	
    	this.callParent();
    },
    onOk : function(){
    	var me = this;
    	me.form.getForm().submit({
    		clientValidation: true,
    	    url: channel_save,
    	    params:me.channel?{id:me.channel.get('id')}:(me.parent?{parentId:me.parent.get('id')}:(me.site?{siteId:me.site.get('id')}:null)),
            success: 'onSuccess',
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            },
            scope:me
        });
    },
    okReset : function(){
    	var me = this;
    	
    	if(me.channel){
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