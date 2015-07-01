Ext.define('MyCms.model.Site', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id' },
        { name: 'name' },
        { name: 'descr' },
        { name: 'crTime' },
        { name: 'crUser' }
    ]
});
