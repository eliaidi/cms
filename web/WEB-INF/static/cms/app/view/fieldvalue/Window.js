Ext.define('MyCms.view.fieldvalue.Window', {
	extend : 'MyCms.view.ux.MyWindow',
	modal : true,
	width : 850,
	height : 590,
	title : '扩展字段',
	initComponent : function() {
		var me = this;
		me.extraValue = {};
		me.extraStore = {};

		me.form = Ext.create('MyCms.view.fieldvalue.Form', {
			from : me
		});
		var btns = [ {
			text : '确定',
			handler : 'onOk',
			scope : me
		}, {
			text : '重置',
			handler : 'okReset',
			scope : me
		} ];
		
		Ext.apply(me,{
			items:[me.form],
			buttons:btns
		});
		
		if(me.document){
			me.initDate();
		}
		
		me.on('fieldWinOk','onFieldWinOK',me);
		me.on('afterrender','onAfterRender',me);

		me.callParent();
	},
	onAfterRender:function(){
		var me = this;
		//console.log(me.document);
	},
	onFieldWinOK:function(me,fWin,store){
		var rs = [],originRs = [],group = 0;
		store.each(function(r){
			originRs.push(r);
			for(var k in r.data){
				if(k=='id'){
					continue;
				}
				var v = r.get(k);
				if(typeof v =='object'&&v){
					v = Ext.Date.format(new Date(v.getTime()),'Y-m-d');
				}
				rs.push({
					"field.id":k,
					"extField.id":fWin.extFieldId,
					"value":v,
					"group":group
				})
			}
			group++;
		});
		me.extraValue[fWin.extFieldId] = rs;
		me.extraStore[fWin.extFieldId] = originRs;
		
		me.form.getForm().findField(me.getFieldById(fWin.extFieldId).field.name+'-show').setValue('已输入'+originRs.length+'条记录！');
		fWin.close();
	},
	getFieldById:function(id){
		var me = this,extFields = me.channel.get('extFields');
		for(var i=0;i<extFields.length;i++){
			if(extFields[i].id==id){
				return extFields[i];
			}
		}
		return null;
	},
	initDate:function(){
		var me = this,form = me.form,fieldValues = me.document.get('fieldValues');
		for(var i=0;i<fieldValues.length;i++){
			var fieldValue = fieldValues[i],formField = form.findField(fieldValue.extField.field.name);
			//if(!formField) continue;
			if(!fieldValue.extField.field.custom){
				formField.setValue(fieldValue.value);
			}else{
				if(!me.extraValue[fieldValue.extField.id]){
					me.extraValue[fieldValue.extField.id] = [];
				}
				me.extraValue[fieldValue.extField.id].push({
					"field.id":fieldValue.field.id,
					"extField.id":fieldValue.extField.id,
					"value":fieldValue.value,
					"group":fieldValue.group
				});
				
				if(!me.extraStore[fieldValue.extField.id]){
					me.extraStore[fieldValue.extField.id] = [];
				}
				var group = fieldValue.group;
				
				if(!me.extraStore[fieldValue.extField.id][group]){
					me.extraStore[fieldValue.extField.id][group] = {};
				}
				me.extraStore[fieldValue.extField.id][group][fieldValue.field.id] = fieldValue.value;
			}
		}
//		console.log(me.document)
		console.log('extraStore',me.extraStore);
		console.log('extraValue',me.extraValue);
		
		for(var i=0;i<fieldValues.length;i++){
			var fieldValue = fieldValues[i];
			if(fieldValue.extField.field.custom){
				var formField = form.findField(fieldValue.extField.field.name+'-show');
				if(!formField) continue;
				formField.setValue('已输入'+me.extraStore[fieldValue.extField.id].length+'条记录！');
			}
		}
	},
	onOk:function(){
		var me = this,form = me.form,fields = me.channel.get('extFields'),rs = [];
		for(var i=0;i<fields.length;i++){
			var field = fields[i],f = form.findField(field.field.name);
			if(field.field.custom){
				continue;
			}
			rs.push({
				"extField.id" : field.id,
				"field.id":field.field.id,
				"value" : field.field.type=='date'?f.formatDate(f.getValue()):f.getValue()
			});
		}
		me.from.fireEvent('extFieldComplete',me.from,me,rs);
	},
	okReset:function(){
		var me = this;
		me.extraValue = {};
		me.extraStore = {};
		
		me.form.getForm().reset();
		if(me.document){
			me.initDate();
		}
	}
});