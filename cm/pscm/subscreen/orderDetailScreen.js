var objData = {
    name : "",      // 상품명
    price : "",     // 가격
    inputCd : "",   // 입력바코드
    restQT : "",    // 재고수량
    recvWaitQ : "", // 입고 대기
    deliWaitQ : "", // 출고 대기
    etc : "" ,      // 기타
    image : ""      // 이미지
};

var orderData = {
    name : "",       // 상품명
    price : "",      // 가격
    inputCd : "",    // 입력바코드
    orderPlace : "", // 주문 지점
    orderType : "",  // 주문 유형
    orderCd : "",    // 주문 코드
    recvQT : "" ,    // 입고 수량
    deliQT : "",     // 출고 수량
    orderDT : "",    // 주문 날짜
    recvDT : "",     // 입고 날짜
    deliDT : "",     // 출고 날짜
    etc : "",        // 기타(메모)
};

$(document).ready(function(){
    if(subscreenData){
        objData = subscreenData;
        setData();
    }
});

function setData(){
    $('.orderSpread .name')[0].value = objData.name;
    $('.orderSpread .price')[0].value = putThousandSeparate(objData.price) + '원';
    $('.orderSpread .inputCd')[0].value = objData.inputCd;
    $('.orderSpread .restQT')[0].value = objData.restQT || "0";
    $('.orderSpread .etc')[0].value = objData.etc;

    if(objData.image && objData.image.length > 0){
        $('.orderSpread .image')[0].src = objData.image;
    }
}
function onTypeSelectChange(){
    var orderSelect = document.getElementById("orderType");  
  
    // select element에서 선택된 option의 value가 저장된다.  
    var orderType = orderSelect.options[orderSelect.selectedIndex].value;
    if(orderType != 'none'){
        orderData.orderType = orderType;
        if(orderType == "01"){
            $('.recvWaitQ').removeAttr('disabled');
            $('.deliWaitQ').attr('disabled',true);
        }
        else if(orderType == "03"){
            $('.recvWaitQ').attr('disabled',true);
            $('.deliWaitQ').removeAttr('disabled');
        }
    }
    else{
        $('.recvWaitQ').attr('disabled',true);
        $('.deliWaitQ').attr('disabled',true);
    }
}

function onPlaceSelectChange(){
    var orderSelect = document.getElementById("orderPlace");  
  
    // select element에서 선택된 option의 value가 저장된다.  
    var orderPlaceName = orderSelect.options[orderSelect.selectedIndex].text;
    var orderPlace = orderSelect.options[orderSelect.selectedIndex].value;
    
    if(orderType != 'none'){
        debugger
        orderData.orderType = orderType;
        if(orderType == "01"){
            $('.recvWaitQ').removeAttr('disabled');
            $('.deliWaitQ').attr('disabled',true);
        }
        else if(orderType == "03"){
            $('.recvWaitQ').attr('disabled',true);
            $('.deliWaitQ').removeAttr('disabled');
        }
    }
    else{
        $('.recvWaitQ').attr('disabled',true);
        $('.deliWaitQ').attr('disabled',true);
    }
}

function onOrderClick(){
    if(orderData.orderType && orderData.orderType != 'none'){
        orderData.name = objData.name;           // 상품명
        orderData.price = objData.price;         // 가격
        orderData.inputCd = objData.inputCd;     // 입력바코드
        // orderData.orderType = "";                // 주문 유형
        orderData.orderDT = new Date().toISOString().replaceAll('-','').replaceAll(':','').split('.')[0]; // 주문 날짜
        orderData.etc = $('.orderSpread .etc')[0].value; // 기타(메모)
        // 입고
        if(orderData.orderType == "01"){
            orderData.orderPlace = "";                                // 주문 지점 /// TODO
            orderData.recvQT = $('.orderSpread .recvWaitQ')[0].value; // 입고 수량
        }
        // 출고
        else if(orderData.orderType == "03"){
            orderData.deliQT = $('.orderSpread .recvWaitQ')[0].value; // 출고 수량
        }

        debugger
        dataManager.requestApi(RQ_INSERT_ORDER_LIST,orderData,function(data,result){
            debugger
            if(result == 'success' && data.length > 0){
                arrOrders = data;
                createPage(arrOrders, $('.dataTable'), null, pageNum);
                createPagination(arrOrders, pageSize, $('.pageArea'));
            }
        });
    }
}