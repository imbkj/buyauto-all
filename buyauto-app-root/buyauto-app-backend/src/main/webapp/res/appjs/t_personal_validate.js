function checkSubmit(){
	var flag=0;
	var fieldsToCheck = $("#form_user_role").find(':input').filter('[required=required], .required, .optional').not('[disabled=disabled]');
	fieldsToCheck.each(function(){
		if($(this).val()==""){
			new PNotify({
				title : '提示消息',
				text : $(this).parent('div').prev().html()+'不能为空',
				type : 'error',
				styling : 'bootstrap3'
			});
			flag=1;
			return false;
		}
	});
	if(flag==1){
		return false;
	}



	if($("#month").val()==""){
		new PNotify({
			title : '提示消息',
			text : '考核销售月数不能为空',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if($("#totalSales").val()=="" && $("#monthlySales").val()=="" &&  $("#totalAmount").val()=="" && $("#monthlyAmount").val()=="" ){
		new PNotify({
			title : '提示消息',
			text : '考核条件至少有一项必填',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}
	
	if($("#totalSales").val()=="0" && $("#monthlySales").val()=="0" &&  $("#totalAmount").val()=="0" && $("#monthlyAmount").val()=="0" ){
		new PNotify({
			title : '提示消息',
			text : '考核条件至少有一项不为0',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}
	
	if($("#month").val()=="0"){
		new PNotify({
			title : '提示消息',
			text : '考核销售月数不能为0',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}
	
	

	return true;
}