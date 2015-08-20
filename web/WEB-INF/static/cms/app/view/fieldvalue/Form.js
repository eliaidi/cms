Ext.define('MyCms.view.fieldvalue.Form',{
	extend:'MyCms.view.ux.MyForm',
	bodyPadding: 5,
	layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    defaultType: 'textfield',
	initComponent:function(){
		var me = this;
		
		var fields = me.getFields();
		
		Ext.apply(me,{
			items:fields
		});
		me.callParent();
	},
	getFields:function(){
		var me = this,extFields = me.from.channel.get('extFields'),fields = [];
		
		extFields.sort(function(a,b){
			return b.crTime - a.crTime;
		});
		for(var i=0;i<extFields.length;i++){
			var extField = extFields[i],fObj;
			
			if(extField.type==4){
				fObj = {
						xtype:'datefield',
						format:'Y-m-d',
						fieldLabel: extField.label,
				        name: extField.name,
				        allowBlank: true
				};
				
			}else if(extField.type==5){
				fObj = {
						xtype: 'fckeditor',
						fieldLabel: extField.label,
				        name: extField.name,
				        height:150,
				        allowBlank: true
				};
				
			}else{
				fObj = {
						xtype:'textfield',
						fieldLabel: extField.label,
				        name: extField.name,
				        vtype:getVType(extField.type),
				        allowBlank: true
				};
			}
			
			fields.push(fObj);
		}
		return fields;
		
		function getVType(type){
			
			switch (type) {
			case 1: return "int";
			case 2: return null;
			case 3: return "float";
			case 4: return null;
			case 5: return null;

			default:
				return null;
			}
		}
	}
});