$(document).ready(function() {
var uid = $("#uid").val();

/*
 * 添加、修改产品种类信息
 */
$("#zlbut").bind("click", function() {
				if($("#zlForm").find("#pName").val() == ""){
					alert("服务种类名称不能为空");
					return false;
				}
				if($("#zlForm").find("#upTime").val() == ""){
					alert("上市日期不能为空");
					return false;
				}
				if($("#zlForm").find("#pNote").val() == ""){
					alert("服务种类简介不能为空");
					return false;
				}
				var idv = $("#zlForm").find("#id").val();
				var url ;
				var msg ;
				if(idv == ""){
					 url = g_requestContextPath+"/h/l/ua/crateMemberProject?uid="+uid;
					 msg="添加";
				}else{
					 url = g_requestContextPath+"/h/l/ua/modifyMemberProject?uid="+uid;
					 msg="修改";
				}
				//alert(idv);
				$.ajax({
		            url: url,
		            type: 'post',
		            dataType: 'json',
		            data: $("#zlForm").serialize(),
		            success: function (data) {
		            	if(data){
		            		$("#zlForm").find("#id").val("");
		    				$("#zlForm").find("#pName").val("");
		    				$("#zlForm").find("input[name='pType']").removeAttr("checked");
		    				$("#zlForm").find("#upTime").val("");
		    				$("#zlForm").find("input[name='pStatus']").removeAttr("checked");
		    				$("#zlForm").find("#pNote").val("");
		    				$("#zlbut").html("添加");
		    				alert(msg+"成功！");
		            	}else{
		            		alert(msg+"失败！");
		            	}
		            	location.href=g_requestContextPath + "/h/l/ua/toMemberProjectInfo?uid="+uid+"&strBut=zl";
		           	},
		           	error : function() {
						alert("请求出错！");
						return false;
					}
		        });
		}
);

/*
 * 添加、修改产品服务信息
 */
$("#fwbut").bind("click", function() {
				
				if($("#fwForm").find("#pName").val() == ""){
					alert("产品服务名称不能为空");
					return false;
				}
				if($("#fwForm").find("#upJyjg").val() == ""){
					alert("请上传交易结构及业务环节图片");
					return false;
				}
				if($("#fwForm").find("#pTerm").val() == ""){
					alert("产品期限不能为空");
					return false;
				}
				if($("#fwForm").find("#mainInfo").val() == ""){
					alert("各环节涉及相关主题情况不能为空");
					return false;
				}
				if($("#fwForm").find("#wayChange").val() == ""){
					alert("平台收费方式/标准不能为空");
					return false;
				}
				if($("#fwForm").find("#wayEarnings").val() == ""){
					alert("投资人收益/风险情况不能为空");
					return false;
				}
				if($("#fwForm").find("#fengkongDetail").val() == ""){
					alert("主要风控措施不能为空");
					return false;
				}
				var idv = $("#fwForm").find("#id").val();
				var url ;
				var msg ;
				if(idv == ""){
					 url = g_requestContextPath+"/h/l/ua/crateMemberProject?uid="+uid;
					 msg="添加";
				}else{
					 url = g_requestContextPath+"/h/l/ua/modifyMemberProject?uid="+uid;
					 msg="修改";
				}
				$.ajax({
		            url: url,
		            type: 'post',
		            dataType: 'json',
		            data: $("#fwForm").serialize(),
		            success: function (data) {
		            	if(data){
		            		$("#fwForm").find("#id").val("");
		    				$("#fwForm").find("#pName").val("");
		    				$("#fwForm").find("#upJyjg").val("");
		    				$("#fwForm").find("#pTerm").val("");
		    				$("#fwForm").find("#mainInfo").val("");
		    				$("#fwForm").find("#wayChange").val("");
		    				$("#fwForm").find("#wayEarnings").val("");
		    				$("#fwForm").find("#fengkongDetail").val("");
		    				$("#fwbut").html("添加");
		    				alert(msg+"成功！");
		            	}else{
		            		alert(msg+"失败！");
		            	}
		            	location.href=g_requestContextPath + "/h/l/ua/toMemberProjectInfo?uid="+uid+"&strBut=fw";
		           	},
		           	error : function() {
						alert("请求出错！");
						return false;
					}
		        });
		}
);


});