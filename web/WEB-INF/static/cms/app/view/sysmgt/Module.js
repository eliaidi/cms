/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.define('MyCms.view.sysmgt.Module', {
	extend : 'Ext.ux.desktop.Module',

	requires : [ 'MyCms.view.template.Window' ],

	id : 'sysmgt-module',

	init : function() {
		var me = this;
		me.launcher = {
			text : '系统管理',
			iconCls : 'accordion',
			menu : [ {
				text : '模板管理',
				handler : 'tempMgt',
				scope : me
			} ]
		}
	},
	tempMgt : function() {
		var me = this, desktop = me.app.getDesktop();

		var win = me.createTempWin();
		if (win) {
			desktop.restoreWindow(win);
		}
	},
	createTempWin : function() {
		var desktop = this.app.getDesktop();
		var win = desktop.getWindow('template-mgt-window');
		if (!win) {
			win = desktop.createWindow({
				id : 'template-mgt-window',
				desktop : desktop
			}, MyCms.view.template.Window);
		}
		return win;
	}
});