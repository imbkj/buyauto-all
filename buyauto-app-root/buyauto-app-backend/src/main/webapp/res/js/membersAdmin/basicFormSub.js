$(document).ready(
		function() {
			var uid = $("#memberForm").find("#id").val();
			/**
			 * 基本信息修改
			 */
			$("#memberSubut").bind(
					"click",
					function() {
						$.ajax({
							url : g_requestContextPath
									+ "/h/l/ua/updMermbersInfoAndCreateReward",
							type : 'post',
							dataType : 'json',
							data : $("#memberForm").serialize(),
							success : function(data) {
								if (data) {
									alert("修改成功！");
								} else {
									alert("修改失败！");
								}
								location.href=g_requestContextPath + "/h/l/ua/toMemberMainInfo?uid="+uid+"&strBut=jiben";
							},
							error : function() {
								alert("请求出错！");
								return false;
							}
						});
						return false;
					});

			/*
			 * 添加、修改股东信息
			 */
			$("#addGDbut").bind(
					"click",
					function() {
						if($("#gdForm").find("#uName").val() == ""){
							alert("股东姓名不能为空");
							return false;
						}
						if($("#gdForm").find("#uManage").val() == ""){
							alert("股东职务不能为空");
							return false;
						}
						if($("#gdForm").find("#age").val() == ""){
							alert("股东年龄不能为空");
							return false;
						}
						if($("#gdForm").find("#schooling").val() == ""){
							alert("股东学历不能为空");
							return false;
						}
						if($("#gdForm").find("#gdTime").val() == ""){
							alert("股东任职日期不能为空");
							return false;
						}
						if($("#gdForm").find("#uNote").val() == ""){
							alert("股东个人简介不能为空");
							return false;
						}
						var idv = $("#gdForm").find("#id").val();
						var url;
						var msg;
						if (idv == "") {
							url = g_requestContextPath
									+ "/h/l/ua/crateMemberManagement?uid="+uid;
							msg = "添加";
						} else {
							url = g_requestContextPath
									+ "/h/l/ua/modifyMemberManagement?uid="+uid;
							msg = "修改";
						}
						console.log(idv);
						$.ajax({
							url : url,
							type : 'post',
							dataType : 'json',
							data : $("#gdForm").serialize(),
							success : function(data) {
								if (data) {
									alert(msg + "成功！");
									$("#gdForm").find("#id").val("");
									$("#gdForm").find("#uName").val("");
									$("#gdForm").find("#uManage").val("");
									$("#gdForm").find("#age").val("");
									$("#gdForm").find("#schooling").val("");
									$("#gdForm").find("#uManage").val("");
									$("#gdForm").find("#uNote").val("");
									$("#gdForm").find("#photo").val("");
									$("#gdForm").find("#gdTime").val("");
									$("#addGDbut").html("添加");
								} else {
									alert(msg + "失败！");
								}
								location.href=g_requestContextPath + "/h/l/ua/toMemberMainInfo?uid="+uid+"&strBut=gudong";
								//
							},
							error : function() {
								alert("请求出错！");
								return false;
							}
						});
						return false;
					});

			/*
			 * 添加、修改高管信息
			 */
			$("#addGGbut").bind(
					"click",
					function() {
						if($("#ggForm").find("#uName").val() == ""){
							alert("高管姓名不能为空");
							return false;
						}
						if($("#ggForm").find("#uManage").val() == ""){
							alert("高管职务不能为空");
							return false;
						}
						if($("#ggForm").find("#age").val() == ""){
							alert("高管年龄不能为空");
							return false;
						}
						if($("#ggForm").find("#cyjl").val() == ""){
							alert("高管从业经历不能为空");
							return false;
						}
						if($("#ggForm").find("#zybj").val() == ""){
							alert("高管专业背景不能为空");
							return false;
						}
						var idv = $("#ggForm").find("#id").val();
						var url;
						var msg;
						if (idv == "") {
							url = g_requestContextPath
									+ "/h/l/ua/crateMemberManagement?uid="+uid;
							msg = "添加";
						} else {
							url = g_requestContextPath
									+ "/h/l/ua/modifyMemberManagement?uid="+uid;
							msg = "修改";
						}
						$.ajax({
							url : url,
							type : 'post',
							dataType : 'json',
							data : $("#ggForm").serialize(),
							success : function(data) {
								if (data) {
									alert(msg + "成功！");
									$("#ggForm").find("#id").val("");
									$("#ggForm").find("#uName").val("");
									$("#ggForm").find("#uManage").val("");
									$("#ggForm").find("#age").val("");
									$("#ggForm").find("#schooling").val("");
									$("#ggForm").find("#cyjl").val("");
									$("#ggForm").find("#zybj").val("");
									$("#addGGbut").html("添加");
								} else {
									alert(msg + "失败！");
								}
								location.href=g_requestContextPath + "/h/l/ua/toMemberMainInfo?uid="+uid+"&strBut=gaoguan";
							},
							error : function() {
								alert("请求出错！");
								return false;
							}
						});
						return false;
					});

			/*
			 * 添加、修改员工信息
			 */
			$("#empBut").bind("click", function() {
				$.ajax({
					url : g_requestContextPath + "/h/l/ua/modifyMemberEmp?uid="+uid,
					type : 'post',
					dataType : 'json',
					data : $("#empForm").serialize(),
					success : function(data) {
						if (data) {
							alert("操作成功！");
						} else {
							alert("操作失败！");
						}
						location.href=g_requestContextPath + "/h/l/ua/toMemberMainInfo?uid="+uid+"&strBut=yuangong";
					},
					error : function() {
						alert("请求出错！");
						return false;
					}
				});
				return false;
			});
			
		});