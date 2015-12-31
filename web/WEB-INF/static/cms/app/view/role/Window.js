Ext.define('MyCms.view.role.Window',{
	extend:'MyCms.view.ux.MyWindow',
	width:680,
	height:330,
	modal:true,
	initComponent:function(){
		var me = this;
		me.title = '配置角色';
		if(me.user){
			me.title += '【'+me.user.get('username')+'】';
		}
		
		var grid = Ext.create('MyCms.view.role.Grid',{user:me.user});
		var buttons = [{
			text : '确定',
			handler : 'onOk',
			scope : me
		},{
			text : '重置',
			handler : 'onReset',
			scope : me
		}];
		var appCfg = {
				layout:'fit',
				items:[grid]
		};
		if(me.from){
			appCfg.buttons = buttons
		}
		Ext.apply(me,appCfg);
		
		me.on('afterrender','doAfterRender',me);
		me.callParent();
	},
	doAfterRender:function(){
		var me = this;
		
		me.down('grid').getStore().on('load',function(){
			if(me.user){
				if(me.user.get('roles')){
					me.selectItems();
				}else{
					me.onReset();
				}
			}
		})
		
	},
	onOk:function(){
		var me = this,grid = me.down('grid'),rs = grid.getSelectionModel().getSelection();
		
		if(rs.length==0){
			Ext.Msg.alert('Error','请选择待操作的记录！');
			return;
		}
		
		me.from.fireEvent('onFinish',rs,me.user,me);
	},
	onReset:function(){
		var me = this;
		if(me.user){
			Ext.Ajax.request({
				url : role_list,
				params : {
					start:0,
					limit:200,
					userId:me.user.get('id')
				},
				success : function(response, opts) {
					var obj = Ext.decode(response.responseText);
					if(obj.list){
						me.user.set('roles',obj.list);
						me.selectItems();
					}
				},
				failure : function(response, opts) {
					console.log('server-side failure with status code '
							+ response.status);
				}
			});
		}
	},
	selectItems:function(){
		var me = this,grid = me.down('grid'),sm = grid.getSelectionModel(),l = me.user.get('roles'),store = grid.getStore(),rs = [];
		
		sm.deselectAll();
		store.each(function(r){
			for(var i=0;i<l.length;i++){
				if(l[i].id===r.get('id')){
					rs.push(r);
				}
			}
		});
		console.log(rs);
		sm.select(rs);
	}
});