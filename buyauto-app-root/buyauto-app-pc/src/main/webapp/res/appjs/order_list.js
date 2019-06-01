$(function(){
	pageChange(50);
	
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
            //getPageInfo();
         }
	})
})

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
		 