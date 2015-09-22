Ext.define('MyCms.view.ux.DatetimePicker',{
	extend:'Ext.picker.Date',
	alias:'widget.datetimepicker',
	renderTpl: [
	            '<div id="{id}-innerEl" data-ref="innerEl">',
	                '<div class="{baseCls}-header">',
	                    '<div id="{id}-prevEl" data-ref="prevEl" class="{baseCls}-prev {baseCls}-arrow" role="button" title="{prevText}"></div>',
	                    '<div id="{id}-middleBtnEl" data-ref="middleBtnEl" class="{baseCls}-month" role="heading">{%this.renderMonthBtn(values, out)%}</div>',
	                    '<div id="{id}-nextEl" data-ref="nextEl" class="{baseCls}-next {baseCls}-arrow" role="button" title="{nextText}"></div>',
	                '</div>',
	                '<table role="grid" id="{id}-eventEl" data-ref="eventEl" class="{baseCls}-inner" {%',
	                    // If the DatePicker is focusable, make its eventEl tabbable.
	                    'if (values.$comp.focusable) {out.push("tabindex=\\\"0\\\"");}',
	                '%} cellspacing="0">',
	                    '<thead><tr role="row">',
	                        '<tpl for="dayNames">',
	                            '<th role="columnheader" class="{parent.baseCls}-column-header" aria-label="{.}">',
	                                '<div role="presentation" class="{parent.baseCls}-column-header-inner">{.:this.firstInitial}</div>',
	                            '</th>',
	                        '</tpl>',
	                    '</tr></thead>',
	                    '<tbody><tr role="row">',
	                        '<tpl for="days">',
	                            '{#:this.isEndOfWeek}',
	                            '<td role="gridcell" id="{[Ext.id()]}">',
	                                '<div hidefocus="on" class="{parent.baseCls}-date"></div>',
	                            '</td>',
	                        '</tpl>',
	                    '</tr></tbody>',
	                '</table>',
	                '<tpl if="showToday">',
	                    '<div id="{id}-footerEl" data-ref="footerEl" role="presentation" class="{baseCls}-footer">{%this.renderTodayBtn(values, out)%}</div>',
	                '</tpl>',
	            '</div>',
	            {
	                firstInitial: function(value) {
	                    return Ext.picker.Date.prototype.getDayInitial(value);
	                },
	                isEndOfWeek: function(value) {
	                    // convert from 1 based index to 0 based
	                    // by decrementing value once.
	                    value--;
	                    var end = value % 7 === 0 && value !== 0;
	                    return end ? '</tr><tr role="row">' : '';
	                },
	                renderTodayBtn: function(values, out) {
	                    Ext.DomHelper.generateMarkup(values.$comp.todayBtn.getRenderTree(), out);
	                },
	                renderMonthBtn: function(values, out) {
	                    Ext.DomHelper.generateMarkup(values.$comp.monthBtn.getRenderTree(), out);
	                }
	            }
	        ]
});