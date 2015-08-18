Ext.define('MyCms.view.ux.MyHtmlEditor',{
	extend:'Ext.form.field.HtmlEditor',
	requires:['Ext.form.Panel'],
	initComponent:function(){
		
		var me = this;
		
		me.callParent();
	},
	createToolbar: function(){
		var me = this,baseCSSPrefix=Ext.baseCSSPrefix ;
		
        this.toolbar = Ext.widget(this.getToolbarCfg());
        this.toolbar.insert(5,{
        	xtype:'button',
        	tooltip:'添加图片',
        	cls: baseCSSPrefix + 'btn-icon',
            iconCls: baseCSSPrefix + 'edit-uploadPic',
            handler: 'showAdd',
            scope:me
        });
        return this.toolbar;
    },
    showAdd:function(_this,e){
    	var me = this;
    	
    	var win = Ext.create('MyCms.view.ux.MyWindow',{
    		title:'添加图片',
    		modal:true,
    		width:433,
    		height:261,
    		items:[me.createUploadPicPanel()],
    		buttons:[{
    			text:'确定',
    			handler:'onOk',
    			scope:me
    		},{
    			text:'取消',
    			handler:'onCancel',
    			scope:me
    		}]
    	});
    	win.show();
    },
    onOk:function(_this){
//    	var me = this;
//    	
//    	var form = _this.up('window').down('form').getForm();
//    	if(form.isValid()){
//    		me.insertAtCursor('<img src="'+form.findField('url').getValue()+'" width="'+form.findField('width').getValue()+'" height="'+form.findField('height').getValue()+'" />');
//    		_this.up('window').close();
//    	}
    	var me = this,window = _this.up('window'),tab = window.down('tabpanel'),currForm = tab.getActiveTab();
    	console.log(currForm);
    	if(currForm.title == '添加远程图片'){
    		
    	}else{
    		
    	}
    },
    onCancel:function(_this){
    	_this.up('window').close();
    },
    createUploadPicPanel:function(){
    	var me = this;
    	
    	return Ext.create('Ext.tab.Panel',{
    		items:[{
    			title:'添加远程图片',
    			xtype:'form',
    			layout:'anchor',
        		margin:'10px 10px',
        		defaults:{
        			xtype:'textfield',
        			allowBlank:false,
        			anchor:'100%'
        		},
        		items:[{
        			fieldLabel:'图片地址',
        			name:'url'
        		},{
        			fieldLabel:'图片宽度',
        			name:'width'
        		},{
        			fieldLabel:'图片高度',
        			name:'height'
        		}]
    		},{
    			title:'上传本地图片',
    			xtype:'form',
    			layout:'anchor',
        		margin:'10px 10px',
        		defaults:{
        			xtype:'textfield',
        			allowBlank:false,
        			anchor:'100%'
        		},
        		items:[{
        			xtype: 'filefield',
    		        name: 'file',
    		        fieldLabel: '文件',
    		        buttonText: '选择文件'
        		}]
    		}]
    	});
    }
	
});