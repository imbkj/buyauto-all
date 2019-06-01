var i = 0;
var j = 0;
var k = 1;
var time = 60;
var mobile = /^1[34578]\d{9}$/;

$(function(){
	var rephone=$("#recommender").val();
	if(rephone==""){
		$("#recommender-error").removeClass("void");
	} else{
		$("#recommender-error").addClass("void");
	}
	
	//查看协议
	$('#check').click(function(){
		$('.boom').show();
		$('.protocol').show();
	})
	//同意继续
	$('.btn01').click(function(){
		$('.boom').hide();
		$('.protocol').hide();
		$('.checkbox').prop('checked',true)
	})
	//不同意
	$('.order-deleat').click(function(){
		$('.boom').hide();
		$('.protocol').hide();
		$('.checkbox').prop('checked',false)
	})
	 
	//遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	})
	
	/*表单验证*/
	$("#register_form").validate({
		rules:{
			mobile: {
				  required: true,
	                isMobile: true
            },
            password: {
            	required: true,
            	isPassword:true,
            	minlength: 6
            },
            repassword:{
            	required: true,
            	minlength: 6,
            	equalTo: "#password"
            },
            math:{
            	required: true,
            	minlength: 6
            },
            code:{
            	required: true,
            	minlength: 6
            },
            recommender: {
                isMobile: true
            }
		},
		messages:{
            password: {
            	required: "请输入6—10位密码",
            	isPassword:'请输入6—10位密码',
            	minlength: "请输入6—10位密码"
            },
            repassword:{
            	required: "请再次输入密码",
            	minlength:"密码不得少于6位",
            	equalTo: "两次输入密码不一致"
            },
            mobile: {
                required: "请输入正确的手机号码",
                isMobile: "请输入有效的手机号码"
            },
            math:{
            	required: "请输入图片上的数字",
            	minlength: "请输入正确的数字"
            },
            code:{
            	required: "请输入您的手机验证码",
            	minlength: "请输入正确的手机验证码"
            },
            recommender:{
            	isMobile: "请输入有效的手机号码"
            }
		},
		errorClass: "error",
        validClass: "valid",
        errorElement: "span",
        submitHandler: function (form) {
          $("#register_submit").attr('disabled', "true"); //添加disabled属性         
            var parm = $("#register_form").serialize();
            if($('.checkbox').prop('checked') == false){
            	$('.boom').show();
            	$('.Errors').show();
                $('.Errors').find("span").html("请先阅读平行进口车网站注册协议并同意再进行注册");
                $("#register_submit").removeAttr("disabled");
                return;
            }
            $.ajax({ 
            	url:g_requestContextPath+"/h/n/user/doRegisterPersonal",
            	dataType:"json",
            	type:"post",
            	async:false,
            	data:parm,
            	success:function(data){
            		if(data !="200"){
                		$("#math").val("");
                		reImg();
                	}
            		alertCode(data);//根据code提示
            	},
            	error:function(){
            		$("#register_submit").removeAttr("disabled");
            	}
            });
        	return false;
        }
	})

    //点击获取验证码
    var isok = true;
    $('#getCode').click(function(){
    	var mobile = /^[1]\d{10}$/;
    	var phone = $('#mobile').val();
        if(isok){
        	//验证手机是否输入
            if(!mobile.test(phone)){
            	$('.boom').show();
                $('.Errors').show();
                $('.Errors').find("span").html("请先输入正确的手机号");
                return false;
            }
            //验证图片验证码是否输入
            var math = ($("#math").val())+"";
            if(math.trim() == "" || math.length != 6 ){
            	$('.boom').show();
            	$('.Errors').show();
        		$('.Errors').find("span").html("请先输入图片验证码");
            	return false;
            }
            $('#getCode').html(time + 's后重新获取').addClass('getCode');
            isok = false;
            var timer = setInterval(function(){
                time--;
                $('#getCode').html(time + 's后重新获取').addClass('getCode');
                
                if(time < 1){
                    clearInterval(timer);
                    isok = true;
                    $('#getCode').html('获取验证码').removeClass('getCode');
                    time = 60;
                }
            },1000);
            //请求手机验证码
            $.ajax({
            	url:g_requestContextPath+"/h/n/user/phoneCodeRegister",
            	data:{"phone":phone,"codeImg":math},
            	type:"post",
            	async:false,
            	success:function(data){
                	if(data && data=="502"){
                		$("#math").val("");
                		reImg();
                		clearInterval(timer);
                        isok = true;
                        $('#getCode').html('获取验证码').removeClass('getCode');
                        time = 60;
                        $('.boom').show();
                        $('.Errors').show();
                		$('.Errors').find("span").html("图片验证码已失效,请先刷新验证码");
                	}else if(data && data=="801"){
                		clearInterval(timer);
                        isok = true;
                        $('#getCode').html('获取验证码').removeClass('getCode');
                        time = 60;
                        $('.boom').show();
                        $('.Errors').show();
                		$('.Errors').find("span").html("图片验证码错误!");
                	}
                }
            });
        }
    });
});
var jump =false;
//点击确定(报错提示)
$('.delBtn').click(function(){
	if(jump){
		location.href=g_requestContextPath+"/";
	}
	$('.Errors').hide();
	$('.boom').hide();
	$("#register_submit").removeAttr("disabled");
})
/*刷新图片验证码*/
function reImg(){  
    var img = document.getElementById("imgPic");  
    img.src = g_requestContextPath+"/h/n/user/codeRegister?rnd=" + Math.random();  
}

/**
 * 根据对应的验证码提示信息 
 */
function alertCode(code){
	if(code == "")return "";
	if(code == "200"){
		jump = true;
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("恭喜您已成功提交注册信息!");
		var time = 3;	
	 	var timer = setInterval(function(){
	 		time--;
	 		$('.Errors').html(time);
	 		if(time<1){
	 			clearInterval(timer);
	 			location.href = g_requestContextPath+"/h/n/products/garageScr";
	 		}
	 	},1000)
	}else if(code == "301"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("经销商名称为空");
	}else if(code == "302"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("密码为空");
	}else if(code == "303"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("公司名称为空");
	}else if(code == "304"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("姓名为空");
	}else if(code == "305"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("手机号码为空");
	}else if(code == "306"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("图片验证码为空");
	}else if(code == "307"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("手机验证码为空");
	}else if(code == "309"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("证件凭证为空");
	}else if(code == "310"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("请先发送手机验证码");
	}else if(code == "401"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("证件凭证最大只能上传3张");
	}else if(code == "402"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("图片验证码错误");
	}else if(code == "403"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("手机验证码错误");
	}else if(code == "501"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("插入数据失败");
	}else if(code == "502"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("图片验证码已失效");
	}else if(code == "601"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("账户已存在");
	}else if(code == "602"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("邮箱已存在");
	}else if(code == "603"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("手机号码已存在");
	}else if(code == "604"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("公司名称已存在");
	}else if(code == "701"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("手机格式不正确");
	}else if(code == "702"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("邮箱格式不正确");
	}
	else if(code == "901"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("推荐人手机格式不正确");
	}
	else if(code == "902"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("推荐人名下名额已满，限额2人");
	}
	else if(code == "903"){
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("推荐人手机不存在");
	}
}




 