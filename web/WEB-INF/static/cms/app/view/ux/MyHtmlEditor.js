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

						me.html = "<label>"+me.fieldLabel+":</label> <iframe  id='"
								+ me.editorFrameId
								+ "' name='"
								+ me.editorFrameId
								+ "' src='' frameborder=0  width='90%' height='100%' > </iframe>";
						me.on('render', 'doAfterRender', me);
						me.callParent();
					},
					doAfterRender : function() {
						this.eWin = window.open(RootPath
								+ "/document/fckeditor?height="+(this.height||400)+"&label="
								+ this.fieldLabel, this.editorFrameId,
								"resizable=no,scrollbars=yes,status=no");

						this.up('form').on('beforeaction', 'fillHtmlValue',
								this);
					},
					getField:function(){
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
					setValue:function(val){
						var me = this, form = me.up('form'),field = me.getField();
						
						field.setValue(val);
						var frameLoop = setInterval(function(){
							if(me.eWin.document&&me.eWin.CKEDITOR){
								var fck = me.eWin.CKEDITOR.instances.editor1;
								if(fck){
									if(fck.getData()!=val){
										fck.setData(val);
									}
									clearInterval(frameLoop);
								}
							}
						}, 200);
					},
					getValue:function(){
						var me = this, fck = me.eWin.CKEDITOR.instances.editor1,val = fck.getData();
						
						me.getField().setValue(val);
						return val;
					}

				});