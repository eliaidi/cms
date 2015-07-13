Ext.define('MyCms.view.site.Window',{
	extend: 'Ext.Window',
	title : '新增站点',
	layout: 'anchor',
	width : 470,
	height : 200,
//	modal: true,
	border: false,
	maximizable:true,
	minimizable:true,
    uses: [
        'Ext.Window',
        'Ext.form.Panel',
        'MyCms.view.site.Form',
        'MyCms.model.Site'
    ],
    id:'site',

    initComponent : function(){
    	
    	var me = this;
    	
    	me.form = Ext.create('MyCms.view.site.Form');
    	
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
    	
    	if(me.site){//编辑站点
    		me.title = '修改站点【'+me.site.get('name')+'】';
    		Ext.Ajax.request({
    		    url: site_detail,
    		    params : {siteId:me.site.get('id')},
    		    success: function(response, opts) {
    		        var obj = Ext.decode(response.responseText);
    		        if(!obj.success){
    		        	Ext.Msg.alert('错误',obj.message);
    		        	return;
    		        }
    		        me.site = new MyCms.model.Site(obj.obj);
    		        //me.form.getForm().loadRecord(me.site);
    		        me.loadForm();
    		    },
    		    failure: function(response, opts) {
    		        console.log('server-side failure with status code ' + response.status);
    		    }
    		});
    	}
    	
    	this.callParent();
    },
    loadForm:function(){
    	var me = this;
    	
    	if(me.site){
    		me.form.getForm().loadRecord(me.site);
    	}
    	
    },
    onOk : function(){
    	var me = this;
    	me.form.getForm().submit({
    		clientValidation: true,
    	    url: site_save,
    	    params:me.site?{id:me.site.get('id')}:null,
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
    	
    	if(me.site){
    		me.form.getForm().loadRecord(me.site);
    	}else{
    		me.form.getForm().reset();
    	}
    },
    onSuccess : function(){
    	var me = this;
    	
    	me.desktop.fireEvent('refresh',me.desktop,me);
    	me.close();
    }
});