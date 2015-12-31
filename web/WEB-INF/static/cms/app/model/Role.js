Ext.define('MyCms.model.Role', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'name'
	},{
		name : 'isAdmin'
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
		
	}
});