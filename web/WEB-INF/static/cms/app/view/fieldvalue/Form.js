Ext.define('MyCms.view.fieldvalue.Form',{
	extend:'MyCms.view.ux.MyForm',
	bodyPadding: 5,
	layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    defaultType: 'textfield',
    autoScroll:true,
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
			var extField = extFields[i],fObj,fieldLabel = extField.name,fieldName = extField.field.name;
			
			if(extField.field.custom){
				fObj = {
						xtype:'container',
						defaultType: 'textfield',
						layout:{
							type: 'hbox',
			    	        align: 'stretch'
						},
						style:{
							'margin-bottom':'5px'
						},
						border:false,
						items:[{
							xtype:'textfield',
							fieldLabel: extField.name,
					        name: fieldName+'-show',
					        vtype:getVType(extField.field.type),
					        allowBlank: true,
					        flex:5,
					        readOnly:true
						},{
							xtype:'button',
							text:'点击编辑',
							flex:1,
							typeId:extField.field.type,
							extFieldId:extField.id,
							handler:function(){
								console.log(this.typeId)
								Ext.create('MyCms.view.fieldvalue.FieldWin',{
									channel:me.from.channel,
									customType:this.typeId,
									extFieldId:this.extFieldId,
									from:me
								}).show();
							}
						}]
				};
			}else if(extField.field.type=='date'){
				fObj = {
						xtype:'datefield',
						format:'Y-m-d',
						fieldLabel: extField.name,
				        name: fieldName,
				        allowBlank: true
				};
				
			}else if(extField.field.type=='text'){
				fObj = {
						xtype: 'fckeditor',
						fieldLabel: extField.name,
				        name: fieldName,
				        height:150,
				        allowBlank: true
				};
				
			}else{
				fObj = {
						xtype:'textfield',
						fieldLabel: extField.name,
				        name: fieldName,
				        vtype:getVType(extField.field.type),
				        allowBlank: true
				};
			}
			
			fields.push(fObj);
		}
		return fields;
		
		function getVType(type){
			
			console.log(type);
			if(type=='int'||type=='float') return type;
			return type=='int';
//			switch (type) {
//			case 1: return "int";
//			case 2: return null;
//			case 3: return "float";
//			case 4: return null;
//			case 5: return null;
//
//			default:
//				return null;
//			}
		}
	}
});