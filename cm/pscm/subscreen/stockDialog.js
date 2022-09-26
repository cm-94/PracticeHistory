var inputCd = "";   /// 상품 바코드
var arrOrders = []; /// 주문내역
var arrPlaces = []; /// 입점처 목록

var stockPageNum = 0;
var stockPageSize = 10;

$(document).ready(function(){
    if(dialogData) {
        inputCd = dialogData.inputCd;

        dataManager.requestApi(RQ_SELECT_ORDERPLACE,null,function(data,result){
            if(result == 'success' && data.length > 0){
                arrPlaces = data;

                dataManager.requestApi(RQ_SELECT_ORDER_LIST, { inputCd : inputCd }, function(data,result){
                    if(result == 'success' && data.length > 0){
                        arrOrders = data;
                        arrOrders.forEach(function(data,idx){
                            var result = arrPlaces.filter(market => market.placeCd == data.placeCd);
                            if(result.length > 0) arrOrders[idx].placeName = result[0].name || "-";
                            else arrOrders[idx]['placeName'] = "-";
                        })
                        
                        createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum, null, onStockItemClick);
                        createPagination(arrOrders, stockPageSize, $('.pageArea2'), onOrderDetailPageClick);
                    }
                });
            }
        });
    }
});

function onStockItemClick(){
    var idx = event.target.getAttribute('dataIdx');
    var item = arrOrders[idx];
    var tr = event.target;

    if(item && item.orderType != "00" && (!tr.classList.contains('btn_confirm') || !tr.classList.contains('btn_change'))){
        dataManager.requestApi(RQ_SELECT_STOCKS, { inputCd : item.inputCd }, function(data,result){
            if(result == 'success'){
                if(data.length > 0){
                    if($('.spreadRow').length > 0){
                        $('.spreadRow').remove();
                    }
                    item.restQT = data[0].restQT
                    spreadPage(item,tr.parentElement,'orderDetail',RQ_UPDATE_ORDER_LIST,function(res){
                        if(res){
                            stockPageNum = 0;
                            dataManager.requestApi(RQ_SELECT_ORDER_LIST, { inputCd : inputCd }, function(data,result){
                                if(result == 'success' && data.length > 0){
                                    arrOrders = data;
                                    createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum, null, onStockItemClick);
                                }
                            });
                        }
                    });
                }
            }
            else alert(result);
        });
    }
}

function onOrderDetailPageClick(){
    if(!event.target.classList.contains('active')){
        var idx = event.target.classList[0].split('_')[1];
        var activePageBtn = $('.stockDetailList').find('.btn_pageIdx.active');
        if(activePageBtn.length > 0) activePageBtn.removeClass('active');
        stockPageNum = parseInt(idx);
        event.target.classList.add("active");
        createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", parseInt(idx), null, onItemClick);
    }
}

function onItemConfirm(){
    if(event.target.classList.length > 1){
        var idx = event.target.classList[1].split('_')[1];
        var item = arrOrders[idx];
        if(item.orderType != '00'){
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
                        dataManager.requestApi(RQ_SELECT_ORDER_LIST, { inputCd : inputCd }, function(data,result){
                            if(result == 'success' && data.length > 0){
                                arrOrders = data;
                                createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum, null, onStockItemClick);
                            }
                        });
                    }
                    else alert(result);
                });
            }else{ }
        }
    }
}

function onItemDelete(){
    var idx = event.target.classList[1].split('_')[1];
    var item = arrOrders[idx];
    if(item.orderType != '00'){
        if(confirm(`${item.name} - 삭제 하시겠습니까?`)){
            dataManager.requestApi(RQ_DELETE_ORDER_LIST, item, function(data,result){
                if(result == 'success'){
                    alert('삭제 완료');
                }
                else alert(result);
            });
        }else{ }
    }
}

function stockPagePrev(){
    if(stockPageNum == 0) return;
    else stockPageNum--;

    var activePageBtn = $('.stockDetailList').find('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');    
    $('.stockDetailList').find('.page_' + stockPageNum)[0].classList.add("active");

    createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum);
}

function stockPageNext(){
    var lastIdx = (stockPageNum + 1) * stockPageSize;
    if(arrOrders.length < lastIdx) return;
    else stockPageNum++;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');
    $('.stockDetailList').find('.page_' + stockPageNum)[0].classList.add("active");

    createPage(arrOrders, $('.dataTable2'), null, 'headerRow2', "dataRow2", stockPageNum);
}