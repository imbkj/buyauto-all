$(function(){
	//按钮设置
	$('#checkCancelOrder').change(function(){
		//不同意取消订单
		if($('#cancelNo').prop("checked")) {
			$("input:radio[name=options]").prop("disabled",true);
			$("#moneyBackCheck").css("display","none");
			$("#writeCheckInfo").css("display","");
			$("#bakckMoneyRadio").css("display","none");
		//同意取消订单
		}else if($('#cancelYes').prop("checked")){
			$("#writeCheckInfo").css("display","none");
			//新建/待支付定金状态不能选择是否退款
			if($('#orderOldStatus').val() == 110 || $('#orderOldStatus').val() == 100 || $('#orderOldStatus').val()==101){
				$("#bakckMoneyRadio").css("display","none");
				$("input:radio[name=options]").prop("disabled",true);
				//隐藏凭证上传控件
				$("#moneyBackCheck").css("display","none");
			}else{
				$("#bakckMoneyRadio").css("display","");
				$("input:radio[name=options]").prop("disabled",false);
				//判断是否同意退款
				if($("#optionsYes").prop("checked")){
					$("#moneyBackCheck").css("display","");
				}else if($("#optionsNo").prop("checked")){
					$("#moneyBackCheck").css("display","none");
				}
			}
		}
	});
	
	//审核是否通过
	$("#checkingResultDiv").change(function(){
		if($("#checkingNo").prop("checked")){
			$("#checkDIV").css("display","");
		}else{
			$("#checkDIV").css("display","none");
		}
	});
	
	//是否退换定金或者尾款
	$("#checkDIV").change(function(){
		//退还
		if($("#optionsCheckingYes").prop("checked")){
			$("#moneyBackChecking").css("display","");
		}else if($("#optionsCheckingNo").prop("checked")){
			$("#moneyBackChecking").css("display","none");
		}
	});
	
});

//不通过原因验证
function validateSaveCheck(){
	var checkInfo=$("#checkInfo").val();
	var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");
	if($("#checkingYes").prop("checked")){
		return true;
	}else {
		if ($.trim(checkInfo).length== 0) {
			new PNotify({
				title : '提示消息',
				text : '请填写未通过原因',
				type : 'error',
				styling : 'bootstrap3'
			});
			return false;
		}
		if(checkInfo != "" && checkInfo != null){
	        if(pattern.test(checkInfo)){
	            new PNotify({
					title : '提示消息',
					text : '非法字符，请输入正确未通过原因！',
					type : 'wanrning',
					styling : 'bootstrap3'
				});
	            return false;  
	        }  
	    }
	   return true; 
	}
}
//审核定金、尾款上传验证
function checkingImage(){
	if($("#optionsCheckingNo").prop("checked")){
    	return true;
    }else{
	    //上传验证
		if(($("#depositBackImageHChecking").val()=="[]" || $("#depositBackImageHChecking").val()=="" || $("#depositBackImageHChecking").val()==null) && ($("#depositBackImageOldChecking").val() == "[]" || $("#depositBackImageOldChecking").val() == "" || $("#depositBackImageOldChecking").val() == null)){
			new PNotify({
				title : '提示消息',
				text : '请先选择退款款凭证',
				type : 'error',
				styling : 'bootstrap3'
			});
			return false;
		}
		return true;
    }
}
//订单编号搜索验证
function validateOrderId(){
	var searchRes = true;
	var orderIdSearch = $('#orderIdForSearch').val();
	var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");  
    if($.trim(orderIdSearch).length != 0 && orderIdSearch != null){
        if(pattern.test(orderIdSearch)){
            new PNotify({
				title : '提示消息',
				text : '非法字符，请输入正确的订单编号！',
				type : 'wanrning',
				styling : 'bootstrap3'
			});
            searchRes = false;  
        }else{
        	searchRes = true;
        }
    }
    return searchRes;
}

