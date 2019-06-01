(function($, w){
	/*加载会员中心的数据 */
	$(function(){
		//供应商状态
		var type = {
				0:"被查询人",1:"直接下级",2:"间接下级",3:"直接上级",4:"间接上级"
		}
		/*初始化条件查询时间框*/
		var tp = new timePlugins("crstartTime","crendTime","reportrange1");
		tp.init();
		var table = null;
		bannerSubmitUrl = "create";
		var tableDataLoad = function(){
	    //获取数据
	    table =$('#audit_list').dataTable({
	            	bProcessing: true,
	            	paging: true,
	            	pageLength: 20,
	            	iDisplayLength: 20,
	            	bInfo: true, // 页脚信息
	            	bSort: false, // 排序功能
	            	bAutoWidth: true, // 自动宽度
	            	bStateSave: false, // 把分页信息保存cookie
	            	sPaginationType: "full_numbers",
	            	bPaginate: true, // 是否开启分页
	            	bLengthChange: false, //改变每页显示数据数量
	            	searching: false, // 隐藏搜索
	            	serverSide: true,
	            	//sDom: '<"top">rt<"bottom"fip><"clear">',
	            	ajax:{
	            		url: g_requestContextPath+'/h/l/oper/recommderList',
			            type: 'get',
			            async: false,
			            dataType: 'json',
			            dataSrc: 'rows',
			            data: function(aoData){
			            	var param = {};
			            	param.page = aoData.start>0 ? (aoData.start/aoData.length +1) : 1 ;
			            	param.rows = 20;//aoData.length;
			            	var formData = $("#mem_search").serializeArray();
							formData.forEach(function(e) {
								if (e.value) {
									param[e.name] = e.value;
								}
							});
			            	return param;
			            }
			            
	            	},
	            	aoColumnDefs: [ { "bSortable": false, "aTargets": [ 0 ] }],
	            	columns:[
	            	    //{data: function(d){return d.id;}},
						{data: function(d){return d.name;}},
						{data: function(d){return d.phone;}},
						{data: function(d){return d.cardID;}},
						{data: function(d){return type[d.type];}},
						{data: function(d){return d.debitCard;}},
						{data: function(d){return d.debitCardBank;}},
						{data: function(d){return d.creditCard;}},
						{data: function(d){return d.creditCardBank;}},
						{data: function(d){if(d.sumSellAmount == null){return "0.00万元";}else{return (d.sumSellAmount/10000).toFixed(2)+"万元";}}},
						{data: function(d){if(d.sumCommission == null){return "0.00元";}else{return d.sumCommission.toFixed(2)+"元";}}}
	            	],
	            	
	            	language: {
	            		sProcessing: "数据加载中...",
						paginate: {//分页的样式文本内容。
							previous: "上一页",
							next: "下一页",
							first: "第一页",
							last: "最后一页"
						},
						zeroRecords: "请输入查询条件",
						info: "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，共_MAX_ 条 ",//左下角的信息显示，大写的词为关键字。
	                	infoEmpty: "0条记录",//筛选为空时左下角的显示。
	                	infoFiltered: ""//筛选之后的左下角筛选提示(另一个是分页信息显示，在上面的info中已经设置，所以可以不显示)，
	            	}
	            	
	            });
	      	
	        }
	        // 初始化数据
	        tableDataLoad();
	        //获取图片前缀
	        $.get(g_requestContextPath + "/getImagePath",function(data){
	        	showImgUrl = data;
	        });
	        
	        //查看原因
	        w.showInfo = function(id){
		    	   $.get(g_requestContextPath + "/h/l/audit/getInfoByMid", {
		                "id": id
		            }, function(data) {
		            	var info = data;
		                if("" == info){
		                	info = "无审核意见";
		                }
		                $("#audit_info").val(info);
		                $(".uas-modal-lg").modal('show');
		            });
		    };
	        //查看详情
	        w.showUser = function(id){
	        	$.get(g_requestContextPath + "/h/l/audit/getPersonalById", {
	                "id": id
	            }, function(data) {
	            	showUserInfo(data);
	            });
	        };
	        w.showUserInfo = function(data){
	        	$("#contacts").val(data.name);
            	$("#phone").val(data.phone);
            	$("#cardID").val(data.cardID);
            	$("#sysLevel").val(level[data.sysLevel]);
            	$("#realLevel").val(level[data.realLevel]);
            	$("#debitCard").val(data.debitCard);
            	$("#debitCardBank").val(data.debitCardBank);
            	$("#creditCard").val(data.creditCard);
            	$("#creditCardBank").val(data.creditCardBank);
            	var file1 = data.cardFile;
            	var cardFile = "";
            	var debitCardFile = "";
            	var creditCardFile = "";
            	if(file1!="" && null!=file1){
            		cardFile+="<a class='control-label col-md-10 col-sm-8 col-xs-8' href='"+showImgUrl+file1+"' download='"+showImgUrl+file1+"'><span class='fl check' >"+file1+"</span><span class='fl'>(点击下载)</span></a>"	;		
            	}
            	var file2 = data.debitCardFile;
            	if(file2!="" && null!=file2){
            		debitCardFile+="<a class='control-label col-md-10 col-sm-8 col-xs-8' href='"+showImgUrl+file2+"' download='"+showImgUrl+file2+"'><span class='fl check' >"+file2+"</span><span class='fl'>(点击下载)</span></a>";			
            	}
            	var file3 = data.creditCardFile;
            	if(file3!="" && null!=file3){
            		creditCardFile+="<a class='control-label col-md-10 col-sm-8 col-xs-8' href='"+showImgUrl+file3+"' download='"+showImgUrl+file3+"'><span class='fl check' >"+file3+"</span><span class='fl'>(点击下载)</span></a>"	;		
            	}
                $("#cardFile").html(cardFile);
                $("#debitCardFile").html(debitCardFile);
                $("#creditCardFile").html(creditCardFile);
                $(".info-modal-lg").modal('show');
	        }
	        //审核弹框
	        w.auditor =function(id){
	        	$("#btn_audit_submit").data("id",id);
	        	$("#auditReason").val("");//清空理由
	        	$(".auditor-modal-lg").modal('show');
	        }
	        //个人级别审核保存按钮
	        $("#btn_audit_submit").click(function(){
	        	var state = $("input[name='auditchk']:checked").val();
	        	var info = $("#auditReason").val().trim();
	        	var id = $(this).data("id");
	        	if(state == "2" && info==""){
	        		new PNotify({
						title : '提示消息',
						text : '审核未通过须填写原因',
						type : 'error',
						styling : 'bootstrap3'
					});
	        		return false;
	        	};
	        	$.ajax({
	        		url:g_requestContextPath+"/h/l/audit/auditPersonal",
	        		data:{
	        			"id":id,
	        			"result":state,
	        			"info":info
	        		},
	        		type:"POST",
	        		success:function(data){
	        			if(data == "200"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '审核成功',
		    					type : 'success',
		    					styling : 'bootstrap3'
		    				});
	        				table.fnDestroy();
	        				tableDataLoad();
	        				$(".auditor-modal-lg").modal('hide');
	        			}else if(data =="501"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '审核失败',
		    					type : 'error',
		    					styling : 'bootstrap3'
		    				});
	        			}else if(data =="500"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '审核插入数据失败',
		    					type : 'error',
		    					styling : 'bootstrap3'
		    				});
	        			}
	        		}
	        	});
	        });
	        //邀请
	        w.levelUp = function(id){
	        	$.confirm({
				    title: '提示',
				    content: '确认发送邀请给该用户?',
				    buttons: {
			        	confirm: {
				            text: '确认',
				            action: function(){
				            	levelUpPersonal(id);
				            }
				        },
				        cancel: {
				           	text: '取消',
				            action: function(){}
				        }
				    }
				});
	        };
	        //冻结
	        w.frozen = function(id){
	        	$.confirm({
				    title: '提示',
				    content: '确认冻结该用户?',
				    buttons: {
			        	confirm: {
				            text: '确认',
				            action: function(){
				            	frozenPersonal(id,3);
				            }
				        },
				        cancel: {
				           	text: '取消',
				            action: function(){}
				        }
				    }
				});
	        };
	        //解冻
	        w.thaw = function(id){
	        	$.confirm({
				    title: '提示',
				    content: '确认解冻该用户?',
				    buttons: {
			        	confirm: {
				            text: '确认',
				            action: function(){
				            	frozenPersonal(id,1);
				            }
				        },
				        cancel: {
				           	text: '取消',
				            action: function(){}
				        }
				    }
				});
	        };
	        //确认冻结
	        function levelUpPersonal(id){
	        	$.ajax({
	        		url:g_requestContextPath+"/h/l/audit/levelUpPersonal",
	        		data:{"id":id},
	        		type:"post",
	        		success:function(data){
	        			if(data == "200"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '操作成功',
		    					type : 'success',
		    					styling : 'bootstrap3'
		    				});
	        				table.fnDestroy();
	        				tableDataLoad();
	        				return false;
	        			}
	        			new PNotify({
	    					title : '提示消息',
	    					text : '操作失败',
	    					type : 'error',
	    					styling : 'bootstrap3'
	    				});
	        		}
	        	});
	        }
	      //确认冻结
	        function frozenPersonal(id,status){
	        	$.ajax({
	        		url:g_requestContextPath+"/h/l/audit/frozenPersonal",
	        		data:{"id":id,"status":status},
	        		type:"post",
	        		success:function(data){
	        			if(data == "200"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '操作成功',
		    					type : 'success',
		    					styling : 'bootstrap3'
		    				});
	        				table.fnDestroy();
	        				tableDataLoad();
	        				return false;
	        			}
	        			new PNotify({
	    					title : '提示消息',
	    					text : '操作失败',
	    					type : 'error',
	    					styling : 'bootstrap3'
	    				});
	        		}
	        	});
	        }
	        
	        //条件查询
			$("#transfer_frmSearch").click(function() {
				table.fnDestroy();
				tableDataLoad();
				new PNotify({
					title : '提示消息',
					text : '查询成功',
					type : 'success',
					styling : 'bootstrap3'
				});
				return false;
			});
	});
	
