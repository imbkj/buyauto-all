var isok = '';
$(function(){
	//遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	})
	/*表单验证*/
	$("#register_form").validate({
		rules:{
            password: {
            	required: true,
            	isPassword:true,
            	minlength: 6
            },
            repassword:{
            	required: true,
            	minlength: 6,
            	equalTo: "#password"
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
            } 
		},
		errorClass: "error",
        validClass: "valid",
        errorElement: "span",
        submitHandler: function (form) {
          $("#register_submit").attr('disabled', "true"); //添加disabled属性 
          var parm = {'password':$('.pas01').val()};
            //获取图片信息
            $.ajax({ 
            	url:g_requestContextPath+"/h/l/user/resetPwd",
            	dataType:"json",
            	type:"post",
            	async:true,
            	data:parm,
            	success:function(data){
            		if(data == '200'){
            			isok = 1;
            			$('.boom').show();
            			$('.Errors').show().find('span').html('密码修改成功！');
            		}else if(data == '201'){
            			isok = 5;
            			$('.boom').show();
            			$('.Errors').show().find('span').html('密码修改成功！');
					}else if(data == '500'){
            			isok = 2;
            			$('.boom').show();
            			$('.Errors').show().find('span').html('非法登录！');
            		}else if(data == '501'){
            			isok = 3;
            			$('.boom').show();
            			$('.Errors').show().find('span').html('密码不能为空！');
            		}else if(data == '601'){
            			isok = 4;
            			$('.boom').show();
            			$('.Errors').show().find('span').html('只有子员工才可以修改密码！');
            		}
            	},
            	error:function(){
            		$("#register_submit").removeAttr("disabled");
            	}
            });
        	return false;
        }
	})
	
	//点击确定跳转页面
	$('.delBtn').click(function(){
		if(isok == '1'){
			window.location.href = g_requestContextPath + "/";
		}else if(isok == '2'){
			window.location.href = g_requestContextPath + "/h/l/user/login";
		}else if(isok == '3'){
			//未获取到密码
			$('.boom').hide();
			$('.Errors').hide();
			$("#register_submit").removeAttr("disabled");
			return;
		}else if(isok == '4'){
			window.location.href = g_requestContextPath + "/";
		}else if(isok == '5'){
			window.location.href = g_requestContextPath + "/h/l/user/leaderIndex";
		}
		
	})

})