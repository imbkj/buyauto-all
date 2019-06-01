var InputsWrapper = $("#uploadDiv"); // Input boxes wrapper ID

var x = 0; // initlal text box count
var FieldCount = 1; // to keep track of text box added


(function($, w) {
	
	var checkedsId = '';
	var table = null;



	$(function() {
		
		table = tableDataLoad();

		
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
		
		$("#comTitle").blur(function(){
			validator.checkField();
		});
				
		$(".singleDatePicker").each(function(k, v){
			$(v).daterangepicker(daterangepicker_options);
			$(v).on('apply.daterangepicker', function(ev, picker) {
				$(v).parent().find("input[type=hidden]").val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
				$(v).find("span").html(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));

				// 计算项目期限
				if($(v).parent().find("input[type=hidden]").attr("name") == "onlineTime" || $(v).parent().find("input[type=hidden]").attr("name") == "paybackTime"){
					computeDay();
				}
		    });
			$(v).on('cancel.daterangepicker', function(ev, picker) {
				console.log("cancel event fired");
				$(v).find("span").html("&nbsp;&nbsp;请选择时间&nbsp;");
				$(v).parent().find("input[type=hidden]").val('');
			});
		});
		
		

	
		$("#btn_create_product").click(function() {
			removeList();
			$("#editwin").modal('show');
		});
	});

	w.tableDataLoad = function() {

		var status = {
				"0" : "可用",
				"1" : "禁用"
		};

	 table = $('#transfer_datatable').dataTable({
		bProcessing : true,
		paging : true,
		pageLength : 20,
		iDisplayLength : 20,
		bInfo : true, // 页脚信息
		bSort : false, // 排序功能
		bAutoWidth : true, // 自动宽度
		bStateSave : false, // 把分页信息保存cookie
		sPaginationType : "full_numbers",
		bPaginate : true, // 是否开启分页
		bLengthChange : false, // 改变每页显示数据数量
		searching : false, // 隐藏搜索
		serverSide : true,
		ajax : {
			url : g_requestContextPath+ '/h/l/configure/addressList',
			type : 'post',
			async : false,
			dataType : 'json',
			dataSrc : 'rows',
			data : function(aoData) {
				var param = {};
				param.page = aoData.start > 0 ? (aoData.start/ aoData.length + 1): 1;
				param.rows = 20; // aoData.length;
				var formData = $("#mem_search").serializeArray();
				formData.forEach(function(e) {
					if (e.value) {
						param[e.name] = e.value;
					}
				});

				return param;
			}

		},
		columns : [
				{	

					data : function(d) {
						return d.id;
					}
				},
				{
					data : function(d) {
						return d.scValue;
					}
				},

				
				{
					data : function(d) {
						return d.scRemark;
					}
				},
				{
					data : function(d) {
						return status[d.state];
					}
				},
				{
					data : function(d) {
						return new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");
					}
				},
				{
					data : function(d) {
						var s = '<div class="table-o-tooltip">';
						
						if(d.state== 0 ){
							s += '<a href="javascript:disable(\''
								+ d.id
								+ '\');" class="btn-link">禁用</a>';
						}		
						if(d.state== 1){
							s += '<a href="javascript:enable(\''
								+ d.id
								+ '\');" class="btn-link">启用</a>';

							s += '<a href="javascript:showUpdateAddress(\''
								+ d.id
								+ '\');" class="btn-link">修改</a>';
							s += '<a href="javascript:deletes(\''
								+ d.id
								+ '\');" class="btn-link">删除</a>';
						}	


						return s;
					}
				} ],

		language : {
			sProcessing : "数据加载中...",
			paginate : {// 分页的样式文本内容。
				previous : "上一页",
				next : "下一页",
				first : "第一页",
				last : "最后一页"
			},
			zeroRecords : "没有内容",
			info : "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，共_MAX_ 条 ",// 左下角的信息显示，大写的词为关键字。
			infoEmpty : "0条记录",// 筛选为空时左下角的显示。
			infoFiltered : ""// 筛选之后的左下角筛选提示(另一个是分页信息显示，在上面的info中已经设置，所以可以不显示)，
		}

	});
	
	return table;
}


		
		$("#btn_submit").click(function() {
			if(!checkSubmit()){
				return false;
			}

			$.ajax({
				url : g_requestContextPath + "/h/l/configure/addAddress",
				type : 'post',
				dataType : 'json',
				data : {
					"id" : $('#id').val(),
					"scValue" : $('#scvalue').val(),
					"scRemark":$('#scremark').val()
					
				},
				success : function(data) {
					if(data=="0"){
						new PNotify({
							title : '提示消息',
							text : '提车地址信息保存成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else if(data=="2"){
						new PNotify({
							title : '提示消息',
							text : '提车地址信息重复',
							type : 'error',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '提车地址信息保存失败',
							type : 'fail',
							styling : 'bootstrap3'
						});
					}
					
					$("#editwin").modal('hide');
					table.fnDestroy();
					tableDataLoad();
				}
			});
			return false;
		});
		

function removeList(){
	$("#id").val("");
	$('#scvalue').val("");
	$('#scremark').val("");
}

w.showUpdateAddress= function(id){
		removeList();
		$.get(g_requestContextPath + "/h/l/configure/updataShowAddress", {
			"id" : id
		},function(data) {
		
			if(data){
				$("#id").val(data.id);
				$('#scvalue').val(data.scValue);
				$('#scremark').val(data.scRemark);

				$("#myUASModalLabel").html("提车地址修改");
				$("#editwin").modal('show');
			}

		});
}

w.disable=function(id){
		$.get(g_requestContextPath + "/h/l/configure/updataDisable", {
			"id" : id
		},function(data) {	
				if(data){
						new PNotify({
							title : '提示消息',
							text : '提车地址信息禁用成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '提车地址信息禁用失败',
							type : 'fail',
							styling : 'bootstrap3'
						});

					}

					table.fnDestroy();
					tableDataLoad();

		});
}

w.enable=function(id){
		$.get(g_requestContextPath + "/h/l/configure/updataEnable", {
			"id" : id
		},function(data) {	
				if(data){
						new PNotify({
							title : '提示消息',
							text : '提车地址信息启用成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '提车地址信息启用失败',
							type : 'fail',
							styling : 'bootstrap3'
						});
					}
					
					table.fnDestroy();
					tableDataLoad();


		});
}

w.deletes=function(id){
	$.confirm({
	    title: '确认删除该条记录吗？',
	    content: '',
	    buttons: {
        	confirm: {
	            text: '确认',
	            action: function(){
	            
	              	$.get(g_requestContextPath + "/h/l/configure/delete", {
						"id" : id
					},function(data) {	
							if(data){
									new PNotify({
										title : '提示消息',
										text : '提车地址信息删除成功',
										type : 'success',
										styling : 'bootstrap3'
									});
								}else{
									new PNotify({
										title : '提示消息',
										text : '提车地址信息删除失败',
										type : 'fail',
										styling : 'bootstrap3'
									});
								}
			
								table.fnDestroy();
								tableDataLoad();
			
					});
	            }
	        },
	        cancel: {
	           	text: '取消',
	            action: function(){
	            }
	        }
	    }
	});
	return false;
	              		
}



	
	w.setdid = function(sId) {
		checkedsId = sId;
	};
	w.mFileCallFunc = function(d){
		
	}
	w.viewPic = function(d) {
		var img=$("#"+d).val();
		if(img==""){
			return false;
		}
		window.open("http://"+img);
	}
	
	


})(jQuery, window)