w.upgrade =function(){
		
		$("#distinguish").text("升级配置")
		$("#category").val("1");
		//查询当前已经有的升级配置
		$.get(g_requestContextPath + "/h/l/configure/queryUpgradeConfig", {
			"category" : "1"
		},function(data) {	
			if(data !=""){
				var oldRule =eval('(' + data.scValue + ')');
				$("#upgradeId").val(data.id);
				$("#month").val(oldRule.month);
				$("#totalSales").val(oldRule.totalSales);
				$("#monthlySales").val(oldRule.monthlySales);
				$("#totalAmount").val(oldRule.totalAmount);
				$("#monthlyAmount").val(oldRule.monthlyAmount);
			}
		});
		$("#ruleConfig").modal('show');
		
	}
	
	w.downgrade=function(){
		eliminate();
		$("#distinguish").text("降级配置")
		$("#category").val("2");
		//查询当前已经有降级配置
		$.get(g_requestContextPath + "/h/l/configure/queryUpgradeConfig", {
			"category" : "2"
		},function(data) {	
			if(data !=""){
				var oldRule =eval('(' + data.scValue + ')');
				$("#upgradeId").val(data.id);
				$("#month").val(oldRule.month);
				$("#totalSales").val(oldRule.totalSales);
				$("#monthlySales").val(oldRule.monthlySales);
				$("#totalAmount").val(oldRule.totalAmount);
				$("#monthlyAmount").val(oldRule.monthlyAmount);
			}
		});
		$("#ruleConfig").modal('show');
	}
	
	$("#btn_submit").click(function(){
		//对配置规则进行验证
		if(!checkSubmit()){
			console.log("验证不通过");
			return false;
		}
		//category 1：升级 2：降级
		console.log($("#category").val(),"category")
		console.log($("#upgradeId").val(),"upgradeId")
		
		
		$.ajax({
				url : g_requestContextPath + "/h/l/configure/addUpgradeConfig",
				 type: 'post',
         		 dataType: 'json',
				 data: $("#form_sysConfig_upgrade").serialize(),
				 success: function (data) {
				  	 
         			   if(data){
         			     new PNotify({
         					   title: '提示消息',
         					   text: '保存成功',
         					   type: 'success',
         					   styling: 'bootstrap3'
         				   });
         				   $("#ruleConfig").modal('hide');
         			   }
         			   //table.fnDestroy();
         			   //tableDataLoad();
					}	
		});
	})
	
	//清空
	w.eliminate =function(){
		$("#category").val("");
		$("#upgradeId").val("");
		$("#month").val("");
		$("#totalSales").val("");
		$("#monthlySales").val("");
		$("#totalAmount").val("");
		$("#monthlyAmount").val("");
	}
	
	
})(jQuery, window)