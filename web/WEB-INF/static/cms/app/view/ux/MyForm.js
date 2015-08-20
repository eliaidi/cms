Ext.define('MyCms.view.ux.MyForm',{
	extend:'Ext.form.Panel',
	initComponent:function(){
		var me = this;
		
		me.callParent();
	},
	loadRecord:function(r){
		var me = this;
		this.getForm().loadRecord(r);
		
		var fcks = me.query('fckeditor');
		if(fcks){
			for(var i=0;i<fcks.length;i++){
				var fck = fcks[i],val = r.get(fck.name);
				fck.setValue(val||'');
			}
		}
	},
	findField:function(n){
		
		var me = this,fcks = me.query('fckeditor');
		if(fcks){
			for(var i=0;i<fcks.length;i++){
				var fck = fcks[i];
				if(fck.name==n){
					return fck;
				}
			}
		}
		return me.getForm().findField(n);
	}
});