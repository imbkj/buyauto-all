

	$(function() {
		var header_ht=$(".tt_hd").height();
		windowheight = $(window).height();
		$("#minbg").height(windowheight-header_ht);
	     //居中显示
	     $(".register_pwd").css({
	           postion:'absolute',
	           left:($(window).width()-$('.register_pwd').outerWidth())/2,
	           top:($(window).height()-$(".register_pwd").outerHeight()-$(".tt_hd").height())/2,
	     });
	     
	     $("#phone").blur(function(){
	        if($("#phone").val() != ""){
	        	$(".phonetips").hide();
	        	}
	        });
	     $("#picyzm").blur(function(){
	         if($("#picyzm").val() != ""){
	         	$(".picyzmtips").hide();
	         	}
	        });
	     $("#psw_new").blur(function(){
	    	  if($("#psw_new").val() !=""){
	    		   $(".newpsw_tips").hide();
	    	  }
	     });
	     $("#psw_two").blur(function(){
	    	  if($("#psw_two").val() !=""){
	    		   $(".newpsw_tips2").hide();
	    	  }
	     })
	     
	     $("#mathyzm,#picyzm,#phone").keyup(function () {
		     this.value = this.value.replace(/[^\d]/g, '');
		 })
		 
	      
	    
	});


	 

   //校验规则
    $("#button_next").bind("click",function(){
    	var  phone=$("#phone").val();
 	    var  phones= /^1[34578]\d{9}$/;
 	    var  picyzm=$("#picyzm").val();
 	    var  mathyzm=$("#mathyzm").val();

     	   if(phone ==""){
     	    	  $(".phonetips").html("手机号不能为空").show();
     	    	  return false;
     	    } else if(!phones.test(phone)){
     	    	  $(".phonetips").html("手机格式不正确").show();
     	    	  return false;
     	    } else if(picyzm ==""){
     	    	  $(".picyzmtips").html("图片验证码不能为空").show();
     	    	  return false; 
     	    } else if(mathyzm ==""){
     	    	  $(".mathyzmtips").html("手机验证码不能为空").show();
     	    	  return false;
     	    } 
     	   
     	   
		        $.ajax({
		     		    url:g_requestContextPath+"/h/n/user/doForget",
		     	        type:'post',
		     	        async:false,
		     	        data:{'phone':phone,'codeImg':picyzm,'codePhone':mathyzm},
		     	        success:function(data){
		     	        	  //console.log(data);
		     	        	  if(data=="301"){
		     	        		  $(".phonetips").html("手机号为空").show();
			     	        	  return false;
		     	        	  }else if(data=="302"){
		     	        		  $(".picyzmtips").html("图片验证码为空").show();
			     	        	  return false;
		     	        	  }else if(data=="303"){
		     	        		  $(".mathyzmtips").html("手机验证码为空").show();
			     	        	  return false;
		     	        	  }else if(data=="401"){
		   	        	    	  $(".picyzmtips").html("图片验证码错误").show();
			     	        	  return false;
		   	        	       }else if(data=="402"){
		   	        	    	  $(".mathyzmtips").html("手机验证码错误").show();
			     	        	  return false;
			   	        	   }else if(data=="501"){
		     	        		  $(".picyzmtips").html("请更新图片验证码").show();
			     	        	  return false;
		    	        	  }else if(data=="502"){
		    	        		  $(".mathyzmtips").html("请先发送手机验证码").show();
			     	        	  return false;
		   	        	       } else if(data=="701"){
	   	        	    	      $(".phonetips").html("手机格式不正确").show();
	    	     	        	  return false;
		       	        	    }else if(data=="802"){
	   	        	    	      $(".phonetips").html("只有管理员才能修改密码").show();
	    	     	        	  return false;
		       	        	    }else if(data=="803"){
		       	        	      $(".phonetips").html("未查到该用户").show();
	    	     	        	  return false;
		       	        	    }else if(data=="200"){
			       	        	     $(".psw_rage").hide();
			                         $(".psw_rege2").show();
			 	        	     } 
		     	        }
		     	   })
	     	  })
    
   
     
   //点击获取验证码
	var time = 60;
    var isok = true;
    $("#getcode").click(function(){
    	var mobile = /^1[34578]\d{9}$/;
    	var phone = $('#phone').val();
        if(isok){
        	//验证手机是否输入
            if(!mobile.test(phone)){
            	$('.phonetips').html("请输入正确的手机号").show();
            	return false;
            }
            //验证图片验证码是否输入
            var math = ($("#picyzm").val())+"";
            if($.trim(math) == "" || math.length != 6 ){
            	$(".picyzmtips").html('请输入六位图片验证码').show();
            	return false;
            }
           $('.getcode').html(time + 's后重新获取').addClass('dealer_rt');
            isok = false;
            var timer = setInterval(function(){
                time--;
                $('.getcode').html(time + 's后重新获取').addClass('dealer_rt');
                if(time < 1){
                    clearInterval(timer);
                    isok = true;
                    $('.getcode').html('获取验证码').removeClass('dealer_rt');
                    time = 60;
                }
            },1000);
            //请求手机验证码
            $.ajax({
            	url:g_requestContextPath+"/h/n/user/phoneCodeForget",
            	data:{
            		"phone":phone,
            		"codeImg":$('#picyzm').val()
            		},
            	type:"post",
            	async:true,
            	success:function(data){
            		console.log(data)
            		if(data==200){
                        $(".mathyzmtips").text("验证码已发送").show();
            		    //alert("验证码已发送");
            		}
                	if(data=="502"){
                		$("#mathyzm").val("");
                		imgclick();/*刷新图片验证码*/
                		clearInterval(timer);
                        isok = true;
                        $('.getcode').html('获取验证码').removeClass('dealer_rt');
                        time = 60;
                        $('.mathyzmtips').html("图片验证码已失效,请先刷新验证码").show();
                	}
                	if(data == '803'){
                		$("#mathyzm").val("");
                		imgclick();/*刷新图片验证码*/
                		clearInterval(timer);
                        isok = true;
                        time = 60;
                        $('.getcode').html('获取验证码').removeClass('dealer_rt');
                		$('.phonetips').html("未找到该用户").show();
                	}
                	if(data == '701'){
                		$("#mathyzm").val("");
                		imgclick();/*刷新图片验证码*/
                		clearInterval(timer);
                        isok = true;
                        $('.getcode').html('获取验证码').removeClass('dealer_rt');
                        time = 60;
                		$('.phonetips').html("手机号码格式不正确").show();
                	}
                	if(data == '402'){
                		$("#mathyzm").val("");
                		imgclick();/*刷新图片验证码*/
                		isok = true;
                		clearInterval(timer);
                		$('.getcode').html('获取验证码').removeClass('dealer_rt');
                        time = 60;
                		 $(".picyzmtips").html("图片验证码错误").show();
                	}
                	if(data == '503'){
                		$("#mathyzm").val("");
                		imgclick();/*刷新图片验证码*/
                		isok = true;
                		clearInterval(timer);
                		$('.getcode').html('获取验证码').removeClass('dealer_rt');
                        time = 60;
                        $('.phonetips').html("账户审核中").show();
                	}
                	if(data == '504'){
                		$("#mathyzm").val("");
                		imgclick();/*刷新图片验证码*/
                		isok = true;
                		clearInterval(timer);
                		$('.getcode').html('获取验证码').removeClass('dealer_rt');
                        time = 60;
                        $('.phonetips').html("账户已冻结").show();
                	}
                	if(data == '802'){
                		$("#mathyzm").val("");
                		imgclick();/*刷新图片验证码*/
                		clearInterval(timer);
                        isok = true;
                        time = 60;
                        $('.getcode').html('获取验证码').removeClass('dealer_rt');
                		$(".phonetips").html("只有管理员才能修改密码").show();
                	}
                }
            });
        }
    });



