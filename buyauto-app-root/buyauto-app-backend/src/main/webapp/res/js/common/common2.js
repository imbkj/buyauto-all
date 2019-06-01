	
function filed(object,input){
	$(input).find("[name]").each(function(v,n){
		
		var fieldName = $(this).attr("name");
		var fieldValue = object[fieldName];
		if($(this).attr("type") == "radio"){
			$("[name="+fieldName+"]").each(function(){
				if($(this).val()== fieldValue){
					$(this).attr("checked",true);
				}
			});
		}else if($(this).attr("type") == "dateTime"){
			$(this).val(new Date(fieldValue).format("yyyy-MM-dd"));
		}else if($(this).attr("type") == "select"){
			$("select[name="+fieldName+"]").each(function(v,n){
				$(this).val(fieldValue);
			});
		}else{
			$(this).val(fieldValue);
		}
		
	});
}
