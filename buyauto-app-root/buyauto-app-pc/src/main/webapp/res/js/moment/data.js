 /**
 * 瀹屾暣浠ｇ爜
 */


// 鍏充簬鏈堜唤锛� 鍦ㄨ缃椂瑕�-1锛屼娇鐢ㄦ椂瑕�+1
$(function () {

  $('#calendar').calendar({
    ifSwitch: true, // 鏄惁鍒囨崲鏈堜唤
    hoverDate: true, // hover鏄惁鏄剧ず褰撳ぉ淇℃伅
    backToday: true // 鏄惁杩斿洖褰撳ぉ
  });

});

;(function ($, window, document, undefined) {

  var Calendar = function (elem, options) {
    this.$calendar = elem;

    this.defaults = {
      ifSwitch: true,
      hoverDate: false,
      backToday: false
    };

    this.opts = $.extend({}, this.defaults, options);

    // console.log(this.opts);
  };

  Calendar.prototype = {
    showHoverInfo: function (obj) { // hover 鏃舵樉绀哄綋澶╀俊鎭�
      var _dateStr = $(obj).attr('data');
      var offset_t = $(obj).offset().top + (this.$calendar_today.height() - $(obj).height()) / 2;
      var offset_l = $(obj).offset().left + $(obj).width();
      var changeStr = addMark(_dateStr);
      var _week = changingStr(changeStr).getDay();
      var _weekStr = '';

      this.$calendar_today.show();

      this.$calendar_today
            .css({left: offset_l + 30, top: offset_t})
            .stop()
            .animate({left: offset_l + 16, top: offset_t});

      switch(_week) {
        case 0:
          _weekStr = 'Sunday';
        break;
        case 1:
          _weekStr = 'Monday';
        break;
        case 2:
          _weekStr = 'Tuesday';
        break;
        case 3:
          _weekStr = 'Wednesday';
        break;
        case 4:
          _weekStr = 'Thursday';
        break;
        case 5:
          _weekStr = 'Friday';
        break;
        case 6:
          _weekStr = 'Saturday';
        break;
      }

      this.$calendarToday_date.text(changeStr);
      this.$calendarToday_week.text(_weekStr);
    },

    showCalendar: function () { // 杈撳叆鏁版嵁骞舵樉绀�
      var self = this;
      var year = dateObj.getDate().getFullYear();
      var month = dateObj.getDate().getMonth() + 1;
      var dateStr = returnDateStr(dateObj.getDate());
      var firstDay = new Date(year, month - 1, 1); // 褰撳墠鏈堢殑绗竴澶�

      this.$calendarTitle_text.text(year + '/' + dateStr.substr(4, 2));

      this.$calendarDate_item.each(function (i) {
        // allDay: 寰楀埌褰撳墠鍒楄〃鏄剧ず鐨勬墍鏈夊ぉ鏁�
        var allDay = new Date(year, month - 1, i + 1 - firstDay.getDay());
        var allDay_str = returnDateStr(allDay);

        $(this).text(allDay.getDate()).attr('data', allDay_str);

        if (returnDateStr(new Date()) === allDay_str) {
          $(this).attr('class', 'item item-curDay');
        } else if (returnDateStr(firstDay).substr(0, 6) === allDay_str.substr(0, 6)) {
          $(this).attr('class', 'item item-curMonth');
        } else {
          $(this).attr('class', 'item');
        }
      });

      // 宸查€夋嫨鐨勬儏鍐典笅锛屽垏鎹㈡棩鏈熶篃涓嶄細鏀瑰彉
      if (self.selected_data) {
        var selected_elem = self.$calendar_date.find('[data='+self.selected_data+']');

        selected_elem.addClass('item-selected');
      }
    },

    renderDOM: function () { // 娓叉煋DOM
      this.$calendar_title = $('<div class="calendar-title"></div>');
      this.$calendar_week = $('<ul class="calendar-week"></ul>');
      this.$calendar_date = $('<ul class="calendar-date"></ul>');
      this.$calendar_today = $('<div class="calendar-today"></div>');


      var _titleStr = '<a href="#" class="title"></a>'+
                      '<a href="javascript:;" id="backToday">T</a>'+
                      '<div class="arrow">'+
                        '<span class="arrow-prev"><</span>'+
                        '<span class="arrow-next">></span>'+
                      '</div>';
      var _weekStr = '<li class="item">日</li>'+
                      '<li class="item">一</li>'+
                      '<li class="item">二</li>'+
                      '<li class="item">三</li>'+
                      '<li class="item">四</li>'+
                      '<li class="item">五</li>'+
                      '<li class="item">六</li>';
      var _dateStr = '';
      var _dayStr = '<i class="triangle"></i>'+
                    '<p class="date"></p>'+
                    '<p class="week"></p>';

      for (var i = 0; i < 6; i++) {
        _dateStr += '<li class="item">26</li>'+
                    '<li class="item">26</li>'+
                    '<li class="item">26</li>'+
                    '<li class="item">26</li>'+
                    '<li class="item">26</li>'+
                    '<li class="item">26</li>'+
                    '<li class="item">26</li>';
      }

      this.$calendar_title.html(_titleStr);
      this.$calendar_week.html(_weekStr);
      this.$calendar_date.html(_dateStr);
      this.$calendar_today.html(_dayStr);

      this.$calendar.append(this.$calendar_title, this.$calendar_week, this.$calendar_date, this.$calendar_today);
      //this.$calendar.show();
    },

    inital: function () { // 鍒濆鍖�
      var self = this;

      this.renderDOM();

      this.$calendarTitle_text = this.$calendar_title.find('.title');
      this.$backToday = $('#backToday');
      this.$arrow_prev = this.$calendar_title.find('.arrow-prev');
      this.$arrow_next = this.$calendar_title.find('.arrow-next');
      this.$calendarDate_item = this.$calendar_date.find('.item');
      this.$calendarToday_date = this.$calendar_today.find('.date');
      this.$calendarToday_week = this.$calendar_today.find('.week');

      this.selected_data = 0;

      this.showCalendar();

      if (this.opts.ifSwitch) {
        this.$arrow_prev.bind('click', function () {
          var _date = dateObj.getDate();

          dateObj.setDate(new Date(_date.getFullYear(), _date.getMonth() - 1, 1));

          self.showCalendar();
        });

        this.$arrow_next.bind('click', function () {
          var _date = dateObj.getDate();

          dateObj.setDate(new Date(_date.getFullYear(), _date.getMonth() + 1, 1));

          self.showCalendar();
        });
      }

      if (this.opts.backToday) {
        var cur_month = dateObj.getDate().getMonth() + 1;

        this.$backToday.bind('click', function () {
          var item_month = $('.item-curMonth').eq(0).attr('data').substr(4, 2);
          var if_lastDay = (item_month != cur_month) ? true : false;

          if (!self.$calendarDate_item.hasClass('item-curDay') || if_lastDay) {
            dateObj.setDate(new Date());

            self.showCalendar();
          }
        });
      }

      this.$calendarDate_item.hover(function () {
        self.showHoverInfo($(this));
      }, function () {
        self.$calendar_today.css({left: 0, top: 0}).hide();
      });

      this.$calendarDate_item.click(function () {
        var _dateStr = $(this).attr('data');
        var _date = changingStr(addMark(_dateStr));
        var $curClick = null;

        self.selected_data = $(this).attr('data');

        dateObj.setDate(new Date(_date.getFullYear(), _date.getMonth(), 1));

        if (!$(this).hasClass('item-curMonth')) {
          self.showCalendar();
        }

        $curClick = self.$calendar_date.find('[data='+_dateStr+']');
        $curDay = self.$calendar_date.find('.item-curDay');
        if (!$curClick.hasClass('item-selected')) {
          self.$calendarDate_item.removeClass('item-selected');

          $curClick.addClass('item-selected');
        }
      });
    },

    constructor: Calendar
  };

  $.fn.calendar = function (options) {
    var calendar = new Calendar(this, options);

    return calendar.inital();
  };


  // ========== 浣跨敤鍒扮殑鏂规硶 ==========

  var dateObj = (function () {
    var _date = new Date();

    return {
      getDate: function () {
        return _date;
      },

      setDate: function (date) {
        _date = date;
      }
    }
  })();

  function returnDateStr(date) { // 鏃ユ湡杞瓧绗︿覆
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();

    month = month <= 9 ? ('0' + month) : ('' + month);
    day = day <= 9 ? ('0' + day) : ('' + day);

    return year + month + day;
  };

  function changingStr(fDate) { // 瀛楃涓茶浆鏃ユ湡
    var fullDate = fDate.split("-");
    
    return new Date(fullDate[0], fullDate[1] - 1, fullDate[2]); 
  };

  function addMark(dateStr) { // 缁欎紶杩涙潵鐨勬棩鏈熷瓧绗︿覆鍔�-
    return dateStr.substr(0, 4) + '-' + dateStr.substr(4, 2) + '-' + dateStr.substring(6);
  };

  // 鏉′欢1锛氬勾浠藉繀椤昏鑳借4鏁撮櫎
  // 鏉′欢2锛氬勾浠戒笉鑳芥槸鏁寸櫨鏁�
  // 鏉′欢3锛氬勾浠芥槸400鐨勫€嶆暟
  function isLeapYear(year) { // 鍒ゆ柇闂板勾
    return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
  }

})(jQuery, window, document);