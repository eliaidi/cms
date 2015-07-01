Ext.define('MyCms.view.document.Module', {
    extend: 'Ext.ux.desktop.Module',
    requires: [
        'MyCms.view.document.Window'
    ],
    id:'document-module',

    init : function(){
    	
    },
    createWindow : function(cfg){
    	if(!cfg) return null;
    	if(cfg.winType=='document-window') return this.createDocumentWin(cfg);
    	
        return null;
    },
    createDocumentWin:function(cfg){
    	var desktop = this.app.getDesktop();
        var win = desktop.getWindow('document-window'+(cfg.document?('-'+cfg.document.get('id')):''));
        if(win){
        	Ext.destroy(win);
        }
        if(cfg.document){
        	cfg["id"] = 'document-window-'+cfg.document.get('id');
        }
        win = desktop.createWindow(cfg,MyCms.view.document.Window);
        
        return win;
    }
});

