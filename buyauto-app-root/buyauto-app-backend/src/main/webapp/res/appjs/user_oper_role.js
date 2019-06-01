(function($, w){
	/*加载会员中心的数据 */
	$(function(){
	        var table = null;
	        roleSubmitUrl = "create";
	        userSubmitUrl = "create";
	        function tableDataLoad(){
	        	
	        	
	      table =$('#user_admeasure_role').dataTable({
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
	            		url: g_requestContextPath+'/h/l/oper/roles',
			            type: 'get',
			            async: false,
			            dataType: 'json',
			            dataSrc: 'rows',
			            data: function(aoData){
			            	var param = {};
			            	param.page = aoData.start>0 ? (aoData.start/aoData.length +1) : 1 ;
			            	param.rows = 20;//aoData.length;
			            	return param;
			            }
			            
	            	},
	            	aoColumnDefs: [ { "bSortable": false, "aTargets": [ 0 ] }],
	            	columns:[
						{data: function(d){return d.roleName;}},
						{data: function(d){return new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");}},
						{data: function(d){
							if (d.isEnable == 1) {
		                        return "启用";
		                    } else {
		                        return "禁用";
		                    }
						}},
						{data: function(d){
							var s = '<div class="table-o-tooltip">';
								if(enable){
									if(d.isEnable == 1){
										s += '<a href="javascript:setDisableTrue(\''+d.id+'\');" class="btn-link">禁用</a>';
									}else{
										s += '<a href="javascript:setDisableFalse(\''+d.id+'\');" class="btn-link">启用</a>';								
									}
								}
								
								if(modify){
									s += '<a href="javascript:showUpdatemodal(\''+d.id+'\');" class="btn-link">编辑</a>';
								}
								if(setRole){
									s += '<a href="javascript:showRolemodal(\''+d.id+'\');" class="btn-link">设置权限</a>';
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
	        
	        $("#frmSearch").click(function(){
	        	var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");  
	            if($("#newsTitle").val() != "" && $("#newsTitle").val() != null){  
	                if(pattern.test($("#newsTitle").val())){  
	                    alert("非法字符，请输入正确的标题！");
	                    return false;  
	                }  
	            }  
	            
	        	if(validator.checkAll($("#mem_search"))){
	               table.fnDestroy();
	               tableDataLoad();
	        	}
	            return false;
	        });
	        
	        // 新增用户
	        $("#btn_create_user").click(function(){
	        	userSubmitUrl = "create";
	        	clearForm();
	        	$("#createTime").val(new Date().format('yyyy-MM-dd'));
    		  	$(".uas-modal-lg").modal('show');
	        	
	        });
	       
	        function loadUserRole(call){
	        	$.get(g_requestContextPath + "/h/l/oper/roles/all/enable", {}, function(data) {
	                $("#ckb_user_role_list").empty();
	                $.each(data, function(i, d) {
	                   var str = '<div class="checkbox">' 
			                           +'<label>'
			                           +'<input type="checkbox" name="roleId" value="'+d.id+'"> '+d.roleName
			                           +'</label>'
			                           +'</div>';
	                   
	                   $("#ckb_user_role_list").append(str);
	                   
	                });
	                
	               if(typeof(call) == "function" ){
	            	   call();
	               }
	            });
	        }
	        
	        $("#btn_user_role_submit").click(function(){
	        	
	        	if(!validator.checkAll($("#form_user_role"))){
	        		return false;
	        	}
	        	var param = $("#form_user_role").serialize();
	        	console.log(param);
	        	$.ajax({
	                url: g_requestContextPath + "/h/l/oper/"+userSubmitUrl+"/role",
	                type: 'post',
	                dataType: 'json',
	                data: $("#form_user_role").serialize(),
	                success: function (data) {
	                	table.fnDestroy();
	 	               	tableDataLoad();
	 	               	if('SUCCESS' == data){
	 	               		new PNotify({
	                          title: '提示消息',
	                          text: '保存成功,请为角色设置权限',
	                          type: 'success',
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
	                	
	                	$(".uas-modal-lg").modal('hide');
	               	}
	            });
	        	
	        	return false;
	        });
	        
	        $("#btn_role_submit").click(function(){
	        	
	        	if(!validator.checkAll($("#form_role"))){
	        		return false;
	        	}
	        	
	        	var items = $('#jqxTree').jqxTree('getCheckedItems');
	        	
	        	var roles = [];
	        	
	        	$.each(items, function(k, v){
	        		var obj = $(v.element);
	        		if(obj.attr('data-type') == "oper"){
	        			roles.push(obj.attr('data-id'));
	        		}
	        	});

	        	
	        	var param = {};
            	
            	 param.id = $("#userId").val();
            	 param.rules = roles.join();
            	 console.log(param);
	        	
	        	$.ajax({
	                url: g_requestContextPath + "/h/l/oper/set/role",
	                type: 'post',
	                dataType: 'json',
	                data: param,
	                success: function (data) {
	                	table.fnDestroy();
	 	               	tableDataLoad();
	 	               	
	                	new PNotify({
                          title: '提示消息',
                          text: '权限设置成功',
                          type: 'success',
                          styling: 'bootstrap3'
                      });
	                	$(".uas-modal-role").modal('hide');
	               	}
	            });
	        	
	        	return false;
	        });
	        
	       w.showUpdatemodal = function(id){
	    	   userSubmitUrl = "edit";
	    	   $.get(g_requestContextPath + "/h/l/oper/findRole", {
	                   "id": id
	               }, function(data) {
	            	   console.log("aaa",data);
	       	   
	            	   if(data){
            	  		   $('input:radio[name="isEnable"][value="'+data.isEnable+'"]').prop('checked',true);
            	  		   if(1 == data.isEnable){
            	  		   	$("#isTrue1").addClass("active");
            	  		   	$("#isTrue2").removeClass("active");
            	  		   }else{
        	  		   		$("#isTrue2").addClass("active");
            	  		   	$("#isTrue1").removeClass("active");
            	  		   }           	  		
		            	   $("#roleName").val(data.roleName);
	            	       $("#roleDesc").val(data.roleDesc);
            	           if(null !=data.createTime){
        	         	   		$("#createTime").val(new Date(data.createTime).format('yyyy-MM-dd hh:mm:ss'));
		            	   }
	            		   if(data.isEnable == 1){
		            		   $("#isEnable").prop("checked", true);
		            	   }
		            	   $("#user_role_id").val(data.id);
		            	   loadUserRole(function(){
		            		   console.log(data.roleId);
		            		   $.each(data.roleId, function(i, d) {
		            			   
		            			   $("input[type=checkbox][value='"+d+"']").prop("checked", true);
			            		   
		                       });
		            		   
		  	        		 $(".uas-modal-lg").modal('show');
		            	   });
	            	   }
	            	   
	               });
	       };
	       
	       w.showRolemodal = function(id){
	       		$("#userId").val(id);
	       		
	       		
               $.get(g_requestContextPath + "/h/l/oper/roleRules", {
                   "id": id
               }, function(data) {
	           		$.each($('li[data-id]'),function(k,v){
	           		 	$('#jqxTree').jqxTree('checkItem', $('li[data-id]')[k], false);
	           		})
     
            	   $.each(eval("(" + data.rules + ")"), function(k, v){
        			   $('#jqxTree').jqxTree('checkItem', $('li[data-id='+v+']')[0], true);

        		   });
            	   
            	   $('.uas-modal-role').modal('show');
            	   
               });
               
	       };
	       
	       w.setDisableTrue = function(id){
	    	   $.get(g_requestContextPath + "/h/l/oper/role/status/disable", {
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
	       
	     w.setDisableFalse = function(id){
	    	  $.get(g_requestContextPath + "/h/l/oper/role/status/enable", {
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
	     
	     // 初始化tree
		 loadUserRole();
	     
	      function loadUserRole(call){

	        		$.get(g_requestContextPath + "/h/l/oper/modules", {}, function(data) {
	                $("#jqxTree ul").empty();
	                
	                dgloadData(data[0]);
	                $("#jqxTree ul").append(tree_str);
	                
	              $('#jqxTree').jqxTree({ height: '500px', hasThreeStates: true, checkboxes: true, width: '430px'});
	              $('#jqxTree').css('visibility', 'visible');
	                
	               if(typeof(call) == "function" ){
	            	   call();
	               }
	               $(".jqx-checkbox").css("margin-top","5px");
	               
	            });
	        }
	        
	        var tree_str = "";
	        
	        function dgloadData(d){
	        	if(d){
	    
	        		if(d.children != undefined && d.children.length > 0){
	        			tree_str += '<li data-id="'+d.id +'" item-expanded="true" data-type="'+d.type+'" >' + d.text ;
	        			tree_str += '<ul>';
	        			$.each(d.children, function(k, v){
	        				dgloadData(v);
	        			});
	        			tree_str += '</ul>';
	        			tree_str += '</li>'
	        		}else{
	        			tree_str += '<li data-id="'+ d.id +'"  data-type="'+d.type+'">' + d.text + '</li>';
	        			
	        			return false;
	        		}
	        	}
	        }
	       
	   	$('.uas-modal-lg').on('hidden.bs.modal', function (e) {
	   		clearForm();
		});
		
	       
	       // 清理表单
	       function clearForm(){
	    	   $("#user_role_id").val('');
	    	   $("#userName").val('');
        	   $("#userPwd").val('');     	   
        	   $("#isEnable").val('');
        	   $("#realName").val('');
        	   $("#position").val('');
        	   
        	   userSubmitUrl = "create";
	       }
	       
	       $('#form_user_role')
	        .on('blur', 'input[required], input.optional, select.required', validator.checkField)
	        .on('change', 'select.required', validator.checkField)
	        .on('keypress', 'input[required][pattern]', validator.keypress);
		
		    $('.multi.required').on('keyup blur', 'input', function() {
		        validator.checkField.apply($(this).siblings().last()[0]);
		    });

	});
})(jQuery, window)