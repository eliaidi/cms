 Ext.define('MyCms.view.role.Grid', {
	extend : 'Ext.grid.Panel',
	uses : [ 'Ext.data.Store',
	         'MyCms.model.Role',
			 'Ext.ux.form.SearchField',
			 'MyCms.view.ux.MyMenu'],
	border : true,
//	title : '用户列表',
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
		text : "是否管理员角色",
		dataIndex : 'isAdmin',
		flex : 1,
		sortable : false
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
			text : '添加角色',
			handler : 'addRole',
			scope : me
		},{
			text : '资源管理',
			handler : 'resMgt',
			scope : me
		},{
			text : '删除角色',
			handler : 'deleteRole',
			scope : me
		} ];

		var store = Ext.create('Ext.data.Store', {
			model : 'MyCms.model.Role',
			pageSize : 200,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : role_list,
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
		me.on('itemdblclick','openRole',me);

		me.callParent();
	},
	resMgt:function(){
		Ext.create('MyCms.view.resource.Window',{}).show();
	},
	openRole:function(_this,record){
		
		var me = this;
		me.modifyRole(record);
	},
	showItemMenu : function(_this, record, item, index, e, eOpts) {
		var me = this;

		Ext.create('MyCms.view.ux.MyMenu', {
			items : [{
				text : '修改',
				handler : function() {
					me.modifyRole(record);
				},
				scope : me
			}, {
				text : '删除',
				handler : function() {
					me.deleteRole(record);
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
	addRole : function() {
		var me = this;
		Ext.create('MyCms.view.role.AddWin',{from:me}).show();
	},
	modifyRole : function(record) {
		var me = this;
		Ext.create('MyCms.view.role.AddWin',{from:me,role:record}).show();
	},
	deleteRole : function(record) {
		var me = this,ids = [];
		if(!record.isModel){
			var rs = me.getSelectionModel().getSelection();
			for (var int = 0; int < rs.length; int++) {
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
					url : role_delete,
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