 Ext.define('MyCms.view.resource.Grid', {
	extend : 'Ext.grid.Panel',
	uses : [ 'Ext.data.Store',
	         'MyCms.model.Resource',
			 'Ext.ux.form.SearchField',
			 'MyCms.view.ux.MyMenu'],
	border : true,
	loadMask : true,

	selModel: {
        selType: 'checkboxmodel'
    },
	multiSelect : true,
	viewConfig : {
		trackOver : false,
		loadingText:'正在加载中...',
		emptyText : '<h1 style="margin:20px">未找到任何记录！！！</h1>'
	},
	columns : [ {
		xtype : 'rownumberer',
		width : 50,
		sortable : false
	}, {
		tdCls : 'x-grid-cell-topic',
		text : "名称",
		dataIndex : 'name',
		flex : 2,
		sortable : false
	},{
		text : "值",
		dataIndex : 'value',
		flex : 1,
		sortable : false
	},{
		text : "类型",
		dataIndex : 'type',
		flex : 1,
		sortable : false,
		renderer:function(v){
			return MyCms.model.Resource.Types[v];
		}
	},{
		text : "创建人",
		dataIndex : 'crUserName',
		flex : 1,
		sortable : false
	}, {
		text : "创建时间",
		dataIndex : 'crTime',
		align : 'center',
		flex : 2,
		sortable : false
	} ],
	initComponent : function() {
		var me = this;
		//面板右键菜单
		me.cmpContextItems = [ {
			text : '刷新',
			handler : 'refresh',
			scope : me
		}, {
			text : '添加',
			handler : 'addRes',
			scope : me
		},{
			text : '删除',
			handler : 'delRes',
			scope : me
		} ];

		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.Resource',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : resource_list,
				reader : {
					rootProperty : 'list',
					totalProperty : 'totalCount'
				},
				simpleSortMode : true,
				filterParam : 'query'
			},
			listeners : {
				totalcountchange : function() {
					me.down('#status').update({
						count : store.getTotalCount()
					});
				}
			},
			remoteFilter : true,
			autoLoad : true
		});

		Ext.apply(me, {
			store : store,
			dockedItems : [ {
				dock : 'top',
				xtype : 'toolbar',
				items : [{
					text:'面板操作',
					menu:me.cmpContextItems
				}, {
					width : 400,
					fieldLabel : '检索',
					labelWidth : 50,
					xtype : 'searchfield',
					store : store
				}, '->', {
					xtype : 'component',
					itemId : 'status',
					tpl : '总记录: {count}',
					style : 'margin-right:5px'
				} ]
			} ]
		});
		
		me.on('containercontextmenu', 'showCmpMenu', me);
		me.on('itemcontextmenu', 'showItemMenu', me);
		me.on('refresh', 'refresh', me);
		me.on('itemdblclick','openRes',me);

		me.callParent();
	},
	openRes:function(_this,record){
		
		var me = this;
		me.modifyRes(record);
	},
	showItemMenu : function(_this, record, item, index, e, eOpts) {
		var me = this;

		Ext.create('MyCms.view.ux.MyMenu', {
			items : [{
				text : '修改',
				handler : function() {
					me.modifyRes(record);
				},
				scope : me
			}, {
				text : '删除',
				handler : function() {
					me.delRes(record);
				},
				scope : me
			}]
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	showCmpMenu : function(_this, e, eOpts) {
		var me = this;

		Ext.create('MyCms.view.ux.MyMenu', {
			items : me.cmpContextItems
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	refresh : function() {
		var me = this;

		me.getStore().load();
	},
	addRes : function() {
		var me = this;
		Ext.create('MyCms.view.resource.AddWin',{from:me}).show();
	},
	modifyRes : function(record) {
		var me = this;
		Ext.create('MyCms.view.resource.AddWin',{from:me,resource:record}).show();
	},
	delRes : function(record) {
		var me = this,ids = [];
		if(!record.isModel){
			var rs = me.getSelectionModel().getSelection();
			for (var i = 0; i < rs.length; i++) {
				ids.push(rs[i].get('id'));
			}
		}else{
			ids.push(record.get('id'));
		}
		if(ids.length==0){
			Ext.Msg.alert('Error','请选择待删除的记录！');
			return;
		}
		
		Ext.Msg.confirm('警告','您确认删除该项吗？',function(m){
			if(m=='yes'){
				Ext.Ajax.request({
					url : resource_delete,
					params : {
						ids : ids.join(',')
					},
					success : function(response, opts) {
						var obj = Ext.decode(response.responseText);
						if (!obj.success) {
							Ext.Msg.alert('错误', obj.message);
							return;
						}
						me.fireEvent('refresh', me);
					},
					failure : function(response, opts) {
						console.log('server-side failure with status code '
								+ response.status);
					}
				});
			}
		});
	}
});