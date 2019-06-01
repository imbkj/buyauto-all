(function($, w){
	var uploadDepositBack = null;
	var uploadDepositBackImageCheck = null;
	var uploadMoneyChecking =  null;
	var uploadThreeCardsImage = null;

	var table = null;
	
	//加载订单管理页面数据
	$(function(){	
		/* 日期控件 订单日期筛选 */
		var tp = new timePlugins("crstartTime","crendTime","reportrange1");
		tp.init();
		//退款凭证上传
	    uploadDepositBack = new imageUploads($("#depositBackImage"),true);//初始化file表单框//退款凭证
	    //审核取消订单时退款凭证上传
	    uploadDepositBackImageCheck = new imageUploads($("#depositBackImageCheck"),true);//审核时上传退款凭证
	    //审核定金或者尾款时凭证上传
	    uploadMoneyChecking = new imageUploads($("#depositBackImageChecking"),true);
	    //上传三证
	    uploadThreeCardsImage = new imageUploads($("#uploadThreeCardsImage"),true);
	    //订单详情样式
	    $("#orderDetialTable").find("tr td:eq(0)").css("width","120px");
	    
		//筛选条件 经销商列表
		$.get(g_requestContextPath+'/h/l/orders/getCompanys',function(data){
			$.each(data,function(idx,obj){
				$('#SearCompanyId').append("<option value='"+obj.companyId+"'>"+obj.companyName+"</option>");
			});
		});
		
	
        // 初始化数据
		tableDataLoad();
	        
       //订单详情
       w.orderDetial = function(id){
    	   $.get(g_requestContextPath + "/h/l/orders/orderDetial", {
                "id": id
	            }, function(data) {
	            	//清除数据
	            	$("#ordercarCertificate").html('');
	            
	            	$('#orderNum').html(data.id);
	            	$('#pdtName').html(data.product.title);
	            	$('#pdtCarModel').html(data.product.carModel);
	            	
	            	$('#pdtEngine').html(data.product.engine);
	            	$('#pdtCarColor').html("车身&nbsp;" + data.product.outColor + "&nbsp;&nbsp;&nbsp;内饰&nbsp;" + data.product.inColor);
	            	$('#pdtGearBox').html(data.product.gearbox);
	            	
	            	$('#mustConfig').html(data.product.mustConfigure);
	            	$('#mustConfigPrice').html("￥"+ thousandBitSeparator(parseInt(data.product.mustConfigurePrice.toFixed(0))));
	            	$('#pdtCarPrice').html("￥"+ thousandBitSeparator(parseInt(data.product.carPrice.toFixed(0))));
	            	//可选配置
	            	var orderOptionalConfig = eval(data.configure);
	            	var carOptionalHtml = "";
	            	$.each(orderOptionalConfig,function(idx,optionalConfigures){
	        			$.each(optionalConfigures,function(idx,configName){
	        				//当前格式标识{"name":"","number":""}
	        				if (idx=="name") {
	        					carOptionalHtml += configName + "&nbsp;&nbsp;";
	        				}
	        			});
	        		});
	        		if($.trim(carOptionalHtml).length == 0){
	        			carOptionalHtml = "无可选配置";
	        		}
	        		$('#optionalCinfig').html(carOptionalHtml);
	        		$('#optionalCinfigPrice').html("￥"+ thousandBitSeparator(parseInt(data.configurePrice.toFixed(0))));
	            	
	            	var orderPayStatus = getOrdersPayStatus(data.payStatus);
	            	
	            	if(data.productImage == null){
	            		$("#productImageAHref").attr("href",g_requestContextPath +"/images/moren.jpg");
	            		$("#productImage").attr("src",g_requestContextPath +"/images/moren.jpg");
	            	}else{
	            		$("#productImageAHref").attr("href",g_imageShowPath+data.productImage.filePath);
		            	$("#productImage").attr("src",g_imageShowPath+data.productImage.filePath);
	            	}
	            	 	
	            	var orderStatusDetial = getOrderStatus(data.status);

	            	$('#oStatus').html(orderStatusDetial);
	            	$('#oPayStatus').html(orderPayStatus);
	            	$('#oDeliverTime').html(data.deliverTime);
	            	$('#oCustomer').html(data.customer);
	            	$('#oCcustomerTel').html(data.customerTel);
	            	$('#oTakeLocation').html(data.takeLocation);
	            	var oTakeWay="";
	            	if(data.takeWay == 0){
	            		oTakeWay="自提";
	            	}else if(data.takeWay == 1){
	            		oTakeWay="配送";
	            	}
	            	$("#oTakeWay").html(oTakeWay);
	            	var leaveMsg = "";
	            	if(data.message == null || data.message == ""){
	            		leaveMsg = "无";
	            	}else{
	            		leaveMsg = data.message;
	            	}
	            	$('#oMessage').html(leaveMsg);
	            	$('#oCreateDate').html(new Date(data.createDate).format("yyyy-MM-dd hh:mm:ss"));
	            	$('#orderTotalPrice').html("￥" + thousandBitSeparator(parseInt(data.amount.toFixed(0))));
	            	if(data.status == 107 || data.status == 200){
	            		$('#carCertificate').css("display","");
	            		//获取车辆三证信息
	            		$.ajax({
			       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
			       			type:'post',
			       			data:{"orderId":id,"type":4},
			       			success:function(data){
			       				var carCertificateImageHtml = "<ul style='list-style-type: none;margin:0;padding:0;'>";
			       				$.each(data,function(k,v){
			       					carCertificateImageHtml += "<li style='margin-bottom: 5px;'><a download='"+g_imageShowPath+v.filePath+"' href='"+g_imageShowPath+v.filePath+"'>"+v.filePath+"&nbsp;(点击下载)</a><li>"; 
			       				});
			       				carCertificateImageHtml += "<ul/>";
			       				$("#ordercarCertificate").append(carCertificateImageHtml);
			       			}
			       		});
	            	}else{
	            		$('#carCertificate').css("display","none");
	            	}
					$("#orderDetialBorder").modal('show');            
	            });
	       }
	       
       //退款
       w.orderMoneyBack = function(id,type){
    	   uploadDepositBack.init("image");
       		//订单id
        	$('#orderDepositId').val(id);
        	$('#orderFilesType').val(type);
        	//标题显示
        	if(type==1){
        		$('#whatMoney').html("尾款");
        		$('#whatMoneyLabel').html("尾款支付");
        	}else if(type==2){
        		$('#whatMoney').html("退款");
        		$('#whatMoneyLabel').html("定金退款");
        	}else if(type==3){
        		$('#whatMoney').html("退款");
        		$('#whatMoneyLabel').html("尾款退款");
        	}else{
        		$('#whatMoney').html("定金");
        		$('#whatMoneyLabel').html("定金支付");
        	}
        	
        	uploadDepositFigure=new Array();
			//获取图片(查看退款凭证)
       		$.ajax({
       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
       			type:'post',
       			data:{"orderId":id,"type":type},
       			success:function(data){
       				$.each(data,function(k,v){
       					if(v.name==""){
       						uploadDepositBack.init("image");
       						uploadDepositFigure.push();
       					}else{
       						uploadDepositFigure.push({filePath:g_imageShowPath,name:v.filePath,preId:v.fileName});
       					}
       				});
       				$("#depositBackImageH").val(JSON.stringify(uploadDepositFigure));//重新赋予多图路径
       				uploadDepositBack.init("image",uploadDepositFigure);

       			}
       		});
        	
			$("#moneyBackBorder").modal('show');
       }

       //重新提交订单
       w.reSubmitOrder = function(id){
       		$.confirm({
			    title: '确认提交',
			    content: '确定重新提交该订单吗&nbsp;?&nbsp;订单编号-'+id,
			    buttons: {
		        	confirm: {
			            text: '确认',
			            action: function(){
			            	//重新提交订单
			            	changeOrdersStatus(id,110,null,0);
			            	$("#shipBorder").modal('hide');
			            }
			        },
			        cancel: {
			           	text: '取消',
			            action: function(){
			            }
			        }
			    }
			});
       }

       //发货
       /*w.orderShip = function(id,carId){
    	    $("#orderIdGo").val(id);
    	   	$("#orderCarIdGo").text(carId);
			$("#shipBorder").modal('show'); 
       }

       //点击发货按钮
		$('#btn_orderShip_submit').click(function(){
			var orderId = $('#orderIdGo').val();
			var carId = $("#orderCarIdGo").val();
			$.confirm({
			    title: '确认发货',
			    content: '确定按照当前发车编号发货吗?',
			    buttons: {
		        	confirm: {
			            text: '确认',
			            action: function(){
			            	//修改订单状态
			            	$("#shipBorder").modal('hide');
			            	changeOrdersStatus(orderId,104,null,null);
			            }
			        },
			        cancel: {
			           	text: '取消',
			            action: function(){
			            }
			        }
			    }
			});
		});*/
	       
       //订单定金、尾款凭证审核
       w.tableCheckMoney = function(id,status){
    	   //初始化
    	   $("#depositImagesShow").html('');
    	   $("#checkDepositOrderId").val('');
    	   $("#checkDepositOrderStatus").val('');
    	   
    	   $("#checkingYes").prop("checked",true);
    	   $("#yesCancelChecking").addClass("active");
    	   $("#noCancelChecking").removeClass("active");
    	   $("#optionsCheckingNo").prop("checked",true);
    	   $("#checkDIV").css("display","none");
    	   $("#moneyBackChecking").css("display","none");
    	   
		   uploadMoneyChecking.init("image");
    	   var whichMoneyCheck=null;
    	   //定金待审核
    	   if(status==102 ){
    		   whichMoneyCheck = 0;
    		   $(".whichMoneyChecking").html("定金");
    	   }else if(status == 105){
    		   //尾款待审核
    		   whichMoneyCheck = 1;
    		   $(".whichMoneyChecking").html("尾款");
    	   }
    	   //获取已上传的凭证
    	   $.ajax({
       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
       			type:'post',
       			data:{"orderId":id,"type":whichMoneyCheck},
       			success:function(data){
       				var checkDepositImageHtml = "<ul style='list-style-type: none;'>";
       				$.each(data,function(k,v){
       					checkDepositImageHtml += "<a target='_blank' href='"+g_imageShowPath+v.filePath+"'><li style='margin:20px 38px;padding:0;float:left;'><img alt='凭证' style='width:180px;height:240px;' src='"+g_imageShowPath+v.filePath+"'/></li></a>";
       				});
       				checkDepositImageHtml += "<div style='clear:both;'></div></ul>";
       				$("#depositImagesShow").html(checkDepositImageHtml);
       			}
       		});
    	   $("#checkDepositOrderId").val(id);
    	   $("#checkDepositOrderStatus").val(status);
    	   $("#depositChecking").modal("show");
       }
       
       //保存定金、尾款审核
       $("#btn_depositCheck_submit").click(function(){
		   var checkingType = null;
		   var moneyBackType = null;
		   var checkingOrderId = $("#checkDepositOrderId").val();
		   var checkingOrdersStatus = $("#checkDepositOrderStatus").val();
		   var checkingResult = $("#checkInfo").val();
		   //验证通过
		   if(validateSaveCheck()){
			   //审核是否通过
			   if($("#checkingNo").prop("checked")){
					checkingType = 1;
				}else{
					checkingType = 0;
				}
				//是否退换
				if($("#optionsCheckingYes").prop("checked")){
					//定金待审核
					if(checkingOrdersStatus == 102){
						moneyBackType = 2;
					//尾款待审核
					}else if(checkingOrdersStatus == 105){
						moneyBackType = 3;
					}
				}
				//定金审核通过修改订单状态为待发货(103)
				if(checkingType == 0 && checkingOrdersStatus == 102){
					changeOrdersStatus(checkingOrderId,104,null,null);
				}
				//尾款审核通过修改订单状态为等待上传三证（106）
				if(checkingType == 0 && checkingOrdersStatus == 105){
					changeOrdersStatus(checkingOrderId,106,null,null);
				}
				//如果定金审核不通过   (并且不同意退款)&& $("#optionsCheckingNo").prop("checked")
				if(checkingType == 1 && checkingOrdersStatus == 102){
					changeOrdersStatus(checkingOrderId,402,null,null);
				}
				//如果尾款审核不通过   (并且不同意退款)&& $("#optionsCheckingNo").prop("checked")
				if(checkingType == 1 && checkingOrdersStatus == 105){
					changeOrdersStatus(checkingOrderId,403,null,null);
				}
				//保存审核结果
				$.ajax({
            		url: g_requestContextPath + "/h/l/orders/cancelOrders",
            		type:'post',
            		data:{"id":checkingOrderId,"result":checkingType,"type":1,"info":checkingResult},
            		dataType:'json',
            		success:function(data){
	            		
            		}
            	});
            	
				$("#depositChecking").modal("hide");
        		table.fnDestroy();
				tableDataLoad();
			   
		   }
    	   
       });
	       
       //点击取消按钮
       w.makeSureCancelOrder=function(id,oldStatus){
	       	$.confirm({
				    title: '确认取消？',
				    content: '确定取消该订单&nbsp;?&nbsp;编号-'+id,
				    buttons: {
			        	confirm: {
				            text: '确认',
				            action: function(){
				            	changeOrdersStatus(id,404,oldStatus,null);
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
		   
	   //点击取消订单审核
       w.CancelOrder=function(id,oldStatus,cancelReason){
       		//初始化
       		$('#cancelYes').prop('checked',true);
       		$('#cancelReason').val('');
       		$("#yesCancel").addClass("active");
  		  	$("#noCancel").removeClass("active");
  		  	$("#writeCheckInfo").css("display","none");
  		  	
       		//如果订单状态为待确认或者待支付定金不能选择是否退还定金
       		if(oldStatus == 110 || oldStatus == 100 || oldStatus==101){
       			$("#bakckMoneyRadio").css("display","none");
       			$("input:radio[name=options]").prop("disabled",true);
       			$("#moneyBackCheck").css("display","none");
       		}else{
       			$("#bakckMoneyRadio").css("display","");
       			$("input:radio[name=options]").prop("disabled",false);
       			uploadDepositBackImageCheck.init("image");
       			$("#moneyBackCheck").css("display","");
       			$('#optionsYes').prop('checked',true);
       		}
       		
       		$("#orderId").val(id);
       		$('#orderOldStatus').val(oldStatus);
       		$('#cancelReason').val(cancelReason);
       		$("#checkCancelOrder").modal('show');
       }
	   
	   //查看支付凭证
       w.checkCertificate = function(id,orderStatus,payStatus){
       		//图片初始化
       		$("#depositImagesShowList").html('');
       		$("#TailmoneyImagesShowList").html('');
       		$('#certificateType').html("支付");
       		//尾款未支付
       		if(payStatus == 11 || payStatus == 12){
       			$('#tailMoney').css('display','');
       		}else{
       			$('#tailMoney').css('display','none');
       		}
       		
       		//获取支付凭证图片
       		$.ajax({
       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
       			type:'post',
       			data:{"orderId":id,"type":null},
       			success:function(data){
       				var depositImageHtml = "<ul style='list-style-type:none;'>";
       				var TailmoneyImageHtml = "<ul style='list-style-type:none;'>";
       				$.each(data,function(k,v){
       					if (v.type==1) { //尾款支付凭证
       						TailmoneyImageHtml += "<li style='margin:20px 38px;padding:0;float:left;'><a target='_blank' href='"+g_imageShowPath+v.filePath+"'><img style='width:180px;height:240px;' alt='凭证' src='"+g_imageShowPath+v.filePath+"'></a></li>";
						}else if(v.type==0){  //定金支付凭证
							depositImageHtml += "<li style='margin:20px 38px;padding:0;float:left;'><a target='_blank' href='"+g_imageShowPath+v.filePath+"'><img style='width:180px;height:240px;' alt='凭证' src='"+g_imageShowPath+v.filePath+"'/></a></li>";
						}
       				});
       				depositImageHtml += "<div style='clear:both;'></div></ul>";
       				TailmoneyImageHtml += "<div style='clear:both;'></div></ul>";
       				$("#depositImagesShowList").html(depositImageHtml);
       				$("#TailmoneyImagesShowList").html(TailmoneyImageHtml);
       			}
       		});
			$("#checkCertificateOrder").modal('show');  
       }
	       
       //取消订单原因
       w.getCencelRea = function(id,status){
       		//title显示
       		if(status == 404){
       			$('#lookReasonFont').html('订单取消原因');
       		}else if(status == 402){
       			$('#lookReasonFont').html('定金审核未通过原因');
       		}else if(status == 403){
       			$('#lookReasonFont').html('尾款审核未通过原因');
       		}else if(status == 109){
       			$('#lookReasonFont').html('取消订单审核未通过原因');
       		}
       
       		$.get(g_requestContextPath + "/h/l/orders/getCancelRea",
   				{"id":id},
   				function(data){
       				$('#orderCancelReason').html(data);
   					$("#orderCancelRea").modal('show');
					}
				);
       }
       
       //订单状态更改
       w.orderStatusChg =function(id,status){
    	   changeOrdersStatus(id,status,null,null);
       } 
	      
       //物流跟踪？
       w.orderLogistics=function(id){
   		 	$('#logistUl').html('');
       		$('#logistUl').css({"list-style-type":"none"},{"margin-left":"20rem"});
       		$('#logistUl li').css("padding","10px");
       		$.get(g_requestContextPath + "/h/l/orders/orderDetial", {
                "id": id
            }, function(data) {
            	var logisticsHtml = "";
				if(data.logisticsInfo == null || data.logisticsInfo == "" || data.logisticsInfo == "[]"){
					logisticsHtml += "<li>当前时间："+new Date().format("yyyy-MM-dd hh:mm:ss")+"&nbsp;&nbsp;"+"物流信息暂无</li>";
				}else{
					$.each(JSON.parse(data.logisticsInfo),function(idx,logs){
						logisticsHtml += "<li>" + new Date(logs.modifiTime).format("yyyy-MM-dd hh:mm:ss") + "&nbsp;&nbsp;&nbsp;" + logs.logistStatus + "</li>";
					});
				}

	    	   $('#logistUl').append(logisticsHtml);

            });

    	   //根据id获取物流状态
    	   $("#ordersLogistics").modal("show");
       }

       //三证上传
       w.uploadTreeCards = function(id){
       		$("#uploadThreeCardsOrderId").val(id);
       		uploadThreeCardsFigure=new Array();
			//获取图片(查看已有的三证图片)
       		$.ajax({
       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
       			type:'post',
       			data:{"orderId":id,"type":4},
       			success:function(data){
       				$.each(data,function(k,v){
       					if(v.name==""){
       						uploadThreeCardsImage.init("pdf");
       						uploadThreeCardsFigure.push();
       					}else{
       						uploadThreeCardsFigure.push({filePath:g_imageShowPath,name:v.filePath,preId:v.fileName});
       					}
       				});
       				$("#uploadThreeCardsImageH").val(JSON.stringify(uploadThreeCardsFigure));//重新赋予多图路径
       				uploadThreeCardsImage.init("pdf",uploadThreeCardsFigure);

       			}
       		});

       		$("#uploadTreeCardsModal").modal("show");
       }

       //上传三证保存
       $("#btn_uploadThreeCards_submit").click(function(){
       		var orderId=$('#uploadThreeCardsOrderId').val();
       		//三证图片
			$("#uploadThreeCardsImageH").val(JSON.stringify(uploadThreeCardsImage.getNewFile()));
			$("#uploadThreeCardsImageOld").val(JSON.stringify(uploadThreeCardsImage.getOldFile()));

			//上传验证
			if(($("#uploadThreeCardsImageH").val()=="[]" || $("#uploadThreeCardsImageH").val()=="" || $("#uploadThreeCardsImageH").val()==null) && ($("#uploadThreeCardsImageOld").val() == "[]" || $("#uploadThreeCardsImageOld").val() == "" || $("#uploadThreeCardsImageOld").val() == null)){
				new PNotify({
					title : '提示消息',
					text : '请先选择三证文件',
					type : 'error',
					styling : 'bootstrap3'
				});
				return false;
			}
			if(!checkFileLength(uploadThreeCardsImage)){
				return false;
			}

			//保存三证图片
			$.ajax({
				url : g_requestContextPath + "/h/l/orders/saveOrdersImg",
				type : 'post',
				dataType : 'json',
				data:{
					"orderId":orderId,
					"type":4,
					"depositBackImageH":$("#uploadThreeCardsImageH").val(),
					"depositBackImageOld":$("#uploadThreeCardsImageOld").val()
				},
				success : function(data) {
					$("#uploadTreeCardsModal").modal("hide");
					if(data){
						//三证上传成功将订单状态改为待交车
						changeOrdersStatus(orderId,107,null,null);
						
						new PNotify({
							title : '提示消息',
							text : '三证图片上传成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '三证图片上传失败',
							type : 'error',
							styling : 'bootstrap3'
						});
					}
					
					table.fnDestroy();
					tableDataLoad();
				}
			});
       });

	    //条件查询
		$("#transfer_frmSearch").click(function() {
			if(validateOrderId()){
				table.fnDestroy();
				tableDataLoad();
				new PNotify({
					title : '提示消息',
					text : '查询成功',
					type : 'success',
					styling : 'bootstrap3'
				});
			}
			return false;
		});
		
		// 点击退款保存
		$('#btn_deposit_submit').click(function(){
			//隐藏参数	        	
			var orderId=$('#orderDepositId').val();
			var orderFilesType=$('#orderFilesType').val();
       		//退款图片
			$("#depositBackImageH").val(JSON.stringify(uploadDepositBack.getNewFile()));
			$("#depositBackImageOld").val(JSON.stringify(uploadDepositBack.getOldFile()));
			//上传验证
			
			if(($("#depositBackImageH").val()=="[]" || $("#depositBackImageH").val()=="" || $("#depositBackImageH").val()==null) && ($("#depositBackImageOld").val() == "[]" || $("#depositBackImageOld").val() == "" || $("#depositBackImageOld").val() == null)){
				new PNotify({
					title : '提示消息',
					text : '请先选择付款凭证',
					type : 'error',
					styling : 'bootstrap3'
				});
				return false;
			}
			if(!checkFileLength(uploadDepositBack)){
				return false;
			}
			//保存退款凭证
			$.ajax({
				url : g_requestContextPath + "/h/l/orders/saveOrdersImg",
				type : 'post',
				dataType : 'json',
				data:{
					"orderId":orderId,
					"type":orderFilesType,
					"depositBackImageH":$("#depositBackImageH").val(),
					"depositBackImageOld":$("#depositBackImageOld").val()
				},
				success : function(data) {
					$("#moneyBackBorder").modal('hide');
					if(data){
						//退款成功将订单状态改为已关闭
						changeOrdersStatus(orderId,405,null,20);
						
						new PNotify({
							title : '提示消息',
							text : '退款凭证上传成功',
							type : 'success',
							styling : 'bootstrap3'
						});
					}else{
						new PNotify({
							title : '提示消息',
							text : '退款凭证上传失败',
							type : 'error',
							styling : 'bootstrap3'
						});
					}
					
					table.fnDestroy();
					tableDataLoad();
				}
			});
		});
		
		//审核取消订单保存按钮
		$('#btn_orderCencel_submit').click(function(){
			//验证
			var canRes=true;
			var orderId=$('#orderId').val();
			//原状态
			var oldOrderStatus=$('#orderOldStatus').val();
			//是否同意取消订单
        	var canResult=null;
        	//是否同意退款给
        	var refundsRes=null;
			
			//理由验证
			var cancelR=$('#checkCencelOrderInfo').val();
			if($('#cancelNo').prop("checked")){
				if($.trim(cancelR).length==0){
					new PNotify({
						title : '提示消息',
						text : '请输入未通过理由！',
						type : 'error',
						styling : 'bootstrap3'
					});
                    canRes=false;
				}
				var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");
	            if(cancelR != "" && cancelR != null){
	                if(pattern.test(cancelR)){  
	                    new PNotify({
							title : '提示消息',
							text : '非法字符，请输入正确的理由！',
							type : 'wanrning',
							styling : 'bootstrap3'
						});
	                    canRes=false;
	                }  
	            }
			}else{
				canRes=true;
				//不退定金
				if($('#optionsNo').prop('checked')){
					refundsRes=0;
				}else if($('#optionsYes').prop('checked')){
					//退还定金
					refundsRes=1;
				}
			}
            //不同意取消订单
			if($('#cancelNo').prop("checked")) {
				canResult = 1;
			}else if($('#cancelYes').prop("checked")){
				canResult = 0;
			}
			//验证通过
            if(canRes){
            	if(canResult==1){
            		//短信通知销售订单取消审核不通过
    				$.get(g_requestContextPath + "/h/l/orders/msgToSeller",{"orderId":orderId});
					//如果不同意取消的话将状态改为原本状态		
            		changeOrdersStatus(orderId,oldOrderStatus,null,null);
            	}else{
            		if(refundsRes==1){
            			//如果订单状态是待确认或者待支付定金的时候不可以选择是否退还定金
            			if($("input:radio[name=options]").prop("disabled")){
            				changeOrdersStatus(orderId,404,null,null);
            			}else{
	            			//同意退款
				           	//修改订单状态为已取消
            				$("#depositBackImageHCheck").val(JSON.stringify(uploadDepositBackImageCheck.getNewFile()));
            				$("#depositBackImageOldCheck").val(JSON.stringify(uploadDepositBackImageCheck.getOldFile()));
            				//uploadDepositBackImageCheck.getOldFile()有问题
            				//上传验证
            				
            				if(($("#depositBackImageHCheck").val()=="[]" || $("#depositBackImageHCheck").val()=="" || $("#depositBackImageHCheck").val()==null)
            						&&($("#depositBackImageOldCheck").val() == "[]" || $("#depositBackImageOldCheck").val() == "" || $("#depositBackImageOldCheck").val() == null)){
            					new PNotify({
            						title : '提示消息',
            						text : '请先选择付款凭证',
            						type : 'error',
            						styling : 'bootstrap3'
            					});
            					return false;
            				}
            				if(!checkFileLength(uploadDepositBackImageCheck)){
            					return false;
            				}
            				//保存退款凭证
            				$.ajax({
            					url : g_requestContextPath + "/h/l/orders/saveOrdersImg",
            					type : 'post',
            					dataType : 'json',
            					data:{
            						"orderId":orderId,
            						"type":2,//定金退款
            						"depositBackImageH":$("#depositBackImageHCheck").val(),
            						"depositBackImageOld":$("#depositBackImageOldCheck").val()
            					},
            					success : function(data) {
            						if(data){
            							//退款成功将订单状态改为已取消
            							changeOrdersStatus(orderId,404,null,20);
            						}else{
            							new PNotify({
            								title : '提示消息',
            								text : '退款凭证上传失败',
            								type : 'fail',
            								styling : 'bootstrap3'
            							});
            						}
            					}
            				
            				});
		            	}
            		}else{
            			//同意取消订单但不退还定金
            			changeOrdersStatus(orderId,404,null,null);
            		}
            	}
            	//保存取消订单审核
            	$.ajax({
            		url: g_requestContextPath + "/h/l/orders/cancelOrders",
            		type:'post',
            		data:{"id":orderId,"result":canResult,"type":2,"info":cancelR},
            		dataType:'json',
            		success:function(data){
        				$(".uas-modal-lg").modal('hide');
            		}
            	});
            }
		});
			
		//修改订单状态
		function changeOrdersStatus(id,status,oldStatus,payStatus){
			$.post(g_requestContextPath + "/h/l/orders/orderChg",
				{"id":id,"status":status,"oldStatus":oldStatus,"orderPayStatus":payStatus},
				function(data){
					if(data==1){
						table.fnDestroy();
						tableDataLoad();
						new PNotify({
							title : '提示消息',
							text : '订单状态修改成功',
							type : 'success',
							styling : 'bootstrap3'
						});
						return false;
					}else{
						table.fnDestroy();
						tableDataLoad();
						new PNotify({
							title : '提示消息',
							text : '订单状态修改失败',
							type : 'error',
							styling : 'bootstrap3'
						});
						return false;
				   }
				}
			);
		}

		//上传图片数量限制
		function checkFileLength(upload){
		    var	fileObj = upload.getNewFile();
		    var fileObjOld = upload.getOldFile();
			var fileLength = parseInt(fileObj.length);
			if(fileObjOld != null){
				 fileLength += parseInt(fileObjOld.length)
			}

			if (fileLength > 5) {
				new PNotify({
					title : '提示消息',
					text : '凭证最多上传&nbsp;5&nbsp;张',
					type : 'error',
					styling : 'bootstrap3'
				});
				return false;
			}
			return true;
		}
				
	});
	
	//订单状态
	function getOrderStatus(status){
		var orderStatuaText = "";
		switch (status) {
    		case 110:
				orderStatuaText = "待系统确认";
				break;
			case 100:
				orderStatuaText = "确认中";
				break;
			case 101:
				orderStatuaText = "待支付定金";
				break;
			case 102:
				orderStatuaText = "定金待审核";
				break;
			case 103:
				orderStatuaText = "待发货";
				break;
			case 104:
				orderStatuaText = "待支付尾款";
				break;
			case 105:
				orderStatuaText = "尾款待审核";
				break;
			case 106:
				orderStatuaText = "待上传车辆三证";
				break;
			case 107:
				orderStatuaText = "等待交车";
				break;
			case 108:
				orderStatuaText = "取消待审核";
				break;
			case 109:
				orderStatuaText = "取消审核未通过";
				break;
			case 200:
				orderStatuaText = "订单已完成";
				break;
			case 401:
				orderStatuaText = "库存确认未通过";
				break;								
			case 402:
				orderStatuaText = "定金审核未通过";
				break;								
			case 403:
				orderStatuaText = "尾款审核未通过";
				break;
			case 404:
				orderStatuaText = "订单已取消";
				break;
			case 405:
				orderStatuaText = "订单已关闭";
				break;
			default:
				break;
		}
		return orderStatuaText;
	}

	//订单支付状态
	function getOrdersPayStatus(payStatus){
		var orderPayStatusText = null;
		switch (payStatus) {
			case 0:
				orderPayStatusText = "未支付";
				break;
			case 1:
				orderPayStatusText = "定金支付超时";
				break;
			case 10:
				orderPayStatusText = "定金已支付";
				break;
			case 20:
				orderPayStatusText = "定金已退款";
				break;
			case 11:
				orderPayStatusText = "尾款已支付";
				break;
			case 12:
				orderPayStatusText = "尾款已退款";
				break;
			default:
				break;
		}
		return orderPayStatusText;

	}
	//加载订单列表
    function tableDataLoad(){
      table =$('#t_orders').dataTable({
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
            		url: g_requestContextPath+'/h/l/orders/ordersList',
		            type: 'post',
		            async: false,
		            dataType: 'json',
		            dataSrc: 'rows',
		            data: function(aoData){
		            	var param = {};
		            	param.page = aoData.start>0 ? (aoData.start/aoData.length +1) : 1 ;
		            	param.rows = 20;//aoData.length;
		            	//param.companyId = $("#SearCompanyId").val();
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
            		//商品
            		{data: function(d){
            			var productHtml = "订单号："+d.id
            			if(d.status == 200){
            				productHtml +="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;成交时间："+new Date(d.completeTime).format('yyyy-MM-dd  hh:mm:ss')
            			}else{
            				productHtml +="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;更新时间："+new Date(d.updateDate).format('yyyy-MM-dd  hh:mm:ss')
            			}
            			productHtml +="<br/><br/><div style='margin:0px;padding:0px;width:100%;height:100%; float:left;'><div style='height:100%;float:left;'>";
            			if(d.productImage == null){
            				productHtml += "<a target='_blank' href='"+ g_requestContextPath +"/images/moren.jpg'><img style='border:1px solid black;width:120px; height:90px; margin:0 0 0 16px;' alt='默认图片' src='"+ g_requestContextPath +"/images/moren.jpg'/></a></div>";
            			}else{
							productHtml += "<a target='_blank' href='" + g_imageShowPath + d.productImage.filePath +"'><img style='border:1px solid black;width:120px; height:90px; margin:0 0 0 16px;' alt='缩略图' src='" + g_imageShowPath + d.productImage.filePath +"'/></a></div>";
						}
            			productHtml += "<div style='height:100%;float:left; padding:10px 0 0 38px;'>"+d.product.title+"<br/><br/>";
            			
            			//产品类型（港口现车、预定车、在途车）
            			var carPosition="";
            			if(d.product.position==0){
            				carPosition="现车";
            			}else if(d.product.position==1){
            				carPosition="预定车";
            			}else if(d.product.position==2){
            				carPosition="在途车";
            			}
            			productHtml += d.product.country+"&nbsp;&nbsp;&nbsp;"+ carPosition + "</div>";
            			productHtml += "</div>";
            			
    					productHtml += "<div style='clear:both;'>"
    					productHtml += "</div>";
            			return productHtml;
        			}},
            		//定金
					{data: function(d){return "￥"+ thousandBitSeparator(parseInt(d.deposit.toFixed(0)));}},
					//总价
					{data: function(d){return "￥"+ thousandBitSeparator(parseInt(d.amount.toFixed(0)));}},
					//买家
					{data: function(d){return d.customer+"<br/><br/>"+d.customerTel;}},
					//交易状态
					{data: function(d){
						var ordersStatusDisplay =  getOrderStatus(d.status);
						return ordersStatusDisplay;
					}},
					//支付状态
					{data: function(d){
						return getOrdersPayStatus(d.payStatus);
					}},
					//订单来源
					{data: function(d){return d.companyName;}},
					{data: function(d){
						var s = '<div class="table-o-tooltip">';
						if (d.status==100) {
							//新建状态
							//抓取到数据之后自动修改为待支付状态
							//s += '<a href="javascript:orderStatusChg(\''+d.id+'\',101);" class="btn-link">确认</a><br/>';
						}else if(d.status==101){
							//待支付定金
						}else if(d.status==102){
							//定金待审核
							if(deposit){
								s += '<a href="javascript:tableCheckMoney(\''+d.id+'\',\''+d.status+'\');" class="btn-link">审核定金</a><br/>';	
							}
							
						}else if(d.status==103){
							//待发货
							/*s += '<a href="javascript:orderShip(\''+d.id+'\',\''+d.carId+'\');" class="btn-link">发货</a><br/>';*/
							if(refund){
								s += '<a href="javascript:orderMoneyBack(\''+d.id+'\',2);" class="btn-link">退款</a><br/>';	
							}
							
						}else if(d.status==403){
							//二审不通过
						}else if(d.status==105){
							//尾款待审核
							if(retainage){
								s += '<a href="javascript:tableCheckMoney(\''+d.id+'\',\''+d.status+'\');" class="btn-link">审核尾款</a><br/>';	
							}
							
						}else if(d.status==106){
							//上传车辆三证
							if(uploadProof){
								s += '<a href="javascript:uploadTreeCards(\''+d.id+'\');" class="btn-link">上传车辆三证</a><br/>';	
							}
							
						}else if(d.status==107){
							//等待交车
							if(logistics){
								s += '<a href="javascript:orderLogistics(\''+d.id+'\');" class="btn-link">物流信息</a><br/>';	
							}
							
							//元初工作人员
							//if(true){
							//	s += '<a href="javascript:orderLogistics(\''+d.id+'\');" class="btn-link">确认交车</a><br/>';
							//}
						}else if(d.status==109){
							//订单取消不通过
							if(reason){
								s += '<a href="javascript:getCencelRea(\''+d.id+'\',\''+d.status+'\');" class="btn-link">未通过原因</a><br/>';	
							}
							
						}else if(d.status==404){
							//订单取消
							//s += '<a href="javascript:getCencelRea(\''+d.id+'\',\''+d.status+'\');" class="btn-link">取消原因</a><br/>';
						}else if(d.status==405 && d.payStatus == 1){
							//订单关闭并且未支付定金
							if(again){
								s += '<a href="javascript:reSubmitOrder(\''+d.id+'\');" class="btn-link">重提订单</a><br/>';	
							}
							
						}else if(d.status == 108){
							//取消订单审核
							if(examine){
								s += '<a href="javascript:CancelOrder(\''+d.id+'\',\''+d.oldStatus+'\',\''+d.cancelReason+'\');" class="btn-link">审核</a><br/>';	
							}
							
						}
						if(d.payStatus > 1){
							if(voucher){
								s += '<a href="javascript:checkCertificate(\''+d.id+'\',\''+d.status+'\',\''+d.payStatus+'\');" class="btn-link">查看付款凭证</a><br/>';	
							}
							
						}
						if(d.payStatus == 20){
							if(voucher){
							s += '<a href="javascript:orderMoneyBack(\''+d.id+'\',2);" class="btn-link">上传/查看退款凭证</a><br/>';
							}
						}
						//查看不通过原因
						if(d.status == 402 || d.status == 403){
							//不通过原因
							if(reason){
								s += '<a href="javascript:getCencelRea(\''+d.id+'\',\''+d.status+'\');" class="btn-link">未通过原因</a><br/>';	
							}
							
						}
						//订单详情
						if(detailed){
							s += '<a href="javascript:orderDetial(\''+d.id+'\');" class="btn-link">订单详情</a>';	
						}
						
						//不可以取消订单的情况下已取消、已关闭、取消待审核、已完成、库存确认不通过
						if(d.status==100 || d.status==101 || d.status==102 || d.status==103){
							//取消按钮
							if(cancel){
							s += '<br/><a href="javascript:makeSureCancelOrder(\''+d.id+'\',\''+d.status+'\');" class="btn-link">取消</a><br/>';
							}
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
                	infoFiltered: ""//筛选之后的左下角筛选提示(另一个是分页信息显示，在上面的info中已经设置，所以可以不显示)
            	}
            });
    	}

})(jQuery, window)