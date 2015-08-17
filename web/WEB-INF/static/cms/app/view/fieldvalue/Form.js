Ext.define('MyCms.view.fieldvalue.Form',{
	extend:'Ext.form.Panel',
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
		
		for(var i=0;i<extFields.length;i++){
			var extField = extFields[i],fObj;
			
			if(extField.type==4){
				fObj = {
						xtype:'datefield',
						format:'Y-m-d H:i:s',
						fieldLabel: extField.label,
				        name: extField.name,
				        allowBlank: true,
				        _eId : extField.id
				};
				
			}else if(extField.type==5){
				fObj = {
						xtype: 'htmleditor',
						fieldLabel: extField.label,
				        name: extField.name,
				        height:118,
				        allowBlank: true,
				        _eId : extField.id
				};
				
			}else{
				fObj = {
						xtype:'textfield',
						fieldLabel: extField.label,
				        name: extField.name,
				        vtype:getVType(extField.type),
				        allowBlank: true,
				        _eId : extField.id
				};
			}
			
			fields.push(fObj);
		}
		return fields;
		
		function getVType(type){
			
			switch (type) {
			case 1: return "int";
			case 2: return "alphanum";
			case 3: return "float";
			case 4: return null;
			case 5: return null;

			default:
				return null;
			}
		}
	}
});