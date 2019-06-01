var InputsWrapper = $("#uploadDiv"); // Input boxes wrapper ID

var x = 0; // initlal text box count
var FieldCount = 1; // to keep track of text box added


(function($, w) {
	
	var checkedsId = '';
	var table = null;
	var upload1 =null;
   	var upload2 =null;
   	var upload3 = null;
   	var uploadFigure = null;
   	var uploadFigure0 = null;
   	var uploadFigure1 = null;
	var uploadFigure2 = null;
	var uploadFigure3 = null;
	var deletefileName=null;
	
		var selfSupport = {
				0:"非自营",
				1:"自营"
		}
		
		var productState ={
				0:"草稿",
				1:"待审核",
				2:"上架中",
				3:"已删除",
				4:"审核通过",
				5:"审核不通过"
		}


	$(function() {
	
	
		
		
		table = tableDataLoad();


		queryBrandList();
		

	   	upload1 = new imageUploads($("#indexImage"),false);//初始化file表单框//首页

	   	upload2 = new imageUploads($("#shrinkImage"),false);//初始化file表单框//缩略

	   	upload3 = new imageUploads($("#detailedImage"),true);//初始化file表单框//详情

		upload4 = new imageUploads($("#recommendImage"),false);//初始化多图上传//推荐

		
		
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
				

		
		$(document).on("click","#btn_create_config",function() {
			

			var configer ='<div class="item form-group"  configname="configname" >';
				configer += '<label class="control-label col-md-2 col-sm-2 col-xs-12" for="first-name"></label>';
				configer += '<div class="col-md-7 col-sm-7 col-xs-9" id="create_config">';
				configer += '<div class="row">';
				configer += '<div class="col-md-4 col-sm-4 col-xs-4">';
				configer += '<input type="text" class="form-control glName" value="" name="conf_name" maxLength="20" placeholder="名称" />';
				configer += '<input type="hidden" value="" name="conf_id"/>';
				configer += '</div>';
				configer += '<div class="col-md-4 col-sm-4 col-xs-4">';
				//configer += '<input type="text" class="form-control glGx" value="" name="conf_price" maxLength="10" placeholder="价格" />';
				configer += '<input type="text" class="form-control glGx" value="" name="conf_price" maxLength="10" placeholder="价格（元）"';
				configer += 'class="form-control col-md-3 col-xs-12" onkeyup="this.value=this.value.replace(/[^\\d]/g,\''+""+'\')" ';
				configer += 'onafterpaste="this.value=this.value.replace(/[^\\d]/g,\''+""+'\')"';
				configer += ' />';
				configer += '</div>';
				configer += '<div class="col-md-4 col-sm-4 col-xs-4">';
                configer += '<button type="button" onclick="deleteRows(this)" class="btn btn-danger"  ><i class="fa fa-check-square-o"></i>&nbsp;删 除</button>';
				configer += '</div>';
				configer += '</div>';
				configer += '</div>';
				configer += '</div>';
				$("#create_config").append(configer);
		});
			
	
		
		$("#btn_create_product").click(function() {
				removeList();
				upload1.init("image");
				upload2.init("image");
				upload3.init("image");
				upload4.init("image");

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
			url : g_requestContextPath+ '/h/l/mall/showProductsList',
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
					
						return d.productsId;
					}
				},
				{
					data : function(d) {
						return d.title;
					}
				},{
					data : function(d) {
						return d.selfSupport ==null ?"-":selfSupport[d.selfSupport];
					}
				},{
					data : function(d) {
						return d.status ==null ?"-":productState[d.status];
					}
				},{
					data : function(d) {
						return d.carPrice;
					}
				},
				/*{
					data : function(d) {
						return d.stock;
					}
				},
				{
					data : function(d) {
						return 0;
					}
				},*/
				{
					data : function(d) {
						return new Date(d.createDate).format("yyyy-MM-dd hh:mm:ss");
					}
				},
				{
					data : function(d) {
						var s = '<div class="table-o-tooltip">';
						
						if(d.status== 0 ){
							s += '<a href="javascript:showUpdate(\''
								+ d.productsId
								+ '\');" class="btn-link">修改</a>';
						}	
						//自营	
						if(d.selfSupport == 1){
							if(d.status== 1 || d.status== 5 ||d.status== 4){
								s += '<a href="javascript:showUpdate(\''
									+ d.productsId
									+ '\');" class="btn-link">修改</a>';
									
								s += '<a href="javascript:deletes(\''
									+ d.productsId
									+ '\');" class="btn-link">删除</a>';
							}	
							
							if(d.status== 4){
								s += '<a href="javascript:enable(\''
										+ d.productsId
										+ '\');" class="btn-link">上架</a>';
										
							}
	
							if(d.status== 2){
								
							
								s += '<a href="javascript:disable(\''
									+ d.productsId
									+ '\');" class="btn-link">下架</a>';
	
							
								
								
							}	
						}
						
						if(d.status== 1){
							s += '<a href="javascript:examine(\''
									+ d.productsId
									+ '\');" class="btn-link">审核</a>';

							s += '<a href="javascript:showUpdate(\''
									+ d.productsId
									+ '\');" class="btn-link">查看</a>';
						}

						if(d.status== 3){
							s += '<a href="javascript:restore(\''
								+ d.productsId
								+ '\');" class="btn-link">还原</a>';
						}	
						
						if(d.status== 5){
							s += '<a href="javascript:findReasons(\''
								+ d.productsId
								+ '\');" class="btn-link">查看原因</a>';
						}	

						if( d.status== 2){
							if(d.recommend == 0  ){
								s += '<a href="javascript:recommend(\''
								+ d.productsId
								+ '\');" class="btn-link">推荐</a>';
							}else{
								s += '<a href="javascript:cancelRecommend(\''
								+ d.productsId
								+ '\');" class="btn-link">取消推荐</a>';
							}
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
		
		//保存为草稿
		$("#draft_save").click(function(){

			$("#draft_save").val("0");
			$("#btn_submit").click();
		});

		
		$("#btn_submit").click(function() {
			
		

			//封面图
			if(upload1.getNewFile().length == 0){
				if(upload1.getOldFile() ==null){
					$("#indexImageH").val();
				}else if(upload1.getOldFile().length != 0){
					$("#indexImageH").val(upload1.getOldFile()[0].name);
				}else{
					$("#indexImageH").val();
				}	
			}else{
				$("#indexImageH").val(upload1.getNewFile()[0].name);
			}
			
			//封面图
			if(upload2.getNewFile().length == 0){
				if(upload2.getOldFile() ==null){
					$("#shrinkImageH").val();
				}else if(upload2.getOldFile().length != 0){
					$("#shrinkImageH").val(upload2.getOldFile()[0].name);
				}else{
					$("#shrinkImageH").val();
				}	
			}else{
				$("#shrinkImageH").val(upload2.getNewFile()[0].name);
			}


			//推荐图
			
			if(upload4.getNewFile().length == 0){
				if(upload4.getOldFile() ==null){
					$("#recommendImageH").val();
				}else if(upload4.getOldFile().length != 0){
					$("#recommendImageH").val(upload4.getOldFile()[0].name);
				}else{
					$("#recommendImageH").val();
				}	
			}else{
				$("#recommendImageH").val(upload4.getNewFile()[0].name);
			}

			//详情图

			$("#detailedImageH").val(JSON.stringify(upload3.getNewFile()));
			$("#detailedImageOld").val(JSON.stringify(upload3.getOldFile()));
			
			/***** 此处验证 *****/
		
			
			if($("#draft_save").val() !="0"){
				if(!checkSubmit()){
					return false;
				}
			}

			
			 var infoArray = new Array();

			$("div[configname=configname]").each(function(k,v){
					infoArray.push({id:$(v).find("[name=conf_id]").val(),name: $(v).find("[name=conf_name]").val() ,number: $(v).find("[name=conf_price]").val()});		
			});
			


			var comType =$("[name='carType']").parents("label.active").find("[name='carType']").val();
			var priceRange =$("[name='priceRange']").parents("label.active").find("[name='priceRange']").val();
			var position =$("[name='position']").parents("label.active").find("[name='position']").val();

	
			$.ajax({
				url : g_requestContextPath + "/h/l/mall/saveProductsList",
				type : 'post',
				dataType : 'json',
				data : {
					"draftSave":$("#draft_save").val(),//判断是否保存为草稿   空新建 有值为草稿
					"id" : $('#id').val(),
					"title" :$('#title').val(),//车辆名称
					"carType" :comType,//0轿车1SUV2MPV3皮卡4卡车5跑车
					"priceRange" :priceRange,//0:50以下,1:100以下,2:150以下,3:150以上
					"position" :position,//0港口现车1预定车2在途车
					"basicConfigure" :$('#basicConfigure').val(),//基础配置
					"mustConfigure" :$('#mustConfigure').val(),//必选配置
					"optionalConfigure" : JSON.stringify(infoArray),//可选配置(json)
					"carPrice" :$('#carPrice').val(),//裸车价格
					"mustConfigurePrice" :$('#mustConfigurePrice').val(),//必选件总价
					"stock" :$('#stock').val(),//库存
					"content" :ue.getContent(),//富文本
					"factoryDate":$("#startTimeFrame").val(),//出厂日期
					"country":$("#country").val(),//原产地
					"brandId":$("#brandId").val(),//品牌ID
					"indexImageH":$("#indexImageH").val(),//
					"shrinkImageH":$("#shrinkImageH").val(),
					"detailedImageH":$("#detailedImageH").val(),
					"recommendImageH":$("#recommendImageH").val(),
					"detailedImageOld":$("#detailedImageOld").val(),
					"deposit":$("#deposit").val(),//定金支付比例
					"carModel":$("#carModel").val(),//车辆型号
					"outColor":$("#outColor").val(),//车体外观颜色
					"inColor":$("#inColor").val(),//车身内部颜色
					"engine":$("#engine").val(),//发动机
					"gearbox":$("#gearbox").val(),//变速箱
					"chinaPrice":$("#chinaPrice").val()//国内指导价
					
				},
				success : function(data) {
					if(data){
						new PNotify({
							title : '提示消息',
							text : '产品信息保存成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '产品信息保存失败',
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

w.deleteRows =function(T){

	$(T).parent(".col-md-4").parent(".row").parent(".col-md-7").parent(".item").remove();
	
}
		

function removeList(){
		queryBrandList();
		/*"carType" :comType,
		"carPrice" :carPrice,
		"position" :position,
		"optionalConfigure" : JSON.stringify(infoArray),
		"content" :ue.getContent(),*/
		$("[name='carType']").parents("label.active").removeClass("active");
		var oneExh = $("[name='carType']").parents("label").siblings().get(0);
		$(oneExh).addClass("active");

		$("[name='priceRange']").parents("label.active").removeClass("active");
		var twoExh = $("[name='priceRange']").parents("label").siblings().get(0);
		$(twoExh).addClass("active");



		$("[name='position']").parents("label.active").removeClass("active");
		var threeExh = $("[name='position']").parents("label").siblings().get(0);
		$(threeExh).addClass("active");
		$("#create_config").children().remove();
		$('#factoryDate span').html("&nbsp;&nbsp;请选择时间范围&nbsp;");

		
			var configs ='<div class="item form-group"  configname="configname" >';
				configs += '<label class="control-label col-md-2 col-sm-2 col-xs-12" for="first-name">可选配置：</label>';
				configs += '<div class="col-md-7 col-sm-7 col-xs-9" id="create_config">';
				configs += '<div class="row">';
				configs += '<div class="col-md-4 col-sm-4 col-xs-4">';
				configs += '<input type="text" class="form-control glName" value="" name="conf_name" maxLength="20" placeholder="名称" />';
				configs += '</div>';
				configs += '<div class="col-md-4 col-sm-4 col-xs-4">';
				configs += '<input type="text" class="form-control glGx" value="" name="conf_price" maxLength="10" placeholder="价格（元）"';
				configs += 'class="form-control col-md-3 col-xs-12" onkeyup="this.value=this.value.replace(/[^\\d]/g,\''+""+'\')" ';
				configs += 'onafterpaste="this.value=this.value.replace(/[^\\d]/g,\''+""+'\')"';
				configs += ' />';
				
				configs += '</div>';
				configs += '<div class="col-md-4 col-sm-4 col-xs-4">';
                configs += '<button type="button" id="btn_create_config" class="btn btn-warning"  ><i class="fa fa-plus"></i>&nbsp;新 增</button>';
               // configs += '<button type="button" id="delete_rows"  onclick="deleteRows(this)" class="btn btn-danger"  ><i class="fa fa-check-square-o"></i>&nbsp;删 除</button>';
                configs += '</div>';
				configs += '</div>';
				configs += '</div>';
				configs += '</div>';
				$("#create_config").append(configs);


			$("#controlButton").children().remove();
		var controlButton = '<button type="button" class="btn btn-default" data-dismiss="modal" id="updateClose">关闭</button>';
		    controlButton += '<button type="button" id="draft_save" class="btn btn-info ">保存到草稿箱</button>';
		    controlButton += '<button type="button" id="btn_submit" class="btn btn-primary">提交</button>';	
		    $("#controlButton").append(controlButton);
		
		$("#draft_save").val("");
		$('#id').val(""),
		$('#title').val(""),
		$('#basicConfigure').val(""),
		$('#mustConfigure').val(""),	
		$('#carPrice').val(""),
		$('#mustConfigurePrice').val(""),
		$('#stock').val(""),		
		$("#startTimeFrame").val(""),
		$("#country").val(""),
		$("#brandId").val(""),
		$("#indexImageH").val(""),
		$("#shrinkImageH").val(""),
		$("#detailedImageH").val(""),
		$("#recommendImageH").val(""),
		$("#deposit").val(""),
		$("#carModel").val(""),
		$("#outColor").val(""),
		$("#inColor").val(""),
		$("#engine").val(""),
		$("#gearbox").val(""),
		$("#chinaPrice").val("")

		ue.setContent("");	
}



function queryBrandList(){
	$("#brandId").empty();
	$.get(g_requestContextPath + "/h/l/mall/queryBrandList", {}, function(resp){
		if(resp){
			$.each(resp, function(k, v){
				$("#brandId").append('<option value="'+v.id+'" id="'+ v.id+'">'+v.scValue+'</option>');
			});
		}
	});
}


w.showUpdate= function(id){
		removeList();
		$.get(g_requestContextPath + "/h/l/mall/showUpdateProducts", {
			"id" : id
		},function(data) {
			queryBrandList();
			
			if(data){
				uploadFigure1 = new Array();
				uploadFigure2 = new Array();
				uploadFigure3 = new Array();
				uploadFigure4 = new Array();
				//存疑
				//var  uploadFigures = eval($detailedImageH);




				$("#title").val(data.title);
				$('#id').val("data.productsId"),
				
				$("[name='carType']").parents("label.active").removeClass("active");
				var oneExh = $("[name='carType']").parents("label").siblings().get(data.carType);
				$(oneExh).addClass("active");
				
				
				$("[name='priceRange']").parents("label.active").removeClass("active");
				var twoExh = $("[name='priceRange']").parents("label").siblings().get(data.priceRange-1);
				$(twoExh).addClass("active");
				
				$("[name='position']").parents("label.active").removeClass("active");
				var threeExh = $("[name='position']").parents("label").siblings().get(data.position);
				$(threeExh).addClass("active");


				$("#create_config").children().remove();
				console.log(data.optionalConfigure);
				$.each(eval(data.optionalConfigure),function(k,v){
					var configer ='<div class="item form-group"  configname="configname" >';
					if(k==0){
					configer += '<label class="control-label col-md-2 col-sm-2 col-xs-12" for="first-name">可选配置：</label>';
					}else{
					configer += '<label class="control-label col-md-2 col-sm-2 col-xs-12" for="first-name"></label>';
					}
					configer += '<div class="col-md-7 col-sm-7 col-xs-9" id="create_config">';
					configer += '<div class="row">';
					configer += '<div class="col-md-4 col-sm-4 col-xs-4">';
					configer += '<input type="text" class="form-control glName" value="'+v.name+'" name="conf_name" maxLength="20" placeholder="名称" />';
					configer += '<input type="hidden" value="'+v.id+'" name="conf_id"/>';
					configer += '</div>';
					configer += '<div class="col-md-4 col-sm-4 col-xs-4">';
					//configer += '<input type="text" class="form-control glGx" value="'+v.number+'" name="conf_price" maxLength="10" placeholder="价格" />';
					configer += '<input type="text" class="form-control glGx" value="'+v.number+'" name="conf_price" maxLength="10" placeholder="价格（元）"';
					configer += 'class="form-control col-md-3 col-xs-12" onkeyup="this.value=this.value.replace(/[^\\d]/g,\''+""+'\')" ';
					configer += 'onafterpaste="this.value=this.value.replace(/[^\\d]/g,\''+""+'\')"';
					configer += ' />';

					configer += '</div>';
					configer += '<div class="col-md-4 col-sm-4 col-xs-4">';
					if(k==0){
					configer += '<button type="button" id="btn_create_config" class="btn btn-warning"  ><i class="fa fa-plus"></i>&nbsp;新 增</button>';
					}else{
					configer += '<button type="button" onclick="deleteRows(this)" class="btn btn-danger"  ><i class="fa fa-check-square-o"></i>&nbsp;删 除</button>';
					}
                	
					configer += '</div>';
					configer += '</div>';
					configer += '</div>';
					configer += '</div>';
					$("#create_config").append(configer);
				})

				uploadFigure = new Array();
				$.each(data.tProductsImage,function(k,v){
					
					if(v.type ==0){	
						//缩略
						uploadFigure2.push({filePath:data.imgfilePath,name:v.filePath});
						//$("#shrinkImageH").val(v.filePath);
					}
					if(v.type ==1){
						//封面
						uploadFigure1.push({filePath:data.imgfilePath,name:v.filePath});
						//$("#indexImageH").val(v.filePath);	
					}
					if(v.type ==2){
						//推荐
						uploadFigure3.push({filePath:data.imgfilePath,name:v.filePath});
						//$("#recommendImageH").val(v.filePath);	

					}
					if(v.type ==3){
						/*多图上传需要提供preid*/
						uploadFigure4.push({filePath:data.imgfilePath,name:v.filePath,preId:v.fileName});
						//uploadFigure.push({key:v.fileName , value:v.filePath});
					}
				})

				$("#detailedImageH").val(JSON.stringify(uploadFigure));//重新赋予多图路径

				upload1.init("image",uploadFigure1);
				upload2.init("image",uploadFigure2);
				upload3.init("image",uploadFigure4);
				upload4.init("image",uploadFigure3);
			
				


				

				$('#id').val(data.productsId),
				$('#basicConfigure').val(data.basicConfigure),
				$('#mustConfigure').val(data.mustConfigure),	
				$('#carPrice').val(data.carPrice),
				$('#mustConfigurePrice').val(data.mustConfigurePrice),
				$('#stock').val(data.stock),
				$("#deposit").val(data.deposit),
				$("#carModel").val(data.carModel),
				$("#outColor").val(data.outColor),
				$("#inColor").val(data.inColor),
				$("#engine").val(data.engine),
				$("#chinaPrice").val(data.chinaPrice)
				$("#gearbox").val(data.gearbox)
				
				//判断该产品是非自营的时候删除保存按钮
				if(data.selfSupport == 0){
					
					 $("#controlButton").children().remove();					
				}


				//出厂时间	
				$("#startTimeFrame").val(new Date(data.factoryDate).format('yyyy-MM-dd hh:mm:ss')),
				$('#factoryDate span').html(new Date(data.factoryDate).format('yyyy-MM-dd hh:mm:ss'));
				
				$("#country").val(data.country);
				
				$.get(g_requestContextPath + "/h/l/mall/querySelectedBrand", {id:data.brandId}, function(resp){
					if(resp.state!= 0){
					$("#brandId").append('<option value="'+resp.id+'" id="'+ resp.id+'">'+resp.scValue+'</option>');

					
						
					}else{
						
					}
					$("#brandId").val(data.brandId);
					
				})

				

				ue.setContent(data.content);	

				$("#myUASModalLabel").html("产品信息修改");
				$("#editwin").modal('show');


			}

		});
}

w.queryCarNo =function(id){
		$.get(g_requestContextPath + "/h/l/mall/queryCarNo", {
			"id" : id
		},function(data) {	
				$("#carNoBody").children().remove();	
				$.each(data,function(k,v){
					var time = new  Date(v.createTime).format("yyyy-MM-dd hh:mm:ss");
					var carNoStatus = {
							"0" : "待售",
							"1" : "已售"
					};

					var configer ='<tr>';
					 	configer +='<th>'+v.id+'</th>';
					 	configer +='<th>'+v.carNo+'</th>';
					 	configer +='<th>'+ carNoStatus[v.status] +'</th>';
					 	configer +='<th>'+ time +'</th>';
					 	configer +='</tr>';
					 	$("#carNoBody").append(configer);
				})
				$("#carNoList").modal('show');

		});


}

w.disable=function(id){
		$.get(g_requestContextPath + "/h/l/mall/updataDisable", {
			"id" : id
		},function(data) {	
				if(data){
						new PNotify({
							title : '提示消息',
							text : '产品下架成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '产品下架失败',
							type : 'fail',
							styling : 'bootstrap3'
						});

					}

					table.fnDestroy();
					tableDataLoad();

		});
}

w.enable=function(id){
		$.get( g_requestContextPath  + "/h/l/mall/updataEnable", {
			"id" : id
		},function(data) {	
				if(data){
						new PNotify({
							title : '提示消息',
							text : '产品上架成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '产品上架失败',
							type : 'fail',
							styling : 'bootstrap3'
						});
					}
					
					table.fnDestroy();
					tableDataLoad();


		});
}

w.deletes=function(id){
		$.get(g_requestContextPath + "/h/l/mall/updatadelete", {
			"id" : id
		},function(data) {	
				if(data){
						new PNotify({
							title : '提示消息',
							text : '产品删除成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '产品删除失败',
							type : 'fail',
							styling : 'bootstrap3'
						});
					}

					table.fnDestroy();
					tableDataLoad();

		});
}


w.restore=function(id){
		$.get(g_requestContextPath + "/h/l/mall/updataRestore", {
			"id" : id
		},function(data) {	
				if(data){
						new PNotify({
							title : '提示消息',
							text : '产品还原成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '产品还原失败',
							type : 'fail',
							styling : 'bootstrap3'
						});
					}

					table.fnDestroy();
					tableDataLoad();

		});
}

w.recommend=function(id){
		$.get(g_requestContextPath + "/h/l/mall/updataRecommend", {
			"id" : id
		},function(data) {	
				if(data){
						new PNotify({
							title : '提示消息',
							text : '产品推荐成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '产品推荐失败',
							type : 'fail',
							styling : 'bootstrap3'
						});
					}

					table.fnDestroy();
					tableDataLoad();

		});
}

	w.cancelRecommend=function(id){
			$.get(g_requestContextPath + "/h/l/mall/updataCancelRecommend", {
				"id" : id
			},function(data) {	
					if(data){
							new PNotify({
								title : '提示消息',
								text : '产品取消推荐成功',
								type : 'success',
								styling : 'bootstrap3'
							});
						}else{
							new PNotify({
								title : '提示消息',
								text : '产品取消推荐失败',
								type : 'fail',
								styling : 'bootstrap3'
							});
						}

						table.fnDestroy();
						tableDataLoad();

			});
	}

	//产品审核
	w.examine=function(id){
		$("#productId").val(id);
		$("#checkProduct").modal('show');
	}


	$("#btn_audit_submit").click(function(){
	        	var state = $("input[name='auditchk']:checked").val();
	        	var info = $("#auditReason").val().trim();
	        	var id = $("#productId").val();
	        	if(state == "2" && info==""){
	        		new PNotify({
						title : '提示消息',
						text : '审核未通过须填写原因',
						type : 'error',
						styling : 'bootstrap3'
					});
	        		return false;
	        	};
	        	//对商品进行加价处理
	        	if(state == "1"){
	        		emptyproductMarkup();
	        		$(".auditor-modal-lg").modal('hide');
	        		$("#productMarkupId").val(id);
	        		$("#productMarkupInfo").val(info);
	        		$("#productMarkupState").val(state)
	        		$("#productMarkup").modal('show');
	        		return false;
	        	}
	        	$.ajax({
	        		url:g_requestContextPath+"/h/l/audit/productExamine",
	        		data:{
	        			"id":id,
	        			"result":state,
	        			"info":info,
	        			"type":"6"
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

	$("#productMarkup_btn_submit").click(function(){
				$.ajax({
	        		url:g_requestContextPath+"/h/l/audit/productExamineFare",
	        		data:{
	        			"id":$("#productMarkupId").val(),
	        			"result":$("#productMarkupState").val(),
	        			"info":$("#productMarkupInfo").val(),
	        			"type":"6",
	        			"personal":$("#personal").val(),
	        			"sales":$("#sales").val(),
	        			"distributor":$("#distributor").val()
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
	        				$("#productMarkup").modal('hide');
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
	
	w.emptyproductMarkup =function(){
		$("#productMarkupId").val(""),
	    $("#productMarkupState").val(""),
	    $("#productMarkupInfo").val(""),
	    $("#personal").val(""),
	    $("#sales").val(""),
	    $("#distributor").val("")
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
	
	//查找未通过原因
	w.findReasons =function(id){
		$.get(g_requestContextPath + "/h/l/audit/getInfoByMid", {
		                "id": id
		            }, function(data) {
		            	var info = data;
		                if("" == info){
		                	info = "无审核意见";
		                }
		                $("#audit_info").val(info);
		                $("#findReasons").modal('show');
		            });
		
	}


})(jQuery, window)




