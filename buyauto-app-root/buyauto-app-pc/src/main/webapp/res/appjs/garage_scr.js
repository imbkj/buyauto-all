var sortOrder = 0;
var page = 1; //第几页
var rows = 20;
var length = '';
var prefix = null;
var brandId = ""
var carType = "" ;
var priceRange = "";
var title = "";
var openMode = "";
var derail = false;
 $(function(){
	$('.isError').css({'left':($(window).width() - 1100)/2 + 'px'})
   $('.tt_hd .tt_hd_con ul.fl li a:eq(1)').addClass('home_page');
   brandId = $("#brandId").val();
   carType =  $("#carType").val();
   priceRange = $("#priceRange").val();
   title = $("#title").val();
   openMode = $("#openMode").val()
   sortOrder = $("#sortOrder").val();
   if(openMode == '1'){
	   $('.more_brand').show();
   }else{
	   $('.more_brand').hide();
   }
	 //图片前缀
	 getPrefix();
 	
	 //从后台获取车辆品牌，填充数据
	 getType();
	//初始化首页传传递的查询条件
	$('.brand[id='+brandId +']').addClass('limit').siblings().removeClass('limit');
	$('.type[id='+ carType +']').addClass('limit').siblings().removeClass('limit');
   	$('.price[id='+ priceRange+']').addClass('limit').siblings().removeClass('limit');
 	$('.sort[id='+ sortOrder+']').addClass('row').siblings().removeClass('row');
   	$('#titleSearch').val(decrypt(title));
   	//console.log($("#titleSearch").val())  
	if(title !== ""){
		if(decrypt(title).length > 15){
			title = decrypt(title).substr(0,15);
		}
		$('.isError_top span a').text(decrypt(title));
	}
	//点击更多按钮
	$('.more').click(function(){
		 
		if(derail && $('.more_brand').css('display') == 'none'){
			$('.more_brand').show();
			openMode = 1;
		}else{
			$('.more_brand').hide();
			openMode = '';
		}
	})


	 //初始化
	 getInfo(1);
	 //页码切换
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
	             page = id;
	             //console.log(pageNumber)
	             $('.ctPage ul li[id='+id+']').addClass('current').siblings().removeClass('current');
	             if(length > 5 && id > 2 && id < length - 1){
	                 $('.ctPage ul').css({
	                     'left': -39 * (id - 3) + 'px'
	                 })
	             }
	            getPageInfo();
	         }else if(target.id == "nextBtn"){
	        	 var id = $('.current').attr('id');
	             id = parseInt(id) + 1;
	             if(id > length){
	                 id = length;
	                 return;
	             }
	             page = id;
	            //console.log(pageNumber)
	             $('.ctPage ul li[id='+id+']').addClass('current').siblings().removeClass('current');
	             if(length > 5 && id > 3 && id < length - 1){
	                 $('.ctPage ul').css({
	                     'left': -39 * (id - 3) + 'px'
	                 })
	             }
	              
	            getPageInfo();
	         }else if(target.id == ''){
	        	 return;
	         }else{
	        	 var id = target.id;
	        	 page = id;
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
	            getPageInfo();
	         }
		})
	  
 	/*类型选择*/
 	$('.sub').click(function(){ 
 		
 		if($(this).hasClass("sort")){
 			$(this).addClass('row').siblings().removeClass('row');
 			sortOrder = $(this).attr('id');
 		}else if($(this).hasClass("brand")){
 			$('.brand').removeClass('limit');
 			$(this).addClass('limit')
 			
 		}else{
 			$(this).addClass('limit').siblings().removeClass('limit');
 		}
 		
 		if($(this).hasClass("brand")){ 
 			brandId = $(this).attr('id');
 		}
 		
 		if($(this).hasClass("type")){
 			carType = $(this).attr('id');
 		}
 		
 		if($(this).hasClass("price")){
 			priceRange = $(this).attr('id');
 		} 
 		
 		if($(this).hasClass("search_btn")){
 			title = $('#titleSearch').val();
 		}else{
 			title = '';
 			$('#titleSearch').val() == '';
 		}
 		page = 1;
 		getInfo(2);
 	})
 }) 
 
 //获取汽车品牌
 function getType(){
	 $.ajax({
		url: g_requestContextPath + "/h/n/products/garageBrand",
		type: "get",
		dateType: "json",
		async: false,
		success:function(data){
			if(data.length < 6){
				for(var k = 0;k < data.length;k++){
					$('.isError_bot ul').append('<li class="sub brand" id="'+ data[k].id +'"><span>'+ data[k].scValue +'</span></li>')
				}
			}else{
				for(var k = 0;k < 6;k++){
					$('.isError_bot ul').append('<li class="sub brand" id="'+ data[k].id +'"><span>'+ data[k].scValue +'</span></li>')
				}
			}
			
			if(data.length < 9){
				for(var i = 0;i<data.length;i++){
					//console.log(data[i].id);
					$('.car_brand').append('<li class="sub brand" id="'+ data[i].id +'">'+ data[i].scValue +'</li>')
				}
			}else{
				for(var i = 0;i<9;i++){
					//console.log(data[i].id);
					$('.car_brand').append('<li class="sub brand" id="'+ data[i].id +'">'+ data[i].scValue +'</li>')
				}
				for(var j = 9;j <data.length;j++){
					$('.more_brand').append('<li class="sub brand" id="'+ data[j].id +'">'+ data[j].scValue +'</li>')
				}
				derail = true;
				//Math.ceil((data.length-9)/10)
				$('.more_brand').css({'height':55*Math.ceil((data.length-9)/10) + 'px'});
				if(Math.ceil((data.length-9)/10) > 1){
					for(var i = 0;i < Math.ceil((data.length-9)/10) - 1;i++){
						$('.more_brand').append('<div class="line line'+ i +'" ></div>')
						$('.line'+ i +'').css({'top':54*(i+1) +'px'})
					}
				}
			}
		},
		error:function(data){
			//报错信息
		}
	})
 }
 
 //获取前缀
 function getPrefix(){
 		$.ajax({
	 		url:g_requestContextPath + "/getShowAddress",
	 		type:"get",
	 		dataType:"json",
	 		async: false,
	 		success:function(data){
				prefix = data;
            }
	})
 }
 
