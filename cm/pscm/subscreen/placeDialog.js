var objData = {};

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
    $('.dialog .name')[0].value = objData.name;// $('.dialog .name').attr('disabled',true);
    $('.dialog .place')[0].value = objData.place;// $('.dialog .place').attr('disabled',true);
    $('.dialog .placeCd')[0].value = objData.placeCd;// $('.dialog .placeCd').attr('disabled',true);
    $('.dialog .entranceFee')[0].value = objData.entranceFee;
    $('.dialog .salesCommi')[0].value = objData.salesCommi;
    $('.dialog .sattleDate')[0].value = objData.sattleDate;
    $('.dialog .taxBill')[0].value = objData.taxBill;
    $('.dialog .etc')[0].value = objData.etc;
    $('.dialog #entranceType').val(objData.entranceType).prop("seleted",true);
    $('.dialog #salesCommiType').val(objData.salesCommiType).prop("seleted",true);
}

function selectImage(){
    document.querySelector('.inputImg').click();
}

function saveData(){
    var msg = "";
    var type = {};
    
    if(objData.type == "INSERT"){
        type = RQ_INSERT_ORDERPLACE;
    }
    else if(objData.type == "UPDATE"){
        type = RQ_UPDATE_ORDERPLACE;
    }

    var name = $('.dialog .name')[0].value;
    var place  = $('.dialog .place')[0].value;
    var newPlaceCd = $('.dialog .placeCd')[0].value;
    
    if(name.length > 0 && place.length > 0 && newPlaceCd.length > 0){
        objData.name = name;
        objData.place = place;
        objData.newPlaceCd = newPlaceCd;
        objData.entranceFee = $('.dialog .entranceFee')[0].value;
        objData.entranceType = $('.dialog #entranceType')[0].value;
        objData.salesCommi = $('.dialog .salesCommi')[0].value;
        objData.salesCommiType = $('.dialog #salesCommiType')[0].value;
        objData.sattleDate = $('.dialog .sattleDate')[0].value;
        objData.taxBill = $('.dialog .taxBill')[0].value;
        objData.conTerm = $('.dialog .conTerm')[0].value;
        objData.etc = $('.dialog .etc')[0].value;
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