Ext.define('MyCms.view.ux.DatetimeField',{
	extend:'Ext.form.field.Date',
	alias:'widget.datetimefield',
	requires:['MyCms.view.ux.DatetimePicker'],
	createPicker: function() {
        var me = this,
            format = Ext.String.format;

        // Create floating Picker BoundList. It will acquire a floatParent by looking up
        // its ancestor hierarchy (Pickers use their pickerField property as an upward link)
        // for a floating component.
        return new MyCms.view.ux.DatetimePicker({
            pickerField: me,
            floating: true,
            focusable: false, // Key events are listened from the input field which is never blurred
            hidden: true,
            minDate: me.minValue,
            maxDate: me.maxValue,
            disabledDatesRE: me.disabledDatesRE,
            disabledDatesText: me.disabledDatesText,
            disabledDays: me.disabledDays,
            disabledDaysText: me.disabledDaysText,
            format: me.format,
            showToday: me.showToday,
            startDay: me.startDay,
            minText: format(me.minText, me.formatDate(me.minValue)),
            maxText: format(me.maxText, me.formatDate(me.maxValue)),
            listeners: {
                scope: me,
                select: me.onSelect
            },
            keyNavConfig: {
                esc: function() {
                    me.collapse();
                }
            }
        });
    }
});