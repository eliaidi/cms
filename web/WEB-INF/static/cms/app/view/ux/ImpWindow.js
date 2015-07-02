Ext.define('MyCms.view.ux.ImpWindow',{
	extend:'Ext.window.Window',
	width:463,
	height:140,
	title:'批量导入',
	modal:true,
	initComponent:function(){
		var me = this;
		
		Ext.apply(me,{
			items:[{
    			xtype:'form',
    			layout:'anchor',
    			margin:'10px 5px',
    			items:[{
    				xtype:'filefield',
    				fieldLabel:'上传txt文件',
    				name:'file',
    				anchor:'100%',
    				allowBlank: false,
    				buttonText: '选择文件'
    			}]
    		}],
    		buttons:[{
    			text:'确定',
    			handler:'doImp',
    			scope:me
    		},{
    			text:'取消',
    			handler:function(_this){
    				me.close();
    			},
    			scope:me
    		}]
		})
		me.callParent();
	},
	doImp:function(){
		var me = this;
    	
    	me.down('form').getForm().submit({
    		clientValidation: true,
    	    url: me.url,
    	    params:me.params,
            success: function(form, action) {
               Ext.Msg.alert('成功', action.result.message,'doSuccess',me);
            },
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            }
        });
	},
	doSuccess:function(){
		var me = this;
		
		me.view.fireEvent('refresh',me.view);
		me.close();
	}
});