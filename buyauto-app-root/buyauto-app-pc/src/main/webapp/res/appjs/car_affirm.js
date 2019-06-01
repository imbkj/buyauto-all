$(function(){
	var chooseContent="";	
	var optionalAmount=0;
	var idLists = new Array();
	var timeout;
	//可选配置
	if($("#userChoose").val() == "" || $("#userChoose").val()=="[]" || eval($("#userChoose").val()).length ==0){
	//无选择配置
		$("#content").text("无可选配置");
		$("#amount").text("￥ 0")
	}else{
		var choose= eval($("#userChoose").val());
		$.each(choose,function(k,v){
			chooseContent+= v.name +" ";
			optionalAmount =optionalAmount+ parseInt(v.number);
			idLists.push(v.id);
		})	
		$("#content").text(chooseContent);
		$("#amount").text("￥ "+ thousandBitSeparator(parseInt(optionalAmount)));
		$("#optionalConfigure").val(idLists);
		$("#optionalConfigure_modify").val(idLists);
		
	}
	//总金额
	if($("#financeType").val() == 1){
	var totalAmount = parseInt(optionalAmount) +  parseInt($("#carPrice").val())  +  parseInt($("#mustConfigurePrice").val());
	$("[name=totalAmount]").text("￥ "+thousandBitSeparator(parseInt(totalAmount)));
	}
	var deposit = parseInt($("#deposit").val()) * parseInt($("#carPrice").val()) /100;
	deposit = Math.round(deposit);
	$("[name=downpayment]").text("￥ "+thousandBitSeparator(parseInt(deposit)));
	
	if($("#financeType").val() == 2){
	$("[name=otherPay]").html("￥ "+$.formatMoney(parseFloat($("#mustConfigurePrice").val()) + parseFloat(optionalAmount)));
	$("[name=fmPay]").html("￥ "+$.formatMoney($("#firstMonPay").val()));
	}
	//提交订单
	$("#order_submit").click(function(){
		clearTimeout(timeout);
	    timeout = setTimeout(function() {


		$.ajax({
		     	 url:g_requestContextPath+"/h/l/products/orderSubmit",
     	         type:'post',
     	         async:false,
     	         data:{
	     	         'sysConfigId':$("#sysConfigId").val(),
	     	         'extractPerson':$("#extractPerson").val(),
					 'phone':$("#phone").val(),
					 'extractDate':$("#extractDate").val(),
					 'optionalConfigure':$("#optionalConfigure").val(),
					 'productsId':$("#productsId").val(),     	         
	     	         'delivery':$("#delivery").val(),
	     	         'distribution':$("#distribution").val(),
	     	         'insuranceType':$("#insuranceType").val(),
	     	         'remarks':$("#remarks").val(),
	     	        'financeType':$("#financeType").val(),
	     	       'term':$("#term").val()
     	         },
     	         success:function(data){
     	         	if(data == null || data==""){
     	         		//window.location.href = g_requestContextPath+ "/error/404"
     	         	}else if(data == 1 ){
     	         		$(".pmtcom").html("用户不存在！");
     	         		$(".promptbox,.pmtbox").show();
     	         		//window.location.href = g_requestContextPath+ "/error/404"
     	         	}else if(data == 2){
     	         		$(".pmtcom").html("提车地址不存在！");
    					$(".promptbox,.pmtbox").show();
     	         		//window.location.href = g_requestContextPath+ "/error/404"
     	         	}else if(data == 3){
     	         		$(".pmtcom").html("配送方式有误！");
    					$(".promptbox,.pmtbox").show();
     	         		//window.location.href = g_requestContextPath+ "/error/404"
     	         	}else if(data == 4){
     	         		$(".pmtcom").html("车辆库存不足,请重新选择车型！");
    					$(".promptbox,.pmtbox").show();
     	         	}else if(data == 5){
     	         		$(".pmtcom").html("下单失败,请重新提交订单！");
    					$(".promptbox,.pmtbox").show();
     	         		//window.location.href = g_requestContextPath+ "/error/404"
     	         	}else if(data == 6){
     	         		$(".pmtcom").html("车辆已下架!");
    					$(".promptbox,.pmtbox").show();
     	         	}else if(data == 7){
     	         		window.location.href = g_requestContextPath+ "/error/notPermission"
     	         	}else if(data == 8){
     	         		$(".pmtcom").html("保险信息有误!");
    					$(".promptbox,.pmtbox").show();
     	         	}else if(data == 9){
     	         		$(".pmtcom").html("产品库存不足!");
    					$(".promptbox,.pmtbox").show();
     	         	}else{
     	         		window.location.href = g_requestContextPath+ "/h/l/orders/orderPayment/" + data
     	         	}
     	         }
     	         });
		//$("#register_form").submit();
		},500);


	})


})