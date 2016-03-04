Ext.define('MyCms.view.channel.View', {
	extend : 'Ext.panel.Panel',
	alias : 'channel.view',
	requires : [ 'Ext.data.Store', 
	             'MyCms.model.Channel',
	             'MyCms.view.ux.ImpWindow',
	             'MyCms.view.channel.ImgView',
	             'MyCms.view.ux.MyMenu',
	             'MyCms.model.ClipBoard'],

	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			layout : 'fit',
			items : [ Ext.create('MyCms.view.channel.ImgView',{
				view:me
			})]

		});

		me.on('afterrender', 'doRegisterEvt', me);
		me.on('refresh', 'refresh', me);

		this.callParent();
	},
	doRegisterEvt : function() {
		var me = this;

		var view = me.down('dataview');
		view.on('containercontextmenu', 'showCmpMenu', me);
		view.on('itemcontextmenu', 'showItemMenu', me);
		view.on('itemdblclick', 'openChannel', me);
	},
	openChannel : function(_this, record, item, index, e, eOpts) {

		var me = this, desktop = me.up('desktop'), module = desktop.app
				.getModule('channel-module'), win = module
				&& module.createWindow({
					winType : 'channel-complex-window',
					desktop : desktop,
					channel : record,
					view : me
				});
		if (win) {
			desktop.restoreWindow(win);
		}
	},
	showItemMenu : function(_this, record, item, index, e, eOpts) {
		var me = this,rs = me.down('dataview').getSelectionModel().getSelection();

		var sMenu = Ext.getCmp('channel-item-menu');
		if (sMenu) {
			Ext.destroy(sMenu);
		}
		Ext.create('MyCms.view.ux.MyMenu', {
			id : 'channel-item-menu',
			items : [ {
				text : '预览',
				handler : function() {
					me.previewChannel(record);
				},
				scope : me
			},{
				text : '发布',
				menu:[{
					text : '仅发布栏目首页',
					handler : function() {
						me.publish(record,0);
					},
					scope : me
				},{
					text : '增量发布',
					handler : function() {
						me.publish(record,1);
					},
					scope : me
				},{
					text : '全部发布',
					handler : function() {
						me.publish(record,2);
					},
					scope : me
				}]
			},{
				text : '打开',
				handler : function(){
					me.openChannel(_this, record, item, index, e, eOpts);
				},
				scope : me
			}, {
				text : '修改',
				handler : function() {
					me.modifyChannel(record);
				},
				scope : me
			},{
				text : '扩展字段',
				handler : function() {
					me.extFieldMgt(record);
				},
				scope : me
			},{
				text : '复制',
				handler : function(){
					me.copy(record);
				},
				scope : me
			},{
				text : '剪切',
				handler : function(){
					me.cut(record);
				},
				scope : me
			}, {
				text : '删除',
				handler : function() {
					me.deleteChannel(record);
				},
				scope : me
			},rs.length>0?{
				text : '删除所选',
				handler : 'deleteSelected',
				scope : me
			}:'-' ]
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	deleteSelected:function(){
		var me = this,rs = me.down('dataview').getSelectionModel().getSelection(),ids=[];
		rs.forEach(function(r){
			ids.push(r.get('id'));
		});
		
		Ext.Msg.confirm('警告','你确定删除选中的栏目吗？',function(m){
			if(m=='yes'){
				Ext.Ajax.request({
					url:channel_delete_multi,
					params:{ids:ids.join(',')},
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
	showCmpMenu : function(_this, e, eOpts) {
		var me = this;
		var sMenu =  Ext.create('MyCms.view.ux.MyMenu', {
			id : 'channel-view-menu',
			items : [ {
				text : '刷新',
				handler : 'refresh',
				scope : me
			},{
				text : '修改',
				handler : 'modify',
				scope : me
			}, {
				text : '添加栏目',
				handler : 'addChannel',
				scope : me
			},{
				text : '导入栏目',
				handler : 'impChannel',
				scope : me
			},{
				text : '粘贴',
				handler : 'paste',
				scope : me,
				disabled:!MyCms.Application.clipBoard
			} ]
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	refresh : function() {
		var me = this,view = me.down('dataview');

		view.getStore().load();
		view.mixins.dragSelector.init(view);
	},
	modify:function(){
		var me = this;
		
		if(me.site){
			Ext.create('MyCms.view.site.Window',{modal:true,site:me.site}).show();
		}else if(me.parent){
			Ext.create('MyCms.view.channel.Window',{modal:true,channel:me.parent}).show();
		}else{
			Ext.Msg.alert('error','未知的栏目类型~~');
		}
	},
	publish:function(r,t){
		Ext.Ajax.request({
			url : channel_publish,
			params : {
				id : r.get('id'),
				type : t
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if (!obj.success) {
					Ext.Msg.alert('错误', obj.message);
					return;
				}
				//window.open(obj.obj);
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
	},
	previewChannel:function(r){
		
		Ext.Ajax.request({
			url : channel_preview,
			params : {
				id : r.get('id')
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
				if (!obj.success) {
					Ext.Msg.alert('错误', obj.message);
					return;
				}
				window.open(obj.obj);
			},
			failure : function(response, opts) {
				console.log('server-side failure with status code '
						+ response.status);
			}
		});
	},
	copy:function(r){
		var me = this,rs = me.down('dataview').getSelectionModel().getSelection();
		if(rs.length==0){
			rs = [r];
		}
		MyCms.Application.copy(new MyCms.model.ClipBoard({
			eType : MyCms.view.channel.View.ENAME,
			aType : 'copy',
			data : rs
		}));
	},
	cut:function(r){
		var me = this,rs = me.down('dataview').getSelectionModel().getSelection();
		if(rs.length==0){
			rs = [r];
		}
		MyCms.Application.copy(new MyCms.model.ClipBoard({
			eType : MyCms.view.channel.View.ENAME,
			aType : 'cut',
			data : rs,
			from : me
		}));
	},
	paste:function(){
		var me = this,obj = MyCms.Application.clipBoard;
		if(!obj||obj.get('eType')!=MyCms.view.channel.View.ENAME){
			return;
		}
		var ids = [];
		obj.get('data').forEach(function(r){
			ids.push(r.get('id'));
		});
		Ext.Ajax.request({
			url : obj.get('aType')=='copy'?channel_copy:channel_cut,
			params : {
				objIds : ids.join(','),
				siteId : me.site?me.site.get('id'):'',
				parentId : me.parent?me.parent.get('id'):''
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
	addChannel : function() {
		var me = this, desktop = me.up('desktop'), module = desktop.app
				.getModule('channel-module'), win = module
				&& module.createWindow({
					winType : 'channel-window',
					site : me.site,
					parent:me.parent,
					view : me
				});

		if (win) {
			desktop.restoreWindow(win);
		}
	},
	impChannel:function(){
		var me = this;
    	Ext.create('MyCms.view.ux.ImpWindow',{
    		title:'批量导入栏目',
    		view:me,
    		params:me.parent?{parentId:me.parent.get('id')}:(me.site?{siteId:me.site.get('id')}:null),
    		url:channel_imp
    	}).show();
	},
	modifyChannel : function(record) {
		var me = this, desktop = me.up('desktop'), module = desktop.app
				.getModule('channel-module'), win = module
				&& module.createWindow({
					winType : 'channel-window',
					desktop : desktop,
					site : me.site,
					channel : record,
					view : me
				});

		if (win) {
			desktop.restoreWindow(win);
		}
	},
	deleteChannel : function(record) {
		var me = this;

		Ext.Msg.confirm('警告', '你确认删除此栏目吗？', function(m) {
			if (m == 'yes') {
				Ext.Ajax.request({
					url : channel_delete,
					params : {
						channelId : record.get('id')
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
	extFieldMgt:function(r){
		var me = this;
		
		Ext.create('MyCms.view.extfield.Window',{
			from:me,
			channel:r
		}).show();
	},
	statics:{
		ENAME : 'channel'
	}
});