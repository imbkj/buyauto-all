var jump = false;
$(document).ready(function() {
			var window_wh = $(window).width();
			var window_ht = $(window).height();
			$(".biger_box").height(window_ht);
			$(".biger_box").width(window_wh);   
			var userId = GetQueryString("id");
			//console.log(userId);
			//时间选择
			$('.date_picker').date_input();
			$("#hiredate").attr("readonly","readonly"); 
			
			$.ajax({
				  url:g_requestContextPath+"/h/l/user/getUserById",
				  type:"post",
				  data:{ "id":userId},
				  dataType:"json",
				  success:function(data){
					  //console.log(data)
					  $("input[type=text],input[type=password]").val("");//清空文本框
					  if(!data)return false;
					  var name=data.name;    //姓名
					  var power=data.position;   //岗位
				      var username=data.account;    //账号名
					  var companyName = data.companyName;//经销商名称
					  var phone=data.phone;    //手机号
					  var email=data.email     //邮箱
					  var hiredate=getLocalTime(data.hiredate);   //入职时间
					  var jobnumber=data.jobNumber;       //工号
					  var region=data.region            //所在区域
					  
					  $("#name").val(name);
					  $("#user").html(companyName+"&nbsp;&nbsp;:");
					  $("#username").text(username);
					  var position="";
					  if(power=='1'){
						  position = "销售";
					  }else if(power=='2'){
						  position = "财务";
					  }
					  $("#power").text(position);
					  $("#phone").text(phone);
					  $("#hiredate").val(hiredate);
					  $("#leadername").val(jobnumber);
					  $("#inform").val(region);
					  if(email!="" && email!=null){
						  $("#email").text(email);
					  }else{
						  $("#email_con").find(".con").remove();
						  var email_html = '<input type="text" name="email" id="email_val" value="" placeholder="请输入邮箱" maxlength="30"/>';
						  $("#email_con").append(email_html);
					  }
				  }
			})

			/*表单验证*/
				$("#register_form").validate({
					rules:{
						name: {
			                required: true,
			                minlength: 2
			            },
			            user:{
			            	required: true,
			            },
			            password:{
			            	isPassword:true,
			            	isPassword02:true,
			            	//required:true,
			            	rangelength: [6, 10]
			            }
					},
					messages:{
						name: {
			                required: "请输入您的姓名",
			                minlength: "最少2个中文文字、数字或字母"
			            },
			            user:{
			            	required: "请请输入账号名",
			            },
		                password:{
		                	isPassword:"请输入6-10位密码",
		                	isPassword02:'密码不能为中文字符',
		                	//required:"请输入6-10位密码",
			            	rangelength: "请输入6-10位密码"
			            }
					},
					errorClass: "error",
			        validClass: "valid",
			        errorElement: "span",
			        submitHandler:function(label){
			        	$("#submit_sure").attr("disbled", "disabled");
			            var param={
		            		"id":userId,
		    				"name":$("#name").val(),
		    				"password":$("#password").val().trim(),
		    				"hirdeate":$("#hiredate").val(),
		    				"leadername":$("#leadername").val().trim(),
		    				"inform":$("#inform").val().trim(),
		    				"email":$("#email_val").val()
			            };
			            
			            $.ajax({
						  	  type:"post",
						  	  async:false,
						  	  url:g_requestContextPath+"/h/l/user/editUser",
						  	  data:param,
						  	  dataType:"json",
						  	  success:function(data){
						  		 editInfo(data);
						  	  } 		  
						  });
			           return false;
			         }
			     });

				$(".surebtn").click(function(){
					$(".biger_box,.error_msg").hide();
					if(jump){
						location.href=g_requestContextPath+"/h/l/user/employee";//员工列表
					}
				});

          });

			
function editInfo(data){
	//console.log(data);
	jump = false;
    if(data=="200"){
    	jump = true;
    	$(".biger_box,.error_msg").show();
		$(".tips p").text("修改成功").show();
	}else if(data=="302"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("密码为空").show();
	}else if(data=="304"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("姓名为空").show();
	}else if(data=="306"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("岗位为空").show();
	}else if(data=="307"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("修改无主键").show();
	}else if(data=="501"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("插入数据失败").show();	
	}else if (data == "502") {
		$(".biger_box,.error_msg").show();
		$(".tips p").text("请关闭页面重试").show();
	}else if(data=="802"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("此员工不是当前经销商名下员工").show();	
	}else if(data=="702"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("邮箱格式不正确").show();	
		$("#email_val").focus();
	}else if(data=="602"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("邮箱已存在").show();	
		$("#email_val").focus();
	}else if(data=="604"){
		$(".biger_box,.error_msg").show();
		$(".tips p").text("工号已被占用").show();	
		$("#email_val").focus();
	}
    $("#submit_sure").removeAttr('disabled');
   return false;
 }


//获取url参数
function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return '';
}
//时间戳改成日期格式
function getLocalTime(nS){
	if(nS=="" ||　nS == null || (nS+"").trim()=="") return "";
	var date = new Date(parseInt(nS));
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	D = date.getDate() + ' ';
	return Y+M+D; 
} 

