Ext.define('MyCms.model.ExtField', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'channel'
	}, {
		name : 'name'
	}, {
		name : 'fieldName',
		convert : function(v, r) {
			var v = r.get('field');
			if (v) {
				v = v.name;
			}
			return v;
		}
	},{
		name : 'fieldType',
		convert : function(v, r) {
			var v = r.get('field');
			if (v) {
				v = v.type;
			}
			return v;
		}
	}, {
		name : 'fieldLength',
		convert : function(v, r) {
			var v = r.get('field');
			if (v) {
				v = v.length;
			}
			return v;
		}
	}, {
		name : 'crUser'
	},{
		name : 'chnlName',
		convert : function(v, r) {
			var v = r.get('channel');
			if (v) {
				v = v.descr;
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
	} ]
});
