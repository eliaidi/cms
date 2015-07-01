Ext.define('MyCms.view.channel.View', {
	extend : 'Ext.panel.Panel',
	alias : 'channel.view',
	requires : [ 'Ext.data.Store', 'MyCms.model.Channel' ],

	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			layout : 'fit',
			items : [ {
				xtype : 'dataview',
				tpl : [
						'<tpl for=".">',
						'<div class="thumb-wrap">',
						'<div class="thumb">',
						'<img src="' + RootPath
								+ '/resources/images/forms.png" />', '</div>',
						'<span>{name}</span>', '</div>', '</tpl>' ],

				itemSelector : 'div.thumb-wrap',
				multiSelect : true,
				singleSelect : false,
				cls : 'images-view',
				scrollable : true,
				store : new Ext.data.Store({
					autoLoad : true,
					model : 'MyCms.model.Channel',
					proxy : {
						type : 'ajax',
						url : list_channel_by_siteid,
						extraParams : me.parent ? {
							parentId : me.parent.get('id')
						} : (me.site ? {
							siteId : me.site.get('id')
						} : null),
						reader : {
							type : 'json',
							rootProperty : 'channels'
						}
					}
				})
			} ]

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
		var me = this;

		var sMenu = Ext.getCmp('channel-item-menu');
		if (sMenu) {
			Ext.destroy(sMenu);
		}
		Ext.create('Ext.menu.Menu', {
			id : 'channel-item-menu',
			items : [ {
				text : '打开',
				handler : 'open',
				scope : me
			}, {
				text : '修改',
				handler : function() {
					me.modifyChannel(record);
				},
				scope : me
			}, {
				text : '删除',
				handler : function() {
					me.deleteChannel(record);
				},
				scope : me
			} ]
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	showCmpMenu : function(_this, e, eOpts) {
		var me = this;
		var sMenu = Ext.getCmp('channel-view-menu');
		if (sMenu) {
			Ext.destroy(sMenu);
		}
		sMenu = Ext.create('Ext.menu.Menu', {
			id : 'channel-view-menu',
			items : [ {
				text : '刷新',
				handler : 'refresh',
				scope : me
			}, {
				text : '添加栏目',
				handler : 'addChannel',
				scope : me
			} ]
		}).showAt(e.getXY());

		e.stopEvent();
		e.stopPropagation();
	},
	refresh : function() {
		var me = this;

		me.down('dataview').getStore().load();
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
	}
});