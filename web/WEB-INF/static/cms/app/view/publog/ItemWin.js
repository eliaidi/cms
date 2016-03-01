Ext.define('MyCms.view.publog.ItemWin',{
	extend:'MyCms.view.ux.MyWindow',
	width : 821,
	height : 844 ,
	modal : true,
	title : '查看日志详情',
	initComponent:function(){
		var me = this;
		var form = Ext.create('Ext.form.Panel',{
			layout: 'anchor',
		    defaults: {
		        anchor: '100%',
		        margin:'10px auto',
		        readOnly:true
		    },
		    defaultType: 'textfield',
			items:[{
				fieldLabel: 'ID',
		        name: 'id'
			},{
				fieldLabel: '发布对象',
		        name: 'obj'
			},{
				fieldLabel: '发布对象ID',
		        name: 'objId'
			},{
				fieldLabel: '发布对象类型',
		        name: 'objType'
			},{
				fieldLabel: '发布是否成功',
		        name: 'success'
			},{
				fieldLabel: '发布开始时间',
		        name: 'startTime'
			},{
				fieldLabel: '发布结束时间',
		        name: 'endTime'
			},{
				fieldLabel: '发布总耗时（秒）',
		        name: 'zhs'
			},{
				fieldLabel: '发布人',
		        name: 'pubUser'
			},{
				fieldLabel: '描述',
		        name: 'descr',
		        xtype : 'textarea'
			},{
				fieldLabel: '异常信息',
		        name: 'exception',
		        xtype : 'textarea'
			}]
		});
		Ext.apply(me,{
			items : [form]
		});
		
		me.callParent();
	},
	loadData:function(r){
		var me = this;
		
		me.down('form').getForm().loadRecord(r);
		return me;
	}
});