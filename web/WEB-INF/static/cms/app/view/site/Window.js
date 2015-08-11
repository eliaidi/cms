Ext.define('MyCms.view.site.Window',{
	extend: 'MyCms.view.ux.MyWindow',
	title : '新增站点',
	layout: 'anchor',
	width : 581,
	height : 306,
//	modal: true,
	border: false,
	maximizable:true,
	minimizable:true,
    uses: [
        'MyCms.view.ux.MyWindow',
        'Ext.form.Panel',
        'MyCms.view.site.Form',
        'MyCms.model.Site'
    ],
    id:'site',

    initComponent : function(){
    	
    	var me = this;
    	
    	me.form = Ext.create('MyCms.view.site.Form',{_parent : me});
    	
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
    		
    		me.form.add({
    			xtype:'fieldcontainer',
    			fieldLabel:'发布模板',
    			defaultType: 'textfield',
    			layout:'hbox',
    			items:[{
    		        name: 'tempIds',
    		        allowBlank: true,
    		        flex:4,
    		        readOnly:true
    			},{
    				xtype:'button',
    				flex:1,
    				text:'选择',
    				handler:'chooseTemp',
    				scope:me
    			}]
    		});
    		
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
    		
    		var staArr = me.site.get('canPubSta')?me.site.get('canPubSta').split(','):[];
    		for(var k in MyCms.model.Document.StatusMapping){
    			var staCheck = me.form.down('#status'+k);
    			staCheck.setValue(Ext.Array.contains(staArr,k));
    		}
    	}
    	
    },
    onOk : function(){
    	var me = this;
    	me.form.getForm().submit({
    		clientValidation: true,
    	    url: site_save,
    	    params:me.site?{id:me.site.get('id')}:null,
            success: 'onSuccess',
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            },
            scope:me
        });
    },
    okReset : function(){
    	var me = this;
    	
    	if(me.site){
    		me.loadForm();
    	}else{
    		me.form.getForm().reset();
    	}
    },
    onSuccess : function(){
    	var me = this;
    	
    	me.desktop.fireEvent('refresh',me.desktop,me);
    	me.close();
    },
    chooseTemp : function(){
    	var me = this;
    	me.tempWin = Ext.create('MyCms.view.template.Window',{
    		site : me.site,
    		modal : true,
    		from : me,
    		showType : 'outline',
    		buttons : [{
    			text : '确定',
    			handler : 'doChooseTemp',
    			scope : me
    		}],
    		initChooseData : me.form.getForm().findField('tempIds').getValue()
    	});
    	me.tempWin.show();
    },
    doChooseTemp : function(){
    	var me = this,rs = me.tempWin.down('grid').getSelectionModel().getSelection();
    	if(rs.length==0){
    		Ext.Msg.alert('错误','请选择模板！');
    		return;
    	}
    	var ids = [];
    	Ext.Array.each(rs,function(r){
    		ids.push(r.get('id'));
    	});
    	
    	var tempField = me.form.getForm().findField('tempIds');
    	tempField.setValue(ids.join(','));
    	
    	me.tempWin.close();
    }
});