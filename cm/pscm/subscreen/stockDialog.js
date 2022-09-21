var inputCd = "";

var arrOrders = [
    {

    },
];

var stockPageNum = 0;
var stockPageSize = 10;

$(document).ready(function(){
    if(dialogData) {
        inputCd = dialogData.inputCd;
        dataManager.requestApi(RQ_SELECT_ORDER_LIST, { inputCd : inputCd }, function(data,result){
            if(result == 'success' && data.length > 0){
                debugger
                arrOrders = data;
                createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum, null, onStockItemClick);
                createPagination(arrOrders, stockPageSize, $('.pageArea2'), onOrderDetailPageClick);
            }
        });
    }
});

function onStockItemClick(){
    var idx = event.target.getAttribute('dataIdx');
    var item = arrOrders[idx];
    debugger
}

function onOrderDetailPageClick(){

}

function onItemChange(){
    var idx = event.target.classList[1].split('_')[1];
    var item = arrOrders[idx];
    if(confirm(`${item.name} - 수정 하시겠습니까?`)){
        dataManager.requestApi(RQ_UPDATE_ORDER_LIST, item, function(data,result){
            if(result == 'success'){
                alert('수정 완료');
            }
            else alert(result);
        });
    }else{ }
    debugger
}

function onItemConfirm(){
    var idx = event.target.classList[1].split('_')[1];
    var item = arrOrders[idx];
    if(confirm(`${item.name} - 확정 하시겠습니까?`)){
        if(item.orderType == "01") {
            item.orderType = "02";
            item.recvDT = new Date().toISOString().replaceAll('-','').replaceAll(':','').split('.')[0]; // 주문 날짜
        }
        else if(item.orderType == "03") {
            item.orderType = "04";
            item.deliDT = new Date().toISOString().replaceAll('-','').replaceAll(':','').split('.')[0]; // 주문 날짜
        }

        dataManager.requestApi(RQ_UPDATE_ORDER_LIST, item, function(data,result){
            if(result == 'success'){
                alert('확정 완료');
            }
            else alert(result);
        });
    }else{ }
    debugger
}

function onItemDelete(){
    var idx = event.target.classList[1].split('_')[1];
    var item = arrOrders[idx];
    if(confirm(`${item.name} - 삭제 하시겠습니까?`)){
        dataManager.requestApi(RQ_DELETE_ORDER_LIST, item, function(data,result){
            if(result == 'success'){
                alert('삭제 완료');
            }
            else alert(result);
        });
    }else{ }
    debugger
}


function stockPagePrev(){
    debugger
    if(stockPageNum == 0) return;
    else stockPageNum--;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');    
    $('.page_' + stockPageNum)[0].classList.add("active");

    createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum);
}

function stockPageNext(){
    debugger
    var lastIdx = (stockPageNum + 1) * stockPageSize;
    if(arrOrders.length < lastIdx) return;
    else stockPageNum++;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');
    $('.page_' + stockPageNum)[0].classList.add("active");

    createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum);
}