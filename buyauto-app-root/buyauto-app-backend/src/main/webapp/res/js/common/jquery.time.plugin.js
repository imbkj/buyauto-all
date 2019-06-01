var timePlugins = (function () {

    var _this;

    function timePlugins(starDate, endDate, reportrange) {
        _this = this;
        this.starDate = starDate;
        this.endDate = endDate;
        this.reportrange = reportrange;
        this.daterangepicker_options = {
            "singleDatePicker": true,
            "timePicker": true,
            timePicker12Hour : false, //是否使用12小时制来显
            "timePickerSeconds": true,
            "timePickerIncrement" : 1, //时间的增量，单位为分钟
            "showDropdowns": true,
            "startDate": new Date().format('MM/dd/yyyy'),
            "endDate": "12/31/2028",
            format: 'MM/DD/YYYY',
            locale: {
                applyLabel: '确定',
                cancelLabel: '清除',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                //firstDay: 1
            }
        };
        
        this.cb = function (start, end, label) {

        	if(this.endDate.length == 0){
	        	$('#' + this.reportrange + ' span').html(start.format('YYYY/MM/DD HH:mm:ss') );
	        	 $("#" + this.starDate).val(start.format('YYYY/MM/DD HH:mm:ss'));
        	}else{
        		$('#' + this.reportrange + ' span').html(
	            	start.format('YYYY/MM/DD') + ' - ' + end.format('YYYY/MM/DD'));
	            	 $("#" + this.starDate).val(start.format('YYYY-MM-DD 00:00:00'));
	        		$("#" + this.endDate).val(end.format('YYYY-MM-DD 23:59:59'));
        	}
	        
	       
	    }

        this.optionSet1 = {
            /*
             * startDate: moment().subtract(29, 'days'), endDate: moment(),
             */
            minDate: '01/01/2012',
            maxDate: '12/31/2028',
            /*
             * dateLimit: { days: 60 },
             */
            showDropdowns: true,
            showWeekNumbers: true,
            timePicker: false,
            timePickerIncrement: 1,
            timePicker12Hour: true,
            ranges: {
                '今天': [moment(), moment()],
                '昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '最近7天': [moment().subtract(6, 'days'), moment()],
                '最近30天': [moment().subtract(29, 'days'), moment()],
                '当月': [moment().startOf('month'), moment().endOf('month')],
                '上月': [moment().subtract(1, 'month').startOf('month'),
                    moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'left',
            buttonClasses: ['btn btn-default'],
            applyClass: 'btn-small btn-primary',
            cancelClass: 'btn-small',
            format: 'MM/DD/YYYY',
            separator: ' to ',
            locale: {
                applyLabel: '确定',
                cancelLabel: '清除',
                fromLabel: '开始时间',
                toLabel: '结束时间',
                customRangeLabel: '时间范围',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10',
                    '11', '12'],
                // firstDay: 1
            }
        };
    }



    timePlugins.prototype.init = function (v) {
    	var that  = this;
        if (v) {
            $('#' + this.reportrange).daterangepicker(this.daterangepicker_options);
            $('#' + this.reportrange).on('apply.daterangepicker', function (ev, picker) {
                  console.log("apply event fired, start/end dates are " + picker.startDate.format('YYYY-MM-DD 00:00:00') + " to " + picker.endDate.format('YYYY-MM-DD 23:59:59'));
                 that.cb(picker.startDate, picker.endDate);

            });

            $('#' + this.reportrange).on('cancel.daterangepicker', function (ev, picker) {
                $('#' + that.reportrange).find("span").html("&nbsp;&nbsp;请选择时间&nbsp;");
                $('#' + that.reportrange).parent().find("input[type=hidden]").val('');
            });
        } else {


            $('#' + this.reportrange).daterangepicker(this.optionSet1);
            $('#' + this.reportrange).on('show.daterangepicker', function () {
                console.log("show event fired");
            });
            $('#' + this.reportrange).on('hide.daterangepicker', function () {
                console.log("hide event fired");
            });

            $('#' + this.reportrange).on('apply.daterangepicker', function (ev, picker) {
                console.log("apply event fired, start/end dates are " + picker.startDate.format('YYYY-MM-DD 00:00:00') + " to " + picker.endDate.format('YYYY-MM-DD 23:59:59'));

                that.cb(picker.startDate, picker.endDate);
            });


            $('#' + this.reportrange).on('cancel.daterangepicker', function (ev, picker) {
                $('#' + that.reportrange + ' span').html("&nbsp;&nbsp;请选择时间范围&nbsp;");
                $("#" + that.starDate).val('');
                $("#" + that.endDate).val('');
            });

        }
    }

    return timePlugins;
})();