//按条件从后台获取数据
 function getInfo(status){
     //console.log(page)
     if(status == 1){
        title = decrypt(title);
     }else if(status == 2){
    	title = encrypt(title);
        window.location.href = g_requestContextPath + "/h/n/products/garageScr?sortOrder="+sortOrder+"&brandId="+brandId+"&carType="+carType+"&priceRange="+priceRange+"&openMode="+openMode+"&title="+ title;
     }
     $('.car_list ul').html("");
     $.ajax({
        url: g_requestContextPath + "/h/n/products/garageProducts",
        type: "post",
        dateType: "json",
        async: true,
        data:{
            'brandId':brandId,
            'carType':carType,
            'priceRange':priceRange,
            'sortOrder': sortOrder,
            'pageNumber':page,
            'pageSize': rows,
            'title':title
            },
            success:function(data){
            	//console.log(data)
                if(data.rows.length == 0){
                    $('.isError').show();
                }else{
                    $('.isError').hide();
                }
                
                if(data.total > 20){
                	 $('.page').show();
                }else{
                	 $('.page').hide();
                	 $('.car_list').css({'marginBottom':'110px'})
                }
                //总页面
                pageNum = Math.ceil(data.total/rows);
                pageChange(parseInt(pageNum) + 1);
                var row = data.rows;
                var html = '';
                for(var i = 0;i < row.length;i++){
                    
                    html += '<li id="'+ row[i].productsId +'">'; 
                    html += '<a href="'+g_requestContextPath +"/h/n/products/carInfo/"+row[i].productsId+'">';
                    if(row[i].carNoCount == '0'){
                        html += '<div class="car_boom"></div><div class="car_img"></div>';
                    } 	 
                	html += '<div class="img_box"><img src="'+ prefix + row[i].filePath +'"></div>';
                    html += '<h3>'+ row[i].title +'</h3>';
                    html += '<div class="position"><span class="dist dist1"><h5>'+ string(row[i].country) +'</h5></span>';
                    html += '<span class="dist dist2"><h5>'+ change(row[i].position) +'</h5></span></div>';
                    html += '<span class="n_price01">'+ ("￥ "+thousandBitSeparator(parseInt(row[i].carPrice))) +'&nbsp;<h6>起</h6></span>';
                    html += '<div><span class="o_price">'+ ("￥ "+thousandBitSeparator(parseInt(row[i].chinaPrice))) +'</span>';
                    html += '<span class="save01"><h6>省</h6>'+ ("￥ "+thousandBitSeparator(parseInt(row[i].chinaPrice - row[i].carPrice))) +'</span></div>';
                    html += '</a>';
                    html += '</li>';
                    
                }
                $('.car_list ul').append(html);
            },
            error:function(data){
                //报错信息
            }
        })
 }
 
 function change(date){
     switch(date)
     {
     case 0:
       return "现车"
       break;
     case 1:
       return "预定车"
       break;
     case 2:
       return "在途车"
       break;
     }
 }
 
 function pageChange(page){
     $('.ctPage ul').html('');
     $('.ctPage ul').css({
         'left': "0"
     })
     for(var i = 1;i < page;i++){
         $('.ctPage ul').append('<li id="'+i+'">'+i+'</li>')
     }
     $('.ctPage ul li:eq(0)').addClass('current'); 
     length = $('.ctPage ul li').length;
     var wid = length * 40 - 10;
     $('.ctPage ul').css({'width':wid + 'px'})
 }
  
 function getPageInfo(){
	 //console.log()
     $('.car_list ul').html("");
     $.ajax({
        url: g_requestContextPath + "/h/n/products/garageProducts",
        type: "post",
        dateType: "json",
        async : true,
        data:{
            'brandId':brandId,
            'carType':carType,
            'priceRange':priceRange,
            'sortOrder': sortOrder,
            'pageNumber':page,
            'pageSize': rows,
            'title':title
            },
            success:function(data){
                var row = data.rows;
                var html = '';
                for(var i = 0;i < row.length;i++){
                    
                    html += '<li id="'+ row[i].productsId +'">'; 
                    html += '<a href="'+g_requestContextPath +"/h/n/products/carInfo/"+row[i].productsId+'">';
                    if(row[i].carNoCount == '0'){
                        html += '<div class="car_boom"></div><div class="car_img"></div>';
                    } 	 
                	html += '<div class="img_box"><img src="'+ prefix + row[i].filePath +'"></div>';
                    html += '<h3>'+ row[i].title +'</h3>';
                    html += '<div class="position"><span class="dist dist1"><h5>'+ string(row[i].country) +'</h5></span>';
                    html += '<span class="dist dist2"><h5>'+ change(row[i].position) +'</h5></span></div>';
                    html += '<span class="n_price01">'+ ("￥ "+thousandBitSeparator(parseInt(row[i].carPrice))) +'&nbsp;<h6>起</h6></span>';
                    html += '<div><span class="o_price">'+ ("￥ "+thousandBitSeparator(parseInt(row[i].chinaPrice))) +'</span>';
                    html += '<span class="save01"><h6>省</h6>'+ ("￥ "+thousandBitSeparator(parseInt(row[i].chinaPrice - row[i].carPrice))) +'</span></div>';
                    html += '</a>';
                    html += '</li>';
                   
                }
                $('.car_list ul').append(html);
            },
            error:function(data){
                //报错信息
            }
        })
 }

  
var encrypt = function (str)
{
	return escape(escape(str));
}
var decrypt = function (str)
{
	return unescape(unescape(str));
}

function string(data){
	if(data.length < 7){
		return data;
	}else{
		return data.substr(0,6)
	}
}
 
  