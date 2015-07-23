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
		/*me.launcher = {
			text : '系统管理',
			iconCls : 'accordion',
			menu : [ {
				text : '模板管理',
				handler : 'tempMgt',
				scope : me
			} ]
		}*/
	},
	tempMgt : function() {
		var me = this, desktop = me.app.getDesktop();

		var win = me.createTempWin();
		if (win) {
			desktop.restoreWindow(win);
		}
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
				id : 'template-mgt-window'+cfg.obj.get('id'),
				desktop : desktop,
				site : cfg.obj
			}, MyCms.view.template.Window);
		}
		return win;
	}
});
