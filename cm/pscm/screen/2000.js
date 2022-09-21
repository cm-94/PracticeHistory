var arrStocks = [];
// {
//     name : "",      // 상품명
//     price : "",     // 가격
//     inputCd : "",   // 입력바코드
//     restQT : "",    // 재고수량
//     recvWaitQT : "", // 입고 대기
//     deliWaitQT : "", // 출고 대기
//     etc : "" ,      // 기타
//     image : ""      // 이미지
// }

var pageNum = 0;
var pageSize = 10;

$(document).ready(function(){
    dataManager.requestApi(RQ_SELECT_STOCKS,null,function(data,result){
        if(result == 'success' && data.length > 0){
            arrStocks = data;
            createPage(arrStocks, $('.dataTable'), null, 'headerRow', "dataRow", pageNum, null, onItemClick);
            createPagination(arrStocks, pageSize, $('.pageArea'),onStockPageClick);
        }
    });
});

function onStockPageClick(event){
    if(!event.target.classList.contains('active')){
        var idx = event.target.classList[0].split('_')[1];
        var activePageBtn = $('.btn_pageIdx.active');
        if(activePageBtn.length > 0) activePageBtn.removeClass('active');
        
        event.target.classList.add("active");
        createPage(arrStocks, $('.dataTable'), null, 'headerRow', "dataRow", parseInt(idx), null, onItemClick);
    }
}

function onItemClick(){
    var tdClass = event.target.classList;
    var idx = event.target.getAttribute('dataIdx');
    var data = arrStocks[idx];

    if(tdClass.contains('arrow_down') || tdClass.contains('arrow_up')){
        if(tdClass.contains('arrow_down')){
            spreadPage(data,event.target.parentElement,'orderDetail');
            $(event.target).removeClass('arrow_down');
            $(event.target).addClass('arrow_up');        
        }
        else if(tdClass.contains('arrow_up')){
            spreadPage(null,event.target.parentElement);
            $(event.target).removeClass('arrow_up');
            $(event.target).addClass('arrow_down');
        }
    }
    else{
        openDialog('stock',{ inputCd : data.inputCd },(res) => {
            if(res){
                location.reload();
            }
        });
        spreadPage(false);
        $('.arrow_up').addClass('arrow_down');
        $('.arrow_up').removeClass('arrow_up');
    }
}

function pagePrev(){
    if(pageNum == 0) return;
    else pageNum--;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');    
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrStocks, $('.dataTable'), null, 'headerRow', "dataRow", pageNum, null, onItemClick);
}

function pageNext(){
    var lastIdx = (pageNum + 1) * pageSize;
    if(arrStocks.length < lastIdx) return;
    else pageNum++;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrStocks, $('.dataTable'), null, 'headerRow', "dataRow", pageNum, null, onItemClick);
}
