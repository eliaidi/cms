Ext
		.define(
				'MyCms.view.channel.ImgView',
				{
					extend : 'Ext.view.View',
					tpl : [
							'<tpl for=".">',
							'<div class="thumb-wrap">',
							'<div class="thumb">',
							'<img src="' + RootPath
									+ '/resources/images/forms.png" title="编号：{id}" />',
							'</div>', '<span>{name}</span>', '</div>', '</tpl>' ],

					itemSelector : 'div.thumb-wrap',
					mixins : {
						dragSelector : 'Ext.ux.DataView.DragSelector',
						draggable : 'Ext.ux.DataView.Draggable'
					},
					multiSelect : true,
					cls : 'images-view',
					scrollable : true,
					initComponent : function() {
						var me = this;

						Ext.apply(me, {
							store : new Ext.data.Store({
								autoLoad : true,
								model : 'MyCms.model.Channel',
								proxy : {
									type : 'ajax',
									url : list_channel_by_siteid,
									extraParams : me.view.parent ? {
										parentId : me.view.parent.get('id')
									} : (me.view.site ? {
										siteId : me.view.site.get('id')
									} : null),
									reader : {
										type : 'json',
										rootProperty : 'channels'
									}
								}
							})
						});

						me.on('render', me.initDragDrop);

						me.callParent();
					},
					initDragDrop : function() {
						var me = this;
						initDragZone(me);
						initDropZone(me);

						function initDragZone(v) {
							v.dragZone = Ext.create('Ext.dd.DragZone',v.getEl(),
								{
									getDragData : function(e) {
										var sourceEl = e.getTarget(v.itemSelector, 10), d;
										if (sourceEl) {
											d = sourceEl.cloneNode(true);
							                d.id = Ext.id();
											return (v.dragData = {
													sourceEl: sourceEl,
								                    repairXY: Ext.fly(sourceEl).getXY(),
								                    ddel: d,
								                    rec: getDataByDom(sourceEl)
											});
										}
									},
									getRepairXY : function() {
										return this.dragData.repairXY;
									}
								});
						}

						function initDropZone(v) {
							v.dropZone = Ext.create('Ext.dd.DropZone',v.el,
								{
									getTargetFromEvent : function(e) {
										return e.getTarget('.thumb-wrap');
									},
									onNodeEnter : function(target,dd, e, data) {
										if(this.onNodeOver(target,dd, e, data)==Ext.dd.DropZone.prototype.dropAllowed){
											Ext.fly(target).addCls('view-target-hover');
										}
									},
									onNodeOut : function(target,dd, e, data) {
										Ext.fly(target).removeCls('view-target-hover');
									},
									onNodeOver : function(target,dd, e, data) {
										console.log(target);
										var  proto = Ext.dd.DropZone.prototype,tData = getDataByDom(target),nextData = target.previousSibling?getDataByDom(target.previousSibling):null;

										return  tData.get('id')==data.rec.get('id')||(nextData?nextData.get('id')==data.rec.get('id'):false)?proto.dropNotAllowed:proto.dropAllowed;
									},

									onNodeDrop : function(target,dd, e, data) {
										if(this.onNodeOver(target,dd, e, data)==Ext.dd.DropZone.prototype.dropAllowed){
											var tRec = getDataByDom(target),cRec = data.rec;
											doResort(cRec,tRec);
											return true;
										}
										return false;
									}
								});
							
						}
						
						function getDataByDom(t){
							return me.getRecord(t);
						}
						
						function doResort(c,t){
							Ext.Ajax.request({
								url : channel_move,
								params : {
									currId : c.get('id'),
									targetId : t.get('id')
								},
								success : function(response, opts) {
									var obj = Ext.decode(response.responseText);
									if (!obj.success) {
										Ext.Msg.alert('错误', obj.message);
										return;
									}
									me.view.fireEvent('refresh',me.view);
								},
								failure : function(response, opts) {
									console.log('server-side failure with status code '
											+ response.status);
								}
							});
						}
					}
				});