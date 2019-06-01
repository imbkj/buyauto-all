$(document).ready(function() {

	var uid = $("#uid").val();
/*
 * 添加、修改业务信息
 */
$("#updbut").bind("click", function() {
				var idv = $("#ywForm").find("#id").val();
				var url ;
				var msg ;
				if(idv == ""){
					 url = g_requestContextPath+"/h/l/ua/addMemberReport?uid="+uid;
					 msg="添加";
				}else{
					 //url = g_requestContextPath+"/h/l/ua/modifyMemberReport?uid="+uid;
					 url = g_requestContextPath+"/h/l/ua/addMemberReport?uid="+uid;
					 msg="修改";
				}
				
				$.ajax({
		            url: url,
		            type: 'post',
		            dataType: 'json',
		            async:false,
		            data: $("#ywForm").serialize(),
		            success: function (data) {
		            	if(data){
		    				alert(msg+"成功！");
		            	}else{
		            		alert(msg+"失败！");
		            	}
		            	location.href=g_requestContextPath + "/h/l/ua/toMemberReport?uid="+uid;
		           	},
		           	error : function() {
						alert("请求出错！");
						return false;
					}
		        });
		}
);


});