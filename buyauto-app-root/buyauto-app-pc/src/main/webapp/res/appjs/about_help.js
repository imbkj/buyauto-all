 $(function(){
	//初始化
	$('._about').show();
	$('.about h1').css({
		'color':'#fff',
		'background':'#ff9900 url("'+g_requestContextPath+'/res/images/help01.png") no-repeat 10px center',
	})
	$('._title').html($('.about h1').html());
	//点击切换
	$('.type').click(function(){
		if($(this).hasClass('ty01')){
			$('._title').html($('.about h1').html());
			$(this).css({
				'color':'#fff',
				'background':'#ff9900 url("'+g_requestContextPath+'/res/images/help01.png") no-repeat 10px center',
			})
			$('.order h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help2.png") no-repeat 10px center',
			})
			$('.buy h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help3.png") no-repeat 10px center',
			})
			$('.service h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help4.png") no-repeat 10px center',
			})
			$('._about').show();
			$('._order').hide();
			$('._buy').hide();
			$('._service').hide();
		}else if($(this).hasClass('ty02')){
			$('._title').html($('.order h1').html());
			$(this).css({
				'color':'#fff',
				'background':'#ff9900 url("'+g_requestContextPath+'/res/images/help02.png") no-repeat 10px center',
			})
			$('.about h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help1.png") no-repeat 10px center',
			})
			$('.buy h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help3.png") no-repeat 10px center',
			})
			$('.service h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help4.png") no-repeat 10px center',
			})
			$('._about').hide();
			$('._order').show();
			$('._buy').hide();
			$('._service').hide();
		}else if($(this).hasClass('ty03')){
			$('._title').html($('.buy h1').html());
			$(this).css({
				'color':'#fff',
				'background':'#ff9900 url("'+g_requestContextPath+'/res/images/help03.png") no-repeat 10px center',
			})
			$('.about h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help1.png") no-repeat 10px center',
			})
			$('.order h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help2.png") no-repeat 10px center',
			})
			$('.service h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help4.png") no-repeat 10px center',
			})
			$('._about').hide();
			$('._order').hide();
			$('._buy').show();
			$('._service').hide();
		}else if($(this).hasClass('ty04')){
			$('._title').html($('.service h1').html());
			$(this).css({
				'color':'#fff',
				'background':'#ff9900 url("'+g_requestContextPath+'/res/images/help04.png") no-repeat 10px center',
			})
			$('.about h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help1.png") no-repeat 10px center',
			})
			$('.order h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help2.png") no-repeat 10px center',
			})
			$('.buy h1').css({
				'color':'#ff9900',
				'background':'#e6e6e6 url("'+g_requestContextPath+'/res/images/help3.png") no-repeat 10px center',
			})
			$('._about').hide();
			$('._order').hide();
			$('._buy').hide();
			$('._service').show();
		}
	})
})