/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('MyCms.view.setting.Settings', {
    extend: 'MyCms.view.ux.MyWindow',

    uses: [
        'Ext.tree.Panel',
        'Ext.tree.View',
        'Ext.form.field.Checkbox',
        'Ext.layout.container.Anchor',
        'Ext.layout.container.Border',

        'Ext.ux.desktop.Wallpaper',

        'MyCms.model.Wallpaper'
    ],

    layout: 'anchor',
    title: '背景图设置',
    modal: true,
    width: 640,
    height: 480,
    border: false,

    initComponent: function () {
        var me = this;

        me.selected = me.desktop.getWallpaper();
        me.stretch = me.desktop.wallpaper.stretch;

        me.preview = Ext.create('widget.wallpaper');
        me.preview.setWallpaper(me.selected);
        me.tree = me.createTree();

        me.buttons = [
            { text: '确定', handler: me.onOK, scope: me },
            { text: '取消', handler: me.close, scope: me }
        ];

        me.items = [
            {
                anchor: '0 -30',
                border: false,
                layout: 'border',
                items: [
                    me.tree,
                    {
                        xtype: 'panel',
                        title: '预览',
                        region: 'center',
                        layout: 'fit',
                        items: [ me.preview ]
                    }
                ]
            },
            {
                xtype: 'checkbox',
                boxLabel: 'Stretch to fit',
                checked: me.stretch,
                listeners: {
                    change: function (comp) {
                        me.stretch = comp.checked;
                    }
                }
            }
        ];
        
        Ext.apply(me,{
        	tbar:[{
        		text: '添加背景图', handler: 'addPic', scope: me 
        	}]
        });

        me.callParent();
    },
    addPic:function(){
    	var me2 = this;
    	Ext.create('MyCms.view.ux.MyWindow',{
    		title:'上传背景图',
    		modal:true,
    		width:500,
    		height:138,
    		items:[{
    			xtype:'form',
    			margin:'5px 5px',
    			layout:'anchor',
    			defaults:{
    				anchor: '100%',
    		        margin:'10px auto',
    				allowBlank:false
    			},
    			items:[{
    				xtype: 'filefield',
    		        name: 'f',
    		        fieldLabel: '文件',
    		        buttonText: '选择文件'
    			}]
    		}],
    		onSuccess:function(form, action){
    			var r = action.result;
    	    	if(r.success){
    	    		Ext.Msg.alert('提示','背景图保存成功！');
    	    		me2.refreshPicList();
    	    		this.close();
    	    	}
    		},
    		buttons:[
    		         { text: '确定', handler: function(){
    		        	 var btn = this,me = this.up('window'),form = me.down('form');
    		    			form.getForm().submit({
    		    	    		clientValidation: true,
    		    	    	    url: wallpaper_save,
    		    	            success: 'onSuccess',
    		    	            failure: function(form, action) {
    		    	               Ext.Msg.alert('失败', action.result ? action.result.message : 'No response');
    		    	            },
    		    	            scope:me
    		    	        });
    		         } },
    		         { text: '取消', handler: function(){
    		        	 
    		         } }
    		         ]
    	}).show();
    },
    refreshPicList:function(){
    	var me = this;
    	
    	me.tree.getStore().load();
    },

    createTree : function() {
        var me = this;

        function child (img) {
            return { img: img, text: me.getTextOfWallpaper(img), iconCls: '', leaf: true };
        }

        var tree = new Ext.tree.Panel({
            title: '选择',
            rootVisible: false,
            lines: false,
            scrollable: true,
            width: 150,
            region: 'west',
            split: true,
            minWidth: 100,
            listeners: {
                afterrender: { fn: this.setInitialSelection, delay: 100 },
                select: this.onSelect,
                itemcontextmenu : this.onItemContextMenu,
                scope: this
            },
            store: new Ext.data.TreeStore({
                model: 'MyCms.model.Wallpaper',
                proxy : {
    				type : 'ajax',
    				url : wallpaper_list,
    				reader : {
    					rootProperty : 'list',
    					totalProperty : 'totalCount'
    				}
    			},
                root: {
                    text:'Wallpaper',
                    expanded: true
                }
            })
        });

        return tree;
    },
    onItemContextMenu:function( _this, record, item, index, e, eOpts ){
    	e.preventDefault();
    	var me = this;
    	Ext.create('MyCms.view.ux.MyMenu',{
    		items:[{ text: '删除', handler: function(){
	        	 Ext.Msg.confirm('警告','你确认删除该项吗？',function(m){
	        		 if(m=='yes'){
	        			 Ext.Ajax.request({
	     					url : wallpaper_delete,
	     					params : {
	     						id : record.get('id')
	     					},
	     					success : function(response, opts) {
	     						var obj = Ext.decode(response.responseText);
	     						if (!obj.success) {
	     							Ext.Msg.alert('错误', obj.message);
	     							return;
	     						}
	     						me.refreshPicList();
	     					},
	     					failure : function(response, opts) {
	     						console.log('server-side failure with status code '
	     								+ response.status);
	     					}
	     				});
	        		 }
	        	 })
	         } }]
    	}).showAt(e.pageX,e.pageY);
    },

    getTextOfWallpaper: function (path) {
        var text = path, slash = path.lastIndexOf('/');
        if (slash >= 0) {
            text = text.substring(slash+1);
        }
        var dot = text.lastIndexOf('.');
        text = Ext.String.capitalize(text.substring(0, dot));
        text = text.replace(/[-]/g, ' ');
        return text;
    },

    onOK: function () {
        var me = this;
        if (me.selected) {
            me.desktop.setWallpaper(me.selected.get('img'), me.stretch);
        }
        if(!me.selected.isModel) return;
        Ext.Ajax.request({
				url : wallpaper_use,
				params : {
					id : me.selected.get('id')
				},
				success : function(response, opts) {
					var obj = Ext.decode(response.responseText);
					if (!obj.success) {
						Ext.Msg.alert('错误', obj.message);
						return;
					}
					me.destroy();
				},
				failure : function(response, opts) {
					console.log('server-side failure with status code '
							+ response.status);
				}
			});
    },

    onSelect: function (tree, record) {
        var me = this;

        if (record.data.img) {
            //me.selected = record.data.img;
        	me.selected = record;
        	me.preview.setWallpaper(me.selected.get('img'));
        } else {
            me.selected = Ext.BLANK_IMAGE_URL;
            me.preview.setWallpaper(me.selected);
        }

        
    },

    setInitialSelection: function () {
        var s = this.desktop.getWallpaper();
        if (s) {
            var path = '/Wallpaper/' + this.getTextOfWallpaper(s);
            this.tree.selectPath(path, 'text');
        }
    }
});
