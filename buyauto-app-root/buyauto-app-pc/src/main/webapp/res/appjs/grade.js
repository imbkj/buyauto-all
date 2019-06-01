$(function() {
	// 遮罩层
	$('.boom').css({
		'width' : $(window).width(),
		'height' : $(window).height()
	});
	$(".personal_back").click(function(){
		dialog();
	});
	// 点击确定(报错提示)
	$('.delBtn').click(
			function() {
				window.location.reload();
			});
});

/**
 * 根据对应的验证码提示信息
 */

function alertCode(code) {
	if (code == "")
		return "";
	if (code == "0") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("操作成功，您目前等级为个人销售。");
	} else if (code == "301") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您目前级别未满足要求");
	}  
}

function dialog() {
	$('.boom').show();
	$('.errors').show();
	$('.errors').find("span").html("您确定要降级为个人销售吗？");
}

$("#sure").click(function() {
	$('.errors').hide();
	$('.boom').hide();
	$(".personal_back").attr('disabled', "true");
	$.ajax({
		url : g_requestContextPath + "/h/l/msg/levelDown",
		dataType : "json",
		type : "post",
		async : true,
		data : {
		},
		success : function(data) {
		        	  alertCode(data);// 根据code提示
	        		  $(".personal_back").removeAttr("disabled");
		}
	});
});
$("#cancel").click(function() {
	$('.errors').hide();
	$('.boom').hide();
});