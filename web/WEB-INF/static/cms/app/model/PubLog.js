Ext.define('MyCms.model.PubLog', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id'
	}, {
		name : 'obj'
	}, {
		name : 'objId'
	}, {
		name : 'objType'
	}, {
		name : 'descr'
	}, {
		name : 'success'
	}, {
		name : 'exception'
	},{
		name : 'zhs',convert:function(v,r){
			var st = r.get('startTime'),et = r.get('endTime'),v = '';
			if(st&&et){
				//v = Math.floor((new Date(et).getTime() - new Date(st).getTime())/1000);
				v = (new Date(et).getTime() - new Date(st).getTime())/1000;
			}
        	return v;
        } 
	},{
		name : 'startTime',type:'date',dateFormat:'Y-m-d H:i:s',convert:function(v){
        	if(v){
        		v = Ext.Date.format(new Date(v),this.dateFormat);
        	}
        	return v;
        } 
	},{
		name : 'endTime',type:'date',dateFormat:'Y-m-d H:i:s',convert:function(v){
        	if(v){
        		v = Ext.Date.format(new Date(v),this.dateFormat);
        	}
        	return v;
        } 
	},{
		name : 'pubUser'
	} ]
});