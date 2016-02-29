Ext
		.define(
				'MyCms.view.ux.MyHtmlEditor',
				{
					extend : 'Ext.Component',
					alias : 'widget.fckeditor',
					requires : [ 'Ext.form.Panel' ],
					initComponent : function() {

						var me = this;
						me.editorFrameId = Ext.id();
						
						me.html = '<label  data-ref="labelEl" class="x-form-item-label x-form-item-label-default  x-unselectable" style="padding-right:5px;width:105px;" ><span class="x-form-item-label-inner x-form-item-label-inner-default" style="width:100px">'
							+ me.fieldLabel + ':</span></label><div style="width:'+(this.width?this.width+'px':'100%')+';height:'+(this.height?this.height+'px':'150px')+'" data-ref="bodyEl" class="x-form-item-body x-form-item-body-default x-form-text-field-body x-form-text-field-body-default  "><div  data-ref="triggerWrap" class="x-form-trigger-wrap x-form-trigger-wrap-default"><div  data-ref="inputWrap" class="x-form-text-wrap x-form-text-wrap-default"><iframe  id="'
							+ me.editorFrameId
							+ '" name="'
							+ me.editorFrameId
							+ '" src="" frameborder=0  width="100%" height="100%" > </iframe></div></div></div>';
						me.on('render', 'doAfterRender', me);
						me.callParent();
						
					},
					doAfterRender : function() {
						var me = this,form = me.up('form');
						
						form.on('afterlayout',function(){
							var pw = me.el.dom.style.width,pw = pw.substring(0,pw.length-2);
							var fcw = me.el.dom.children[0].style.width;fcw = fcw.substring(0,fcw.length-2);
							me.el.dom.children[1].style.width = me.width?me.width+'px':(pw-fcw)+'px';
							me.el.dom.children[1].style.maxWidth = me.width?me.width+'px':(pw-fcw)+'px';
						});
						
						this.eWin = window.open(RootPath
								+ "/document/fckeditor?height="
								+ (this.height || 400) + "&label="
								+ this.fieldLabel, this.editorFrameId,
								"resizable=no,scrollbars=yes,status=no");

						this.up('form').on('beforeaction', 'fillHtmlValue',
								this);
					},
					getField : function() {
						var me = this, form = me.up('form');
						var thisField = form.getForm().findField(me.name);
						if (!thisField) {
							thisField = Ext.create('Ext.form.field.Hidden', {
								name : me.name
							});
							form.add(thisField);
						}
						return thisField;
					},
					fillHtmlValue : function() {
						var me = this, form = me.up('form'), fck = me.eWin.CKEDITOR.instances.editor1;

						me.getField().setValue(fck.getData());
					},
					setValue : function(val) {
						var me = this, form = me.up('form'), field = me
								.getField();

						field.setValue(val);
						
						var frameLoop = setInterval(function() {
							if (me.eWin && me.eWin.document && me.eWin.CKEDITOR) {
								var fck = me.eWin.CKEDITOR.instances.editor1;
								if (fck) {
									if (fck.getData() != val) {
										fck.setData(val);
									}
									clearInterval(frameLoop);
								}
							}
						}, 200);
					},
					getValue : function() {
						var me = this, fck = me.eWin.CKEDITOR.instances.editor1, val = fck
								.getData();

						me.getField().setValue(val);
						return val;
					}

				});