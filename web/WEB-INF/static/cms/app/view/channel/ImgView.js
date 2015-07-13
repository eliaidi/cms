Ext.define('MyCms.view.channel.ImgView', {
	extend : 'Ext.view.View',
	tpl : [ '<tpl for=".">', '<div class="thumb-wrap">', '<div class="thumb">',
			'<img src="' + RootPath + '/resources/images/forms.png" />',
			'</div>', '<span>{name}</span>', '</div>', '</tpl>' ],

	itemSelector : 'div.thumb-wrap',
	mixins: {
        dragSelector: 'Ext.ux.DataView.DragSelector',
        draggable   : 'Ext.ux.DataView.Draggable'
    },
	multiSelect : true,
	singleSelect : false,
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
		this.mixins.dragSelector.init(this);
		me.callParent();
	}
});