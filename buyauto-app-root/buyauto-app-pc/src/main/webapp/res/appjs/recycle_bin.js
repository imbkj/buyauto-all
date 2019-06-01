var status;//审核状态
var pageNumber = 1;
var pageSize = 15;
var productname;
var productid;
var isok = 1;
var jump=false;
 
	$(function(){
			getTable(isok);
			//遮罩层
	    	$('.boom').css({
	    		'width':$(window).width(),
	    		'height':$(window).height(),
	    	})
	    	//点击确定(报错提示)
	    	$('.delBtn').click(function(){
	    		if(jump){
	    			getTable();
	    		}
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
			getTable();
		});
		
		//商品列表
		function getTable(isok){
			productname=$("#productname").val();
			productid=$("#productid").val();
			 $.ajax({
				 url:g_requestContextPath + "/h/l/supplier/showProductsList",
				 data:{
					 "productStatus":0,
					 "title":productname,
				  	 "productsId":productid,
				  	 "page":pageNumber,
				  	 "rows":pageSize
				 },
				 type:"post",
				 dataType:"json",
				 async:false,
				 success:function(data){
					 if(isok == 1){
						 var Page = Math.ceil(data.total/pageSize)
						 pageChange(Page+1)
					 }
					 
					var total=data.total;
					if(total == "0"){
						$(".nolist_ser").show();
						$(".nolist_ser span").html("暂时没有满足筛选条件的订单");
					}else{
						$(".nolist_ser").hide();
					}
					
					if(total>5){
						$(".page").show();
					}else{
						$(".page").hide();
					}

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
							 tablist+= '<td><a href="javascript:void(0)" onclick="lookinfo(\''+id+'\')">提交</a>'+
									   '<a href="'+g_requestContextPath+'/h/l/supplier/toEditProducts?id='+id+'" >修改</a>'+
									   '<a href="javascript:void(0)" onclick="delate(\''+id+'\')">删除</a>'; 
						tablist+= ' </td>';
						tablist+= '</tr>';
					  }
			          $(".count_product table tbody").html(tablist);
				 }
			 });
				 
		}

	//提交审核
	function lookinfo(id){
		 $.ajax({
			 url:g_requestContextPath + "/h/l/supplier/subAudit",
			 type:'post',
			 data:{
				 "id":id,
			 },
			 async:false,
		     dataType:'json',
		     success:function(data){
		    	  alert(data);
		    	  return false;
		     },
		     error:function(data){
		    	  return false;
		     }
		 })
	}
	
	//提示
	function alert(data){
		 $("#submit_sure").removeAttr('disabled');
		 if(data=="200"){
			jump=true;
 		    $('.boom').show();
	  		$('.Errors').show();
	  		$('.Errors').find("span").html("修改成功!");
		 }
		 else if(data=="500"){
 		    $('.boom').show();
	  		$('.Errors').show();
	  		$('.Errors').find("span").html("修改失败!");
		 }
		 else if(data=="601"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("标题为空!");
			 }
		 else if(data=="602"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("品牌ID为空!");
			 }
		 else if(data=="603"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("车辆状态为空!");
			 }
		 else if(data=="604"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("价格区间为空!");
			 }
		 else if(data=="605"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("车辆类型为空!");
			 }
		 else if(data=="606"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("定金支付比例不能大于100或者小于0!");
			 }
		 else if(data=="607"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("出厂日期为空!");
			 }
		 else if(data=="608"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("裸车价格为空!");
			 }
		 else if(data=="609"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("必选总价为空!");
			 }
		 else if(data=="610"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("国内指导价为空!");
			 }
		 else if(data=="611"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("库存为空!");
			 }
		 else if(data=="612"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("缩略图为空!");
			 }
		 else if(data=="613"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("封面图为空!");
			 }
		 else if(data=="614"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("详情图为空!");
			 }
		 else if(data=="615"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("推荐图为空!");
			 }
		 else if(data=="616"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("产品正文为空!");
			 }
		/* else if(data=="617"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("车辆三证为空!");
			 }*/
		 else if(data=="618"){
	 		    $('.boom').show();
		  		$('.Errors').show();
		  		$('.Errors').find("span").html("未查到该产品!");
			 }
		 
		 
		   return false; 	
	}

    //删除
	function delate(ids){
		 $.ajax({
			  url:g_requestContextPath + "/h/l/supplier/updatadelete",
			  type:'post',
			  data:{
				  "id":ids,
			  },
			  async:false,
		      datype:'json',
		      success:function(data){
		    	 if(data){
		    		 	jump=true;
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



	
		
		




      


