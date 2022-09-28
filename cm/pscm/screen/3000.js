var arrPlaces = [];
var pageNum = 0;
var pageSize = 10;

$(document).ready(function(){
    dataManager.requestApi(RQ_SELECT_ORDERPLACE,null,function(data,result){
        if(result == 'success' && data.length > 0){
            arrPlaces = data;
            createPage(arrPlaces, $('.dataTable'), null, 'headerRow', 'dataRow', pageNum, null, onItemClick);
            createPagination(arrPlaces, pageSize, $('.pageArea'),onMarketPageClick);
        }
    });
});

var onColumnRender = function(td,tdClass,text,data){
    if(tdClass == "entranceFee"){
        return { text : putThousandSeparate(data['entranceFee']) + data['entranceType'] }
    }
    else if(tdClass == "salesCommi"){
        return { text : putThousandSeparate(data['salesCommi']) + data['salesCommiType'] }
    }
    else if(tdClass == "sattleDate"){
        if(data['sattleDate']) return { text : data['sattleDate'] + '일' }
        else return { text : '-' }
    }
    else if(tdClass == "conTerm"){
        if(data['conTerm'] && data['conTerm'].length > 0) return { text : data['conTerm'] };
        else return { text : "-" };
    }
}

function onMarketPageClick(event){
    if(!event.target.classList.contains('active')){
        var idx = event.target.classList[0].split('_')[1];
        var activePageBtn = $('.btn_pageIdx.active');
        if(activePageBtn.length > 0) activePageBtn.removeClass('active');
        
        event.target.classList.add("active");
        pageNum = parseInt(idx);
        createPage(arrPlaces, $('.dataTable'), null, 'headerRow', "dataRow", pageNum, null, onItemClick);
    }
}

function onItemClick(){
    var idx = event.target.getAttribute('dataIdx');
    var data = arrPlaces[idx];
    if(!event.target.classList.contains("check") && event.target.type != 'checkbox'){
        data['type'] = "UPDATE";
        openDialog('place',data,(res) => {
            if(res){
                dataManager.requestApi(RQ_SELECT_ORDERPLACE,null,function(data,result){
                    if(result == 'success' && data.length > 0){
                        arrPlaces = data;
                        createPage(arrPlaces, $('.dataTable'), null, 'headerRow', 'dataRow', pageNum, null, onItemClick);
                    }
                });
            }
        });
    }
}

function onItemAdd(){
    openDialog('place',{ type : "INSERT"},(res) => {
        if(res){
            dataManager.requestApi(RQ_SELECT_ORDERPLACE,null,function(data,result){
                if(result == 'success' && data.length > 0){
                    arrPlaces = data;
                    createPage(arrPlaces, $('.dataTable'), null, 'headerRow', 'dataRow', pageNum, null, onItemClick);
                }
            });
        }
    });
}

function onItemDelete(){
    var arrChk = $('.chk_delete');
    var arrDelete = [];
    for(var i = 0; i < arrChk.length; i++){
        if(arrChk[i].checked) {
            arrDelete.push(arrPlaces[arrChk[i].parentElement.getAttribute('dataIdx')])
        }
    }
    if(arrDelete.length > 0){
        if(confirm("정말 삭제하시겠습니까?")){
            dataManager.requestApi(RQ_DELETE_ORDERPLACE,{items:arrDelete},function(data,result){
                if(result == 'success'){
                    alert('삭제 완료');
                    dataManager.requestApi(RQ_SELECT_ORDERPLACE,null,function(data,result){
                        if(result == 'success' && data.length > 0){
                            arrPlaces = data;
                            createPage(arrPlaces, $('.dataTable'), null, 'headerRow', 'dataRow', pageNum, null, onItemClick);
                        }
                    });
                }
                else alert(result);
            });
        }else{ }        
    }
}

function pagePrev(){
    if(pageNum == 0) return;
    else pageNum--;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');    
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrPlaces, $('.dataTable'), null, 'headerRow', "dataRow", pageNum, null, onItemClick);
}

function pageNext(){
    var lastIdx = (pageNum + 1) * pageSize;
    if(arrPlaces.length < lastIdx) return;
    else pageNum++;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrPlaces, $('.dataTable'), null, 'headerRow', "dataRow", pageNum, null, onItemClick);
}