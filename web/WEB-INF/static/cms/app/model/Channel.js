Ext.define('MyCms.model.Channel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'name'
	}, {
		name : 'descr'
	}, {
		name : 'site'
	}, {
		name : 'parent'
	}, {
		name : 'crTime'
	}, {
		name : 'crUser'
	},{
		name : 'otempIds'
	},{
		name : 'dtempIds'
	} ]
});