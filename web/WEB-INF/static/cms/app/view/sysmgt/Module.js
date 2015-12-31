/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.define('MyCms.view.sysmgt.Module', {
	extend : 'Ext.ux.desktop.Module',

	requires : [ 'MyCms.view.template.Window',
	             'MyCms.view.publog.Window',
	             'MyCms.view.user.Window',
	             'MyCms.view.role.Window',
	             'MyCms.view.resource.Window'],

	id : 'sysmgt-module',

	init : function() {
		var me = this;
		me.launcher = {
			text : '系统管理',
			iconCls : 'accordion',
			menu : [ {
				text : '发布监控',
				handler : 'pubLogMgt',
				scope : me
			},{
				text : '用户管理',
				handler : 'userMgt',
				scope : me
			},{
				text : '角色管理',
				handler : 'roleMgt',
				scope : me
			},{
				text : '资源管理',
				handler : 'resMgt',
				scope : me
			} ]
		}
	},
	roleMgt:function(){
		var desktop = this.app.getDesktop();
		var win = desktop.getWindow('role-mgt-window');
		if (!win) {
			win = desktop.createWindow({
				id : 'role-mgt-window',
				desktop : desktop
			}, MyCms.view.role.Window);
		}
		desktop.restoreWindow(win);
	},
	resMgt:function(){
		var desktop = this.app.getDesktop();
		var win = desktop.getWindow('res-mgt-window');
		if (!win) {
			win = desktop.createWindow({
				id : 'res-mgt-window',
				desktop : desktop
			}, MyCms.view.resource.Window);
		}
		desktop.restoreWindow(win);
	},
	userMgt:function(){
		var desktop = this.app.getDesktop();
		var win = desktop.getWindow('user-mgt-window');
		if (!win) {
			win = desktop.createWindow({
				id : 'user-mgt-window',
				desktop : desktop
			}, MyCms.view.user.Window);
		}
		desktop.restoreWindow(win);
	},
	tempMgt : function() {
		var me = this, desktop = me.app.getDesktop();

		var win = me.createTempWin();
		if (win) {
			desktop.restoreWindow(win);
		}
	},
	pubLogMgt:function(){
		var me = this, desktop = me.app.getDesktop();

		var win = me.createPubLogWin();
		if (win) {
			desktop.restoreWindow(win);
		}
	},
	createPubLogWin:function(){
		var desktop = this.app.getDesktop();
		var win = desktop.getWindow('publog-mgt-window');
		if (!win) {
			win = desktop.createWindow({
				id : 'publog-mgt-window',
				desktop : desktop
			}, MyCms.view.publog.Window);
		}
		return win;
	},
	createWindow:function(cfg){
		var me = this;
		if(!cfg) return false;
		
		if(cfg.winType == 'template-mgt-window') return me.createTempWin(cfg);
	},
	createTempWin : function(cfg) {
		var desktop = this.app.getDesktop();
		var win = desktop.getWindow('template-mgt-window-'+cfg.obj.get('id'));
		if (!win) {
			win = desktop.createWindow({
				id : 'template-mgt-window-'+cfg.obj.get('id'),
				desktop : desktop,
				site : cfg.obj
			}, MyCms.view.template.Window);
		}
		return win;
	}
});
