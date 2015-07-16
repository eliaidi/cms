Ext.define('MyCms.model.Appendix', {
	extend : 'Ext.data.Model',
	fields : [
			{
				name : 'id'
			},
			{
				name : 'document'
			},
			{
				name : 'name'
			},
			{
				name : 'fileId',
				convert : function(v, r) {
					var f = r.get('file');
					if (f) {
						v = f.id;
					}
					return v;
				}
			},
			{
				name : 'fileName',
				convert : function(v, r) {
					var f = r.get('file');
					if (f) {
						v = f.fileName;
					}
					return v;
				}
			},
			{
				name : 'fileExt',
				convert : function(v, r) {
					var f = r.get('file');
					if (f) {
						v = f.fileExt;
					}
					return v;
				}
			},
			{
				name : 'fileSize',
				type : 'int',
				convert : function(v, r) {
					var f = r.get('file');
					if (f) {
						v = f.fileSize;
					}
					return v;
				}
			},
			{
				name : 'type',
				type : 'int',
				convert : function(v) {
					if (v) {
						v = MyCms.model.Appendix.TypeMapping[v];
					}
					return v;
				}
			},
			{
				name : 'addition'
			},
			{
				name : 'crTime',
				type : 'date',
				dateFormat : 'Y-m-d H:i:s',
				convert : function(v, r) {
					var f = r.get('file');
					if (f) {
						v = Ext.Date.format(new Date(f.crTime),
								this.dateFormat);
					}
					return v;
				}
			}, {
				name : 'crUser',
				convert : function(v, r) {
					var f = r.get('file');
					if (f) {
						v = f.crUser ? f.crUser.username : v;
					}
					return v;
				}
			}, {
				name : 'url',
				convert : function(v, r) {
					var f = r.get('file')
					if (f) {
						v = "file/app/" + f.id;
					}
					return v;
				}
			} ],
	statics : {
		TypeMapping : {
			1 : '图片',
			2 : '文件',
			3 : '其他'
		}
	}
});