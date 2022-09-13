var objData = {};

$(document).ready(function(){
    debugger;
    if(dialogData) {
        objData = dialogData;
        setData();
    }

    function setData(){
        debugger
        $('.dialog .name')[0].textContent = objData.name;
        $('.dialog .price')[0].textContent = objData.price;
        $('.dialog .inputCd')[0].textContent = objData.inputCd;
        $('.dialog .outputCd')[0].textContent = objData.outputCd;
        $('.dialog .etc')[0].textContent = objData.etc;
    }
});
