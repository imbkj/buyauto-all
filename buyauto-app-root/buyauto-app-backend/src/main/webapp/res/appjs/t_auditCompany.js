(function($, w){
	/*加载会员中心的数据 */
	$(function(){
		//经销商状态
		var status = {
				0:"待审核",1:"可用",2:"审核未通过",3:"冻结"
		}
		/*初始化条件查询时间框*/
		var tp = new timePlugins("crstartTime","crendTime","reportrange1");
		tp.init();
	    var showImgUrl = "";
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
	            		url: g_requestContextPath+'/h/l/audit/auditCommpanyList',
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
	            	    {data: function(d){return d.id;}},
						{data: function(d){return d.name;}},
						{data: function(d){return d.phone;}},
						{data: function(d){return d.email;}},
						{data: function(d){return d.companyName;}},
						{data: function(d){return d.account;}},
						{data: function(d){return status[d.status];}},
						{data: function(d){return new Date(d.createDate).format("yyyy-MM-dd hh:mm:ss");}},
						{data: function(d){
							var updateDate = d.updateDate;
							if(updateDate!=null &&　updateDate!=""){
								return new Date(d.updateDate).format("yyyy-MM-dd hh:mm:ss");
							}
							return "";
						}},
						{data: function(d){
							var statusStr = d.status;
							var s = "";
							s += '<div class="table-o-tooltip">';
							s += '<a href="javascript:showUser(\''+d.id+'\');" class="btn-link">查看</a>';
							s += '</div>';
							if(statusStr == 0){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:auditor(\''+d.id+'\');" class="btn-link">审核</a>';
								s += '</div>';
							}
							if(statusStr == 1){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:frozen(\''+d.id+'\');" class="btn-link">冻结</a>';
								s += '</div>';
							}
							if(statusStr == 2){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:showInfo(\''+d.id+'\');" class="btn-link">查看原因</a>';
								s += '</div>';
							}
							if(statusStr == 3){
								s += '<div class="table-o-tooltip">';
								s += '<a href="javascript:thaw(\''+d.id+'\');" class="btn-link">启用</a>';
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
	        	$.get(g_requestContextPath + "/h/l/audit/getUserById", {
	                "id": id
	            }, function(data) {
	            	showUserInfo(data);
	            });
	        };
	        w.showUserInfo = function(data){
	        	$("#contacts").val(data.name);
            	$("#companyPhone").val(data.phone);
            	$("#companyEmail").val(data.email);
            	$("#companyName").val(data.companyName);
            	$("#account").val(data.account);
            	$("#officeSpace").val(data.address);
            	var file1 = data.file1;
            	var imgStr = "";
            	if(file1!="" && null!=file1){
            		//imgStr+="<div><a target='_blank' href='"+showImgUrl+file1+"'><img title='点击查看凭证原图'  width='650' height='400' src='"+showImgUrl+file1+"' /></a></div>";
            			
					imgStr+="<a class='control-label col-md-10 col-sm-8 col-xs-8' href='"+showImgUrl+file1+"' download='"+showImgUrl+file1+"'><span class='fl check' >"+file1+"</span><span class='fl'>(点击下载)</span></a>"			
            	}
            	var file2 = data.file2;
            	if(file2!="" && null!=file2){
            		//imgStr+="<div><a target='_blank' href='"+showImgUrl+file2+"'><img title='点击查看凭证原图' width='650' height='400' src='"+showImgUrl+file2+"' /></a></div>";
            		imgStr+="<a class='control-label col-md-10 col-sm-8 col-xs-8' href='"+showImgUrl+file2+"' download='"+showImgUrl+file2+"'><span class='fl check' >"+file2+"</span><span class='fl'>(点击下载)</span></a>"			
            	}
            	var file3 = data.file3;
            	if(file3!="" && null!=file3){
            		//imgStr+="<div><a target='_blank' href='"+showImgUrl+file3+"'><img title='点击查看凭证原图' width='650' height='400' src='"+showImgUrl+file3+"' /></a></div>";
            		imgStr+="<a class='control-label col-md-10 col-sm-8 col-xs-8' href='"+showImgUrl+file3+"' download='"+showImgUrl+file3+"'><span class='fl check' >"+file3+"</span><span class='fl'>(点击下载)</span></a>"			
            	}
                $("#showImg").html(imgStr);
                $(".info-modal-lg").modal('show');
	        }
	        //审核弹框
	        w.auditor =function(id){
	        	$("#btn_audit_submit").data("id",id);
	        	$("#auditReason").val("");//清空理由
	        	$(".auditor-modal-lg").modal('show');
	        }
	        //经销商注册审核保存按钮
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
	        		url:g_requestContextPath+"/h/l/audit/auditCompany",
	        		data:{
	        			"id":id,
	        			"result":state,
	        			"info":info,
	        			"type":"0"
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
	        
	        //冻结
	        w.frozen = function(id){
	        	$.confirm({
				    title: '提示',
				    content: '确认冻结该经销商?',
				    buttons: {
			        	confirm: {
				            text: '确认',
				            action: function(){
				            	frozenCommpany(id,3);
				            }
				        },
				        cancel: {
				           	text: '取消',
				            action: function(){}
				        }
				    }
				});
				return false;
	        };
	        //解冻
	        w.thaw = function(id){
	        	$.confirm({
				    title: '提示',
				    content: '确认解冻该经销商?',
				    buttons: {
			        	confirm: {
				            text: '确认',
				            action: function(){
				            	frozenCommpany(id,1);
				            }
				        },
				        cancel: {
				           	text: '取消',
				            action: function(){}
				        }
				    }
				});
				return false;
	        };
	        
	      //确认冻结
	        function frozenCommpany(id,status){
	        	$.ajax({
	        		url:g_requestContextPath+"/h/l/audit/frozenCommpany",
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
})(jQuery, window)