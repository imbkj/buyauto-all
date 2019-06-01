  var imageUploads = (function(){
            var _this;

            var imageUploads = function(obj,isMultitude){
            	 _this = this;
            	this.obj = obj;
                this.isMultitude = isMultitude;
            }
            imageUploads.prototype.getNewFile = function(){
            	return this.saveData;
            }
            
              imageUploads.prototype.getOldFile = function(){
            	return this.reData;
            }
            

            imageUploads.prototype.setInitJson = function(djson){
            	/*多图上传需要提供djson.preid*/
                this.preList = new Array();
                this.preConfigList = new Array();
                this.previewJson = new Array();
                this.reData = eval(djson);
                this.saveData = new Array();
                if (this.reData != null) {
                  
                    for (var i = 0; i < this.reData.length; i++) {
                        var array_element = this.reData[i];
                        // 此处指针对.txt判断，其余自行添加
                        if (array_element.name.indexOf("txt") > 0) {
                            // 非图片类型的展示
                            this.preList[i] = "<div class='file-preview-other-frame'><div class='file-preview-other'><span class='file-icon-4x'><i class='fa fa-file-text-o text-info'></i></span></div></div>"
                        }else if (array_element.name.indexOf("pdf") > 0) {
                            // 非图片类型的展示
                            this.preList[i] = "<div class='file-preview-other-frame'><div class='file-preview-other'><span class='file-icon-4x'><i class='fa fa-file-pdf-o text-danger'></i></span></div></div>"
                        } else {
                            // 图片类型
                           	this.preList[i] = "<img src=\"" + array_element.filePath + array_element.name + "\" class=\"file-preview-image\">";
                            //this.preList[i] = "<img src=\"http://avatar.csdn.net/C/7/A/1_u013694979.jpg\" class=\"file-preview-image\">";
                        }
                    }

                    this.previewJson = this.preList;

                    // 与上面 预览图片json数据组 对应的config数据
                    for (var i = 0; i < this.reData.length; i++) {
                        var array_element = this.reData[i];
                        var tjson = {
                            caption: array_element.name, // 展示的文件名
                            width: '120px',
                            url: g_requestContextPath+'/deleteFile', // 删除url
                            key: array_element.preId, // 删除是Ajax向后台传递的参数
                            extra: {}
                        };
                        this.preConfigList[i] = tjson;
                    }
                }else{
                        this.preConfigList = new Array();
               			this.previewJson = new Array();
                }
            }
			
            imageUploads.prototype.init = function(type,djson){  
           	 	console.log(type)
           		var allowedPreviewTypesValue =null;
                var allowedFileTypesValue =null;
                var allowedFileExtensionsValue =null;
           	 	var previewFileIconValue = null;
            	this.setInitJson(djson);
            	$(this.obj).fileinput('destroy');  
             	 var that = this;
                // 具体参数自行查询
               $(this.obj).attr("accept", "image/gif,image/png,image/jpeg")
             if(type=="image"){
                allowedPreviewTypesValue = ['image'];
                allowedFileTypesValue = ['image'];
                allowedFileExtensionsValue =  ['jpg', 'png'];
                previewFileIconValue = '<i class="fa fa-file"></i>',
                $(this.obj).attr("accept", "image/gif,image/png,image/jpeg")
             }else if(type=="pdf"){
             	allowedPreviewTypesValue = [''];
                allowedFileTypesValue = [''];
                allowedFileExtensionsValue =  ['pdf'];
                previewFileIconValue = '<i class="fa fa-file-pdf-o text-danger"></i>',
                $(this.obj).attr("accept", "application/pdf")
                
             }
               $(this.obj).fileinput({
                    autoReplace:true,
                    language: 'zh',
                    showClose:false,
                    uploadUrl: g_requestContextPath+'/uploadFile',
                    uploadAsync: true,
                    showCaption: true,
                    showUpload: false,//是否显示上传按钮
                    showRemove: false,//是否显示删除按钮
                    showCaption: true,//是否显示输入框                    
                    showPreview: true,
                    showCancel: true,
                    dropZoneEnabled: false,
                    maxFileCount: 10,
                    maxFileSize:5000,
                    initialPreviewShowDelete: true,
                    msgFilesTooMany: "选择上传的文件数量 超过允许的最大数值！",
                    initialPreview: that.previewJson,
                    previewFileIcon: previewFileIconValue,
                    allowedPreviewTypes: allowedPreviewTypesValue,
                    allowedFileTypes: allowedPreviewTypesValue,
                    allowedFileExtensions: allowedFileExtensionsValue,
                    uploadExtraData: function (previewId, index) {   //额外参数的关键点
                        var obj = {};
                        obj.pre = previewId;
                        return obj;
	                    },
                    previewFileIconSettings: {
                      	'docx': '<i class="fa fa-file-word-o text-primary"></i>',
				        'xlsx': '<i class="fa fa-file-excel-o text-success"></i>',
				        'pptx': '<i class="fa fa-file-powerpoint-o text-danger"></i>',
				        'doc': '<i class="fa fa-file-word-o text-primary"></i>',
				        'xls': '<i class="fa fa-file-excel-o text-success"></i>',
				        'ppt': '<i class="fa fa-file-powerpoint-o text-danger"></i>',
				        'pdf': '<i class="fa fa-file-pdf-o text-danger"></i>',
                    },
                    initialPreviewConfig: that.preConfigList
                }).on("fileuploaded", function (event, outData) {
            
                	var obj = {filePath:outData.response.filePath,name:outData.response.name,preId:outData.response.preid};
                	if(that.saveData.indexOfName(obj.name) == 1){
                		
                		if(!that.isMultitude){
                			that.saveData= new Array();
                			$(that.obj).parents(".input-group").hide();
                		}
                		that.saveData.push(obj);
                	}

                	
                }).on('filesuccessremove', function (event, id) {

    	
					for(var i=0; i<that.saveData.length;i++){
                		if(that.saveData[i].preId == id){
                		 	that.saveData.remove(that.saveData[i]);
                		 }
                	}
                	
               		if(that.saveData.length == 0){
               			that.init(type,that.reData);
               		}
         
                }).on("filedeleted", function (event, id) {
           
                	for(var i=0; i<that.reData.length;i++){
                		if(that.reData[i].preId == id){
                		 	that.reData.remove(that.reData[i]);
                		 }
                	}
                	

                }).on("filebatchselected", function(event, files) {
		            $(this).fileinput("upload");
		        });

            }
            return imageUploads;
        })();