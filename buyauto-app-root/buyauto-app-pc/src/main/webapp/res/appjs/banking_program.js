var bzj = 10;// 保证金常量
var nll = 6.5;// 年利率常量
var yll = 1.5;// 月利率常量
var jump = false;
$(document)
		.ready(
				function() {

					$(".rt_menu li:first-child").click(
							function() {
								$(".captital").hide();
								$(this).addClass("currt").siblings()
										.removeClass("currt");
								$(".banking_info").show().siblings("captital")
										.hide();
							});
					$(".rt_menu li:last-child").click(
							function() {
								$(".banking_info").hide();
								$(this).addClass("currt").siblings()
										.removeClass("currt");
								$(".captital").show().siblings(".banking_info")
										.hide();
							});
					// 遮罩层
					$('.boom').css({
						'width' : $(window).width(),
						'height' : $(window).height()
					})

					// 点击确定(报错提示)
					$('.delBtn').click(
							function() {
								if (jump) {
									window.location.href = g_requestContextPath
											+ "/h/l/supplier/myOrder";
								}
								$('.Errors').hide();
								$('.boom').hide();
								$("#btn_levelUp").removeAttr("disabled");
							})
					/* 表单验证 */
					$("#bank_form").validate({
						rules : {
							money : {
								required : true,
								maxlength : 10
							}
						},
						messages : {
							money : {
								required : "请您填写金额",
								maxlength : "长度不能大于10位"
							}
						},
						errorClass : "error",
						validClass : "valid",
						errorElement : "span",
						submitHandler : function(form) {
							dialog(1); // 确认提示
							return false;

						}
					});

					/* 表单验证 */
					$("#captital_form").validate({
						rules : {
							amount : {
								required : true,
								maxlength : 10
							}
						},
						messages : {
							amount : {
								required : "请您填写金额",
								maxlength : "长度不能大于10位"
							}
						},
						errorClass : "error",
						validClass : "valid",
						errorElement : "span",
						submitHandler : function(form) {
							dialog(2); // 确认提示
							return false;

						}
					});

					$("#money")
							.keyup(
									function() {
										this.value = this.value.replace(
												/[^0-9-]+/, '');
										if ($(this).val() != "") {
											$("#bzj_bank")
													.html(
															($(this).val()
																	* bzj * 0.01)
																	.toFixed(2)
																	+ "元");
											$("#sxf_bank")
													.html(
															($(this).val()
																	* $(
																			"input[name='userType']:checked")
																			.val() * 0.01)
																	.toFixed(2)
																	+ "元");
											$("#myyh_bank")
													.html(
															((parseFloat($(this)
																	.val()) + parseFloat($(
																	this).val()
																	* nll
																	* 0.01
																	* $("#qx")
																			.val()
																	/ 12)) / $(
																	"#qx")
																	.val())
																	.toFixed(2)
																	+ "元");
										} else {
											$("#bzj_bank").html("0.00元");
											$("#sxf_bank").html("0.00元");
											$("#myyh_bank").html("0.00元");
										}
									});
					$("input[name='userType']")
							.change(
									function() {
										if ($("#money").val() != "") {
											$("#sxf_bank")
													.html(
															($("#money").val()
																	* $(
																			"input[name='userType']:checked")
																			.val() * 0.01)
																	.toFixed(2)
																	+ "元");
										}
									});
					$("#qx")
							.change(
									function() {
										if ($("#money").val() != "") {
											$("#myyh_bank")
													.html(
															((parseFloat($(
																	"#money")
																	.val()) + parseFloat($(
																	"#money")
																	.val()
																	* nll
																	* 0.01
																	* $("#qx")
																			.val()
																	/ 12)) / $(
																	"#qx")
																	.val())
																	.toFixed(2)
																	+ "元");
										}
									});
					$("#zq")
							.change(
									function() {
										if ($("#amount").val() != "") {
											if ($("#lx").val() == 0) {
												$("#myyh_captital")
														.html(
																((parseFloat($(
																		"#amount")
																		.val()) + parseFloat($(
																		"#amount")
																		.val()
																		* yll
																		* 0.01
																		* $(
																				"#zq")
																				.val())) / $(
																		"#zq")
																		.val())
																		.toFixed(2)
																		+ "元");
											}
										}
									});
					$("#lx")
							.change(
									function() {
										if ($("#amount").val() != "") {
											if ($("#lx").val() == 0) {
												$("#myyh_captital")
														.html(
																((parseFloat($(
																		"#amount")
																		.val()) + parseFloat($(
																		"#amount")
																		.val()
																		* yll
																		* 0.01
																		* $(
																				"#zq")
																				.val())) / $(
																		"#zq")
																		.val())
																		.toFixed(2)
																		+ "元");
											} else {
												$("#myyh_captital").html(
														($("#amount").val()
																* yll * 0.01)
																.toFixed(2)
																+ "元");
											}
										}
									})
					$("#amount")
							.keyup(
									function() {
										this.value = this.value.replace(
												/[^0-9-]+/, '');
										if ($(this).value != "") {
											$("#bzj_captital")
													.html(
															($(this).val()
																	* bzj * 0.01)
																	.toFixed(2)
																	+ "元");
											$("#sxf_captital").html(
													($(this).val() * 0.01)
															.toFixed(2)
															+ "元");
											if ($("#lx").val() == 0) {
												$("#myyh_captital")
														.html(
																((parseFloat($(
																		this)
																		.val()) + parseFloat($(
																		this)
																		.val()
																		* yll
																		* 0.01
																		* $(
																				"#zq")
																				.val())) / $(
																		"#zq")
																		.val())
																		.toFixed(2)
																		+ "元");
											} else {
												$("#myyh_captital")
														.html(
																($(this).val()
																		* yll * 0.01)
																		.toFixed(2)
																		+ "元");
											}
										} else {
											$("#bzj_captital").html("0.00元");
											$("#sxf_captital").html("0.00元");
											$("#myyh_captital").html("0.00元");
										}
									});
				});

