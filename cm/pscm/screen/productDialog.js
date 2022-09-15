var objData = {};

$(document).ready(function(){
    if(dialogData) {
        objData = dialogData;
        setData();
    }

    function setData(){
        $('.dialog .name')[0].value = objData.name;
        $('.dialog .price')[0].value = objData.price;
        $('.dialog .inputCd')[0].value = objData.inputCd;
        $('.dialog .outputCd')[0].value = objData.outputCd;
        $('.dialog .etc')[0].value = objData.etc;
    }
});
