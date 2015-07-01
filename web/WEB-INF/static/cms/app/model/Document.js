Ext.define('MyCms.model.Document', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id' },
        { name: 'title' },
        { name: 'abst' },
        { name: 'content' },
        { name: 'status',type:'int',convert:function(v){
        	if(v){
        		v = MyCms.model.Document.StatusMapping[v];
        	}
        	return v;
        } },
        { name: 'author' },
        { name: 'writeTime',type:'date',dateFormat:'Y-m-d',convert:function(v){
        	if(v){
        		v = Ext.Date.format(new Date(v),this.dateFormat);
        	}
        	return v;
        } },
        { name: 'channel' },
        { name: 'site' },
        { name: 'crUser' },
        { name: 'crTime',type:'date',dateFormat:'Y-m-d H:i:s',convert:function(v){
        	if(v){
        		v = Ext.Date.format(new Date(v),this.dateFormat);
        	}
        	return v;
        } }
    ],
    statics:{
    	StatusMapping:{
    		1:'新稿',
    		2:'已编辑',
    		3:'已审核',
    		4:'已发布',
    		5:'已退回'
    	}
    }
});