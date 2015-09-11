Ext.define('MyCms.view.extfield.Grid',{
	extend : 'Ext.grid.Panel',
	requires : [ 'MyCms.model.ExtField',
	         'MyCms.model.Field',
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
		dataIndex : 'name',
		flex : 2,
		sortable : false
	},{
		text : "字段名",
		dataIndex : 'fieldName',
		flex : 2,
		sortable : false
	}, {
		text : "字段类型",
		dataIndex : 'fieldType',
		flex : 1,
		sortable : false,
		renderer:function(v){
			console.log(v)
			return MyCms.model.Field.Type[v]||'自定义';
		}
	},{
		text : "字段长度",
		dataIndex : 'fieldLength',
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
		var me = this,win = me.from;
		
		me.cmpContextItems = [{
			text : '刷新',
			handler : 'doRefresh',
			scope : me
		},{
			text : '添加扩展字段',
			handler : 'addField',
			scope : me
		}];
		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.ExtField',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : extfield_list,
				extraParams : {
					channelId:win.channel.get('id')
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

		me.on('containercontextmenu', 'showCmpMenu', me);
		me.on('itemcontextmenu', 'showItemMenu', me);
		me.on('refresh', 'doRefresh', me);
		me.callParent();
		
	},
	modifyDoc:function(r){
		Ext.create('MyCms.view.extfield.AddEdit',{
			from:this,
			extField:r
		}).show();
	},
	del:function(r){
		var me = this,rs = me.getSelectionModel().getSelection(),ids = [];
		for(var i=0;i<rs.length;i++){
			ids.push(rs[i].get('id'));
		}
		Ext.Msg.confirm('警告','您确认删除该项吗？',function(m){
			if(m=='yes'){
				Ext.Ajax.request({
					url : extfield_delete,
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
	},
	showItemMenu : function(_this, record, item, index, e, eOpts) {
		var me = this;

		Ext.create('MyCms.view.ux.MyMenu', {
			items : [ {
				text : '修改',
				handler : function() {
					me.modifyDoc(record);
				},
				scope : me
			},{
				text : '删除',
				handler : function() {
					me.del(record);
				},
				scope : me
			} ]
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	doRefresh:function(){
		
		this.getStore().load();
	},
	addField:function(){
		var me = this;
		
		Ext.create('MyCms.view.extfield.AddEdit',{
			from : me
		}).show();
	},
	showCmpMenu : function(_this, e, eOpts) {
		var me = this;

		Ext.create('MyCms.view.ux.MyMenu', {
			items : me.cmpContextItems
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	}
});