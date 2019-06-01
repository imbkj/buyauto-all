var status;//审核状态
var pageNumber = 1;
var pageSize = 15;
var productname;
var productid;
var isok = 1;

 
	$(function(){
			getTable(isok);
			//遮罩层
	    	$('.boom').css({
	    		'width':$(window).width(),
	    		'height':$(window).height(),
	    	})
	    	
	    	//点击确定(报错提示)
	    	$('.delBtn').click(function(){
	    		$('.Errors').hide();
	    		$('.boom').hide();
	    		$("#btn_levelUp").removeAttr("disabled");
	    	})
			
			//翻页
			$('.page').click(function(e){
				 var e = e || window.event;
		         var target = e.target || e.srcElement;
		         if(target.id == "preBtn"){
		        	 var id = $('.current').attr('id');
		             id = id - 1;
		             if(id < 1){
		                 id = 1;
		                 return;
		             }
		             pageNumber = id;
		             //console.log(pageNumber)
		             
		             $('.ctPage ul li[id='+id+']').addClass('current').siblings().removeClass('current');
		             if(length > 5 && id > 2 && id < length - 1){
		                 $('.ctPage ul').css({
		                     'left': -39 * (id - 3) + 'px'
		                 })
		             }
		              getTable(0)
		         }else if(target.id == "nextBtn"){
		        	 var id = $('.current').attr('id');
		             id = parseInt(id) + 1;
		             if(id > length){
		                 id = length;
		                 return;
		             }
		             pageNumber = id;
		            //console.log(pageNumber)
		             $('.ctPage ul li[id='+id+']').addClass('current').siblings().removeClass('current');
		             if(length > 5 && id > 3 && id < length - 1){
		                 $('.ctPage ul').css({
		                     'left': -39 * (id - 3) + 'px'
		                 })
		             }
		              
		             getTable(0)
		         }else if(target.id == ''){
		        	 return;
		         }else{
		        	 var id = target.id;
		        	 pageNumber = id;
		            //console.log(pageNumber) 
		             $('.page ul li[id="'+ id +'"]').addClass('current').siblings().removeClass('current');
		             if(length > 5 && id > 3 && id < length - 2){
		                 $('.ctPage ul').css({
		                     'left': -39 * (id - 3) + 'px'
		                 })
		             }else if(length > 5 && id < 4){
		                 $('.ctPage ul').css({
		                     'left': 0
		                 })
		             }else if(length > 5 && id > length - 3){
		                 $('.ctPage ul').css({
		                     "left":-39 * (length - 5) + 'px'
		                 })
		             }
		            getTable(0)
		         }
		         //console.log(id)
		        // pageChange(10)
			})
			
		});
		


		//时间戳
		function getLocalTime(nS) {  
			if(nS=="" ||nS == null || (nS+"").trim()=="") return "";
			var date = new Date(parseInt(nS));
			Y = date.getFullYear() + '-';
			M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
			D = date.getDate() + ' ';
			return Y+M+D; 
		} 
		
		$("#search").click(function(){
			productname=$("#productname").val();
			productid=$("#productid").val();
			getTable();
		});
		
		$(".status").click(function(){
			status = $(this).data("status");
		});
		
		$(".count_list li dd").click(function(){
			$(this).addClass("currt").siblings().removeClass("currt");
		})
		
		//商品列表
		function getTable(isok){
			//console.log(pageNumber)
			//console.log(productid)
			 $.ajax({
				 url:g_requestContextPath + "/h/l/supplier/showProductsList",
				 data:{
					 "productStatus":1,
					 "status":status,
					 "title":productname,
				  	 "productsId":productid,
				  	 "page":pageNumber,
				  	 "rows":pageSize
				 },
				 type:"post",
				 dataType:"json",
				 async:true,
				 success:function(data){
					 if(isok == 1){
						 var Page = Math.ceil(data.total/pageSize)
						 pageChange(Page+1)
					 }
					 
					var total = data.total;
					//console.log(total);
					if(total == '0'){
						$('.nolist_ser').show();
						$('.nolist_ser span').html("暂时没有满足搜索条件的订单");
					}else{
						$('.nolist_ser').hide();
					}

					if(total > 5){
						$('.page').show();
					}else{
						$('.page').hide();
					}
					
					 //console.log(data);
					 if(data==null){
						 return false;
					  }
					  var rows = data.rows;
					  var  tablist="";
					  for (var i=0;i<rows.length;i++){
						var id = rows[i].productsId;
					    tablist+='<tr>';
						tablist+='<td><p class="maths">'+id+'</p></td>';
						tablist+='<td><p class="pdt_name">'+rows[i].title+'</p></td>';
						tablist+='<td><p class="price">'+rows[i].carPrice+'</p></td>';
						tablist+='<td>'+getLocalTime(rows[i].createDate)+'</td>';
						var status = rows[i].status;
						if(status==1){
							 tablist+= ' <td>未审核</td>'; 
						}else if(status==2 ){
							 tablist+= ' <td>已上架</td>'; 
						}else if(status==4){
							 tablist+= ' <td>审核通过</td>'; 
						}else if(status==5){
							 tablist+= ' <td>审核不通过</td>'; 
						}else{
							 tablist+= ' <td></td>'; 
						}
						tablist+= ' <td>';
						if(status==1){
							 tablist+= ''; 
						}else if(status==2 ){
							 tablist+= '<a href="javascript:void(0)" onclick="underchange(\''+id+'\')">下架</a>'; 
						}else if( status==4){
							 tablist+= '<a href="javascript:void(0)" onclick="derchange(\''+id+'\')">上架</a>'; 
							 tablist+= '<a href="'+g_requestContextPath+'/h/l/supplier/toEditProducts?id='+id+'">编辑</a>'; 
						}else if(status==5){
							 tablist+= '<a href="javascript:void(0)" onclick="lookinfo(\''+id+'\')">查看原因</a>'+
									   '<a href="'+g_requestContextPath+'/h/l/supplier/toEditProducts?id='+id+'" >修改</a>'+
									   '<a href="javascript:void(0)" onclick="delate(\''+id+'\')">删除</a>'; 
						}else{
							 tablist+= ' <td></td>'; 
						}
						tablist+= ' </td>';
						tablist+= '</tr>';
					  }
			          $(".count_product table tbody").html(tablist);
				 }
			 });
				 
		}
		
	//下架
	function underchange(id){
		//console.log(id)
		 $.ajax({
			  url:g_requestContextPath + "/h/l/supplier/updataDisable",
			  type:'get',
			  data:{
				  "id":id
			  },
			  async:false,
			  dataType:'json',
			  success:function(data){
				  //console.log(data);  
				  if(data){
					  getTable();	
				  }else{
					  
				  }
				  return false;
			  },
			  error:function(){
				 return false;
			  }
		 })
	}
	//上架
	function derchange(id){
		//console.log(id)
		 $.ajax({
			  url:g_requestContextPath + "/h/l/supplier/updataEnable",
			  type:'get',
			  data:{
				  "id":id
			  },
			  async:false,
			  dataType:'json',
			  success:function(data){
				  if(data){
					  getTable();	
				  }else{
					  
				  }
				  return false;
			  },
			  error:function(){
				 return false;
			  }
		 })
	}
	
	//查看原因
	function lookinfo(id){
		//console.log(id)
		 $.ajax({
			 url:g_requestContextPath + "/h/l/supplier/getReasonById",
			 type:'get',
			 data:{
				 "id":id,
			 },
			 async:false,
		     dataType:'json',
		     success:function(data){
		    	  //console.log(1,data);
	    		  $('.boom').show();
			  	  $('.Errors').show();
			  	  $('.Errors').find("span").html(data);
		    	  return false;
		     },
		     error:function(data){
		    	 //console.log(2,data);
		    	  return false;
		     }
		 })
	}

    //删除
	function delate(ids){
		//console.log(ids)
		 $.ajax({
			  url:g_requestContextPath + "/h/l/supplier/updatadelete",
			  type:'get',
			  data:{
				  "id":ids,
			  },
			  async:false,
		      datype:'json',
		      success:function(data){
		    	 if(data){
		    		    $('.boom').show();
				  		$('.Errors').show();
				  		$('.Errors').find("span").html("删除成功!");
		    	 }else{
		    		 $('.boom').show();
				  		$('.Errors').show();
				  		$('.Errors').find("span").html("删除失败!");
		    	 }
		         return false;
		      },
		      error:function(){
		    	 return false;
		      }
		 })
	}
	
	//修改
	function register(idt){
		 $.ajax({
			 url:g_requestContextPath + "/h/l/supplier/getProductsById",
			 type:'get',
			 data:{
				 "id":idt
			 },
			 async:false,
		     datype:'json',
		     success:function(data){
		    	 alert(data);
		         return false;
		      },
		      error:function(){
		    	 return false;
		      }
		 })
	}
	
	
	
//翻页配置
function pageChange(page) {
	$('.ctPage ul').html('');
	$('.ctPage ul').css({
		'left' : "0"
	})
	for (var i = 1; i < page; i++) {
		$('.ctPage ul').append('<li id="' + i + '">' + i + '</li>')
	}
	$('.ctPage ul li:eq(0)').addClass('current');
	length = $('.ctPage ul li').length;
	var wid = length * 40 - 10;
	$('.ctPage ul').css({
		'width' : wid + 'px'
	})
}
	
		
		




      


