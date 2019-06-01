//控制排序1-100
function numCheck(){
	var num=$('#orderNum').val();
	if (num<1 || num>100) {
		$('#orderNum').val("1");
	}
}
/*新闻管理提交验证*/
function validateSubmit(upload,ue){
	var validateRes = true;
	
	var newsTitle = $('#title').val();
	var newsAutor = $('#author').val();
	var newsIntroduction = $('#introduction').val();
	
	if($.trim(newsTitle).length == 0){
		new PNotify({
			title : '提示消息',
			text : '请填写新闻标题',
			type : 'error',
			styling : 'bootstrap3'
		});
		$('#title').focus();
		validateRes = false;
	}
	
	/*if(validateRes && $.trim(newsAutor).length == 0){
		new PNotify({
			title : '提示消息',
			text : '请填写新闻作者',
			type : 'error',
			styling : 'bootstrap3'
		});
		$('#author').focus();
		validateRes = false;
	}*/
	
	if(validateRes && $.trim(newsIntroduction).length == 0){
		new PNotify({
			title : '提示消息',
			text : '请填写新闻简介',
			type : 'error',
			styling : 'bootstrap3'
		});
		$('#introduction').focus();
		validateRes = false;
	}
	
	//验证新闻首图
	var	fileObj = upload.getNewFile();
	var fileObjOld = upload.getOldFile();
	var fileLength = parseInt(fileObj.length);
	if(fileObjOld != null){
		 fileLength += parseInt(fileObjOld.length)
	}
	if (validateRes && fileLength == 0) {
		new PNotify({
			title : '提示消息',
			text : '请先选择新闻首图',
			type : 'error',
			styling : 'bootstrap3'
		});
		validateRes = false;
	}
	
	//验证富文本编辑器
	var contentText = ue.hasContents();
	if(validateRes && !contentText){
		new PNotify({
          title: '提示消息',
          text: '请输入新闻正文',
          type: 'error',
          styling: 'bootstrap3'
  		});
		ue.focus();
		validateRes = false;
	}
	
	return validateRes;
}