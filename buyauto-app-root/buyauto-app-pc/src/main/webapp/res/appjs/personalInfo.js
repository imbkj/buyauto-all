var upload1;
var upload2;
var upload3;
var jump = false;
$(function() {
	upload1 = new imageUploads($("#cardFile"), true);
	upload1.init("image");
	upload2 = new imageUploads($("#debitCardFile"), true);
	upload2.init("image");
	upload3 = new imageUploads($("#creditCardFile"), true);
	upload3.init("image");

	// 遮罩层
	$('.boom').css({
		'width' : $(window).width(),
		'height' : $(window).height()
	})

	// 点击确定(报错提示)
	$('.delBtn').click(function() {
		if (jump) {
			window.location.href = g_requestContextPath + "/h/l/msg/mainPage";
		}
		$('.Errors').hide();
		$('.boom').hide();
		$("#btn_levelUp").removeAttr("disabled");
	})

	/* 表单验证 */
	$("#register_form")
			.validate(
					{
						rules : {
							name : {
								required : true,
								isname : true
							},
							cardID : {
								required : true,
								isIDCard : true
							},
							debitCard : {
								required : true,
								isdebitCardBank : true
							},
							debitCardBank : {
								required : true,
							},
							creditCard : {
								required : true,
								iscreditCardBank : true
							},
							creditCardBank : {
								required : true,
							}
						},
						messages : {
							name : {
								required : "请填写您的姓名",
								isname : "姓名格式不正确"
							},
							cardID : {
								required : "请填写您的身份证号码",
								isIDCard : "身份证账号格式不正确"
							},
							debitCardBank : {
								required : "请填写您的借记卡行",

							},
							debitCard : {
								required : "请填写您的借记卡号码",
								isdebitCardBank : "借记卡号格式不正确"
							},
							creditCardBank : {
								required : "请填写您的信用卡行",
							},
							creditCard : {
								required : "请填写您的信用卡号码",
								iscreditCardBank : "信用卡号格式不正确"
							}
						},
						errorClass : "error",
						validClass : "valid",
						errorElement : "span",
						submitHandler : function(form) {
							var fileObj1 = upload1.getNewFile();
							var fileObj2 = upload2.getNewFile();
							var fileObj3 = upload3.getNewFile();
							$
									.ajax({
										url : g_requestContextPath
												+ "/h/l/msg/levelUp",
										dataType : "json",
										type : "post",
										async : true,
										data : {
											msgId : $("#msgId").val(),
											name : $("#name").val(),
											cardID : $("#cardID").val(),
											cardFile : JSON.parse(JSON
													.stringify(fileObj1))[0].name,
											cardBFile : JSON.parse(JSON
													.stringify(fileObj1))[1].name,
											debitCard : $("#debitCard").val(),
											debitCardBank : $("#debitCardBank")
													.val(),
											debitCardFile : JSON.parse(JSON
													.stringify(fileObj2))[0].name,
											creditCard : $("#creditCard").val(),
											creditCardBank : $(
													"#creditCardBank").val(),
											creditCardFile : JSON.parse(JSON
													.stringify(fileObj3))[0].name
										},
										success : function(data) {
											jump = true;
											alertCode(data);// 根据code提示
											$("#btn_levelUp").removeAttr(
													"disabled");
										},
										error : function() {
											$("#btn_levelUp").removeAttr(
													"disabled");
										}
									});
							return false;

						}
					});
});

function submitForm() {
	$("#btn_levelUp").attr('disabled', "true"); // 添加disabled属性
	var fileObj1 = upload1.getNewFile();
	var fileObj2 = upload2.getNewFile();
	var fileObj3 = upload3.getNewFile();
	var fileLen1 = fileObj1.length;
	var fileLen2 = fileObj2.length;
	var fileLen3 = fileObj3.length;
	if (fileLen1 <= 1) {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("请上传身份证正反面两张图片");
		$("#btn_levelUp").removeAttr("disabled");
		return false;
	} else if (fileLen1 > 2) {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("身份证正反面最多上传两张");
		$("#btn_levelUp").removeAttr("disabled");
		return false;
	}
	if (fileLen2 <= 0 || fileLen3 <= 0) {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("请上传凭证");
		$("#btn_levelUp").removeAttr("disabled");
		return false;
	} else if (fileLen2 > 1 || fileLen3 > 1) {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("凭证最多上传一张");
		$("#btn_levelUp").removeAttr("disabled");
		return false;
	}
	$("#register_form").submit();
	// 获取图片信息
	// var fileValue = JSON.stringify(upload1.getNewFile());
	// //console.log(fileValue);
	// parm += "&file="+fileValue;
	// //console.log(parm);

}

/**
 * 根据对应的验证码提示信息
 */
function alertCode(code) {
	if (code == "")
		return "";
	if (code == "200") {
		// jump = true;
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您已提交个人信息，请耐心等待审核。");
	} else if (code == "301") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您目前级别未满足升级要求");
	} else if (code == "401") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您之前已经提交过审核，请耐心等待审核。");
	} else if (code == "402") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您的审核已经通过，请不要重复提交。");
	}
}