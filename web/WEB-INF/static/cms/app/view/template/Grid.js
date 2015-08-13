Ext
		.define(
				'MyCms.view.template.Grid',
				{
					extend : 'Ext.grid.Panel',
					uses : [ 'MyCms.model.Template',
							'MyCms.view.template.AddEdit' ],
					border : true,
					loadMask : true,
					selModel : {
						selType : 'checkboxmodel'
					},
					multiSelect : true,
					viewConfig : {
						trackOver : false,
						loadingText : '正在加载中...',
						emptyText : '<h1 style="margin:20px">未找到任何记录！！！</h1>'
					},
					columns : [ {
						xtype : 'rownumberer',
						width : 50,
						sortable : false
					},{
						text : "ID",
						dataIndex : 'id',
						flex : 1,
						sortable : false
					}, {
						tdCls : 'x-grid-cell-topic',
						text : "名称",
						dataIndex : 'name',
						flex : 3,
						sortable : false
					},{
						text : "类型",
						dataIndex : 'type',
						flex : 1,
						sortable : false,
						renderer:function(v){
							return MyCms.model.Template.Type[v]||'未知';
						}
					}, {
						text : "所属站点",
						dataIndex : 'siteName',
						flex : 1,
						sortable : false
					}, {
						text : "创建人",
						dataIndex : 'crUser',
						flex : 1,
						sortable : false
					}, {
						text : "创建时间",
						dataIndex : 'crTime',
						align : 'center',
						flex : 2,
						sortable : false
					} ],
					initComponent : function() {
						var me = this;
						me.cmpContextItems = [ {
							text : '刷新',
							handler : 'doRefresh',
							scope : me
						}, {
							text : '新增模板',
							handler : 'addTemplate',
							scope : me
						}, {
							text : '加载远程页面',
							handler : 'loadRemoteDoc',
							scope : me
						},{
							text : '导入',
							handler : 'import',
							scope : me
						},{
							text : '模板附件',
							handler : 'tempFileMgt',
							scope : me
						} ];

						var store = Ext.create('Ext.data.BufferedStore', {
							model : 'MyCms.model.Template',
							pageSize : 20,
							leadingBufferZone : 200,
							proxy : {
								type : 'ajax',
								url : template_list,
								extraParams : {
									siteId : me._view.site.get('id'),
									show : me._view.showType
								},
								reader : {
									rootProperty : 'list',
									totalProperty : 'totalCount'
								},
								simpleSortMode : true,
								filterParam : 'query'
							},
							listeners : {
								totalcountchange : function() {
									me.down('#status').update({
										count : store.getTotalCount()
									});
								}
							},
							remoteFilter : true,
							autoLoad : true
						});

						Ext.apply(me, {
							store : store,
							dockedItems : [ {
								dock : 'top',
								xtype : 'toolbar',
								items : [ {
									text : '面板操作',
									menu : me.cmpContextItems
								}, {
									width : 400,
									fieldLabel : '检索',
									labelWidth : 50,
									xtype : 'searchfield',
									store : store
								}, '->', {
									xtype : 'component',
									itemId : 'status',
									tpl : '总记录: {count}',
									style : 'margin-right:5px'
								} ]
							} ]
						});

						me.on('containercontextmenu', 'showCmpMenu', me);
						me.on('itemcontextmenu', 'showItemMenu', me);
						me.on('refresh', 'doRefresh', me);
						me.callParent();
					},
					tempFileMgt:function(){
						var me = this;
						
						Ext.create('MyCms.view.tempfile.Window',{
							from : me
						}).show();
					},
					loadRemoteDoc : function() {
						var me = this;

						Ext
								.create(
										'MyCms.view.ux.RemoteDocWin',
										{
											view : me,
											sureLoad : function() {
												var m = this, form = m
														.down('form'), url = form
														.getForm().findField(
																'url')
														.getValue();

												form
														.getForm()
														.submit(
																{
																	clientValidation : true,
																	url : template_remote,
																	params:{siteId : me._view.site.get('id')},
																	success : 'onLoadSuccess',
																	failure : function(
																			form,
																			action) {
																		Ext.Msg
																				.alert(
																						'失败',
																						action.result ? action.result.message
																								: 'No response');
																	},
																	scope:m
																});
											},
											onLoadSuccess : function(form,
													action) {
												if (action.result.success) {
													this.close();
													
													me.doRefresh();
												}
											}
										}).show()
					},
					doRefresh : function() {
						var me = this;
						me.getStore().load();
					},
					showItemMenu : function(_this, record, item, index, e,
							eOpts) {
						var me = this;
						Ext.create('MyCms.view.ux.MyMenu', {
							items : [ {
								text : '修改',
								handler : function() {
									me.modifyTemplate(record);
								},
								scope : me
							}, {
								text : '删除',
								handler : function() {
									me.deleteTemplate(record);
								},
								scope : me
							} ]
						}).showAt(e.getXY());

						e.stopEvent();
						e.stopPropagation();
					},
					showCmpMenu : function(_this, e, eOpts) {
						var me = this;
						Ext.create('MyCms.view.ux.MyMenu', {
							items : me.cmpContextItems
						}).showAt(e.getXY());

						e.stopEvent();
						e.stopPropagation();
					},
					addTemplate : function() {
						var win = Ext.getCmp('template-add-win')
								|| Ext.create('MyCms.view.template.AddEdit', {
									id : 'template-add-win',
									view : this
								});
						win.show();
					},
					modifyTemplate : function(record) {
						var win = Ext.getCmp('template-edit-win-'
								+ record.get('id'))
								|| Ext.create('MyCms.view.template.AddEdit', {
									id : 'template-edit-win-'
											+ record.get('id'),
									view : this,
									template : record
								});
						win.show();
					},
					deleteTemplate : function(record) {
						var me = this, rs = me.getSelectionModel()
								.getSelection(), ids = [];
						if (rs.length == 0) {
							ids.push(record.get('id'));
						} else {
							rs.forEach(function(r) {
								ids.push(r.get('id'));
							});
						}
						Ext.Msg
								.confirm(
										'警告',
										'您确认删除该项吗？',
										function(m) {
											if (m == 'yes') {
												Ext.Ajax
														.request({
															url : template_delete,
															params : {
																ids : ids
																		.join(',')
															},
															success : function(
																	response,
																	opts) {
																var obj = Ext
																		.decode(response.responseText);
																if (!obj.success) {
																	Ext.Msg
																			.alert(
																					'错误',
																					obj.message);
																	return;
																}
																me
																		.fireEvent(
																				'refresh',
																				me);
															},
															failure : function(
																	response,
																	opts) {
																console
																		.log('server-side failure with status code '
																				+ response.status);
															}
														});
											}
										});
					},
					import : function(){
						var me = this;
						Ext.create('MyCms.view.ux.ImpWindow',{
							view : me,
							url : template_imp,
							params : {
								siteId : me._view.site.get('id')
							}
						}).show();
					}
				});