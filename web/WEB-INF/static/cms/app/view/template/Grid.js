Ext.define('MyCms.view.template.Grid',{
	extend:'Ext.grid.Panel',
	uses : [ 'MyCms.model.Template',
			 ],
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
	initComponent:function(){
		var me = this;
		me.cmpContextItems = [];
		
		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.Template',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : template_list,
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
		
		me.callParent();
	}
});