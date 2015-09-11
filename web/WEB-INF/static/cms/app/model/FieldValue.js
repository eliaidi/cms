Ext.define('MyCms.model.FieldValue',{
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
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
	},{
		name : 'value'
	},{
		name : 'crUser'
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