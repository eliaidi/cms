Ext.define('MyCms.model.ExtField', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'channel'
	}, {
		name : 'label'
	}, {
		name : 'name'
	},{
		name : 'type'
	}, {
		name : 'length'
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
	} ],
	statics:{
		Type:{
			1:'整型',
			2:'字符型',
			3:'浮点型',
			4:'日期',
			5:'文本'
		}
	}
});
