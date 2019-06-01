
var btnflag = 0;
var rescode = 200;

$(document).ready(function($) {
	var window_wh = $(window).width();
	var window_ht = $(window).height();
	$(".biger_box").height(window_ht);
	$(".biger_box").width(window_wh);
   
	$("#phone").keyup(function () {
	     this.value = this.value.replace(/[^\d]/g, '');
	 })
	 
	  $("#register_form input").blur(function () {  
          var area = $(this);  
          var max = parseInt(area.attr("maxlength"), 10);
	  });
	 
	 //时间选择
    $('.date_picker').date_input();
    $("#hirdeate").attr("readonly","readonly"); 
	
	$("#btn_createCont").click(function() {
		btnflag = 1;
		$("#register_form").submit();
	});
	$("#submit_sure").click(function() {
		btnflag = 0;
	});
	$("#surebtn").on("click", function() {
		if (rescode == 200) {
			if (btnflag == 0) {
				location.href = g_requestContextPath + "/h/l/user/employee";
			} else if (btnflag == 1) {
				location.href = g_requestContextPath + "/h/l/user/add";
			}
		}
		$(".error_msg,.biger_box").hide();
   
	});
	
    
	/* 表单验证 */
	$("#register_form").validate({
		rules:{
			name: {
                required: true,
            },
            power: {
                required: true,
            },
            user:{
            	required: true,
            },
            phone:{
            	required: true,
            	isMobile:true
            },
            email:{
            	email:true
            },
            password:{
            	required: true,
            	isPassword:true,
            	isPassword02:true,
            	rangelength: [6, 10]
            }
		},
		messages:{
			name: {
                required: "请输入您的姓名",
            },
            power:{
            	required: "请选择岗位", 
            },
            user:{
            	required: "请请输入账号名",
            },
            phone:{
            	required: "请输入您的手机号码",
            	isMobile: "请输入有效的手机号码"
            },
            email:{
            	email: "请输入有效的电子邮件地址"
            },
             password:{
            	required: "请输入您的密码",
            	isPassword:"请输入6-10位密码",
            	isPassword02:'密码不能为中文字符',
            	rangelength: "请输入6-10位密码"
            }
		},
		errorClass : "error",
		validClass : "valid",
		errorElement : "span",
		submitHandler : function(label) {
			var parm = {
				"companyid" : $("#register_form").data("cid"),
				"name" : $("#name").val(),
				"position" : $("#power").val(),
				"companyname" : $("#companyName").data("cname"),
				"account" : $("#user").val(),
				"phone" : $("#phone").val(),
				"email" : $("#email").val(),
				"pwd" : $("#password").val(),
				"hirdeate" : $("#hirdeate").val(),
				"jobNumber" : $("#leadername").val(),
				"region" : $("#inform").val()
			};
			// console.log(parm);
			$.ajax({
				url : g_requestContextPath + "/h/l/user/addUser",
				type : "post",
				dataType : "json",
				data : parm,
				success : function(data) {
					rescode = 0;
					alertInfo(data);// 弹框
				}
			});
			return false;
		}
	});

});
function alertInfo(code) {
	if (!code)
		return false;
	if (code == "200") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("创建成功").show();
		rescode = 200;
	} else  if (code == "301") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("账户名为空").show();
	} else if (code == "302") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("密码为空").show();
	} else if (code == "303") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("经销商名称为空").show();
	} else if (code == "304") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("姓名为空").show();
	} else if (code == "305") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("手机号码为空").show();
	} else if (code == "306") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("岗位为空").show();
	} else if (code == "501") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("插入数据失败").show();
	} else if (code == "601") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("账户已存在").show();
	} else if (code == "602") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("邮箱已存在").show();
	} else if (code == "603") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("手机号码已存在").show();
	} else if (code == "701") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("手机格式不正确").show();
	} else if (code == "702") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("邮箱格式不正确").show();
	} else if (code == "801") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("一位经销商名下只能创建20个子员工").show();
	}else if(code=="604"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("工号已被占用").show();	
		$("#email_val").focus();
	}
}
