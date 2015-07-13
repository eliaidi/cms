Ext.define('MyCms.model.Site', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id' },
        { name: 'name' },
        { name: 'descr' },
        { name: 'crTime' },
        { name: 'crUser' },
        { name: 'canPubSta' }
    ],
    statics:{
    	DefaultPubSta:{
    		1:'新稿',
    		2:'已编辑',
    		3:'已审核',
    		4:'已发布'
    	}
    }
});
