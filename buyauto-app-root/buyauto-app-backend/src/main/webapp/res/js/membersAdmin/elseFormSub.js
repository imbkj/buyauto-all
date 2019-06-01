$(document).ready(function() {

var uid = $("#uid").val();
/*
 * 添加、修改财务信息
 */
$("#addbut").bind("click", function() {
				var idv = $("#elseForm").find("#id").val();
				var url = g_requestContextPath+"/h/l/ua/modifyMemberOther?uid="+uid;
				var msg ;
				if(idv == ""){
					 msg="添加";
					 $("#addbut").html("添加");
				}else{
					 msg="修改";
					 $("#addbut").html("修改");
				}
				
				$.ajax({
		            url: url,
		            type: 'post',
		            dataType: 'json',
		            async:false,
		            data: $("#elseForm").serialize(),
		            success: function (data) {
		            	if(data){
		    				alert(msg+"成功！");
		    				/*if(idv == ""){
			   					 $("#addbut").html("添加");
			   				}else{
			   					 $("#addbut").html("修改");
			   				}*/
		    				location.href=g_requestContextPath + "/h/l/ua/toMemberOther?uid="+uid;
		            	}else{
		            		alert(msg+"失败！");
		            	}
		           	},
		           	error : function() {
						alert("请求出错！");
						return false;
					}
		        });
		}
);


});