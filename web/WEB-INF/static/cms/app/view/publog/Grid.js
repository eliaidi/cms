Ext.define('MyCms.view.publog.Grid',{
	extend:'Ext.grid.Panel',
	extend : 'Ext.grid.Panel',
	requires : [ 'Ext.data.Store',
			 'Ext.ux.form.SearchField',
			 'MyCms.view.ux.MyMenu','MyCms.model.PubLog'],
	border : true,
	mixins : {
		dragSelector : 'Ext.ux.DataView.DragSelector',
		draggable : 'Ext.ux.DataView.Draggable'
	},
	loadMask : true,
	anchor:'100%',
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
		text : "发布对象",
		dataIndex : 'obj',
		flex : 3,
		sortable : false
	},{
		text : "对象类型",
		dataIndex : 'objType',
		flex : 1,
		sortable : false
	},{
		text : "是否成功",
		dataIndex : 'success',
		flex : 1,
		sortable : false
	}, {
		text : "开始时间",
		dataIndex : 'startTime',
		flex : 1,
		sortable : false
	}, {
		text : "结束时间",
		dataIndex : 'endTime',
		flex : 1,
		sortable : false
	},{
		text : "总耗时",
		dataIndex : 'zhs',
		flex : 1,
		sortable : false
	} ],
	initComponent : function() {
		var me = this;
		//面板右键菜单
		me.cmpContextItems = [ {
			text : '刷新',
			handler : 'refresh',
			scope : me
		} ];

		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.PubLog',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : me.url,
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
		me.on('refresh', 'refresh', me);

		me.callParent();
	},
	showCmpMenu : function(_this, e, eOpts) {
		var me = this;

		Ext.create('MyCms.view.ux.MyMenu', {
			items : me.cmpContextItems
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	refresh:function(){
		this.getStore().load();
	}
});