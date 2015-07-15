Ext.define('MyCms.model.ClipBoard',{
	extend: 'Ext.data.Model',
    fields: [
        { name: 'eType' },
        { name: 'aType' },
        { name: 'data' }
    ],
    initComponent:function(e,a,d){
    	var me = this;
    	
    	me.set('eType',e);
    	me.set('aType',a);
    	me.set('data',a);
    	me.callParent();
    }
})