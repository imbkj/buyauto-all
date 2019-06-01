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
	            		url: g_requestContextPath+'/h/l/configure/upgradeDowngradeConfigList',
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
						{data: function(d){return d.scRemark;}},
						{data: function(d){
							if(eval('(' + d.scValue + ')').category==1 || eval('(' + d.scValue + ')').category==3){
								return eval('(' + d.scValue + ')').totalSales;
							}else{
								return "-";
							}
						}},
						{data: function(d){
							if(eval('(' + d.scValue + ')').category==1|| eval('(' + d.scValue + ')').category==3){
								return eval('(' + d.scValue + ')').monthlySales;
							}else{
								return "-";
							}
						}},
						{data: function(d){	if(eval('(' + d.scValue + ')').category==2|| eval('(' + d.scValue + ')').category==4){
								return eval('(' + d.scValue + ')').month;
							}else{
								return "-";
							};}},
						{data: function(d){return new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");}},
						{data: function(d){
							/*用户状态*/
							var statusStr = d.state;
							var s = "";
							s += '<div class="table-o-tooltip">';
							s += '<a href="javascript:modify(\''+d.id+'\');" class="btn-link">修改</a>';
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
	        	
	    w.modify=function(id){
		eliminate();
		//查询当前已经有降级配置
		$.get(g_requestContextPath + "/h/l/configure/queryUpgradeConfig", {
			"id" : id
		},function(data) {	
			if(data !=""){
				var oldRule =eval('(' + data.scValue + ')');
				$("#upgradeId").val(data.id);
				$("#month").val(oldRule.month);
				$("#totalSales").val(oldRule.totalSales);
				$("#monthlySales").val(oldRule.monthlySales);
				$("#totalAmount").val(oldRule.totalAmount);
				$("#monthlyAmount").val(oldRule.monthlyAmount);
				$("#distinguish").text(data.scRemark)
				$("#category").val(oldRule.category);
			}
		});
		$("#ruleConfig").modal('show');
	} 	

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
         			   table.fnDestroy();
         			   tableDataLoad();
					}	
		});
	})


	})	    
})(jQuery, window)