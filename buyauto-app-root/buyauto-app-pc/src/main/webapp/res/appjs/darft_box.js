var status;//审核状态
var pageNumber = 1;
var pageSize = 15;
var productname;
var productid;
var isok = 1;
var jump = false;
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
	    			getTable(isok);
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
			getTable(isok);
		});
		
		
		//商品列表
		function getTable(isok){
			productname=$("#productname").val();
			productid=$("#productid").val();
			 $.ajax({
				 url:g_requestContextPath + "/h/l/supplier/showProductsList",
				 data:{
					 "productStatus":3,
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
					 if( total=="0"){
						$(".nolist_ser").show();
						$(".nolist_ser span").html("暂时没有满足筛选条件的订单");
					 } else{
						 $(".nolist_ser").hide();
					 }
					 
					 if(total>5){
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
						tablist+= '<td><a href="javascript:void(0)" onclick="lookinfo(\''+id+'\')">还原</a></td>'
						tablist+= '</tr>';
					  }
			          $(".count_product table tbody").html(tablist);
				 }
			 });
				 
		}

	//还原
	function lookinfo(id){
		//console.log(id)
		 $.ajax({
			 url:g_requestContextPath + "/h/l/supplier/updataRestore",
			 type:'get',
			 data:{
				 "id":id,
			 },
			 async:false,
		     dataType:'json',
		     success:function(data){
		    	  if(data){
		    		  jump = true;
		    		  $('.Errors').show();
		    		  $('.Errors').find("span").html("还原成功");
			    	  $('.boom').show();
		    	  }else{
		    		  jump = false;
		    		  $('.Errors').hide();
			    	  $('.boom').hide();
		    	  }
		    	  return false;
		     },
		     error:function(data){
		    	 return false;
		     }
		 })
	}


	

	
	

	

	
		
		




      


