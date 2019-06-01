var type = {
	"image" : "*.jpg;*.jpeg;*.png;*.gif;*.bmp",
	"pdf" : "*.pdf" ,
	"both" :  "*.jpg;*.jpeg;*.png;*.gif;*.bmp;*.pdf"
};
var imageSize = {
	"image" : "10MB",
	"pdf" : "50MB",
	"both" : "50MB"
};


var uploadCommon = (function() {
	function uploadCommon(o, p, v, t, isMulti, fileName,bindImagePath) {
		$(o).uploadify({
			'swf' : g_requestContextPath + '/res/js/uploadify/uploadify.swf',
			'uploader' : g_requestContextPath + '/h/n/common/upload',
			'wmode' : 'transparent',
			'multi' : isMulti,
			'auto' : true,
			'fileTypeDesc' : 'Select Files',
			'fileTypeExts' : type[t],
			'queueID' : p,
			'formData' : {
				'uploadFile' : '/' + fileName
			},
			'queueSizeLimit' : 5,
			'buttonText' : '',
			'hideButton' : false,
			'queueSizeLimit' : 100, // 允许同时上传文件数量
			'uploadLimit' : 1000, // 允许上传文件总数，指打开一次浏览器
			'fileSizeLimit' : imageSize[t], // 限制单个文件大小，限制IIS大小请到Web.Config修改
			'removeCompleted' : false,
			/*'onCancel': function(event,queueId,fileObj,data){
				$(v).val("");
			},*/
			'onUploadSuccess' : function(file, data, response) {
				console.log('isMulti:'+isMulti);
				if(isMulti){
					$(v).val($(v).val() + "," + data);
				}else{
					$(v).val(data);
				}
				console.log($(v).val());
				//console.log(g_imagePath+$(v).val());
				
				$(bindImagePath).attr("src" , g_imagePath+$(v).val());
				console.log($(bindImagePath).attr("src"));
			}
		});

	}
	return uploadCommon;
})();
