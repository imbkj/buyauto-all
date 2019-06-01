(function($, w){
	/*加载会员中心的数据 */
	$(function(){
		//供应商状态
		var status = {
				0:"可用",1:"审核不通过",2:"待审核"
		}
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
	            		url: g_requestContextPath+'/h/l/configure/commissionConfList',
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
						{data: function(d){return d.scRemark;}},
						{data: function(d){return d.scValue + "%";}},
						{data: function(d){return status[d.state];}},
						{data: function(d){return new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");}},
						{data: function(d){
							/*用户状态*/
							var statusStr = d.state;
							var s = "";
							s += '<div class="table-o-tooltip">';
							s += '<a href="javascript:editUser(\''+d.id+'\',\''+d.scRemark+'\',\''+d.scValue+'\');" class="btn-link">修改</a>';
							s += '</div>';
							if(statusStr == 2){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:auditor(\''+d.id+'\');" class="btn-link">审核</a>';
								s += '</div>';
							}
							if(statusStr == 1){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:showInfo(\''+d.id+'\');" class="btn-link">查看原因</a>';
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
	        //修改
	        w.editUser = function(id,scRemark,scValue){
	        	$("#btn_edit_submit").data("id",id);
	        	$("#scRemark").val(scRemark);
            	$("#scValue").val(scValue);
                $(".info-modal-lg").modal('show');
	        }
	        //审核弹框
	        w.auditor =function(id){
	        	$("#btn_audit_submit").data("id",id);
	        	$("#auditReason").val("");//清空理由
	        	$(".auditor-modal-lg").modal('show');
	        }
	        //佣金审核保存按钮
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
	        		url:g_requestContextPath+"/h/l/audit/auditCommission",
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
	        //佣金修改保存按钮
	        $("#btn_edit_submit").click(function(){
	        	var scValue = $("#scValue").val().trim();
	        	if(scValue==""){
	        		new PNotify({
	        			title : '提示消息',
	        			text : '佣金比例不能为空',
	        			type : 'error',
	        			styling : 'bootstrap3'
	        		});
	        		return false;
	        	}
	        	var id = $(this).data("id");
	        	$.ajax({
	        		url:g_requestContextPath+"/h/l/configure/editCommission",
	        		data:{
	        			"id":id,
	        			"scValue":scValue
	        		},
	        		type:"POST",
	        		success:function(data){
	        			if(data == "200"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '修改成功',
		    					type : 'success',
		    					styling : 'bootstrap3'
		    				});
	        				table.fnDestroy();
	        				tableDataLoad();
	        				$(".info-modal-lg").modal('hide');
	        			}else if(data =="501"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '修改失败',
		    					type : 'error',
		    					styling : 'bootstrap3'
		    				});
	        			}else if(data =="500"){
	        				new PNotify({
		    					title : '提示消息',
		    					text : '修改插入数据失败',
		    					type : 'error',
		    					styling : 'bootstrap3'
		    				});
	        			}
	        		}
	        	});
	        });
	});
})(jQuery, window)