/*刷新图片验证码*/
function imgclick(){
	 $(".mathyzmtips").html("").hide();
    var img = document.getElementById("img_code");  
    img.src = g_requestContextPath+"/h/n/user/codeForget?end=" + Math.random();
    
}

     
     
     
   //next-step 
   $("#sure_btn").bind("click",function(){
		    var  phone=$("#phone").val();
		    var  phones= /^1\d{10}$/;
		    var  picyzm=$("#picyzm").val();
		    var  mathyzm=$("#mathyzm").val();
	   
     	    var  psw_new=$("#psw_new").val();
     	    var  psw_two=$("#psw_two").val();

     	    if(psw_new ==""){
	 	    	  $(".newpsw_tips").html("新密码不能为空").show();
	 	    	  return false;
     	    }else if(psw_new.length < 6){
   	    	  $(".newpsw_tips").html("新密码不能少于6位").show();
 	    	  return false;
 	        }else if(psw_two ==""){
     	    	  $(".newpsw_tips2").html("确认密码不能为空").show();
     	    	  return false;
     	    }else if(psw_two.length < 6){
	   	    	  $(".newpsw_tips2").html("确认密码不能少于6位").show();
	 	    	  return false;
 	         }else if(psw_new !==psw_two){
     	    	 $(".newpsw_tips2").html("确认密码不一致").show();
     	    	 return false;
     	     } 
     		     $.ajax({
     		    	  type:'post',
     		    	  async:false,
     		    	  url:g_requestContextPath+"/h/n/user/editPwd",
     		    	  data:{'phone':phone,'pwd':psw_new,'codePhone':mathyzm},
     		    	  success:function(data){
     		    		    if(data=="201"){
     		    			  $(".newpsw_tips").html("修改密码失败").show();
     		    			  return false;
     		    		   }else if(data=="301"){
     		    			   $(".phonetips").html("手机号为空").show();
     		    			   return false;
     		    		   }else if(data=="304"){
     		    			   $(".newpsw_tips").html("密码为空").show();
     		    			   return false;
     		    		   }else if(data=="502"){
     		    			  $(".mathyzmtips").html("请重新发送手机验证码").show();
     		    			  return false;
     		    		   }else if(data=="701"){
     		    			   $(".newpsw_tips").html("手机格式不正确").show();
     		    			   return false;
     		    		   }else if(data=="801"){
     		    			   $(".mathyzmtips").html("手机验证码验证失败").show();
     		    			   return false;
     		    		   }else if(data=="200"){
     		    			  jumpLogin();
     		    		   }
     		    	  } 
     		     });
     	   });
     	    
  //修改密码成功 跳转登录 
  function jumpLogin(){
	  var time_jump = 3;
	  $(".psw_rege2").hide();
	  $("#tips_jump").text("您已修改成功,"+time_jump+"秒后自动为您跳转到登录页。");
	  $(".psw_tips").show();
	  var timer_jump = setInterval(function(){
		  time_jump--;
		  //console.log(time_jump+"==");
		  $("#tips_jump").text("您已修改成功,"+time_jump+"秒后自动为您跳转到登录页。");
          if(time_jump < 1){
              clearInterval(timer_jump);
              location.href=g_requestContextPath+"/h/n/user/login";
          }
      },1000);
	  
  };

     