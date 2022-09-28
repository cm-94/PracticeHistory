var orderData = {
    name : "",      // 상품명
    price : "",     // 가격
    inputCd : "",   // 입력바코드
    restQT : "",    // 재고수량
    recvQT : "", // 입고 대기
    deliQT : "", // 출고 대기
    etc : "" ,      // 기타
    image : ""      // 이미지
};

var arrPlaces = [];
$(document).ready(function(){
    if(subscreenData){
        orderData = subscreenData;

        dataManager.requestApi(RQ_SELECT_ORDERPLACE,null,function(data,result){
            if(result == 'success' && data.length > 0){
                arrPlaces = data;
                setData();
            }
        });
    }
});

function setData(){
    $('.orderSpread .name')[0].value = orderData.name;
    $('.orderSpread .price')[0].value = putThousandSeparate(orderData.price) + '원';
    $('.orderSpread .inputCd')[0].value = orderData.inputCd;
    $('.orderSpread .restQT')[0].value = putThousandSeparate(orderData.restQT) + "개" || "0개";
    $('.orderSpread .etc')[0].value = orderData.etc;

    if(orderData.image && orderData.image.length > 0){
        $('.orderSpread .image')[0].src = orderData.image;
    }

    /// 매장 세팅
    for(var i = 0; i < arrPlaces.length; i++){
        $("#orderPlace").append(`<option value='${arrPlaces[i].placeCd}'>${arrPlaces[i].name}</option>`);
    }

    /// 입출고 수정
    if(spreadType == RQ_UPDATE_ORDER_LIST){
        if(orderData.orderType == "01"){
            $('.orderSpread .recvQT').removeAttr('disabled');
            $('.orderSpread .recvQT')[0].value = orderData.recvQT;
            $('#orderType').val("01").prop("seleted",true);
        }
        else if(orderData.orderType == "03"){
            $('.orderSpread .deliQT').removeAttr('disabled');
            $('.orderSpread .deliQT')[0].value = orderData.deliQT;
            $('#orderType').val("03").prop("seleted",true);
            $('#orderPlace').val(orderData.placeCd).prop("seleted",true);
        }
        $('#orderType').attr('disabled',true);
    }
}
function onTypeSelectChange(){
    var orderSelect = document.getElementById("orderType");  
  
    // select element에서 선택된 option의 value가 저장된다.  
    var orderType = orderSelect.options[orderSelect.selectedIndex].value;
    if(orderType != 'none'){
        orderData.orderType = orderType;
        if(orderType == "01"){
            $('.recvQT').removeAttr('disabled');
            $('.deliQT').attr('disabled',true);
            $("#orderPlace").attr('disabled',true);
        }
        else if(orderType == "03"){
            $('.recvQT').attr('disabled',true);
            $('.deliQT').removeAttr('disabled');
            $("#orderPlace").removeAttr('disabled');
        }
    }
    else{
        $('.recvQT').attr('disabled',true);
        $('.deliQT').attr('disabled',true);
    }
}

function onPlaceSelectChange(){
    var orderSelect = document.getElementById("orderPlace");  
  
    var orderPlaceName = orderSelect.options[orderSelect.selectedIndex].text;
    var orderPlace = orderSelect.options[orderSelect.selectedIndex].value;
    orderData.placeCd = orderPlace;
}

function onOrderSave(){
    if(orderData.orderType && orderData.orderType != 'none'){
        orderData.name = orderData.name;           // 상품명
        orderData.price = orderData.price;         // 가격
        orderData.inputCd = orderData.inputCd;     // 입력바코드
        // orderData.orderType = "";                // 주문 유형
        orderData.orderDT = new Date().toISOString().replaceAll('-','').replaceAll(':','').split('.')[0]; // 주문 날짜
        orderData.etc = $('.orderSpread .etc')[0].value; // 기타(메모)
        // 입고
        if(orderData.orderType == "01"){
            if(spreadType == RQ_INSERT_ORDER_LIST) orderData.recvQT = $('.orderSpread .recvQT')[0].value; // 입고 수량
            else orderData.recvNewQT = $('.orderSpread .recvQT')[0].value; // 입고 정정 수량
        }
        // 출고
        else if(orderData.orderType == "03"){
            if(spreadType == RQ_INSERT_ORDER_LIST) orderData.deliQT = $('.orderSpread .deliQT')[0].value; // 입고 수량
            else orderData.deliNewQT = $('.orderSpread .deliQT')[0].value; // 입고 정정 수량
        }

        dataManager.requestApi(spreadType,orderData,function(data,result){
            if(result == 'success' ){
                spreadPage(false);
                if($('.arrow_up').length > 0){
                    $('.arrow_up').addClass('arrow_down');
                    $('.arrow_up').removeClass('arrow_up');
                }
            }
            alert(result);
        });
    }
}

function onOrderCancel(){
    spreadPage(false);
    if($('.arrow_up').length > 0){
        $('.arrow_up').addClass('arrow_down');
        $('.arrow_up').removeClass('arrow_up');
    }
}