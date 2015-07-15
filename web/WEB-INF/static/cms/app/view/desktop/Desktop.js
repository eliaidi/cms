/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('MyCms.view.desktop.Desktop', {
    extend: 'Ext.ux.desktop.App',
    xtype:'my-desktop',

    requires: [
        'Ext.window.MessageBox',
        'Ext.ux.desktop.ShortcutModel',
        'MyCms.view.systemstatus.SystemStatus',
        'MyCms.view.videowindow.VideoWindow',
        'MyCms.view.gridwindow.GridWindow',
        'MyCms.view.tabwindow.TabWindow',
        'MyCms.view.accordion.AccordionWindow',
        'MyCms.view.notepad.Notepad',
        'MyCms.view.bogusmenu.BogusMenuModule',
        'MyCms.view.bogusmodule.BogusModule',
        'MyCms.view.setting.Settings',
        'MyCms.view.site.Window',
        'MyCms.view.site.Module',
        'MyCms.view.channel.Module',
        'MyCms.view.document.Module',
        'MyCms.view.ux.ImpWindow',
        'MyCms.view.ux.MyMenu'
    ],
    init: function() {
    	var me = this;
    	
        me.callParent();

        me.on('refresh','doRefresh',me);
        me.desktop.on('refresh','doRefresh',me);
        me.desktop.shortcutsView.on('itemcontextmenu','onItemMenu',me);
        me.desktop.shortcutsView.on('itemclick', me.onShortcutItemClick, me);
    },
    onShortcutItemClick:function(dataView, record,item, index, e, eOpts){
    	var me = this, module = me.desktop.app.getModule(record.data.module),
        win = module && module.createWindow({winType:'site-view-win',obj:record});

	    if (win) {
	        me.desktop.restoreWindow(win);
	    }
    },
    onItemMenu:function(_this, record, item, index, e, eOpts){
    	var me = this;
    	var sMenu =  Ext.create('MyCms.view.ux.MyMenu',{
            items: [{
                text: '修改',
                handler:function(){
                	me.modifySite(record)
                },
                scope:me
            },{
                text: '删除',
                handler:function(){
                	me.deleteSite(record)
                },
                scope:me
            },{
            	text: '复制',
                handler:function(){
                	MyCms.Application.copy(record);
                },
                scope:me
            }]
    	}).showAt(e.getXY());
    	
    	e.stopEvent();
    	e.stopPropagation();
    },

    getModules : function(){
        return [
            new MyCms.view.videowindow.VideoWindow(),
            new MyCms.view.systemstatus.SystemStatus(),
            new MyCms.view.gridwindow.GridWindow(),
            new MyCms.view.tabwindow.TabWindow(),
            new MyCms.view.accordion.AccordionWindow(),
            new MyCms.view.notepad.Notepad(),
            new MyCms.view.bogusmenu.BogusMenuModule(),
            new MyCms.view.bogusmodule.BogusModule(),
            new MyCms.view.site.Module(),
            new MyCms.view.channel.Module(),
            new MyCms.view.document.Module()
        ];
    },

    getDesktopConfig: function () {
        var me = this, ret = me.callParent();

        return Ext.apply(ret, {

            contextMenuItems: [
                { text: '刷新', handler: 'doRefresh',scope:me },
                { text: '设置', handler: 'onSettings',scope:me },
                { text: '新增站点', handler: 'addSite',scope:me },
                { text: '导入站点', handler: 'impSite',scope:me }
            ],

            shortcuts: Ext.create('Ext.data.Store', {
            	model : 'Ext.ux.desktop.ShortcutModel',
                proxy: {
                    type: 'ajax',
                    url: site_list_all,
                    reader: {
                        type: 'json',
                        rootProperty: 'sites'
                    }
                },
                autoLoad: true
            }),

            wallpaper: 'resources/images/wallpapers/Blue-Sencha.jpg',
            wallpaperStretch: false
        });
    },

    // config for the start menu
    getStartConfig : function() {
        var me = this, ret = me.callParent();

        return Ext.apply(ret, {
            title: 'Don Griffin',
            iconCls: 'user',
            height: 300,
            toolConfig: {
                width: 100,
                items: [
                    {
                        text:'Settings',
                        iconCls:'settings',
                        handler: me.onSettings,
                        scope: me
                    },
                    '-',
                    {
                        text:'Logout',
                        iconCls:'logout',
                        handler: me.onLogout,
                        scope: me
                    }
                ]
            }
        });
    },

    getTaskbarConfig: function () {
        var ret = this.callParent();

        return Ext.apply(ret, {
            quickStart: [
                { name: 'Accordion Window', iconCls: 'accordion', module: 'acc-win' },
                { name: 'Grid Window', iconCls: 'icon-grid', module: 'grid-win' }
            ],
            trayItems: [
                { xtype: 'trayclock', flex: 1 }
            ]
        });
    },

    onLogout: function () {
        Ext.Msg.confirm('Logout', 'Are you sure you want to logout?');
    },

    onSettings: function () {
        var dlg = new MyCms.view.setting.Settings({
            desktop: this.desktop
        });
        dlg.show();
    },
    doRefresh :function(){
    	var me = this;
    	me.desktop.shortcuts.load();
    },
    addSite : function(){
    	
    	var me = this, module = me.desktop.app.getModule('site-module'),
        win = module && module.createWindow({winType:'site-add-win'});

	    if (win) {
	        me.desktop.restoreWindow(win);
	    }
    },
    impSite:function(){
    	var me = this;
    	var win = Ext.create('MyCms.view.ux.ImpWindow',{
    		view:me,
    		url:site_imp,
    		title:'批量导入站点'
    	});
    	win.show();
    },
    modifySite:function(record){
    	var me = this, module = me.desktop.app.getModule('site-module'),
        win = module && module.createWindow({winType:'site-modify-win',obj:record});

	    if (win) {
	        me.desktop.restoreWindow(win);
	    }
    },
    deleteSite:function(record){
    	var me = this;
    	Ext.Msg.confirm('警告','你确定要删除该站点吗？',function(opt){
    		if(opt=='yes'){
    			Ext.Ajax.request({
        		    url: site_delete,
        		    params : {siteId:record.get('id')},
        		    success: function(response, opts) {
        		        var obj = Ext.decode(response.responseText);
        		        if(!obj.success){
        		        	Ext.Msg.alert('错误',obj.message);
        		        	return;
        		        }
        		        Ext.Msg.alert('提示',obj.message,function(){
        		        	me.desktop.fireEvent('refresh');
        		        },me); 
        		    },
        		    failure: function(response, opts) {
        		        console.log('server-side failure with status code ' + response.status);
        		    }
        		});
    		}
    	});
    }
    
});
