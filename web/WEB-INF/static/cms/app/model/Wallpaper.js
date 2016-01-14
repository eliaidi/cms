Ext.define('MyCms.model.Wallpaper', {
    extend: 'Ext.data.TreeModel',
    fields: [
        { name: 'text',convert:function(v,r){
        	var f = r.get('file');
        	if(f){
        		return f.fileName;
        	}
        	return null;
        } },
        { name: 'img',convert:function(v,r){
        	
        	var f = r.get('file');
        	if(f){
        		return RootPath+'/file/app/'+f.id;
        	}
        	return null;
        } },
        { name: 'leaf',convert:function(v,r){
        	
        	return r.get('id')!='root';
        } }
    ]
});
