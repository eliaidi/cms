Ext.define('MyCms.model.Field',{
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'name'
	},{
		name : 'type'
	},{
		name : 'isCustom',
		type : 'boolean'
	},{
		name : 'length'
	},{
		name : 'site'
	},{
		name : 'parent'
	},{
		name : 'siteName',
		convert : function(v, r) {
			var v = r.get('site');
			if (v) {
				v = v.descr;
			}
			return v;
		}
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
	} ],
	statics:{
		Type:{
			'int':'整型',
			'string':'字符型',
			'float':'浮点型',
			'date':'日期',
			'text':'文本'
		}
	}
});