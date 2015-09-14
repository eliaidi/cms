Ext.define('MyCms.view.fieldvalue.FieldWin',{
	extend:'MyCms.view.ux.MyWindow',
	modal:true,
	width:765,
	height:467,
	title:'编辑字段内容',
	initComponent:function(){
		var me = this;
		
		Ext.Ajax.request({
			url : field_detail,
			params : {
				id : me.customType
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if (!obj.success) {
					Ext.Msg.alert('错误', obj.message);
					return;
				}
				me.renderGrid(obj.obj);
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
		var btns = [{
			xtype:'button',
			text:'确定',
			handler:'onOk',
			scope:me
		},{
			xtype:'button',
			text:'取消',
			handler:'onCancel',
			scope:me
		}];
		Ext.apply(me,{
			buttons:btns
		});
		
		me.callParent();
	},
	onCancel:function(){
		this.close();
	},
	onOk:function(){
		var me = this,grid = me.down('grid'),store = grid.getStore(),pWin = me.from.from;
		
		pWin.fireEvent('fieldWinOk',pWin,me,store);
	},
	renderGrid:function(f){
		var me = this,pWin = me.from.from,gridColumn = [],fields = f.children,fs = [];
		
		for(var i=0;i<fields.length;i++){
			var field = fields[i],item = {};
			
			fs.push({
				name : field.id,
				type:field.type
			});
			item.editor = {};
			if(field.type=='int'||field.type=='float'){
				item = {
						xtype: 'numbercolumn',
			            editor: {
			            	xtype: 'numberfield'
			            }	
				};
			}else if(field.type=='date'){
				item = {
						xtype: 'datecolumn',
			            format:'Y-m-d',
			            editor: {
			            	xtype: 'datefield',
			                format:'Y-m-d'
			            }	
				};
			}else{
				item.editor.xtype = 'textfield';
			}
			item.header = field.name;
			item.dataIndex = field.id;
			item.width = 135;
			item.editor.vtype = 'customField';
			item.editor.maxLength = field.length;
			
			gridColumn.push(item);
		}
		if(typeof FieldModel =='undefined'){
			Ext.define('FieldModel',{
				extend:'Ext.data.Model',
				fields:fs
			});
		}else{
			Ext.apply(FieldModel,{
				fields:fs
			});
		}
		
		var store = Ext.create('Ext.data.Store', {
	        autoDestroy: true,
	        model: 'FieldModel',
	        proxy: {
	            type: 'memory'
	        },
	        data: []
	    });
		var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	        clicksToMoveEditor: 2,
	        autoCancel: false
	    });
		
		me.add({
			xtype:'gridpanel',
			store: store,
	        columns: gridColumn,
	        tbar: [{
	            text: '添加属性',
	            iconCls: 'employee-add',
	            handler : function() {
	                rowEditing.cancelEdit();
	
	                store.insert(0, Ext.create('FieldModel'));
	                rowEditing.startEdit(0, 0);
	            }
	        }, {
	            itemId: 'removeEmployee',
	            text: '删除属性',
	            iconCls: 'employee-remove',
	            handler: function() {
	                var grid = me.down('grid'),r = grid.getSelectionModel().getSelection();
	                if(r.length==0){
	                	Ext.Msg.alert('错误','请选择待删除的记录！');
	                	return;
	                }
	                rowEditing.cancelEdit();
	                store.remove(r);
	                if (store.getCount() > 0) {
	                    sm.select(0);
	                }
	            }
	        }],
	        plugins: [rowEditing]
		});
		
		if(pWin.extraStore&&pWin.extraStore[me.extFieldId]){
			store.loadData(pWin.extraStore[me.extFieldId]);
		}
	}
});