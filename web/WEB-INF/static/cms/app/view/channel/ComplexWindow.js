Ext.define('MyCms.view.channel.ComplexWindow',{
	extend: 'Ext.Window',
	layout: 'anchor',
	width : 1137,
	height : 613,
	border: false,
    uses: [
        'MyCms.view.channel.View',
        'MyCms.view.document.Grid'
    ],

    initComponent : function(){
    	
    	var me = this;
    	me.title = '栏目【'+me.channel.get('name')+'】';
    	
    	me.view = Ext.create('MyCms.view.channel.View',{
    		flex:2,
    		title:'子栏目',
    		parent:me.channel,
    		desktop:me.desktop
    	});
    	
    	me.grid = Ext.create('MyCms.view.document.Grid',{
    		flex:3,
    		channel:me.channel,
    		desktop:me.desktop
    	});
    	
    	Ext.apply(me,{
    		layout:{
    			type: 'hbox',
    	        align: 'stretch'
    		},
    		items:[me.view,me.grid]
    	});
    	
    	me.on('afterrender','doAfterRender',me);
    	
    	this.callParent();
    },
    doAfterRender:function(){
    	var me = this;
    	
    },
    onOk : function(){
    	var me = this;
    	me.form.getForm().submit({
    		clientValidation: true,
    	    url: channel_save,
    	    params:me.channel?{id:me.channel.get('id')}:(me.parent?{parentId:me.parent.get('id')}:(me.site?{siteId:me.site.get('id')}:null)),
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