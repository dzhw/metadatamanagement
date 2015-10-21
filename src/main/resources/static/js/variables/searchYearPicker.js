var YearPicker = {};

YearPicker.escapeId = function(id) {
    	return id.replace(/\./g, "\\.");
    };
    
YearPicker.trimm = function(date) {
	try{
	var tmp = date.split("-");
    	return tmp[0];
	}catch(e){
		return date;
	}
    };

$(document).ready(function(){
    "use strict";
    $(".yearPicker").on("input",function(e){
    	var tmp;
	    if(this.value.length === 4){
	    	 if(this.id == "surveyPeriod.startDate"){
	    		    document.querySelector("#" + YearPicker.escapeId($(this).attr('id')) + "_alt").value=this.value+"-01-"+"01";
	    		    }else{
	    		    	 document.querySelector("#" + YearPicker.escapeId($(this).attr('id')) + "_alt").value=this.value+"-12-"+"31";
	    		    }
	    }else{
	    	 document.querySelector("#" + YearPicker.escapeId($(this).attr('id')) + "_alt").value = null;
	    }
    });
    
 // we need to clear the alt field when the user cleared the visible input
	$(".yearPicker").change(function(){
		if (!$(this).val()) {
			$("#" + YearPicker.escapeId($(this).attr('id')) + "_alt").val('');
		}
	});	
	
	//the datepicker is under the input line
	$.extend($.yearPicker,{
		_checkOffset:function(inst,offset,isFixed){
			return offset;
		}
	});
});
