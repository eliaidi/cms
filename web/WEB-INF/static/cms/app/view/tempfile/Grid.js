Ext.define('MyCms.view.tempfile.Grid',{
	extend : 'Ext.grid.Panel',
	requires : ['MyCms.model.TempFile'],
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
		text : "文件名称",
		dataIndex : 'fileName',
		flex : 3,
		sortable : false
	},{
		text : "文件后缀",
		dataIndex : 'fileExt',
		flex : 1,
		sortable : false
	}, {
		text : "文件大小",
		dataIndex : 'fileSize',
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
	initComponent:function(){
		var me = this;
		
		me.cmpContextItems = [{
			text : '刷新',
			handler : 'doRefresh',
			scope : me
		},{
			text : '导入新文件',
			handler : 'upload',
			scope : me
		}];
		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.TempFile',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : tempfile_list,
				extraParams : {
					siteId : me.from.from._view.site.get('id')
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
		
		me.on('itemcontextmenu', 'showItemMenu', me);
		me.on('refresh', 'doRefresh', me);
		me.callParent();
	},
	showItemMenu:function(_this, record, item, index, e,eOpts){
		var me = this,showItems = [{
			text : '重新上传',
			handler : function() {
				me.upload(record);
			},
			scope : me
		},{
			text : '删除',
			handler : function() {
				me.delete(record);
			},
			scope : me
		}];
		if(!record.isPic()){
			showItems.splice(1,0,{
				text : '编辑',
				handler : function() {
					me.modify(record);
				},
				scope : me
			});
		}
		
		Ext.create('MyCms.view.ux.MyMenu', {
			items : showItems
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	upload:function(r){
		var me = this;
		Ext.create('MyCms.view.ux.ImpWindow',{
			view:me,
			title:'重新上传',
			url:tempfile_upload,
			params:{
				id:r.isMenuItem?'':r.get('id'),
				siteId:me.from.from._view.site.get('id')
			}
		}).show();
	},
	modify:function(r){
		var me = this;
		Ext.create('MyCms.view.tempfile.EditWin',{
			from:me,
			tempFile:r
		}).show();
	},
	doRefresh:function(){
		
		this.getStore().load();
	}
});