/**
 * 根据对应的验证码提示信息
 */
function alertCode(code) {
	if (code == "")
		return "";
	if (code == "200") {
		jump = true;
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您已成功提交融资申请，请耐心等待审核。");
	} else if (code == "500") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您目前级别未满足升级要求");
	}
}

/* 确认提示 */
function dialog(flag) {
	$('.boom').show();
	$('.errors').show();
	if (flag == 1) {
		$('.errors').find("span").html("您确定要提交库存融资方案吗？");
		$("#sure2").hide();
		$("#sure1").show();
	} else {
		$('.errors').find("span").html("您确定要提交配资方案吗？");
		$("#sure1").hide();
		$("#sure2").show();
	}

}
$("#sure1").click(function() {
	$("#bank_submit").attr('disabled', "true");
	$('.errors').hide();
	$.ajax({
		url : g_requestContextPath + "/h/l/supplier/supplierFinance",
		dataType : "json",
		type : "post",
		async : true,
		data : {
			amount : $("#money").val(),
			port : $("input[name='userType']:checked").val(),
			term : $("#qx").val(),
			type : 1
		},
		success : function(data) {
			alertCode(data);// 根据code提示
			$("#bank_submit").removeAttr("disabled");

		},
		error : function() {
			$("#bank_submit").removeAttr("disabled");
		}
	});
});
$("#sure2").click(function() {
	$("#captital_submit").attr('disabled', "true");
	$('.errors').hide();
	$.ajax({
		url : g_requestContextPath + "/h/l/supplier/supplierFinance",
		dataType : "json",
		type : "post",
		async : true,
		data : {
			amount : $("#amount").val(),
			repayment : $("#lx").val(),
			term : $("#zq").val(),
			type : 2
		},
		success : function(data) {
			alertCode(data);// 根据code提示
			$("#captital_submit").removeAttr("disabled");

		},
		error : function() {
			$("#captital_submit").removeAttr("disabled");
		}
	});

});

$("#cancel").click(function() {
	$('.errors').hide();
	$('.boom').hide();
})
