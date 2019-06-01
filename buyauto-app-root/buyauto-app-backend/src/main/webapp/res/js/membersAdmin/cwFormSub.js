$(document).ready(function() {

var uid =  $("#uid").val();
/*
 * 添加、修改财务信息
 */
$("#addbut").bind("click", function() {
				var report = $("#cwForm").find("#address").val();
				var idv = $("#cwForm").find("#id").val();
				var url = g_requestContextPath+"/h/l/ua/modifyMemberYear?uid="+uid;
				var msg ;
				if($("#cwForm").find("#yName").val() == ""){
					alert("报告名称不能为空！");
					return false;
				}
				if(report == ""){
					alert("上传报告不能为空！");
					return false;
				}
				if(idv == ""){
					 msg="添加";
				}else{
					 msg="修改";
				}
				
				$.ajax({
		            url: url,
		            type: 'post',
		            dataType: 'json',
		            async:false,
		            data: $("#cwForm").serialize(),
		            success: function (data) {
		            	if(data){
		    				alert(msg+"成功！");
		    				$("#cwForm").find("#id").val("");
		    				$("#cwForm").find("#yName").val("");
		    				$("#cwForm").find("#address").val("");
		    				$("#cwForm").find("#upTime").val("");
		    				$("#addbut").html("添加");
		            	}else{
		            		alert(msg+"失败！");
		            	}
		            	location.href=g_requestContextPath + "/h/l/ua/toMemberYear?uid="+uid;
		           	},
		           	error : function() {
						alert("请求出错！");
						return false;
					}
		        });
		}
);


});