(function($, w){
	/*加载会员中心的数据 */
	$(function(){
		/*初始化条件查询时间框*/
		var tp = new timePlugins("crstartTime","crendTime","reportrange1");
		tp.init();
		/*创建时间日期控件*/
		var tp1 = new timePlugins("createTime", "", "reportrange2");
	    tp1.init(true);
	    
	    //var upload = new imageUploads($("#bannerImg"),successfunc,failfunc);//初始化file表单框
	    //初始化文件上传控件
	    var upload = new imageUploads($("#bannerImg"),false);//初始化file表单框//首页

		//模块
		var modelTypes = {
				0:"首页滚动",
				1:"新闻页",
				2:"车辆详情页"
		}
		/*成功回调方法*/
	    var	successfunc = function(v,k){
            var filePath = k.response.filePath;//路径
            var fileName = k.response.name;//文件名
            console.log(k.response);
            $("#bannerImgPath").val(fileName);//图片路径赋值
	    }
		/*删除回调方法*/
	    var	failfunc = function(v,k){
	    	$("#bannerImgPath").val("");//图片路径清空
	    }
	  
	    
		var table = null;
		bannerSubmitUrl = "create";
		var tableDataLoad = function(){
	      //获取数据
	    table =$('#cms_banner_list').dataTable({
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
	            		url: g_requestContextPath+'/h/l/cms/banner',
			            type: 'get',
			            async: false,
			            dataType: 'json',
			            dataSrc: 'rows',
			            data: function(aoData){
			            	var param = {};
			            	param.page = aoData.start>0 ? (aoData.start/aoData.length +1) : 1 ;
			            	param.rows = 20;//aoData.length;
			            	var formData = $("#mem_search").serializeArray();
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
						{data: function(d){return d.title;}},
						{data: function(d){return new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");}},
						{data: function(d){return d.typeName;}},
						{data: function(d){return d.operName;}},
						{data: function(d){ 
							if(d.updateTime != null){
								return new Date(d.updateTime).format("yyyy-MM-dd hh:mm:ss");
							}
							return "";
						}},
						{data: function(d){
							if (d.status == 1) {
		                        return "已上线";
		                    } else {
		                        return "下线";
		                    }
						}},
						{data: function(d){return d.orderNum}},
						{data: function(d){
							var s = '<div class="table-o-tooltip">';
								if(d.status == 1){
									s += '<a href="javascript:bannerDown(\''+d.id+'\');" class="btn-link">下线</a>';
								}else{
									s += '<a href="javascript:bannerUp(\''+d.id+'\');" class="btn-link">上线</a>';								
								}
								if(d.status != 1){
									s += '<a href="javascript:showUpdatemodal(\''+d.id+'\');" class="btn-link">编辑</a>';
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
	        
	        // 初始化数据
	        tableDataLoad();
	        //下线
	        w.bannerDown = function(id){
		    	   $.get(g_requestContextPath + "/h/l/cms/bannerDown", {
		                "id": id
		            }, function(data) {
		                
		                if("SUCCESS" == data){
		                	table.fnDestroy();
		 	               	tableDataLoad();
		 	               	
		                	new PNotify({
		                          title: '提示消息',
		                          text: '设置成功',
		                          type: 'success',
		                          styling: 'bootstrap3'
		                      });
		                	
		                	return false;
		                }
		                
		                new PNotify({
	                        title: '提示消息',
	                        text: data,
	                        type: 'error',
	                        styling: 'bootstrap3'
	                    });
		                
		            });
		       }
		     //上线
		     w.bannerUp = function(id){
		    	  $.get(g_requestContextPath + "/h/l/cms/bannerUp", {
	                  "id": id
	              }, function(data) {
	                  
	                 if("SUCCESS" == data){
	                	  table.fnDestroy();
		 	               	tableDataLoad();
		                	new PNotify({
		                          title: '提示消息',
		                          text: '设置成功',
		                          type: 'success',
		                          styling: 'bootstrap3'
		                      });
		                	return false;
	                  }
	                 console.log("ddd",data);
	                 if("101" == data){
	                	new PNotify({
                          title: '提示消息',
                          text: '上线条数不能超过10条',
                          type: 'wanrning',
                          styling: 'bootstrap3'
	                    });
		                return false;
	                  }
	                  new PNotify({
	                      title: '提示消息',
	                      text: data,
	                      type: 'error',
	                      styling: 'bootstrap3'
	                  });
	              });
		     }
		     //删除
		     w.delBannerByID = function(id){
		    	 $.get(g_requestContextPath + "/h/l/cms/delBannerById", {
	                  "id": id
	              }, function(data) {
	                  
	                 if("SUCCESS" == data){
	                	  table.fnDestroy();
		 	               	tableDataLoad();
		 	               	
		                	new PNotify({
		                          title: '提示消息',
		                          text: '删除成功',
		                          type: 'success',
		                          styling: 'bootstrap3'
		                      });
		                	
		                	return false;
	                  }
	                  
	                  new PNotify({
	                      title: '提示消息',
	                      text: data,
	                      type: 'error',
	                      styling: 'bootstrap3'
	                  });
	              });
		     }
	        
			
			// 新增按钮
			$("#btn_create_banner").click(function(){
				bannerSubmitUrl = "create";
				upload.init("image");//初始化file表单框
				clearForm();//清空表单
				var dateTime = new Date().format('yyyy/MM/dd hh:mm:ss');
				$("#createTime").val(dateTime);
				$("#reportrange2").find("span").text(dateTime);
				$(".uas-modal-lg").modal('show');
			});
			
            //提交
            $("#btn_cms_banner_submit").click(function(){
            	$("#bannerImgPath").val("");//清空文本框
				if(!validator.checkAll($("#form_cms_banner"))){
					return false;
				}
				//图片名称赋值
				if(upload.getNewFile().length == 0){
					if(upload.getOldFile() ==null){
						$("#bannerImgPath").val();
					}else if(upload.getOldFile().length != 0){
						$("#bannerImgPath").val(upload.getOldFile()[0].name);
					}else{
						$("#bannerImgPath").val();
					}	
				}else{
					$("#bannerImgPath").val(upload.getNewFile()[0].name);
				}
				var imgPath = $("#bannerImgPath").val();//图片路径
				if(imgPath == "" ||　null == imgPath){
					new PNotify({
					   title: '提示消息',
					   text: '请先上传图片',
					   type: 'error',
					   styling: 'bootstrap3'
					});
					return false;
				}
         	   var param = $("#form_cms_banner").serialize();
         	   console.log(param);
         	   $.ajax({
         		   url: g_requestContextPath + "/h/l/cms/"+bannerSubmitUrl+"/banner",
         		   type: 'post',
         		   dataType: 'json',
         		   data: $("#form_cms_banner").serialize(),
         		   success: function (data) {
         			   table.fnDestroy();
         			   tableDataLoad();
         			   if('SUCCESS' == data){
         				   new PNotify({
         					   title: '提示消息',
         					   text: '保存成功',
         					   type: 'success',
         					   styling: 'bootstrap3'
         				   });
         				   $(".uas-modal-lg").modal('hide');
         			   }else if("101" == data){
         				   new PNotify({
         					   title: '提示消息',
         					  text: '上线条数不能超过10条,请修改状态!',
                              type: 'wanrning',
         					   styling: 'bootstrap3'
         				   });
         			   }else{
         				   new PNotify({
         					   title: '提示消息',
         					   text: data,
         					   type: 'error',
         					   styling: 'bootstrap3'
         				   });
         			   }
         			   
         		   }
         	   });
         	   return false;
            });

            //选择模块
			w.bannerModel = function(id,text){
				$("#bannerModel").text(text);
				$("#type").val(id);
				validator.checkAll($("#form_cms_banner"));
			}
			
			//选择编辑
			w.showUpdatemodal = function(id){
				clearForm();//清空表单框
				bannerSubmitUrl = "edit";
				$.get(g_requestContextPath + "/h/l/cms/findBannerById", {
			       "id": id
				}, function(value) {
				   console.log(value);
				   var data = value.banner;//banner数据
				   var djson = value.djson;//图片数据
				   
				   upload.init("image",djson);//显示图片
				   if(data){
					   var imgName = djson[0].name;//banner 标题名称
					   $("#bannerImgPath").val(imgName);
					   $("#id").val(data.id);
					   $("#title").val(data.title);
				       $("#link").val(data.link);
				       //是否上线
				       if(0 == data.status){
				    	   $("#isTrue1").addClass("active");
				    	   $("#isTrue2").removeClass("active");
				       }else{
				    	   $("#isTrue2").addClass("active");
				    	   $("#isTrue1").removeClass("active");
				       }           	  		
				       $('input:radio[name="status"][value="'+data.status+'"]').prop('checked',true);
			           if(null !=data.createTime){
			        	   var dateTime = new Date(data.createTime).format('yyyy/MM/dd hh:mm:ss');
			        	   $("#createTime").val(dateTime);
			        	   $("#reportrange2").find("span").text(dateTime);
			    	   }
			           if(null != data.orderNum){
			        	   $("#orderNum").val(data.orderNum);
			           }
			           if(null != data.type){
			        	   $("#bannerModel").text(modelTypes[data.type]);
			        	   $("#type").val(data.type);
			           }
			      
			           $(".uas-modal-lg").modal('show');//显示
			           validator.checkAll($("#form_cms_banner"));
			        }
			        	   
			      });
			};
			
			 // 清理表单
			w.clearForm = function(){
			   $("#id").val('');
			   $("#title").val('');
			   $("#link").val('');
			   $("#bannerModel").text('请选择模块');     	   
			   $("#type").val('');
			   $("#orderNum").val('');
			   $("#createTime").val('');
			   $('input:radio[name="status"][value=0]').prop('checked',true);
			   $("#isTrue1").trigger("click");
			   $("#bannerImgPath").val('');//图片路径清空
			   bannerSubmitUrl = "create";
			}
			
			//条件查询
			$("#transfer_frmSearch").click(function() {
				var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");  
				var searchTitle =  $("#searchTitle").val();
	            if(searchTitle != "" && searchTitle != null){
	                if(pattern.test(searchTitle)){  
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
			
			
			
		    
        
	});
})(jQuery, window)