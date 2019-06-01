$(document).ready(function(){
	$('.slider4').bxSlider({
	    slideWidth: 100,
	    minSlides: 2,
	    maxSlides: 10,
		moveSlides: 1,
		startSlide: 1,
	    slideMargin: 10
	});
});
$(function(){
	//新闻公告tab切换
	$(".memu li").click(function(){
		var index = $(this).index();
		$(this).addClass("active").siblings().removeClass("active");
		$(".new_ifmt").eq(index).addClass("new_ifmt_active").siblings().removeClass("new_ifmt_active");
		if($(this).text() == "新闻资讯"){
			var swiper3 = new Swiper('.swiper3', {
		        pagination: '.swiper-pagination3',
		        paginationClickable: true,
		        spaceBetween: 0,
		        loop:true,
		        autoplay:5000,
		        speed:1000,
		    });
		}else if($(this).text() == "麦卡公告"){
			var swiper4 = new Swiper('.swiper4', {
		        pagination: '.swiper-pagination4',
		        paginationClickable: true,
		        spaceBetween: 0,
		        loop:true,
		        autoplay:5000,
		        speed:1000,
		    });
		}
	})
	
    
	//头部点击
	var aUrl = $(".tt_hd_con li a");
	for(var i = 0; i < aUrl.length; i++){
		if(aUrl[i].href == window.document.location.href){
			aUrl[i].className = "opt_for";
		};
	};
	
	//头部悬浮
	var oNav=document.getElementById('suspend_hd');
	var hd=document.getElementById('tt_hd');
	
	window.onscroll = function(){
		var scrTop = document.documentElement.scrollTop||document.body.scrollTop;
		if(scrTop > 150){
			oNav.style.display = "block";
			hd.style.display = "none";
		}else{
			oNav.style.display = "none";
			hd.style.display = "block";
		}
	};

	var prefix = null;
	$.getJSON(g_requestContextPath + "/getShowAddress",function(data){
		prefix = data;
	})
	
	//头部banner
	$.getJSON(g_requestContextPath + "/h/n/index/getCmsBannerList",function(data){
		var cartt;
		//console.log(data);
		for(var i = 0; i < data.length; i++){
			cartt =		'<div class="swiper-slide swiper-no-swiping">';
			cartt+= 		'<a href="'+g_requestContextPath+''+data[i].link+'">';
			cartt+=				'<img src="'+prefix+''+data[i].image+'"/>';
			cartt+=	    	'</a>';
			cartt+=		'</div>';
			$(".carousel").append(cartt);
		};
		var swiper1 = new Swiper('.swiper1', {
	        pagination: '.swiper-pagination1',
	        paginationClickable: true,
	        spaceBetween: 0,
	        loop:true,
	        speed:1000,
	        autoplay:5000,
	    });
	})
	
	//热门推荐
	$.getJSON(g_requestContextPath + "/h/n/index/getIndexProductsList",function(data){
		var various;
		for(var i = 0; i < data.length; i++){
			if(data[i].carNoCount == 0){
				//console.log(data[i].carNoCount,"车架号数")
				various = '<li class="fl cl">';
				various+= '<a href="'+g_requestContextPath +"/h/n/products/carInfo/"+data[i].productsId+'">';
				various+=		'<div class="fl sHoverItem">';
				various+=			'<img src="'+prefix+''+data[i].filePath+'"/>';
				various+=			'<div class="sIntro car_deploy">';
				various+=				'<div class="sHoverItem_ncount"></div>';
				various+=			'</div>';
				various+=			'<div class="cl btt">';
				various+=				'<div class="fl cl">'+data[i].title+'</div>';
				various+=				'<div class="fr cl pcice">'+changePriceT(parseInt(data[i].carPrice))+'起</div>';
				various+=			'</div>';
				various+=		'</div>';
				various+= '</a>';
				various+= '</li>';
			}else{
				various = '<li class="fl cl">';
				various+= '<a href="'+g_requestContextPath +"/h/n/products/carInfo/"+data[i].productsId+'">';
				various+=		'<div class="fl sHoverItem">';
				various+=			'<img src="'+prefix+''+data[i].filePath+'"/>';
				various+=			'<div class="sIntro car_deploy">';
				various+=				'<ul>';
				various+=					'<li class="cl">型 号：'+data[i].carModel+'</li>';
				various+=					'<li class="cl">颜 色：'+data[i].outColor+'</li>';
				various+=					'<li class="cl">发动机：'+data[i].engine+'</li>';
				various+=					'<li class="cl">变速箱：'+data[i].gearbox+'</li>';
				various+=				'</ul>';
				various+=			'</div>';
				various+=			'<div class="cl btt">';
				various+=				'<div class="fl cl">'+data[i].title+'</div>';
				various+=				'<div class="fr cl pcice">'+changePriceT(parseInt(data[i].carPrice))+'起</div>';
				various+=			'</div>';
				various+=		'</div>';
				various+= '</a>';
				various+= '</li>';
			}
	    	$(".car_recommend").append(various);
		}
		var b = new sHover('head','headIntro');
		var a = new sHover("sHoverItem","sIntro");
	})
	
	//车辆品牌
	$.getJSON(g_requestContextPath + "/h/n/index/getBrandList",function(data){
		var brand;
		//console.log(data);
		for(var i = 0; i < data.length; i++){
		    brand = 	'<div class="slide fl">';
		    brand +=	'<a href="'+g_requestContextPath +'/h/n/products/garageScr?sortOrder=0&brandId='+data[i].id+'&carType=-1&priceRange=-1&openMode=-1&title=">';
		    brand +=		'<img src="'+prefix+''+data[i].scRemark+'" width="65" height="50">';
		    brand +=		'<p class="cl">'+data[i].scValue+'</p>';
		    brand +=	'</a>';
		    brand +=	'</div>';
	    	$(".car_styles").append(brand);
		}
		$('.slider4').bxSlider({
	        slideWidth: 100,
	        minSlides: 2,
	        maxSlides: 10,
			moveSlides: 5,
			startSlide: 1,
	        slideMargin: 10
	    });
	})
	
	
	//精选好车
	$.getJSON(g_requestContextPath + "/h/n/index/getCommodityList",function(data){
		
		var sift;
		for(var i = 0; i < data.length; i++){
			//console.log(prefix)
			if(data[i].carNoCount == 0){
				sift = '<li class="fl car_nocount">';
		    	sift+=			'<a href="'+g_requestContextPath +"/h/n/products/carInfo/"+eval(data[i]).productsId+'">';
		    	sift+=				'<div class="no_count">';
		    	sift+=					'<div></div>';	
		    	sift+=				'</div>';
		    	sift+=				'<div class="cl img"><img width="250" height="180" src="'+prefix+''+data[i].filePath+'" alt="" /></div>';
		    	sift+=				'<h5 class="cl">'+data[i].title+'</h5>';
		    	sift+=				'<div class="cl car_site">';
		    	sift+=					'<span class="fl site">';
		    	sift+=						'<i>'+data[i].country+'</i>';
		    	sift+=					'</span>';
		    	if(data[i].position == 0){
		    	sift+=					'<span class="fl spot">';
		    	sift+=						'<i>港口现车</i>';
		    	sift+=					'</span>';
		    	}else if(data[i].position == 1){
		    	sift+=					'<span class="fl spot">';
		    	sift+=						'<i>预定车</i>';
		    	sift+=					'</span>';
		    	}else if(data[i].position == 2){
		    	sift+=					'<span class="fl spot">';
		    	sift+=						'<i>在途车</i>';
		    	sift+=					'</span>';
		    	}
		    	sift+=				'</div>';
		    	sift+=				'<div class="current"><span>'+changePriceT(parseInt(data[i].carPrice))+'</span> 起</div>';
		    	sift+=				'<div class="before"><span>'+changePriceT(parseInt(data[i].chinaPrice))+'</span> 省<span class="save">'+changePriceT(parseInt((data[i].chinaPrice - data[i].carPrice)))+'</span></div>';
		    	sift+=			'</a>';
		    	sift+=		'</li>';	
			}else{
				sift = '<li class="fl">';
		    	sift+=			'<a href="'+g_requestContextPath +"/h/n/products/carInfo/"+eval(data[i]).productsId+'">';
		    	sift+=				'<div class="cl img"><img width="250" height="180" src="'+prefix+''+data[i].filePath+'" alt="" /></div>';
		    	sift+=				'<h5 class="cl">'+data[i].title+'</h5>';
		    	sift+=				'<div class="cl car_site">';
		    	sift+=					'<span class="fl site">';
		    	sift+=						'<i>'+data[i].country+'</i>';
		    	sift+=					'</span>';
		    	if(data[i].position == 0){
		    	sift+=					'<span class="fl spot">';
		    	sift+=						'<i>港口现车</i>';
		    	sift+=					'</span>';
		    	}else if(data[i].position == 1){
		    	sift+=					'<span class="fl spot">';
		    	sift+=						'<i>预定车</i>';
		    	sift+=					'</span>';
		    	}else if(data[i].position == 2){
		    	sift+=					'<span class="fl spot">';
		    	sift+=						'<i>在途车</i>';
		    	sift+=					'</span>';
		    	}
		    	sift+=				'</div>';
		    	sift+=				'<div class="current"><span>'+changePriceT(parseInt(data[i].carPrice))+'</span> 起</div>';
		    	sift+=				'<div class="before"><span>'+changePriceT(parseInt(data[i].chinaPrice))+'</span> 省<span class="save">'+changePriceT(parseInt((data[i].chinaPrice - data[i].carPrice)))+'</span></div>';
		    	sift+=			'</a>';
		    	sift+=		'</li>';
	    	}
	    	$(".append_sift").append(sift);
		}
	})
	
	//新闻资讯
	$.getJSON(g_requestContextPath + "/h/n/index/getCmsNewsList",function(data){
		var carNews;
		//console.log(data)
		for(var i = 0; i < data.length; i++){
			carNews = 	'<div class="swiper-slide swiper-no-swiping">';
			carNews+=      '<a class="news_link" target="_target" href="'+g_requestContextPath+'/h/n/news/info?id='+data[i].newsId+'">';
			carNews+=      '<div class="cl ifmt_cen">';
			carNews+=          '<ul>';
			carNews+=            	'<li class="fl cl cen_img"><img width="550" height="330" src="'+prefix+''+data[i].pic+'"/></li>';
			carNews+=            	'<li class="fr cen_cen">';
			carNews+=            		'<h5>'+data[i].title+'</h5>';
			carNews+=            		'<p class="cl">'+data[i].introduction+'</p>';
			carNews+=            		'<i>'+new Date(data[i].onlinetime).format("yyyy.MM.dd")+'</i>';
			carNews+=           	'</li>';
			carNews+=            '</ul>';
			carNews+=         '</div>';
			carNews+=         '</a>';
			carNews+=   '</div>';
			
	    	$(".car_news").append(carNews);
		}
		var swiper3 = new Swiper('.swiper3', {
	        pagination: '.swiper-pagination3',
	        paginationClickable: true,
	        spaceBetween: 0,
	        loop:true,
	        autoplay:5000,
	        speed:1000,
	    });
	})
	
	//公告
	$.getJSON(g_requestContextPath + "/h/n/index/getCmsNoticeList",function(data){
		var carNotice;
		//console.log(data)
		for(var i = 0; i < data.length; i++){
			carNotice  = 	'<div class="swiper-slide swiper-no-swiping">';
			carNotice+=      	'<a class="news_link" target="_target" href="'+g_requestContextPath+'/h/n/news/info?id='+data[i].newsId+'">';
			carNotice+=      	'<div class="cl ifmt_cen">';
			carNotice+=          	'<ul>';
			carNotice+=            		'<li class="fl cl cen_img"><img width="550" height="330" src="'+prefix+''+data[i].pic+'"/></li>';
			carNotice+=            		'<li class="fr cen_cen">';
			carNotice+=            			'<h5>'+data[i].title+'</h5>';
			carNotice+=            			'<p class="cl">'+data[i].introduction+'</p>';
			carNotice+=            			'<i>'+new Date(data[i].onlinetime).format("yyyy.MM.dd")+'</i>';
			carNotice+=           		'</li>';
			carNotice+=            '</ul>';
			carNotice+=         '</div>';
			carNotice+=      	'</a>';
			carNotice+=   	'</div>';
			
	    	$(".car_notice").append(carNotice);
		};
		var swiper4 = new Swiper('.swiper4', {
	        pagination: '.swiper-pagination4',
	        paginationClickable: true,
	        spaceBetween: 0,
	        loop:true,
	        autoplay:5000,
	        speed:1000,
	    });
	})
})

