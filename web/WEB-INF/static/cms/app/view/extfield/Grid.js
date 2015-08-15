Ext.define('MyCms.view.extfield.Grid',{
	extend : 'Ext.grid.Panel',
	uses : [ 'MyCms.model.ExtField',
			 ],
	border : true,
	loadMask : true,
	selModel : {
		selType : 'checkboxmodel'
	},
	multiSelect : true,
	viewConfig : {
		trackOver : false,
		loadingText : '正在加载中...',
		emptyText : '<h1 style="margin:20px">未找到任何记录！！！</h1>'
	},
	columns : [ {
		xtype : 'rownumberer',
		width : 50,
		sortable : false
	},{
		text : "ID",
		dataIndex : 'id',
		flex : 1,
		sortable : false
	}, {
		tdCls : 'x-grid-cell-topic',
		text : "名称",
		dataIndex : 'label',
		flex : 2,
		sortable : false
	},{
		text : "字段名",
		dataIndex : 'name',
		flex : 2,
		sortable : false,
		renderer:function(v){
			return MyCms.model.Template.Type[v]||'未知';
		}
	},{
		text : "所属栏目",
		dataIndex : 'chnlName',
		flex : 1,
		sortable : false
	}, {
		text : "字段类型",
		dataIndex : 'type',
		flex : 1,
		sortable : false,
		renderer:function(v){
			return MyCms.model.ExtField.Type[v]||'未知';
		}
	},{
		text : "字段长度",
		dataIndex : 'length',
		flex : 1,
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
	initComponent : function() {
		var me = this,win = me.up('window');
		
		me.cmpContextItems = [];
		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.ExtField',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : extfield_list,
				extraParams : {
					channelId:win.from.channel.get('id')
				},
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
				items : [ {
					text : '面板操作',
					menu : me.cmpContextItems
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

//		me.on('containercontextmenu', 'showCmpMenu', me);
//		me.on('itemcontextmenu', 'showItemMenu', me);
//		me.on('refresh', 'doRefresh', me);
		me.callParent();
		
	}
});