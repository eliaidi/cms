Ext.define('MyCms.view.ux.RemoteDocWin',{
	extend:'Ext.window.Window',
	uses:['Ext.form.Panel'
	      ],
	modal:true,
	width:645,
	height:133,
	title:'加载远程文档',
	initComponent:function(){
		var me = this;
		
		Ext.apply(me,{
			items:[{
				xtype:'form',
				layout:'anchor',
				margin:'5px 5px',
				items:[{
					xtype:'textfield',
					fieldLabel:'输入远程网址',
					anchor:'100%',
					name:'url',
					allowBlank:false
				}]
			}],
			buttons:[{
				text:'确定',
				handler:'sureLoad',
				scope:me
			}]
		});
		
		me.callParent();
	},
	sureLoad:function(){
		var me = this,form=me.down('form'),url=form.getForm().findField('url').getValue();
//		alert(url);
		
		form.getForm().submit({
    		clientValidation: true,
    	    url: document_remote,
            success: function(form, action) {
            	if(action.result.success){
            		me.view.fireEvent('remoteComplete',me.view,action.result.obj);
            		me.close();
            	}
               //Ext.Msg.alert('成功', action.result.message,'onSuccess',me);
            },
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            }
        });
	}
});