var id = ''; 
var scrollTop= null;
var amount = 0;
var insuranceAmount = 0;
var firstMonPay = 0;
$(function(){
	$('#singleDateRange').click(function(){
		$('.calendar').show();
	})
	$('.calendar').click(function(e){
		 var e = e || window.event;
         var target = e.target || e.srcElement;
        // console.log(target.id)//arrow-prev//arrow-next
         if(target.className == 'arrow-prev' || target.className == 'arrow-next' || target.className == 'calendar-title' || target.id == 'backToday'){
        	 return;
         }else{
        	 $('#singleDateRange').val($('.title').html() + '/' + $('.item-selected').html())
     		$('.calendar').hide();
         }
	})
	
	// 金融方案
	getFinanceData();
	
	
	
	// 初始化时间
	var time = new Date();
	time = time.getTime();
	time = time + 604800000;
	time = new Date(time);
	var year = time.getFullYear();
	var month = time.getMonth() + 1;
	var date = time.getDate();
	var InsuranceType=0;
	
	 
	$('#singleDateRange').val(year+'/'+month+'/'+date);
	// 遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	})
	var optionalObj = eval($("#optional").val());
	var idLists = new Array();
	priceCalculation();
	$.each(optionalObj,function(k,v){
	// console.log(v);
	 if(v.number != "" && v.number != null){
		var optionalCont =  "<li>"; 
            optionalCont += "<div name='optionalEntity'>";    
            optionalCont += '<input type="hidden" value="'+v.id+'" name="optional_id"/>';         
            optionalCont += "<span>"+v.name +"</span>";             
            optionalCont += "</div>";             
            optionalCont += "<div name='amount'>";             
            optionalCont += "<span>￥ "+ thousandBitSeparator(parseInt(v.number)) +"</span>"  
			optionalCont += "<input name='amountNumber' type='hidden' value='"+v.number+"'</input>"  
			optionalCont += "</div>"                                           
			optionalCont += "</li>"                                             
			$("#optional_hidden").append(optionalCont);
		}
	})
	// 选择配送方式
	$('.config_type ul li').click(function(){
		id = $(this).attr('id');
		$(this).addClass('getColor').siblings().removeClass('getColor');
		if(id == 0){
			$('.address01').show();
			$('.time01').show();
			$('.address02').hide();
			$('.time02').hide();
		}else if(id == 1){
			$('.address01').hide();
			$('.time01').hide();
			$('.address02').show();
			$('.time02').show();
		}
	})
	
	// 选择保险配置
	$('#insurance1').click(function(){
		if($('#insurance1').hasClass('getSel')){
			$('#insurance1').removeClass('getSel');
			insuranceAmount = insuranceAmount - parseInt($.unformatMoney($("#insurance1").find("[name='insurance_price']").html().replace("￥ ","")));
		}else{
			$('#insurance1').addClass('getSel');
			$('#insurance2').removeClass('getSel');
			if(insuranceAmount == 0){
				insuranceAmount = insuranceAmount + parseInt($.unformatMoney($("#insurance1").find("[name='insurance_price']").html().replace("￥ ","")));
			}else{
				insuranceAmount = insuranceAmount - parseInt($.unformatMoney($("#insurance2").find("[name='insurance_price']").html().replace("￥ ","")));
				insuranceAmount = insuranceAmount + parseInt($.unformatMoney($("#insurance1").find("[name='insurance_price']").html().replace("￥ ","")));
			}
		}
		priceCalculation();
		getOtherPrice();
	});
	
	$('#insurance2').click(function(){
		if($('#insurance2').hasClass('getSel')){
			$('#insurance2').removeClass('getSel');
			insuranceAmount = insuranceAmount - parseInt($.unformatMoney($("#insurance2").find("[name='insurance_price']").html().replace("￥ ","")));
		}else{
			$('#insurance2').addClass('getSel');
			$('#insurance1').removeClass('getSel');
			if(insuranceAmount == 0){
				insuranceAmount = insuranceAmount + parseInt($.unformatMoney($("#insurance2").find("[name='insurance_price']").html().replace("￥ ","")));
			}else{
				insuranceAmount = insuranceAmount - parseInt($.unformatMoney($("#insurance1").find("[name='insurance_price']").html().replace("￥ ","")));
				insuranceAmount = insuranceAmount + parseInt($.unformatMoney($("#insurance2").find("[name='insurance_price']").html().replace("￥ ","")));
			}
		}
		priceCalculation();
		getOtherPrice();
	});
	
	
	// 选择付款方式
	$('.Price').click(function(){
		$('.price_ul').show();
		$('.loan_ul').hide();
		$(this).addClass('getback').find('span').addClass('getline');
		$('.Loan').removeClass('getback').find('span').removeClass('getline');
	})
	
	$('.Loan').click(function(){
		$('.price_ul').hide();
		$('.loan_ul').show();
		$(this).addClass('getback').find('span').addClass('getline');
		$('.Price').removeClass('getback').find('span').removeClass('getline');
	})
	
	// 分期方式下选择期限以及首付款
	$('.Chioce span').click(function(){
		$(this).addClass('getColor_f').siblings().removeClass('getColor_f');
		getFinanceData();
	})

	// 选择可选配置
	$('.config_sel ul li').toggler(function(){
		$(this).addClass('getSel')
		idLists.push($(this).find("[name='optional_id']").val());
		optionalAmt(this,"add");
	},function(){
		$(this).removeClass('getSel')
		idLists.remove($(this).find("[name='optional_id']").val());
		optionalAmt(this,"rm");
	})
	
	
	function optionalAmt(obj,opt){
		if(opt == "add"){
			amount = amount + parseInt($(obj).find("[name='amountNumber']").val());
		}else{
			amount = amount - parseInt($(obj).find("[name='amountNumber']").val());
		}
		$("[name=optional_amount]").text("￥ "+thousandBitSeparator(parseInt(amount)) );
		priceCalculation();
		getOtherPrice();
		// console.log($("#singleDateRange").val());
		time:{
				required: "请选择提车时间"
			}
	} 
	
	// 计算价格
	function priceCalculation(){
		var totalAmount = parseInt(amount) +  parseInt($("#carPrice").val())  +  parseInt($("#mustConfigurePrice").val()) + parseInt(insuranceAmount);
		var carPrice =  parseInt($("#carPrice").val());
		$("[name=price_calculation]").text("￥ "+thousandBitSeparator(parseInt(totalAmount)));
		var deposit = parseInt($("#deposit").val()) * carPrice /100;
		deposit = Math.round(deposit);
		$("[name=deposit]").text("￥ "+thousandBitSeparator(parseInt(deposit)));
		
	}

	
	$("#register_submit").click(function(){
		if($('#insurance1').hasClass('getSel')){
			InsuranceType=1;
		}else if($('#insurance2').hasClass('getSel')){
			InsuranceType=2;
		}else{
			$('.boom').show();
			$('#InsuranceErrors').show();
		 	return false;
		}
		
		$("#register_form").submit();
	});
	


	// 表单验证
	$("#register_form").validate({
		
	
		rules:{
			address: {
				required: true
			},
			time:{
				required: true
			},
			name:{
				required: true,
				isAddname:true
			},
			mobile:{
				required: true,
                isMobile: true
			},
			address02:{
				required: true,
				isAddress:true
			}
		},
		messages:{
			address: {
				required: "请选择提车地点"
			},
			time:{
				required: "请选择提车时间"
			},
			name:{
				required: "请输入您的姓名",
				isAddname:"名称中不能含有特殊字符"
			},
			mobile:{
				required: "请输入您的手机号码",
                isMobile: "请输入有效的手机号码"
			},
			address02:{
				required: "请输入配送地址",
				isAddress:"地址中不能含有特殊字符"
			}
		},
		errorClass: "error",
        validClass: "valid",
        errorElement: "span",
        submitHandler: function (form) {
        	if(id==""){
        		$('.boom').show();
        		$('#DistributionErrors').show();
        		return false;
        	}else if(id=="0"){
        		var sysConfigId =$("#address  option:selected").attr("id");	
    		    $("#sysConfigId").val(sysConfigId);
    		    $("#extractionTime").val($("#singleDateRange").val());
        		// console.log($("#singleDateRange").val())
        	}else if(id=="1"){
        		$("#extractionAddress").val($("#distribution_address").val());
        		$("#extractionTime").val($("#distribution_time").val());
        		
        	}
        	$("#deliveryMode").val(id);
			$("#optionalConfigure").val(idLists);
			$("#insuranceType").val(InsuranceType);
			$("#financeType").val($(".getback").attr("name"));
			$("#term").val($(".getColor_f").html().replace("年", ""));
      	     form.submit();   // 提交表单
          
        }
	})
	// 点击确认，提示框隐藏
	$('.delBtn').click(function(){
		$('.boom').hide();
		$('#DistributionErrors').hide();
		$('#InsuranceErrors').hide();
	})
})

