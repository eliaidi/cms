/*!
* Copyright(c) 2006-2014 Sencha Inc.
* licensing@sencha.com
* http://www.sencha.com/license
*/

// From code originally written by David Davis (http://www.sencha.com/blog/html5-video-canvas-and-ext-js/)

Ext.define('MyCms.view.copyright.AboutUs', {
    extend: 'Ext.ux.desktop.Module',
    uses: [
        'Ext.ux.desktop.Video'
    ],
    windowId: 'AboutUs',
    tipWidth: 160,
    tipHeight: 96,
    init : function(){
        this.launcher = {
            text: '关于我们'
        }
    },

    createWindow : function(){
        var me = this, desktop = me.app.getDesktop(),
            win = desktop.getWindow(me.windowId);

        if (!win) {
            win = desktop.createWindow({
                id: me.windowId,
                title: '关于我们',
                width: 740,
                height: 480,
                animCollapse: false,
                border: false,

                layout: 'fit',
                items: [{
                	xtype:'panel',
                	html:'<div style="margin:20px auto;text-align:center;line-height:20px"><h2 >这只是一个测试系统，仅供学习用途，测试内容：ExtJS 5.0.1、Spring Framework 4.1.2、Hibernate 4.3.7</h2></div>'
                }]
            });
        }

        if (me.tip) {
            me.tip.setTarget(win.taskButton.el);
        }

        return win;
    }

});
