/**  
 * @author vanya  
 */  
$(document).ready(function(){        
	
	$.validator.setDefaults({       
		onkeyup:false      
	});
	
	// ��֤���֤����
	function isIdCardNo(num) {
		var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
		var parityBit=new Array("1","0","X","9","8","7","6","5","4","3","2");
		var varArray = new Array();
		var intValue;
		var lngProduct = 0;
		var intCheckDigit;
		var intStrLen = num.length;
		var idNumber = num;
		// initialize
		if ((intStrLen != 15) && (intStrLen != 18)) {
			return false;
		}
		// check and set value
		for(i=0;i<intStrLen;i++) {
			varArray[i] = idNumber.charAt(i);
			if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
				return false;
			} else if (i < 17) {
				varArray[i] = varArray[i] * factorArr[i];
			}
		}
		
		if (intStrLen == 18) {
			//check date
			var date8 = idNumber.substring(6,14);
			if (isDate8(date8) == false) {
				return false;
			}
			// calculate the sum of the products
			for(i=0;i<17;i++) {
				lngProduct = lngProduct + varArray[i];
			}
			// calculate the check digit
			intCheckDigit = parityBit[lngProduct % 11];
			// check last digit
			if (varArray[17] != intCheckDigit) {
				return false;
			}
		}else{        //length is 15
			//check date
			var date6 = idNumber.substring(6,12);
			
			if (isDate6(date6) == false) {
				return false;
			}
		}
		return true;
	}
	
	// ��֤���� 00 00 00
	function isDate6(sDate) {
		if(!/^[0-9]{6}$/.test(sDate)) {
			return false;
		}
		var year, month, day;
		year = 19 + sDate.substring(0, 2);
		month = sDate.substring(4, 6);
		if (year < 1700 || year > 2500) return false
		if (month < 1 || month > 12) return false
		return true
	}
	
	// ��֤���� 0000 00 00
	function isDate8(sDate) {
		if(!/^[0-9]{8}$/.test(sDate)) {
			return false;
		}
		var year, month, day;
		year = sDate.substring(0, 4);
		month = sDate.substring(4, 6);
		day = sDate.substring(6, 8);
		var iaMonthDays = [31,28,31,30,31,30,31,31,30,31,30,31]
		if (year < 1700 || year > 2500) return false
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1]=29;
		if (month < 1 || month > 12) return false
		if (day < 1 || day > iaMonthDays[month - 1]) return false
		return true
	}
	
	// �ַ���֤ ֻ�ܰ��������֡�Ӣ����ĸ�����ֺ��»���,������ȫ��ǿո��ȫ��Ӣ����ĸ
	jQuery.validator.addMethod("stringCheck", function(value, element) {       ///^[\u0391-\uFFE5\w]+$/
		return this.optional(element) || /^[0-9a-zA-Z\u4e00-\u9fa5_-]*$/.test(value);       
	}, "");
	
	// ��֤����,Ӣ��,����
	jQuery.validator.addMethod("stringCheckSub", function(value, element) {       
		return this.optional(element) || /^[a-zA-Z0-9\u4E00-\u9FA5]+$/.test(value);       
	}, "");
	
	// ��֤����
	jQuery.validator.addMethod("isChinese", function(value, element) {
		var newstr = value.replace(/[^\u4e00-\u9fa5]/gi,"");
		var chineseStr = false;
		if(newstr.length == value.length) {
			chineseStr = true;
		}
		return this.optional(element) || chineseStr;
	}, "");  
	
	// �����������ֽ�       
	jQuery.validator.addMethod("byteRangeLength", function(value, element, param) {       
		var length = value.length;       
		for(var i = 0; i < value.length; i++){       
			if(value.charCodeAt(i) > 127){       
			length++;       
			}       
		}
		return this.optional(element) || ( length >= param[0] && length <= param[1] );       
	}, "");   
	  
	// ���֤������֤       
	jQuery.validator.addMethod("isIdCardNo", function(value, element) {       
		return this.optional(element) || isIdCardNo(value);       
	}, "");    
		 
	// �ֻ�������֤       
	jQuery.validator.addMethod("isMobile", function(value, element) {       
		var length = value.length;   
		//var mobile = /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/; 
		var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;    
		return this.optional(element) || (length == 11 && mobile.test(value));       
	}, "");       
		 
	// �绰������֤       
	jQuery.validator.addMethod("isTel", function(value, element) {       
		//var tel = /^\d{3,4}-?\d{7,9}$/;    //�绰�����ʽ010-12345678  
		var tel = /^\d{3,4}?\d{7,8}$/;
		return this.optional(element) || tel.test(value);       
	}, "");   
	  
	// ��ϵ�绰(�ֻ�/�绰�Կ�)��֤   
	jQuery.validator.addMethod("isPhone", function(value,element) {   
		var length = value.length;   
		//var mobile = /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/; 
		var cmccMobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
		var tel = /^\d{3,4}?\d{7,9}$/;   
		return this.optional(element) || ((length == 12 && tel.test(value)) || (length == 11 && cmccMobile.test(value)));   
	  
	}, "");   
		 
	// ����������֤       
	jQuery.validator.addMethod("isZipCode", function(value, element) {       
		var tel = /^[0-9]{6}$/;       
		return this.optional(element) || (tel.test(value));       
	}, "");
	
	// �����֤       
	jQuery.validator.addMethod("isMoney", function(value, element) {       
		var val = /^([1-9][\d]*|0)(\.[\d]{1,2})?$/;       
		return this.optional(element) || (val.test(value) && value > 0);       
	}, "");
	
	
	// �Ƚ��ض�����       
	jQuery.validator.addMethod("isDateOk", function(value, element) {       
 		var beginTime=$("#send_date_start").val();
		var endTime=value;
		var beginTimes=beginTime.substring(0,10).split('-');
        var endTimes=endTime.substring(0,10).split('-');
		beginTime=beginTimes[1]+'/'+beginTimes[2]+'/'+beginTimes[0]+' '+beginTime.substring(10,19);
		endTime=endTimes[1]+'/'+endTimes[2]+'/'+endTimes[0]+' '+endTime.substring(10,19);
		return this.optional(element) || (Date.parse(beginTime)<Date.parse(endTime));       
	}, "");
	
	// �Ƚ��ض�ʱ��       
	jQuery.validator.addMethod("isTimeOk", function(value, element, param) {       
 		var starttime=Number($("#actStartTime").val());
		var endtime=Number(value);
		if ( !this.depend(param, element) )return "dependency-mismatch";//ȡ����param��ֵ�Ƿ�ѡ��
		return this.optional(element) || (starttime<endtime);
	}, "");
	
	// ʱ����ڵ��ڽ���       
	jQuery.validator.addMethod("betoday", function(value, element, param) {  
		var endTime=value;
		var endTimes=endTime.substring(0,10).split('-');
		endTime=endTimes[1]+'/'+endTimes[2]+'/'+endTimes[0]+' '+endTime.substring(10,19);
 		var myDate = new Date();
		var today = Date.parse((myDate.getMonth()+1)+"/"+myDate.getDate()+"/"+myDate.getFullYear()+" "+myDate.getHours()+":"+myDate.getMinutes()+":"+myDate.getSeconds()); 
		if ( !this.depend(param, element) )return "dependency-mismatch";//ȡ����param��ֵ�Ƿ�ѡ��
		return this.optional(element) || (Date.parse(endTime)>today);
	}, "");
	
});