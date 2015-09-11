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
	onOk:function(){
		var me = this,grid = me.down('grid'),store = grid.getStore(),pWin = me.from.from,rs = [],group = 0;
		
		store.each(function(r){
			for(var k in r.data){
				if(k=='id'){
					continue;
				}
				var v = r.get(k);
				if(typeof v =='object'){
					v = Ext.Date.format(new Date(v.getTime()),'Y-m-d');
				}
				rs.push({
					"field.id":k,
					"extField.id":me.extFieldId,
					"value":v,
					"group":group
				})
			}
			group++;
		});
		if(!pWin.extraValue){
			pWin.extraValue = {};
		}
		pWin.extraValue[me.extFieldId] = rs;
		me.close();
	},
	renderGrid:function(f){
		var me = this,gridColumn = [],fields = f.children,fs = [];
		
		for(var i=0;i<fields.length;i++){
			var field = fields[i],item;
			
			fs.push({
				name : field.id,
				type:field.type
			});
			if(field.type=='int'||field.type=='float'){
				item = {
						xtype: 'numbercolumn',
			            header: field.name,
			            dataIndex: field.id,
			            width: 135,
			            editor: {
			            	xtype: 'numberfield',
			                allowBlank: false,
			                minValue: 1,
			                maxValue: 150000
			            }	
				};
			}else if(field.type=='date'){
				item = {
						xtype: 'datecolumn',
			            header: field.name,
			            dataIndex: field.id,
			            format:'Y-m-d',
			            width: 135,
			            editor: {
			            	xtype: 'datefield',
			                allowBlank: false,
			                format:'Y-m-d'
			            }	
				};
			}else if(field.type=='text'){
				item = {
			            header: field.name,
			            dataIndex: field.id,
			            width: 135,
			            editor: {
			            	xtype: 'textarea',
			                allowBlank: false
			            }	
				};
			}else{
				item = {
			            header: field.name,
			            dataIndex: field.id,
			            width: 135,
			            editor: {
			            	xtype: 'textfield',
			                allowBlank: false
			            }	
				};
			}
			gridColumn.push(item);
		}
		if(typeof FieldModel =='undefined'){
			Ext.define('FieldModel',{
				extend:'Ext.data.Model',
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
		})
	}
});