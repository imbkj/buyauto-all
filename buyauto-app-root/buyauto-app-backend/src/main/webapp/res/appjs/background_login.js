$(function(){
	//表单验证
	$("#submit_from").click(function(){
		if($("#userName").val() == ""){
			$("#errorMsg").html("请输入用户名");
			return ;
		}else if($("#userPwd").val() == ""){
			$("#errorMsg").html("请输入密码");
			return ;
		}else{
			document.getElementById('register_form').submit();
		}
	})
})