Ext.define('MyCms.view.document.Grid', {
	extend : 'Ext.grid.Panel',
	uses : [ 'Ext.data.Store',
	         'MyCms.model.Document',
			 'Ext.ux.form.SearchField',
			 'MyCms.view.ux.MyMenu'],
	border : true,
	title : '文档列表',
	mixins : {
		dragSelector : 'Ext.ux.DataView.DragSelector',
		draggable : 'Ext.ux.DataView.Draggable'
	},
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
		text : "标题",
		dataIndex : 'title',
		flex : 3,
		sortable : false
	},{
		text : "状态",
		dataIndex : 'status',
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
		var me = this;
		//面板右键菜单
		me.cmpContextItems = [ {
			text : '刷新',
			handler : 'refresh',
			scope : me
		}, {
			text : '添加文档',
			handler : 'addDocument',
			scope : me
		},{
			text : '粘贴',
			handler : 'paste',
			scope : me/*,
			disabled:!MyCms.Application.clipBoard*/
		},{
			text : '发布',
			handler : 'publishDoc',
			scope : me
		} ];

		var store = Ext.create('Ext.data.BufferedStore', {
			model : 'MyCms.model.Document',
			pageSize : 20,
			leadingBufferZone : 200,
			proxy : {
				type : 'ajax',
				url : document_list,
				extraParams : {
					channelId : me.channel.get('id')
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
		
		me.on('render', me.initDragDrop);

		me.on('containercontextmenu', 'showCmpMenu', me);
		me.on('itemcontextmenu', 'showItemMenu', me);
		me.on('refresh', 'refresh', me);
		me.on('itemdblclick','openDoc',me);

		me.callParent();
	},
	openDoc:function(_this,record){
		
		window.open(RootPath+'/document/view/'+record.get('id'));
	},
	showItemMenu : function(_this, record, item, index, e, eOpts) {
		var me = this;

		var dMenu = Ext.getCmp('document-item-menu');
		if (dMenu) {
			Ext.destroy(dMenu);
		}
		dMenu = Ext.create('MyCms.view.ux.MyMenu', {
			id : 'document-item-menu',
			items : [{
				text : '预览',
				handler : function() {
					me.previewDoc(record);
				},
				scope : me
			},{
				text : '发布',
				handler : 'publishDoc',
				scope : me
			}, {
				text : '修改',
				handler : function() {
					me.modifyDoc(record);
				},
				scope : me
			},{
				text : '复制',
				handler : function() {
					me.copy(record);
				},
				scope : me
			},{
				text : '剪切',
				handler : function() {
					me.cut(record);
				},
				scope : me
			}, {
				text : '删除该条',
				handler : function() {
					me.deleteDoc(record);
				},
				scope : me
			},me.getSelectionModel().getSelection().length>1?{
				text : '删除所选',
				handler : function() {
					me.deleteDocs();
				},
				scope : me
			}:'-' ]
		});
		
		dMenu.showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	previewDoc:function(r){
		var me = this;
		
		Ext.Ajax.request({
			url : document_preview,
			params : {
				id:r.get('id')
			},
			success : function(response, opts) {
				var o = Ext.decode(response.responseText);
				if (!o.success) {
					Ext.Msg.alert('错误', o.message);
					return;
				}
				window.open(o.obj);
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
	},
	copy:function(r){
		var me = this,rs = me.getSelectionModel().getSelection();
		if(rs.length==0){
			rs = [r];
		}
		MyCms.Application.copy(new MyCms.model.ClipBoard({
			eType : MyCms.view.document.Grid.ENAME,
			aType : 'copy',
			data : rs
		}));
	},
	cut:function(r){
		var me = this,rs = me.getSelectionModel().getSelection();
		if(rs.length==0){
			rs = [r];
		}
		MyCms.Application.copy(new MyCms.model.ClipBoard({
			eType : MyCms.view.document.Grid.ENAME,
			aType : 'cut',
			data : rs,
			from : me
		}));
	},
	paste:function(){
		var me = this,obj = MyCms.Application.clipBoard;
		if(!obj||obj.get('eType')!=MyCms.view.document.Grid.ENAME){
			return;
		}
		var ids = [];
		obj.get('data').forEach(function(r){
			ids.push(r.get('id'));
		});
		Ext.Ajax.request({
			url : obj.get('aType')=='copy'?document_copy:document_cut,
			params : {
				objIds : ids.join(','),
				channelId : me.channel.get('id')
			},
			success : function(response, opts) {
				var o = Ext.decode(response.responseText);
				if (!o.success) {
					Ext.Msg.alert('错误', o.message);
					return;
				}
				me.fireEvent('refresh', me);
				if(obj.get('aType')=='cut'&&obj.get('from')){
					obj.get('from').fireEvent('refresh',obj.get('from'));
				}
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
	},
	showCmpMenu : function(_this, e, eOpts) {
		var me = this;

		var dMenu = Ext.getCmp('document-cmp-menu');
		if (dMenu) {
			Ext.destroy(dMenu);
		}
		dMenu = Ext.create('MyCms.view.ux.MyMenu', {
			id : 'document-cmp-menu',
			items : me.cmpContextItems
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	refresh : function() {
		var me = this;

		me.getStore().load();
	},
	addDocument : function() {
		var me = this, desktop = me.up('desktop'), module = desktop.app
				.getModule('document-module'), win = module
				&& module.createWindow({
					winType : 'document-window',
					channel : me.channel,
					view : me
				});

		if (win) {
			desktop.restoreWindow(win);
		}
	},
	modifyDoc : function(record) {
		var me = this, desktop = me.up('desktop'), module = desktop.app
				.getModule('document-module'), win = module
				&& module.createWindow({
					winType : 'document-window',
					channel : me.channel,
					document:record,
					view : me
				});

		if (win) {
			desktop.restoreWindow(win);
		}
	},
	deleteDoc : function(record) {
		var me = this;
		
		Ext.Msg.confirm('警告','您确认删除该项吗？',function(m){
			if(m=='yes'){
				Ext.Ajax.request({
					url : document_delete,
					params : {
						id : record.get('id')
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
	deleteDocs:function(){
		var me = this,ids;
		
		var rs = me.getSelectionModel().getSelection();
		var ids = new Array();
		rs.forEach(function(r){
			ids.push(r.get('id'));
		});
		
		Ext.Msg.confirm('警告','您确认删除该项吗？',function(m){
			if(m=='yes'){
				Ext.Ajax.request({
					url : document_deleteMuti,
					params : {
						ids : ids.join(',')
					},
					success : function(response, opts) {
						var obj = Ext.decode(response.responseText);
						if (!obj.success) {
							Ext.Msg.alert('错误', obj.message);
							return;
						}
						Ext.Msg.alert('提示', obj.message, function() {
							me.fireEvent('refresh', me);
						});
					},
					failure : function(response, opts) {
						console.log('server-side failure with status code '
								+ response.status);
					}
				});
			}
		});
	},
	initDragDrop:function(){
		var me = this,view = me.getView();
		initDragZone(me);
		initDropZone(me);

		function initDragZone(v) {
			v.dragZone = Ext.create('Ext.dd.DragZone',v.getEl(),
				{
					getDragData : function(e) {
						var sourceEl = e.getTarget('.x-grid-item', 10), d;
						if (sourceEl) {
							d = sourceEl.cloneNode(true);
			                d.id = Ext.id();
							return (v.dragData = {
									sourceEl: sourceEl,
				                    repairXY: Ext.fly(sourceEl).getXY(),
				                    ddel: d,
				                    rec: getDataByDom(sourceEl)
							});
						}
					},
					getRepairXY : function() {
						return this.dragData.repairXY;
					}
				});
		}

		function initDropZone(v) {
			v.dropZone = Ext.create('Ext.dd.DropZone',v.el,
				{
					getTargetFromEvent : function(e) {
						return e.getTarget('.x-grid-item');
					},
					onNodeEnter : function(target,dd, e, data) {
						if(this.onNodeOver(target,dd, e, data)==Ext.dd.DropZone.prototype.dropAllowed){
							Ext.fly(target).addCls('grid-target-hover');
						}
					},
					onNodeOut : function(target,dd, e, data) {
						Ext.fly(target).removeCls('grid-target-hover');
					},
					onNodeOver : function(target,dd, e, data) {
						var  proto = Ext.dd.DropZone.prototype,tData = getDataByDom(target),nextData = target.previousSibling?getDataByDom(target.previousSibling):null;
						return  tData.get('id')==data.rec.get('id')||(nextData?nextData.get('id')==data.rec.get('id'):false)?proto.dropNotAllowed:proto.dropAllowed;
						//return Ext.dd.DropZone.prototype.dropAllowed
					},

					onNodeDrop : function(target,dd, e, data) {
						if(this.onNodeOver(target,dd, e, data)==Ext.dd.DropZone.prototype.dropAllowed){
							var tRec = getDataByDom(target),cRec = data.rec;
							doResort(cRec,tRec);
							return true;
						}
						return false;
					}
				});
			
		}
		
		function getDataByDom(t){
			return view.getRecord(t);
		}
		
		function doResort(c,t){
			Ext.Ajax.request({
				url : document_move,
				params : {
					currId : c.get('id'),
					targetId : t.get('id')
				},
				success : function(response, opts) {
					var obj = Ext.decode(response.responseText);
					if (!obj.success) {
						Ext.Msg.alert('错误', obj.message);
						return;
					}
					me.fireEvent('refresh',me);
				},
				failure : function(response, opts) {
					console.log('server-side failure with status code '
							+ response.status);
				}
			});
		}
	},
	publishDoc : function(){
		var me = this,rs = me.getSelectionModel().getSelection(),ids = [];
		
		if(rs.length==0){
			Ext.Msg.alert('error','请选择待发布的记录！');
			return;
		}
		
		for(var i=0;i<rs.length;i++){
			ids.push(rs[i].get('id'));
		}
		
		Ext.Ajax.request({
			url : document_publish,
			params : {
				ids : ids.join(',')
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if (!obj.success) {
					Ext.Msg.alert('错误', obj.message);
					return;
				}
				me.fireEvent('refresh',me);
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
	}
});