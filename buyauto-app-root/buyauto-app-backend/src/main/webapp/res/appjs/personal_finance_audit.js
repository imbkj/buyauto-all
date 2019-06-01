var bzj = 10;//保证金常量
var nll = 6.5;//年利率常量
var yll = 1.5;//月利率常量
(function($, w){
	/*加载会员中心的数据 */
	$(function(){
		//供应商状态
		var status = {
				0:"待审核",1:"审核通过",2:"审核未通过"
		}
		var termType = {
				1:"1年期",2:"2年期",3:"3年期"
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
	            		url: g_requestContextPath+'/h/l/audit/personalFinanceList',
			            type: 'post',
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
						{data: function(d){return d.userPhone;}},
						{data: function(d){return d.loanAmount.toFixed(2)+"元";}},
						{data: function(d){return termType[d.term];}},
						{data: function(d){return new Date(d.createDate).format("yyyy-MM-dd hh:mm:ss");}},
						{data: function(d){return status[d.status];}},
						{data: function(d){
							/*用户状态*/
							var statusStr = d.status;
							var s = "";
							s += '<div class="table-o-tooltip">';
							s += '<a href="javascript:showInfo(\''+ d.orderId +'\',\''+ d.name +'\',\''+ d.userPhone +'\',\''+ d.loanAmount.toFixed(2) +'\',\''+ d.term +'\',\''+ new Date(d.createDate).format("yyyy-MM-dd hh:mm:ss") +'\');" class="btn-link">查看</a>';
							s += '</div>';
							if(statusStr == 0){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:auditor(\''+d.id+'\');" class="btn-link">审核</a>';
								s += '</div>';
							}
							if(statusStr == 2){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:showAudiInfo(\''+d.id+'\');" class="btn-link">查看原因</a>';
								s += '</div>';
							}
							return s;
						}}
	            	],
	            	
	            	language: {
	            		sProcessing: "数据加载中...",
						paginate: {//分页的样式文本内容。
							previous: "上一页",
							next: "下一页",
							first: "第一页",
							last: "最后一页"
						},
						zeroRecords: "没有内容",
						info: "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，共_MAX_ 条 ",//左下角的信息显示，大写的词为关键字。
	                	infoEmpty: "0条记录",//筛选为空时左下角的显示。
	                	infoFiltered: ""//筛选之后的左下角筛选提示(另一个是分页信息显示，在上面的info中已经设置，所以可以不显示)，
	            	}
	            	
	            });
	      	
	        }
	        // 初始化数据
	        tableDataLoad();
	        
	        //查看原因
	        w.showAudiInfo = function(id){
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
	        w.showInfo = function(id,name,userPhone,loanAmount,term,createDate){
	        	$("#contacts").val(name);
            	$("#mobile").val(userPhone);
            	$("#amount").val(loanAmount+"元");
            	$("#term").val(term + "年期");
            	$("#appltime").val(createDate);
            	$.get(g_requestContextPath + "/h/l/audit/getCarFinance", {
	                "id": id,
	                "loanAmount": loanAmount,
	                "term": term
	            }, function(data) {
	                $("#MonPay").val(data.MonPay.toFixed(2)+"元");
	                $("#otherPay").val(data.otherPay.toFixed(2)+"元");
	                if(data.outRange){
	                	$("#bzjPay").val(data.bzjPay.toFixed(2)+"元");
	                	$("#sxfPay").val(data.sxfPay.toFixed(2)+"元");
	                	$("#firstMon").hide();
	                	$("#bzj").show();
	                	$("#sxf").show();
	                }else{
	                	$("#bzj").hide();
	                	$("#sxf").hide();
	                	$("#firstMon").show();
	                	$("#firstMonPay").val(data.firstMonPay.toFixed(2)+"元");
	                }
	                $(".info-modal-lg").modal('show');
	            });
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
	        		url:g_requestContextPath+"/h/l/audit/auditPersonalFinance",
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
	
	
	
})(jQuery, window)