Ext.define('MyCms.view.channel.Module', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'MyCms.view.channel.Window',
        'MyCms.view.channel.ComplexWindow'
    ],

    id:'channel-module',

    init : function(){
    	
    },

    createWindow : function(cfg){
    	if(!cfg) return null;
    	if(cfg.winType=='channel-window') return this.createChannelWin(cfg);
    	if(cfg.winType=='channel-complex-window') return this.createComplexChannelWin(cfg);
    	
        return null;
    },
    createComplexChannelWin:function(cfg){
    	var desktop = this.app.getDesktop();
        var win = desktop.getWindow('channel-complex-window-'+cfg.channel.get('id'));
        if(!win){
        	cfg['id'] = 'channel-complex-window-'+cfg.channel.get('id');
        	win = desktop.createWindow(cfg,MyCms.view.channel.ComplexWindow);
        }
        return win;
    },
    createChannelWin:function(cfg){
    	var desktop = this.app.getDesktop();
        var win = desktop.getWindow('channel-window');
        if(win){
        	Ext.destroy(win);
        }
        win = desktop.createWindow(cfg,MyCms.view.channel.Window);
        
        return win;
    }
});

