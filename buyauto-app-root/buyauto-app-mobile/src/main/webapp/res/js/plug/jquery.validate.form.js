   

  // 手机号码验证 
    $.validator.addMethod("isMobile", function(value, element) { 
      var length = value.length; 
      var mobile = /^1[34578]\d{9}$/;
      return this.optional(element) || (length == 11 && mobile.test(value)); 
    }, "请正确填写您的手机号码");

    //名称格式验证
    $.validator.addMethod("isName", function(value, element) { 
        var length = value.length; 
        var name = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
        return this.optional(element) || (length > 3 && length < 21 && name.test(value)); 
      }, "名称中不能含有特殊字符");
    
  //姓名格式验证
    $.validator.addMethod("isname", function(value, element) { 
        var length = value.length; 
        var name = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
        return this.optional(element) || (length > 1 && length < 6  &&  name.test(value)); 
      }, "名称中不能含有特殊字符");
    
    //联系人姓名格式验证
    $.validator.addMethod("isAddname", function(value, element) { 
        var length = value.length; 
        var name = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
        return this.optional(element) || (length < 21 && name.test(value)); 
      }, "联系人姓名中不能含有特殊字符");
    
  //借记卡格式验证
    $.validator.addMethod("isdebitCardBank", function(value, element) { 
        var length = value.length; 
        var isdebitCardBank = /^\d{16,19}$/;
        return this.optional(element) || (isdebitCardBank.test(value)); 
      }, "借记卡号格式不正确");
    
  //信用卡格式验证
    $.validator.addMethod("iscreditCardBank", function(value, element) { 
        var length = value.length;
        var iscreditCardBank = /^\d{13,19}$/;
        return this.optional(element) || (iscreditCardBank.test(value)); 
      }, "信用卡号格式不正确");
    
    //身份证格式验证
    $.validator.addMethod("isIDCard", function(value, element) { 
        var length = value.length; 
        var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/; 
        var isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
        return this.optional(element) || (isIDCard2.test(value) || isIDCard1.test(value)); 
      }, "身份证账号格式不正确");

  //密码
    $.validator.addMethod("isPassword", function(value, element) { 
    	//console.log(element)
    	value = value.trim();
        var length = value.length; 
        //console.log(length)
        return this.optional(element) || (length > 5 && length < 11); 
      }, "密码不能为空");
    
  //密码02
    $.validator.addMethod("isPassword02", function(value, element) { 
    	//console.log(element)
        //console.log(length)
    	var test =  /^[\u4e00-\u9fa5]+$/;
        return this.optional(element) || !(test.test(value)); 
      }, "密码不能为中文字符");

    
  //联系人姓名格式验证
    $.validator.addMethod("isAddress", function(value, element) { 
        var length = value.length; 
        var name = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
        return this.optional(element) || (length < 41 && name.test(value)); 
      }, "联系人姓名中不能含有特殊字符");

$.validator.setDefaults({
    /*关闭键盘输入时的实时校验*/
    onkeyup: null,
    /*添加校验成功后的执行函数--修改提示内容，并为正确提示信息添加新的样式(默认是valid)*/
    success: function(label){
        /*label的默认正确样式为valid，需要通过validClass来重置，否则这里添加的其他样式不能被清除*/
        label.text('').addClass('valid');
    },
    /*重写校验元素获得焦点后的执行函数--增加[1.光标移入元素时的帮助提示,2.校验元素的高亮显示]两个功能点*/
    onfocusin: function( element ) {
        this.lastActive = element;    
        /*2.校验元素的高亮显示*/
        $(element).addClass('highlight');

        // Hide error label and remove error class on focus if enabled
        if ( this.settings.focusCleanup ) {
            if ( this.settings.unhighlight ) {
                this.settings.unhighlight.call( this, element, this.settings.errorClass, this.settings.validClass );
            }
            this.hideThese( this.errorsFor( element ) );
        }
    },

    /*重写校验元素焦点离开时的执行函数--移除[1.添加的帮助提示,2.校验元素的高亮显示]*/
    onfocusout: function( element ) {
        /*1.帮助提示信息移除*/
        $(element).parent().children(".tip").remove();
        /*2.校验元素高亮样式移除*/
        $(element).removeClass('highlight');
        /*3.替换下面注释的原始代码，任何时候光标离开元素都触发校验功能*/
        this.element( element );
    }
});
   