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



	if($("#title").val().length>30){
		new PNotify({
			title : '提示消息',
			text : '产品名称长度不能大于30',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}


	if($("#country").val().length>30){
		new PNotify({
			title : '提示消息',
			text : '原产地名称长度不能大于30',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if($("#deposit").val()>100){
		new PNotify({
			title : '提示消息',
			text : '定金比例不能大于100',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}


	if($("#carModel").val().length>50){
		new PNotify({
			title : '提示消息',
			text : '车辆型号长度不能大于50',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}


	if($("#outColor").val().length>30){
		new PNotify({
			title : '提示消息',
			text : '车体外观颜色长度不能大于30',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}


	if($("#inColor").val().length>30){
		new PNotify({
			title : '提示消息',
			text : '车身内部颜色长度不能大于30',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}


	if($("#engine").val().length>30){
		new PNotify({
			title : '提示消息',
			text : '发动机长度不能大于30',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if($("#gearbox").val().length>30){
		new PNotify({
			title : '提示消息',
			text : '变速箱长度不能大于30',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}


	if($("#basicConfigure").val().length>200){
		new PNotify({
			title : '提示消息',
			text : '基础配置长度不能大于200',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if($("#mustConfigure").val().length>200){
		new PNotify({
			title : '提示消息',
			text : '必选配置长度不能大于200',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}



	if($("#carPrice").val().length>10){
		new PNotify({
			title : '提示消息',
			text : '商品价格长度不能大于10',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}


		if($("#mustConfigurePrice").val().length>10){
		new PNotify({
			title : '提示消息',
			text : '必选总价长度不能大于10',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if($("#stock").val().length>10){
		new PNotify({
			title : '提示消息',
			text : '库存长度不能大于10',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}



	if($("#brandId").val() == "" || $("#brandId").val() ==null){
		new PNotify({
			title : '提示消息',
			text : '车辆品牌不能为空',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}




	if($("#indexImageH").val() == "" || $("#indexImageH").val() ==null || $("#indexImageH").val() =="[]"){
		new PNotify({
			title : '提示消息',
			text : '封面图不能为空',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if($("#shrinkImageH").val() == "" || $("#shrinkImageH").val() ==null || $("#shrinkImageH").val() =="[]"){
		new PNotify({
			title : '提示消息',
			text : '缩略图不能为空',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if($("#recommendImageH").val() == "" || $("#recommendImageH").val() ==null || $("#recommendImageH").val() =="[]"){
		new PNotify({
			title : '提示消息',
			text : '推荐图不能为空',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if( ($("#detailedImageH").val() == "" || $("#detailedImageH").val() ==null || $("#detailedImageH").val() =="[]")  && ($("#detailedImageOld").val() == "" || $("#detailedImageOld").val() ==null || $("#detailedImageOld").val() =="[]") ){
		new PNotify({
			title : '提示消息',
			text : '展示详图不能为空',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if (!ue.hasContents()) {
		new PNotify({
			title : '提示消息',
			text : '请输入正文内容',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}
	/*if ($('#scvalue').val().length>100) {
		new PNotify({
			title : '提示消息',
			text : '提车地址长度不能大于100字节',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}

	if ($('#scremark').val().length>100) {
		new PNotify({
			title : '提示消息',
			text : '备注长度不能大于100字节',
			type : 'error',
			styling : 'bootstrap3'
		});
		return false;
	}*/

	return true;
}