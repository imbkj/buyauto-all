(function($, w){

	var table = null;
	var upload = null;
	var uploadFigure = null;
	var ue = null;

	/*加载会员中心的数据 */
	$(function(){

		userSubmitUrl = "create";
		//初始化数据
		tableDataLoad();
		/*初始化条件查询时间框*/
		var tp = new timePlugins("crstartTime","crendTime","reportrange1");
		tp.init();
		/*创建时间日期控件*/
		var tp1 = new timePlugins("createTime", "", "reportrange2");
	    tp1.init(true);
		//新闻首图图片上传
        upload = new imageUploads($("#newsPic"),false);
        console.log(UE);
		//初始化百度富文本编辑控件
		ue = UE.getEditor('comContent',{
			//maximumWords : 50000,//最大字符数
			autoHeightEnabled: false //自动适应高度
	        ,initialFrameHeight : 360//编辑器高度，默认320
			});

	});

	// 新增新闻
    $("#btn_create_news").click(function(){
    	clearForm();
	    upload.init("image");
    	var dateTime = new Date().format('yyyy/MM/dd hh:mm:ss');
		$("#createTime").val(dateTime);
		$("#reportrange2").find("span").text(dateTime);
	  	$(".uas-modal-lg").modal('show');
    	
    });
		
   //点击修改新闻

   w.showUpdatemodal = function(id){
	   //修改
	   userSubmitUrl = "edit";
	
	   $.get(g_requestContextPath + "/h/l/cms/news/getNews", {
               "id": id
           }, function(data) {
        	   if(data!=null){
        		   $("#news_id").val(id);         	  		  
    	  		   $('input:radio[name="type"][value="'+data.type+'"]').prop('checked',true);
    	  		   if(1 == data.type){
        	  		   	$("#type1").addClass("active");
        	  		   	$("#type2").removeClass("active");
    	  		   }else{
    	  		   		$("#type2").addClass("active");
        	  		   	$("#type1").removeClass("active");
    	  		   }
				   $("#title").val(data.title);
            	   $("#author").val(data.author);
            	   $("#introduction").val(data.introduction);
            	   uploadFigure = new Array();
            	   //编辑图片回显
            	   if(data.pic == ""){
            		   upload.init("image");
            		   uploadFigure.push();
            	   }else{
            		   uploadFigure.push({filePath:g_imageShowPath,name:data.pic});
        	   		   upload.init("image",uploadFigure);
            	   }
            	 
        	       $("#ImgPic").val(data.pic);
				   ue.setContent(data.content);
				   
				   $('input:radio[name="status"][value="'+data.status+'"]').prop('checked',true);
    	  		   if(1 == data.status){
        	  		   	$("#status1").addClass("active");
        	  		   	$("#status2").removeClass("active");
    	  		   }else{
    	  		   		$("#status2").addClass("active");
        	  		   	$("#status1").removeClass("active");
    	  		   }
				   $("#orderNum").val(data.orderNum);
				   $('input:radio[name="isTop"][value="'+data.isTop+'"]').prop('checked',true);
    	  		   if(1 == data.isTop){
        	  		   	$("#isTop1").addClass("active");
        	  		   	$("#isTop0").removeClass("active");
    	  		   }else{
    	  		   		$("#isTop0").addClass("active");
        	  		   	$("#isTop1").removeClass("active");
    	  		   }
				   
				   $('input:radio[name="isHot"][value="'+data.isHot+'"]').prop('checked',true);
    	  		   if(1 == data.isHot){
        	  		   	$("#isHot1").addClass("active");
        	  		   	$("#isHot0").removeClass("active");
    	  		   }else{
    	  		   		$("#isHot0").addClass("active");
        	  		   	$("#isHot1").removeClass("active");
    	  		   }			   
				   
    	           if(null !=data.createTime){
    	        	   	var dataCreateTime=new Date(data.createTime).format('yyyy/MM/dd hh:mm:ss');
    	           		$("#createTime").val(dataCreateTime);
						$("#reportrange2").find("span").text(dataCreateTime);
            	   }
    	           $(".uas-modal-lg").modal('show');	   
        	   }
        	   
           });
   };

	//添加新闻or编辑
    $("#btn_cms_submit").click(function(){
    	//删除新闻首图操作
    	$("#ImgPic").val("");
    	//图片回显
		if(upload.getNewFile().length == 0){
			if(upload.getOldFile() ==null){
				$("#ImgPic").val();
			}else if(upload.getOldFile().length != 0){
				$("#ImgPic").val(upload.getOldFile()[0].name);
			}else{
				$("#ImgPic").val();
			}	
		}else{
			$("#ImgPic").val(upload.getNewFile()[0].name);
		}
		//验证
		if(!validateSubmit(upload,ue)){
			return false;
		}
		
   		$.ajax({
    		url:g_requestContextPath + "/h/l/cms/news/"+userSubmitUrl,
    		type:'post',
    		dataType:'json',
    		data:$("#form_cms_news").serialize(),
    		success:function(data){
    			table.fnDestroy();
	               	tableDataLoad();
	               	if(1 == data){
	               		new PNotify({
	                      title: '提示消息',
	                      text: '保存成功,请及时发布新闻',
	                      type: 'success',
	                      styling: 'bootstrap3'
	              		});
	               	}else if(3 == data){
	               		new PNotify({
	                      title: '提示消息',
	                      text: '该新闻标题已经存在',
	                      type: 'error',
	                      styling: 'bootstrap3'
	              		});
              			return false;
	               	}else if(101 == data){
		                new PNotify({
	                        title: '提示消息',
	                        text: "公告热点最多设置6条",
	                        type: 'wanrning',
	                        styling: 'bootstrap3'
	                    });
		                return false;
                }else if(102 == data){
	                new PNotify({
                        title: '提示消息',
                        text: "资讯热点最多设置6条",
                        type: 'wanrning',
                        styling: 'bootstrap3'
                    });
	                return false;
                }else{
 	               	new PNotify({
                      title: '提示消息',
                      text: "保存失败",
                      type: 'error',
                      styling: 'bootstrap3'
            		});
               	}
               	$(".uas-modal-lg").modal('hide');
    		}
            	
    	});
		return false;
    });

	//删除新闻
    w.deleteNews = function(id){
   	   $.confirm({
		    title: '确认删除？',
		    content: '确认删除该新闻 '+ id+" ?",
		    buttons: {
	        	confirm: {
		            text: '确认',
		            action: function(){
		              	$.get(g_requestContextPath + "/h/l/cms/news/del", {
			                  "id": id
			              }, function(data) {
			                 if(1 == data){
			                	new PNotify({
		                          title: '提示消息',
		                          text: '该新闻删除成功',
		                          type: 'success',
		                          styling: 'bootstrap3'
		                        });
		                  	 }else{
			                    new PNotify({
			                        title: '提示消息',
			                        text: "删除新闻失败",
			                        type: 'error',
			                        styling: 'bootstrap3'
			                    });
			                 }
			                 
			                 table.fnDestroy();
	   						 tableDataLoad();
			            });
		            }
		        },
		        cancel: {
		           	text: '取消',
		            action: function(){
		            }
		        }
		    }
		});
		return false;
    };

   //修改新闻状态
   w.setNewsStatus = function(id,chgWhat,what){
	   $.post(g_requestContextPath + "/h/l/cms/news/newsChg", {
            "id": id,"chgWhat":chgWhat,"what":what
        }, function(data) {
            if(1 == data){
            	table.fnDestroy();
	               	tableDataLoad();
            	new PNotify({
                      title: '提示消息',
                      text: '修改状态成功',
                      type: 'success',
                      styling: 'bootstrap3'
                  });
            	return false;
            }else if(101 == data){
                new PNotify({
                    title: '提示消息',
                    text: "公告热点最多设置6条",
                    type: 'wanrning',
                    styling: 'bootstrap3'
                });
            }else if(102 == data){
                new PNotify({
                    title: '提示消息',
                    text: "资讯热点最多设置6条",
                    type: 'wanrning',
                    styling: 'bootstrap3'
                });
            }else{
                new PNotify({
                    title: '提示消息',
                    text: "修改新闻状态失败",
                    type: 'error',
                    styling: 'bootstrap3'
                });
            }
            
        });
   }
	       
   //条件查询
	$("#transfer_frmSearch").click(function() {
		var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");  
        if($("#searTitle").val() != "" && $("#searTitle").val() != null){
            if(pattern.test($("#searTitle").val())){  
                new PNotify({
					title : '提示消息',
					text : '非法字符，请输入正确的标题！',
					type : 'wanrning',
					styling : 'bootstrap3'
				});
                return false;  
            }  
        } 
		table.fnDestroy();
		tableDataLoad();
		new PNotify({
			title : '提示消息',
			text : '查询成功',
			type : 'success',
			styling : 'bootstrap3'
		});
		return false;
	});
		
   // 清理表单
   function clearForm(){
	   userSubmitUrl = "create";
	   $("#id").val('');
	   $("#title").val('');
	   $("#ImgPic").val('');   
	   $("#author").val('');     	   
	   $("#position").val('');
	   $("#introduction").val('');
	   $("#content").val('');
	   //初始化富文本编辑器
	   ue.setContent('');
	   $("#type1").addClass("active");
       $("#type2").removeClass("active");
	   $("#orderNum").val('');
       $("#status1").addClass("active");
	   $("#status2").removeClass("active");
   	   $("#isTop1").addClass("active");
	   $("#isTop0").removeClass("active");
   	   $("#isHot1").addClass("active");
	   $("#isHot0").removeClass("active");
	   $("#createTime").val('');
   }


   function tableDataLoad(){
    	//数据加载
    	table = $('#cms_news').dataTable({
            	bProcessing: true,
            	paging: true,
            	pageLength: 20,
            	iDisplayLength: 20,
            	bInfo: true, // 页脚信息
            	bSort: false, // 排序功能
            	bAutoWidth: true, // 自动宽度
            	bStateSave: false, // 把分页信息保存cookie
            	sPaginationType: "full_numbers",
            	bPaginate: true, // 是否开启分页
            	bLengthChange: false, //改变每页显示数据数量
            	searching: false, // 隐藏搜索
            	serverSide: true,
            	//sDom: '<"top">rt<"bottom"fip><"clear">',
            	ajax:{
            		url: g_requestContextPath+'/h/l/cms/news',
		            type: 'post',
		            async: false,
		            dataType: 'json',
		            dataSrc: 'rows',
		            data: function(aoData){
		            	var param = {};
		            	param.page = aoData.start>0 ? (aoData.start/aoData.length +1) : 1 ;
		            	param.rows = 20;//aoData.length;
		            	var formData = $("#news_search").serializeArray();
						formData.forEach(function(e) {
							if (e.value) {
								param[e.name] = e.value;
							}
						});
		            	return param;
		            }
		            
            	},
            	aoColumnDefs: [ { "bSortable": false, "aTargets": [ 0 ] }],
            	columns:[
					{data: function(d){
						if (d.type==1) {
							return "公告";
						}else{
							return "资讯";
						}
					}},
					{data: function(d){return d.title;}},
					{data: function(d){
						if(d.status!=0){
							return new Date(d.onlinetime).format("yyyy-MM-dd hh:mm:ss");
						}else{
							return "未上线";
						}
					}},
					{data: function(d){
						if (d.status==0) {
							return "未发布";
						}else if(d.status==1){
							return "已发布";
						}else{
							return "禁用";
						}
					}},
					{data: function(d){return new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");}},
					{data: function(d){
						if(d.orderNum==null){
							return "未排序";
						}else{
							return d.orderNum;
						}
					}},
					{data: function(d){return d.operName;}},
					{data: function(d){
						var s = '<div class="table-o-tooltip">';
							//新闻状态
							if(d.status == 0){
								//编辑
								s += '<a href="javascript:showUpdatemodal(\''+d.newsId+'\');" class="btn-link">编辑</a>';
								//发布
								s += '<a href="javascript:setNewsStatus(\''+d.newsId+'\',1,1);" class="btn-link">发布</a>';
								//删除
								//s += '<a href="javascript:deleteNews(\''+d.newsId+'\');" class="btn-link">删除</a>';
							}else if(d.status == 1){
								//编辑
								s += '<a href="javascript:showUpdatemodal(\''+d.newsId+'\');" class="btn-link">编辑</a>';
								//是否热点
								if(d.isHot == 0){
									s += '<a href="javascript:setNewsStatus(\''+d.newsId+'\',0,1);" class="btn-link">热点</a>';
								}else{ 
									s += '<a href="javascript:setNewsStatus(\''+d.newsId+'\',0,0);" class="btn-link">取消热点</a>';								
								}
								//是否置顶
								if(d.isTop == 0){
									s += '<a href="javascript:setNewsStatus(\''+d.newsId+'\',2,1);" class="btn-link">置顶</a>';
								}else{ 
									s += '<a href="javascript:setNewsStatus(\''+d.newsId+'\',2,0);" class="btn-link">取消置顶</a>';								
								}
								//下线
								s += '<a href="javascript:setNewsStatus(\''+d.newsId+'\',1,0);" class="btn-link">下线</a>';	
															
							}
							s += '</div>';
						return s;
					}}
            	],
            	
            	language: {
            		sProcessing: "数据加载中...",
					paginate: {//分页的样式文本内容。
						previous: "上一页",
						next: "下一页",
						first: "第一页",
						last: "最后一页"
					},
					zeroRecords: "没有内容",
					info: "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，共_MAX_ 条 ",//左下角的信息显示，大写的词为关键字。
                	infoEmpty: "0条记录",//筛选为空时左下角的显示。
                	infoFiltered: ""//筛选之后的左下角筛选提示(另一个是分页信息显示，在上面的info中已经设置，所以可以不显示)，
            	}
        });

    }

})(jQuery, window)