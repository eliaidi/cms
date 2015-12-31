 Ext.define('MyCms.view.user.Grid', {
	extend : 'Ext.grid.Panel',
	uses : [ 'Ext.data.Store',
	         'MyCms.model.User',
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
		text : "用户名",
		dataIndex : 'username',
		flex : 2,
		sortable : false
	},{
		text : "真实姓名",
		dataIndex : 'truename',
		flex : 1,
		sortable : false
	}, {
		text : "email",
		dataIndex : 'email',
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
			text : '添加用户',
			handler : 'addUser',
			scope : me
		},{
			text : '删除用户',
			handler : 'deleteUser',
			scope : me
		} ];

		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.User',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : user_list,
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
		me.on('itemdblclick','openUser',me);
		me.on('onFinish','doOnFinish',me);

		me.callParent();
	},
	doOnFinish:function(rs,user,win){
		var me = this,rIds = [];
		for(var i=0;i<rs.length;i++){
			rIds.push(rs[i].get('id'));
		}
		
		Ext.Ajax.request({
			url : user_assign_role,
			params : {
				userId : user.get('id'),
				roleIds:rIds.join(',')
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if (!obj.success) {
					Ext.Msg.alert('错误', obj.message);
					return;
				}
				me.fireEvent('refresh', me);
				win.close();
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
	},
	openUser:function(_this,record){
		
		var me = this;
		me.modifyUser(record);
	},
	showItemMenu : function(_this, record, item, index, e, eOpts) {
		var me = this;

		Ext.create('MyCms.view.ux.MyMenu', {
			items : [{
				text : '修改',
				handler : function() {
					me.modifyUser(record);
				},
				scope : me
			},{
				text : '配置角色',
				handler : function() {
					me.configRole(record);
				},
				scope : me
			}, {
				text : '删除',
				handler : function() {
					me.deleteUser(record);
				},
				scope : me
			}]
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	configRole:function(r){
		var me = this;
		Ext.create('MyCms.view.role.Window',{from:me,user:r}).show();
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
	addUser : function() {
		var me = this;
		Ext.create('MyCms.view.user.AddWin',{from:me}).show();
	},
	modifyUser : function(record) {
		var me = this;
		Ext.create('MyCms.view.user.AddWin',{from:me,user:record}).show();
	},
	deleteUser : function(record) {
		var me = this,ids = [];
		if(!record.isModel){
			var rs = me.getSelectionModel().getSelection();
			for (var int = 0; int < rs.length; int++) {
				ids.push(rs[int].get('id'));
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
					url : user_delete,
					params : {
						id : ids.join(',')
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