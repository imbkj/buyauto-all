

var phoneDisplay = function (m) {
    if (m.length >= 11) {
        return m.substring(0, 3) + "****" + m.substring(8, 11);
    }

    return m;
}


// 格式化千位符
var thousandBitSeparator = function (num) {
  num = Math.round(num);
  var num = (num || 0).toString(), result = '';
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) { result = num + result; }
    return result;
	}

// 过滤返回值的null
var filtration_String_return_null = function (s) {
    if (s) {
        return s;
    }
    return "";
};

//小数点两位
var chkdecimalt = function (val) {
    return /^[0-9]+([.]\d{1,2})?$/.test(val);
};

//取数值小数点两位 不足补0
var changeTwoDecimal = function (x) {
    var f_x = x;
    if (isNaN(f_x) || f_x == null) {
        return "0.00";
    }
    var s_x = f_x.toString();
    var pos_decimal = s_x.indexOf(".");
    if (pos_decimal < 0) {
        pos_decimal = s_x.length;
        s_x += ".";
    }
    if (s_x.length - pos_decimal > 3) {
        s_x = s_x.substring(0, pos_decimal + 3);
    } else {
        while (s_x.length <= pos_decimal + 2) {
            s_x += 0;
        }
    }
    return s_x;
};


// 设置价格为 千 与 万
var changePriceT = function(x) {
	var f_x = x;
	if (isNaN(f_x)) {
		return "0元";
	}
	f_x = parseInt(x * 100) / 100;
	if (f_x >= 10000) {
		f_x = parseInt(f_x / 10000 * 100) / 100;
		f_x += "万";
	}
	return f_x;
};
//扩展日期格式化
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
        // millisecond
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

/* 
 *  方法:Array.remove(dx) 
 *  功能:根据元素值删除数组元素. 
 *  参数:元素值 
 *  返回:在原数组上修改数组 
 *  作者：pxp 
 */  
Array.prototype.indexOf = function (val) {  
    for (var i = 0; i < this.length; i++) {  
        if (this[i] == val) {  
            return i;  
        }  
    }  
    return -1;  
};  

Array.prototype.indexOfName = function (name) {  
    for (var i = 0; i < this.length; i++) {  
        if (this[i].name == name) {  
            return -1;  
        }  
    }  
    return 1;  
}; 

Array.prototype.remove = function (val) {  
    var index = this.indexOf(val);  
    if (index > -1) {  
        this.splice(index, 1);  
    }  
};  

$.fn.toggler = function(fn,fn2) {
    var args = arguments,guid = fn.guid || $.guid++,i=0,
    toggler = function( event ) {
      var lastToggle = ( $._data( this, "lastToggle" + fn.guid ) || 0 ) % i;
      $._data( this, "lastToggle" + fn.guid, lastToggle + 1 );
      event.preventDefault();
      return args[ lastToggle ].apply( this, arguments ) || false;
    };
    toggler.guid = guid;
    while ( i < args.length ) {
      args[ i++ ].guid = guid	;
    }
    return this.click( toggler );
};




