$(document).ready(function($) {
	$(window).resize();
	
});

$(function() {
	var header_ht=$(".tt_hd").height();
	windowheight = $(window).height();
	$("#minbg").height(windowheight-header_ht);
	
	//遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	});
	//点击弹框确定按钮
	$('.delBtn').click(function(){
		$('.boom').hide();
		$('.Errors').hide();
	})
	 
});

// 模块居中
$(function() {
	$(".main_login").css({
		postion : 'absolute',
		left : ($(window).width() - $('.main_login').outerWidth()) / 2,
		top : ($(window).height() - $(".main_login").outerHeight()-$(".tt_hd").height()) / 2,
	});
});

// 校验
$("#submit").bind('click',function() {
	
			$("#submit").val("提交").attr("disbled", "disabled");
			var username = $("#uername").val();
			var userphone = /^1\d{10}$/;
			// var userchina=/^[^\x00-\xff]$/ ;
			var useremail = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
			var password = $("#password").val();
		    var psw=/^[A-z0-9!@#$%^&*~]{6,10}$/;
			var index = (username + "").indexOf(":");
			if (username == "") {
				$("#tips").html("账户名不能为空").show();
				return false;
			}else if (!userphone.test(username) && !useremail.test(username)&& index == -1) {
				$("#tips").html("账户名格式不正确").show();
				return false;
			} else if (password == "") {
				$("#tips").html("密码不能为空").show();
				return false;
			}else if (!psw.test(password)) {
				$("#tips").html("密码不能少于6位").show();
				return false;
			}
			var url = g_requestContextPath + "/h/n/user/doLogin";
			$.ajax({
				url : url,
				type : 'POST',
				data : {
					'account' : username,
					'pwd' : password,
					'clientDevice' : 1
				},
				complete : function(xhr, status) {
					//console.log(xhr)
				},
				success : function(code) {
					//console.log(code);
					var data;
					if(code!=null && code != ""){
						data = code.code;
					}
					if (data == 200) {
						// 登录成功跳转主页
						window.location.href = g_requestContextPath + "/";
					}else if(data == 201){
						window.location.href = g_requestContextPath + "/h/l/user/leaderIndex";
					}else if(data == 202){
						window.location.href = g_requestContextPath + "/h/l/user/toResetPwd";
					}else if (data == 301) {
						$("#tips").html("账号不能为空").show();
						$("#submit").val('提交').removeAttr('disabled');
					} else if (data == 302) {
						$("#tips").html("密码不能为空").show();
						$("#submit").val('提交').removeAttr('disabled');
					} else if (data == 501) {
						$("#tips").html("账号或密码错误").show();
						$("#submit").val('提交').removeAttr('disabled');
					} else if (data == 502) {
						$("#tips").html("账号已销户").show();
						$("#submit").val('提交').removeAttr('disabled');
					} else if (data == 504) {
						$("#tips").html("请输入正确账号").show();
						$("#submit").val('提交').removeAttr('disabled');
					} else if (data == 505) {
						$("#tips").html("账号审核中").show();
						$("#submit").val('提交').removeAttr('disabled');
					} else if (data == 506) {
						
						$("#tips").html("账号审核未通过,<span>查看原因</span>").show();
						$('.Errors span').html(code.info);
						$("#submit").val('提交').removeAttr('disabled');
						//点击查看原因
						$('#tips span').click(function(){
							$('.boom').show();
							$('.Errors').show();
						}) 
					} else if (data == 507) {
						$("#tips").html("账号已冻结").show();
						$("#submit").val('提交').removeAttr('disabled');
					} else if (data == 508) {
						$("#tips").html("账号对应经销商已冻结").show();
						$("#submit").val('提交').removeAttr('disabled');
					}
				},
				error : function(data) {
					//console.log(data);
					console.log("非法参数");
				}

			});
			return false;

		})
