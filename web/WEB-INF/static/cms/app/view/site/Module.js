/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.define('MyCms.view.site.Module', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.data.ArrayStore',
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer',
        'MyCms.view.channel.View',
        'MyCms.model.*',
        'Ext.ux.DataView.*'
    ],

    id:'site-module',

    init : function(){
    	
    },

    createWindow : function(cfg){
    	if(!cfg) return null;
    	if(cfg.winType=='site-add-win') return this.createSiteWin();
    	if(cfg.winType=='site-modify-win') return this.createModifyWin(cfg.obj);
    	if(cfg.winType=='site-view-win') return this.createViewWin(cfg.obj);
    	
        return null;
    },
    createViewWin:function(record){
    	var desktop = this.app.getDesktop();
        var win = desktop.getWindow('site-view-'+record.get('id'));
        if(!win){
            win = desktop.createWindow({
                id: 'site-view-'+record.get('id'),
                title:'站点【'+record.get('name')+'】下的栏目',
                width:740,
                height:480,
                iconCls: 'icon-grid',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [
                    Ext.create('MyCms.view.channel.View',{
                    	site:record
                    })
                ],
                tbar:[]
            });
        }
        return win;
    },
    createSiteWin:function(){
    	var desktop = this.app.getDesktop();
        var win = desktop.getWindow('site-add-window');
        if(!win){
            win = desktop.createWindow({
            	id:'site-add-window',
                desktop:desktop
            },MyCms.view.site.Window);
        }
        return win;
    },
    createModifyWin:function(record){
    	var desktop = this.app.getDesktop();
        var win = desktop.getWindow('site-modify-window');
        if(win){
        	Ext.destroy(win);
        }
        win = desktop.createWindow({
        	id:'site-modify-window',
            desktop:desktop,
            site:record
        },MyCms.view.site.Window);
            
        return win;
    }
});

