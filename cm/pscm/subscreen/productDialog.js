var objData = {
    name : "",
    price : "",
    inputCd : "",
    outputCd : "",
    etc : "",
    image : ""
};

$(document).ready(function(){
    if(dialogData) {
        objData = dialogData;
        if(objData.type == "UPDATE") setData();
    }

    $('.inputImg').on('change',function(){
        var reader = new FileReader();
        reader.readAsDataURL(this.files[0]);

        reader.onload = function () {
            objData.image = reader.result;
            if(reader.result.length > 0){
                $('.dialog .image')[0].src = objData.image;
            }            
        };
    });
});

function setData(){
    $('.dialog .name')[0].value = objData.name;
    $('.dialog .price')[0].value = objData.price;
    $('.dialog .inputCd')[0].value = objData.inputCd;
    $('.dialog .outputCd')[0].value = objData.outputCd;
    $('.dialog .etc')[0].value = objData.etc;

    if(objData.image.length > 0){
        $('.dialog .image')[0].src = objData.image;
    }
}

function selectImage(){
    document.querySelector('.inputImg').click();
}

function saveData(){
    var msg = "";
    var type = {};
    
    if(objData.type == "INSERT"){
        type = RQ_INSERT_PRODUCTS;
    }
    else if(objData.type == "UPDATE"){
        type = RQ_UPDATE_PRODUCTS;
    }

    var name = $('.dialog .name')[0].value;
    var price = $('.dialog .price')[0].value;
    var inputCd = $('.dialog .inputCd')[0].value;
    var outputCd = $('.dialog .outputCd')[0].value;
    var etc = $('.dialog .etc')[0].value;

    if(name.length > 0 && price.length > 0 && inputCd.length > 0){
        objData.name = name;
        objData.price = price;
        objData.inputCd = inputCd;
        objData.outputCd = outputCd;
        objData.etc = etc;
        if(!objData.image) objData.image = "";
    }
    else{
        alert('입력값을 확인하세요');
        return;
    }

    dataManager.requestApi(type,objData,function(data,result){
        if(result == 'success'){
            closeDialog(true);
        }
        alert(result);
    });
}