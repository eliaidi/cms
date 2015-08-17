Ext.define('MyCms.view.fieldvalue.Window', {
	extend : 'MyCms.view.ux.MyWindow',
	modal : true,
	width : 850,
	height : 590,
	title : '扩展字段',
	initComponent : function() {
		var me = this;

		var form = Ext.create('MyCms.view.fieldvalue.Form', {
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
			items:[form],
			buttons:btns
		});

		me.callParent();
	},
	onOk:function(){
		var me = this,form = me.down('form'),fields = form.getFields(),rs = [];
		console.log(fields);
		for(var i=0;i<fields.length;i++){
			rs.push({
				"extField.id" : fields[i]._eId,
				"value" : fields[i].getValue()
			});
		}
		me.from.fireEvent('extFieldComplete',me.from,me,rs);
	},
	okReset:function(){
		
	}
});