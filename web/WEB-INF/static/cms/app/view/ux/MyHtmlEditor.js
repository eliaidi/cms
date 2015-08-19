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

						me.html = "<iframe  id='"
								+ me.editorFrameId
								+ "' name='"
								+ me.editorFrameId
								+ "' src='' frameborder=0  width='100%' height='100%' > </iframe>";
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
					fillHtmlValue : function() {
						var me = this, form = me.up('form'), fck = me.eWin.document
								.getElementById("editor1");

						var thisField = form.getForm().findField(this.name);
						if (!thisField) {
							thisField = Ext.create('Ext.form.field.Hidden', {
								name : me.name
							});
							form.add(thisField);
						}
						thisField.setValue(fck.value);
					}

				});