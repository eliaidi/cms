Ext.define('MyCms.view.field.EditGrid',{
	extend:'Ext.grid.Panel',
	initComponent:function(){
		var me = this;
		
	    var store = Ext.create('Ext.data.Store', {
	        autoDestroy: true,
	        model: 'MyCms.model.Field',
	        proxy: {
	            type: 'memory'
	        },
	        data: []
	    });

	    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	        clicksToMoveEditor: 2,
	        autoCancel: false
	    });

	    Ext.apply(me,{
	    	store: store,
	        columns: [{
	            header: '属性名称',
	            dataIndex: 'name',
	            flex: 1,
	            editor: {
	                allowBlank: false
	            }
	        }, {
	            header: '属性类型',
	            dataIndex: 'type',
	            width: 160,
	            editor: {
	                allowBlank: false,
	                xtype:'combobox',
	                store: Ext.create('Ext.data.Store', {
	                	fields: [
	                	         {name: 'id', type: 'string'},
	                	         {name: 'name',  type: 'string'}
	                	     ],
	                    proxy: {
	                        type: 'ajax',
	                        url: field_list_type,
	                        reader: {
	                            type: 'json',
	                            rootProperty: 'types'
	                        }
	                    },
	                    autoLoad: true
	                }),
	                queryMode: 'remote',
	                displayField: 'name',
	                valueField: 'id'
	            }
	        }, {
	            xtype: 'numbercolumn',
	            header: '属性长度',
	            dataIndex: 'length',
	            width: 135,
	            editor: {
	            	xtype: 'numberfield',
	                allowBlank: false,
	                minValue: 1,
	                maxValue: 150000
	            }
	        }],
	        tbar: [{
	            text: '添加属性',
	            iconCls: 'employee-add',
	            handler : function() {
	                rowEditing.cancelEdit();
	
	                var r = Ext.create('MyCms.model.Field', {
	                });
	
	                store.insert(0, r);
	                rowEditing.startEdit(0, 0);
	            }
	        }, {
	            itemId: 'removeEmployee',
	            text: '删除属性',
	            iconCls: 'employee-remove',
	            handler: function() {
	                var grid = me,sm = grid.getSelectionModel();
	                rowEditing.cancelEdit();
	                store.remove(sm.getSelection());
	                if (store.getCount() > 0) {
	                    sm.select(0);
	                }
	            },
	            disabled: true
	        }],
	        plugins: [rowEditing],
	        listeners: {
	            'selectionchange': function(view, records) {
	                me.down('#removeEmployee').setDisabled(!records.length);
	            }
	        }
	    });
		me.callParent();
	}
});