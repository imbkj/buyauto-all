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

	
	if ($('#scvalue').val().length>30) {
		new PNotify({
			title : '提示消息',
			text : '字段长度不能大于30字节',
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
	}

	return true;
}