Ext.define('MyCms.view.appendix.Window',{
	extend:'Ext.window.Window',
	uses:['Ext.form.Panel',
	      'Ext.tab.Panel',
	      'MyCms.model.Appendix',
	      'Ext.grid.plugin.CellEditing',
	      'Ext.grid.plugin.RowEditing'],
	modal:true,
	width:900,
	height:615,
	title:'附件管理',
	initComponent:function(){
		var me = this;
		
		me.addPanel = Ext.create('Ext.form.Panel',{
			title:'添加附件',
			flex:2,
			border:true,
			margin:'5px 5px',
			layout:'anchor',
			defaults:{
				anchor: '100%',
		        margin:'10px auto',
				allowBlank:false
			},
			items:[{
				xtype:'textfield',
				fieldLabel:'附件描述',
				name:'addition'
			},{
				xtype:'combobox',
				fieldLabel: '附件类型',
				name:'type',
			    store: Ext.create('Ext.data.Store', {
			        fields: ['value', 'name'],
			        data : me.formatTypeForCombo()
			    }),
			    queryMode: 'local',
			    displayField: 'name',
			    valueField: 'value'
			},{
				xtype: 'filefield',
		        name: 'file',
		        fieldLabel: '文件',
		        buttonText: '选择文件'
			},{
				xtype:'button',
				text:'上传',
				handler:'onUpload',
				scope:me
			}]
		});
		
		
		me.showPanel = Ext.create('Ext.tab.Panel',{
			flex:3,
			border:true,
			margin:'5px 5px',
			items:me.getAppendixGrids()
		});
		
		Ext.apply(me,{
			layout:{
				type: 'vbox',
    	        align: 'stretch'
			},
			items:[me.addPanel,{
				xtype:'panel',
				flex:3,
				margin:'5px 5px',
				title:'已添加附件',
				layout:{
					type: 'hbox',
	    	        align: 'stretch'
				},
				items:[me.showPanel,{
					xtype:'form',
					flex:2,
					layout:'anchor',
					margin:'5px 5px',
					defaults:{
						xtype:'textfield',
						anchor:'95%'
					},
					items:[{
						name:'id',
						xtype:'hidden'
					},{
						fieldLabel:'名称',
						name:'fileName',
						allowBlank:false
					},{
						xtype:'textarea',
						fieldLabel:'描述',
						name:'addition'
					},{
						fieldLabel:'大小（Byte）',
						name:'fileSize',
						readOnly:true
					},{
						xtype:'container',
						layout:'hbox',
						items:[{
							xtype:'textfield',
							fieldLabel:'附件地址',
							name:'url',
							flex:2,
							readOnly:true
						},{
							xtype:'button',
							flex:1,
							text:'插入到正文',
							handler:'insertApp2Doc',
							scope:me
						}]
					},{
						fieldLabel:'创建人',
						name:'crUser',
						readOnly:true
					},{
						fieldLabel:'创建时间',
						name:'crTime',
						readOnly:true
					},{
						xtype:'container',
						layout:{
							type:'fit',
							align:'center'
						},
						items:[{
							xtype:'button',
							text:'保存',
							handler:'updateApp',
							scope:me
						}]
					}]
				}]
			}],
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
	
		me.on('beforedestroy','onDestroy',me);
		me.callParent();
	},
	insertApp2Doc:function(_this){
		var me = this;
		
		var urlValue = _this.up('form').getForm().findField('url').getValue();
		if(!Ext.isEmpty(urlValue)){
			var conField = me.view.down('form').getForm().findField('content');
			conField.setValue(conField.getValue()+'<img src="'+urlValue+'" />');
		}
	},
	updateApp:function(_this){
		var me = this;
		
		var form = _this.up('form');
		form.getForm().submit({
    		clientValidation: true,
    	    url: appendix_save,
            success: function(form, action) {
               Ext.Msg.alert('成功', action.result.message,'onSuccess',me);
            },
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            }
        });
	},
	onUpload:function(){
		var me = this;
		
		me.addPanel.getForm().submit({
    		clientValidation: true,
    	    url: appendix_save,
//    	    params:me.document?{documentId:me.document.get('id')}:null,
            success: function(form, action) {
            	
            	var fileType = action.result.obj.type;
            	var app = new MyCms.model.Appendix(action.result.obj);
            	
            	Ext.getCmp('appendix-tabgrid-'+fileType).getStore().add(app);
            	Ext.Msg.alert('成功', action.result.message);
            },
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            }
        });
	},
	onDestroy:function(){
		var me = this;
		
		me.view.appWin = null;
	},
	onOk:function(){
		var me = this;
		
		var appIds = [];
		me.getAllRecords().forEach(function(r){
			appIds.push(r.get('id'));
		});
		me.view.form.getForm().findField('appIds').setValue(appIds.join(','));
		me.close();
	},
	onCancel:function(){
		
	},
	formatTypeForCombo:function(){
		var me = this;
		
		var items = []
		for( var t in MyCms.model.Appendix.TypeMapping){
			items.push({
				name:MyCms.model.Appendix.TypeMapping[t],
				value:t
			});
		}
		return items;
	},
	getAllRecords:function(){
		var me = this;
		
		var rs = [];
		for( var t in MyCms.model.Appendix.TypeMapping){
			
			Ext.getCmp('appendix-tabgrid-'+t).getStore().each(function(r){
				rs.push(r);
			});
		}
		return rs;
	},
	getAppendixGrids:function(){
		var me = this;
		
		var items = [];
		for( var t in MyCms.model.Appendix.TypeMapping){
			items.push({
				xtype:'grid',
				id:'appendix-tabgrid-'+t,
				title:MyCms.model.Appendix.TypeMapping[t],
				selModel: {
			        selType: 'checkboxmodel'
			    },
			    _type:t,
				multiSelect : true,
				viewConfig : {
					trackOver : false,
					emptyText : '<h1 style="margin:20px">未找到任何记录！！！</h1>'
				},
				store: Ext.create('Ext.data.Store', {
					model : 'MyCms.model.Appendix',
					data : []
				}),
				columns : [ {
					xtype : 'rownumberer',
					width : 50,
					sortable : false
				},{
					text : "文件名",
					dataIndex : 'fileName',
					flex : 3,
					sortable : false
				}, {
					text : "说明",
					dataIndex : 'addition',
					flex : 3,
					sortable : false
				}, {
					text : "创建人",
					dataIndex : 'crUser',
					flex : 1,
					sortable : false
				}, {
					text : "创建时间",
					dataIndex : 'crTime',
					align : 'center',
					flex : 2,
					sortable : false
				} ],
				listeners:{
					"afterrender":function(){
						var grid = this;
						if(me.document&&!grid.hasLoadData){
							Ext.Ajax.request({
								url : appendix_list,
								params : {
									documentId : me.document.get('id'),
									type:grid._type
								},
								success : function(response, opts) {
									grid.hasLoadData = true;
									var obj = Ext.decode(response.responseText);
									if(obj&&obj.list){
										grid.getStore().add(obj.list);
									}
									
								},
								failure : function(response, opts) {
									grid.hasLoadData = true;
									console.log('server-side failure with status code '
											+ response.status);
								}
							});
						}
					},
					"itemclick":function(_this, record, item, index, e, eOpts){
						var grid = this;
						
						grid.up('tabpanel').ownerCt.down('form').loadRecord(record);
					},
					"itemcontextmenu":function( _this, record, item, index, e, eOpts){
						
						Ext.create('Ext.menu.Menu',{
							items:[{
								text:'复制图片地址',
								handler:function(){
									me.gridItemCopy(record);
								},
								scope:me
							},{
								text:'删除',
								handler:function(){
									me.deleteApp(record);
								},
								scope:me
							}]
						}).showAt(e.getXY());
						
						e.stopEvent();
						e.stopPropagation();
					}
				}
			});
		}
		
		return items;
	},
	gridItemCopy:function(record){
		try {
			MyCms.Application.copyToClipboard("file/app/"+record.get('id'));
			
		} catch (e) {
			alert(e);
		}
	},
	deleteApp:function(record){
		
	}
	
	
});