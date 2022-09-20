var inputCd = "";

var arrOrders = [
    {

    },
];

var pageNum = 0;
var pageSize = 10;

$(document).ready(function(){
    if(dialogData) {
        inputCd = dialogData.inputCd;
        dataManager.requestApi(RQ_SELECT_ORDERPLACE, { inputCd : inputCd }, function(data,result){
            if(result == 'success' && data.length > 0){
                arrOrders = data;
                createPage(arrOrders, $('.dataTable'), null, pageNum);
                createPagination(arrOrders, pageSize, $('.pageArea'));
            }
        });
    }
});

function onItemClick(){
    var tdClass = event.target.classList;
    var idx = event.target.getAttribute('dataIdx');
    var data = arrStocks[idx];

}

function pagePrev(){
    if(pageNum == 0) return;
    else pageNum--;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');    
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrOrders, $('.dataTable'), null, pageNum);
}

function pageNext(){
    var lastIdx = (pageNum + 1) * pageSize;
    if(arrOrders.length < lastIdx) return;
    else pageNum++;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrOrders, $('.dataTable'), null, pageNum);
}