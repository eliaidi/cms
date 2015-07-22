Ext.define('MyCms.model.Template', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'name'
	},{
		name : 'siteName',
		convert : function(v, r) {
			var f = r.get('site');
			if (f) {
				v = f.name;
			}
			return v;
		}
	}, {
		name : 'fileId',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = f.id;
			}
			return v;
		}
	}, {
		name : 'content',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = f.content;
			}
			return v;
		}
	}, {
		name : 'crUser',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = f.crUser;
			}
			return v;
		}
	}, {
		name : 'crTime',
		type : 'date',
		dateFormat : 'Y-m-d H:i:s',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = Ext.Date.format(new Date(f.crTime), this.dateFormat);
			}
			return v;
		}
	} ]
});