$(window).scroll(function(){
	var wid = ($(window).width() - 1100)/2;
	getScrollTop();
	if(scrollTop > 226){
		 $('.car_config_rt').css({
			 'position':'fixed',
			 'top': '0',
			 'right':wid,
			 'marginTop':'0px'
		 })
	}else{
		$('.car_config_rt').css({
			 'position':'static',
			 'float': 'right',
			 'marginTop':'40px'
		 })
	}
})

function getScrollTop()
{
    if(document.documentElement&&document.documentElement.scrollTop)
    {
        scrollTop=document.documentElement.scrollTop;
    }
    else if(document.body)
    {
        scrollTop=document.body.scrollTop;
    }
    return scrollTop;
}

function getFinanceData(){
	$.ajax({
		url : g_requestContextPath + "/h/l/products/carFinanceCalculate",
		dataType : "json",
		type : "post",
		async : true,
		data : {
			amount : financePrice,
			term : $(".getColor_f").html().replace("年", "")
		},
		success : function(data) {
			$("#MonPay").html("￥ " + $.formatMoney(data.MonPay) + "/月");
			if(data.outRange){
				$("#li_firstMonPay").hide();
				$("#bzj").html("￥ " + $.formatMoney(data.bzjPay.toFixed(2)));
				$("#sxf").html("￥ " + $.formatMoney(data.sxfPay.toFixed(2)));
			}else{
				$("#li_bzj").hide();
				$("#li_sxf").hide();
				$("#firstMonPay").html("￥ " + $.formatMoney(data.firstMonPay.toFixed(2)));
			}
			getOtherPrice();
		}
	});
}

function getOtherPrice(){
	$("#price_must").html("￥ " + $.formatMoney(parseFloat(mustPrice) + parseFloat(insuranceAmount)));
	$("#otherPrice").html("￥ " + $.formatMoney((parseFloat(mustPrice) + parseFloat(amount) + parseFloat(insuranceAmount)).toFixed(2)));
}

