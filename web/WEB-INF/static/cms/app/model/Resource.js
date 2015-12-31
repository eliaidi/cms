Ext.define('MyCms.model.Resource', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'name'
	},{
		name : 'value'
	},{
		name : 'type'
	},{
		name : 'crUser'
	},{
		name : 'crUserName',
		convert : function(v, r) {
			var f = r.get('crUser');
			if (f) {
				v = f.username;
			}
			return v;
		}
	}, {
		name : 'crTime',
		type : 'date',
		dateFormat : 'Y-m-d H:i:s',
		convert : function(v, r) {
			if (v) {
				v = Ext.Date.format(new Date(v), this.dateFormat);
			}
			return v;
		}
	} ],
	statics:{
		Types:{
			1:'网站管理',
			2:'系统管理'
		}
	}
});