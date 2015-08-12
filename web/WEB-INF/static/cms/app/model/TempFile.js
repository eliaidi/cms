Ext.define('MyCms.model.TempFile', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	},{
		name : 'content',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = f.content;
			}
			return v;
		}
	}, {
		name : 'fileName',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = f.fileName;
			}
			return v;
		}
	},{
		name : 'fileExt',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = f.fileExt;
			}
			return v;
		}
	},{
		name : 'fileSize',
		convert : function(v, r) {
			var f = r.get('file');
			if (f) {
				v = f.fileSize;
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
	} ],
	isPic : function(){
		var picExts = ['jpg','png','gif','bmp'];
		for(var i=0;i<picExts.length;i++){
			if(picExts[i]==this.get('fileExt').toLowerCase()){
				return true;
			}
		}
		return false;
	}
});