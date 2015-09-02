Ext.define('MyCms.view.field.AddEdit',{
	extend:'MyCms.view.ux.MyWindow',
	modal:true,
	width:626,
	height:379,
	initComponent:function(){
		var me = this;
		me.title = '新增站点【'+me.site.get('name')+'】的自定义字段';
		var grid = Ext.create('MyCms.view.field.EditGrid',{
			title:'字段属性',
			flex : 4
		});
		var btns = [{
			text : '保存',
			handler : 'doSave',
			scope : me
		},{
			text : '重置',
			handler : 'doReset',
			scope : me
		}];
		
		Ext.apply(me,{
			layout:{
				type: 'vbox',
    	        align: 'stretch'
			},
			items:[{
				xtype:'form',
				layout: 'anchor',
			    defaults: {
			        anchor: '100%'
			    },
			    defaultType: 'textfield',
			    items:[{
					fieldLabel: '字段名称',
			        name: 'name',
			        allowBlank: false
				}],
				flex:1
			},grid],
			buttons:btns
		});
		
		me.on('afterrender','doAfterRender',me);
		me.callParent();
	},
	doAfterRender:function(){
		this.doReset();
	},
	doSave:function(){
		var me = this,form = me.down('form'),params = {'site.id':me.site.get('id')},store = me.down('grid').getStore(),index=0;
		console.log(store);
		store.each(function(r){

			for(var k in r.data){
				if((k=='id'&&r.get(k).indexOf('MyCms.model.Field')>=0)||!r.get(k)||typeof r.get(k) == 'object') continue;
				params['children['+index+'].'+k] = r.get(k);
			}
			index++;
		});
		if(me.field){
			params['id'] = me.field.get('id');
		}
		console.log(params);
		form.getForm().submit({
    		clientValidation: true,
    	    url: field_save,
    	    params:params,
            success: 'onSuccess',
            failure: function(form, action) {
               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
            },
            scope:me
        });
	},
	onSuccess:function(){
		var me = this;
		me.close();
		me.from.fireEvent('refresh',me.from);
	},
	doReset:function(){
		
		var me = this,form = me.down('form'),grid = me.down('grid');
		form.getForm().reset();
		if(me.field){
			form.getForm().loadRecord(me.field);
			grid.getStore().loadData(me.field.get('children'));
		}
	}
});