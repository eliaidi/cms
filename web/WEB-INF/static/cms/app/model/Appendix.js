Ext.define('MyCms.model.Appendix', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id' },
        { name: 'document' },
        { name: 'type',type:'int',convert:function(v){
        	if(v){
        		v = MyCms.model.Appendix.TypeMapping[v];
        	}
        	return v;
        } },
        { name: 'addition'},
        { name: 'crTime' },
        { name: 'crUser',type:'date',dateFormat:'Y-m-d H:i:s',convert:function(v){
        	if(v){
        		v = Ext.Date.format(new Date(v),this.dateFormat);
        	}
        	return v;
        } }
    ],
    statics:{
    	TypeMapping:{
    		1:'图片',
    		2:'文件',
    		3:'其他'
    	}
    }
});