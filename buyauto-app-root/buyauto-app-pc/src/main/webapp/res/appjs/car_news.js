var pageNumber = 1;
var pageSize = 10;
var prefix = null;
var type = "";

$(function(){
	var types = $(".car_news").data("type");
	if(types == 1){
		$(".memu li.notice").addClass("active");
	}else if(types == 2){
		$(".memu li.news").addClass("active");
	}
	var type = $(".memu li.active").data("type");
	getInfo(type);
	//console.log(type);
	$(".memu li").click(function(){
		var type = $(this).data("type");
		//console.log(type);
		pageNumber = 1;
		getInfo(type);
		var index = $(this).index();
		$(this).addClass("active").siblings().removeClass("active");
		$(".info_plate").eq(index).addClass("info_plate_active").siblings().removeClass("info_plate_active");
	});
	
	$.getJSON(g_requestContextPath + "/getShowAddress",function(data){
		prefix = data;
	})
	
	function getInfo(type){
		$.ajax({
			url:g_requestContextPath+"/h/n/news/list",
			type:"post",
			data:{
				"type":type,
				"page":pageNumber,
				"rows":pageSize
			},
			success : function(data){
				$('.news_con').html('');
				//console.log(data);
				if(data.total > 5){
					$('.page').show();
				}else{
					$('.page').hide();
					$(".news_con").css("marginBottom","50px");
				}
				var pageNum = Math.ceil(data.total/pageSize);
				pageChange(pageNum+1);
				var carNews = '';
				for(var i = 0; i <= data.rows.length; i++){
					carNews+=		'<li class="cl">';
		    		carNews+=			'<a class="cl" target="_blank" href="'+g_requestContextPath+'/h/n/news/info?id='+data.rows[i].id+'">';
		    		carNews+=				'<img class="fl" src="'+prefix+''+data.rows[i].pic+'" alt=""  width="170" height="119"/>';
		    		carNews+=				'<div class="fr">';
		    		carNews+=					'<h5>'+data.rows[i].title+'</h5>';
		    		carNews+=					'<p class="cl">'+data.rows[i].introduction+'</p>';
		    		carNews+=					'<i>'+new Date(data.rows[i].onlinetime).format("yyyy-MM-dd hh:mm:ss")+'</i>';
		    		carNews+=				'</div>';
		    		carNews+=			'</a>';
		    		carNews+=		'</li>';
		    		
					$(".news_con").html(carNews);
				};
			},
			error : function() {
				// console.log(data);
				console.log("非法参数");
			}
		});
	};
	//翻页
	$('.page').click(function(e){
		var type = $(".memu li.active").data("type");
		var e = e || window.event;
		var target = e.target || e.srcElement;
		//console.log(target.id)
         if(target.id == "preBtn"){
        	 var id = $('.current').attr('id');
             id = id - 1;
             if(id < 1){
                 id = 1;
                 return;
             }
             pageNumber = id;
             //onsole.log(pageNumber);
             $('.ctPage ul li[id='+id+']').addClass('current').siblings().removeClass('current');
             if(length > 5 && id > 2 && id < length - 1){
                 $('.ctPage ul').css({
                     'left': -39 * (id - 3) + 'px'
                 })
             }
            getPageInfo(type);
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
              
            getPageInfo(type);
         }else if(target.id == ''){
        	 return;
         }else{
        	 var id = target.id;
             pageNumber = id;
            // console.log(pageNumber) 
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
            getPageInfo(type);
         }
	})
	
});

//动态插入页码
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

//翻页获取数据函数
function getPageInfo(type){
	//console.log(pageNumber)
	$.ajax({
		url:g_requestContextPath+"/h/n/news/list",
		type:"post",
		data:{
			"type":type,
			"page":pageNumber,
			"rows":pageSize
		},
		success : function(data){
			$('.news_con').html('');
			//console.log(data);
			//var pageNum = Math.ceil(data.total/pageSize);
			//pageChange(pageNum+1);
			var carNews = '';
			for(var i = 0; i <= data.rows.length; i++){
				carNews+=		'<li class="cl">';
	    		carNews+=			'<a class="cl" target="_blank" href="'+g_requestContextPath+'/h/n/news/info?id='+data.rows[i].id+'">';
	    		carNews+=				'<img class="fl" src="'+prefix+''+data.rows[i].pic+'" alt=""  width="170" height="119"/>';
	    		carNews+=				'<div class="fr">';
	    		carNews+=					'<h5>'+data.rows[i].title+'</h5>';
	    		carNews+=					'<p class="cl">'+data.rows[i].introduction+'</p>';
	    		carNews+=					'<i>'+new Date(data.rows[i].onlinetime).format("yyyy-MM-dd hh:mm:ss")+'</i>';
	    		carNews+=				'</div>';
	    		carNews+=			'</a>';
	    		carNews+=		'</li>';
	    		
				$(".news_con").html(carNews);
			};
		},
		error : function() {
			// console.log(data);
			console.log("非法参数");
		}
	});
}