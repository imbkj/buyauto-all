var type = {
	"image" : "*.jpg;*.jpeg;*.png;*.gif;*.bmp",
	"pdf" : "*.pdf"
};
var imageSize = {
	"image" : 10*1024*1024,
	"pdf" : 100*1024*1024
};

fileDelete = function(imgObj, resultId) {
	var fname = $(imgObj).attr("path");
	var p = $("#" + resultId).val();
	var fileNames = p.split(",");
	var newFileNames = [];
	$.each(fileNames, function(i, fn) {
		if (fn != fname) {
			newFileNames.push(fn);
		}
	});
	$("#" + resultId).val(newFileNames.join(","));
	$(imgObj).remove()

}

cleanImgContetn = function(name) {
	var fileEId = name + "FileId";
	var resultId = name + "Id";
	var contentId = name + "ContentId";
	var imgs = $("#" + contentId + " > img");
	$.each(imgs, function(i, imgObj) {
		fileDelete(imgObj, resultId);
	});
}

addImgToContent = function(fnames, name, isMul) {
	var fileEId = name + "FileId";
	var resultId = name + "Id";
	var contentId = name + "ContentId";
	if (!!fnames) {
		var fileNames = fnames.split(",");
		$.each(fileNames, function(i, fn) {
			if (!!fn) {
				var oc = "onclick=fileDelete(this,'" + resultId + "')";
				$("#" + contentId).append(
						"<img  class='show-img' " + oc + " src='" + imgShowPath
								+ fn + "' path='" + fn + "'/>");
			}
		});
	}
}



 var maxsize =0;
var errMsg = "上传的附件文件不能超过100M！";  
var tipMsg = "您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过100M，建议使用IE、FireFox、Chrome浏览器。";  
var  browserCfg = {};  
var ua = window.navigator.userAgent;  
if (ua.indexOf("MSIE")>=1){  
    browserCfg.ie = true;  
}else if(ua.indexOf("Firefox")>=1){  
    browserCfg.firefox = true;  
}else if(ua.indexOf("Chrome")>=1){  
    browserCfg.chrome = true;  
}  
function checkfile(id,isMustFile,fileType){  	
	maxsize  = imageSize[fileType];
		
            try{  
                var obj_file = document.getElementById(id);  
             	
             	if(isMustFile){
	                if(obj_file.value==""){  
	                    return "请先选择上传文件";  
	                }  
                }
                if(!isPicture(obj_file.value,fileType)){
                	if(fileType == "image"){
	      			   return "文件类型不合法,只能是jpg、gif、png类型！";
		            }else{
		          	  return "文件类型不合法,只能是pdf类型！";
		            }
                	
                }
                var filesize = 0;  
                if(browserCfg.firefox || browserCfg.chrome ){  
                    filesize = obj_file.files[0].size;  
                }else if(browserCfg.ie){  
                    var obj_img = document.getElementById(id);  
                    obj_img.dynsrc=obj_file.value;  
                    filesize = obj_img.fileSize;  
                }else{  
                
                    alert(tipMsg);  
                return;  
                }  
                if(filesize==-1){  
                    return tipMsg;  
                }else if(filesize>maxsize){  
                    return errMsg;  
                }else{  
                    return "success";  
                }  
            }catch(e){  
                alert(e);  
        }  
}  
function isPicture(fileName,fileType1) {
    if (fileName != null && fileName != "") {
        //lastIndexOf如果没有搜索到则返回为-1
        if (fileName.lastIndexOf(".") != -1) {
            var fileType = (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)).toLowerCase();
            var suppotFile = new Array();
            if(fileType1 == "image"){
	            suppotFile[0] = "jpg";
	            suppotFile[1] = "gif";
	            suppotFile[3] = "png";
            }else{
                suppotFile[0] = "pdf";
            }
            for (var i = 0; i < suppotFile.length; i++) {
                if (suppotFile[i] == fileType) {
                    return true;
                } else {
                    continue;
                }
            }
            //alert("文件类型不合法,只能是jpg、gif、bmp、png、jpeg类型！");
            return false;
        } else {
            //alert("文件类型不合法,只能是 jpg、gif、bmp、png、jpeg 类型！");
            return false;
        }
    }
}


fileUpload = function(name, isMul, callfunc,isMustFile,fileType) {
	var isMustFile;

	var fileEId = name + "FileId";
	var resultId = name + "Id";
	var contentId = name + "ContentId";
	var msg = checkfile(fileEId,isMustFile,fileType);
	if(msg == "success"){
	
	$.ajaxFileUpload({
		url : g_requestContextPath + '/h/n/common/backendUpload',
		secureuri : false,
		fileElementId : fileEId,
		dataType : 'json',
		success : function(data, status) {
			var obj = eval('(' + data + ')');
			if (obj.msg) {
				alert(obj.msg);
			}
			if (obj.path) {
				var p = $("#" + resultId).val();
				if (isMul) {
					if (!!p) {
						p = p + "," + obj.path;
					} else {
						p = obj.path;
					}
				} else {
					p = obj.path;
					// cleanImgContetn(name);
					// 鍒犻櫎鍥剧墖
				}
				// var oc = (isMul ? "onclick=fileDelete(this,'" + resultId
				// + "')" : "");
				// $("#" + contentId).append("<img " + oc + " src='" +
				// imgShowPath + obj.path + "' path='" + obj.path + "'/>");
				// console.log(p);
				$("#" + resultId).val(p);

				if (callfunc != undefined && typeof (callfunc) == "function") {
					callfunc(p);
				}
			}
		}
	});
	
	$("#"+fileEId).replaceWith($("#"+fileEId).clone(true));
	}else{
		alert(msg);
	}
};