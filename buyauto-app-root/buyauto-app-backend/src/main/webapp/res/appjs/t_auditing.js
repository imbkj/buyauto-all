(function($, w){
	/*加载会员中心的数据 */
	$(function(){
		//审核类型
		var auditType = {
				0:"经销商注册",1:"订单审核",2:"订单取消审核",3:"供应商注册",4:"个人用户审核",5:"佣金比例审核",6:"产品审核",7:"供应商金融方案"
		}
		//审核是否通过
		var resultType = {
				0:"通过",
				1:"未通过",
				"0":"通过",
				"1":"未通过"
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
	            		url: g_requestContextPath+'/h/l/audit/getAuditList',
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
	            	    {data: function(d){return d.matchId;}},
						{data: function(d){return auditType[d.type];}},
						{data: function(d){ 
							var datec = d.createDate; 
							if(datec!=""&&datec!=null){
								return new Date(datec).format("yyyy-MM-dd hh:mm:ss");
							}
							return "";
						}},
						{data: function(d){
							var res = d.result;
							return resultType[res];
						}},
						{data: function(d){return d.operName;}},
						{data: function(d){
							var s = '<div class="table-o-tooltip">';
							s += '<a href="javascript:showInfo(\''+d.id+'\');" class="btn-link">查看原因</a>';
							s += '</div>';
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
	        //通过
	        w.showInfo = function(id){
		    	   $.get(g_requestContextPath + "/h/l/audit/getPassInfo", {
		                "id": id
		            }, function(data) {
		            	var info = data;
		                if("" == info){
		                	info = "无审核意见";
		                }
		                $("#audit_info").val(info);
		                $(".uas-modal-lg").modal('show');
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