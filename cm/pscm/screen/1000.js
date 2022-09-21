var arrProduct = [];
var pageNum = 0;
var pageSize = 10;

var dispType = "LIST";

$(document).ready(function(){
    dataManager.requestApi(RQ_SELECT_PRODUCTS,null,function(data,result){
        if(result == 'success' && data.length > 0){
            arrProduct = data;
            createPage(arrProduct, $('.dataTable'), $('.dataGroup'), 'headerRow', 'dataRow', pageNum, dispType, onItemClick);
            createPagination(arrProduct, pageSize, $('.pageArea'),onProductPageClick);
        }
    });
});

function onProductPageClick(event){
    if(!event.target.classList.contains('active')){
        var idx = event.target.classList[0].split('_')[1];
        var activePageBtn = $('.btn_pageIdx.active');
        if(activePageBtn.length > 0) activePageBtn.removeClass('active');
        
        event.target.classList.add("active");
        createPage(arrProduct, $('.dataTable'), $('.dataGroup'), 'headerRow', "dataRow", parseInt(idx), dispType, onItemClick);
    }
}
function changeType(){
    if(!event.target.classList.contains('active')){
        if(event.target.classList.contains('btn_list')) dispType = "LIST";
        else dispType = "IMAGE";

        
        if(dispType == "IMAGE") $('.btn_delete').css('display','none');
        else $('.btn_delete').css('display','block');

        $('.topArea .active').removeClass('active');
        event.target.classList.add('active');
        
        createPage(arrProduct, $('.dataTable'), $('.dataGroup'), 'headerRow', "dataRow", pageNum, dispType, onItemClick);
    }
}

function onItemClick(){
    var idx = event.target.getAttribute('dataIdx');
    var data = arrProduct[idx];
    if(!event.target.classList.contains("check") && event.target.type != 'checkbox'){
        data['type'] = "UPDATE";
        openDialog('product',data,(res) => {
            if(res){
                location.reload();
            }
        });
    }
}

function onItemDelete(){
    var arrChk = $('.chk_delete');
    var arrDelete = [];
    for(var i = 0; i < arrChk.length; i++){
        if(arrChk[i].checked) {
            arrDelete.push(arrProduct[arrChk[i].parentElement.getAttribute('dataIdx')])
        }
    }
    if(arrDelete.length > 0){
        if(confirm("정말 삭제하시겠습니까?")){
            dataManager.requestApi(RQ_DELETE_PRODUCTS,{items:arrDelete},function(data,result){
                if(result == 'success'){
                    alert('삭제 완료');
                }
                else alert(result);
            });
        }else{ }        
    }
}

function onItemAdd(){
    openDialog('product',{ type : "INSERT"});
}

function pagePrev(){
    if(pageNum == 0) return;
    else pageNum--;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');    
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrProduct, $('.dataTable'), $('.dataGroup'), 'headerRow', "dataRow", pageNum, dispType, onItemClick);
}

function pageNext(){
    var lastIdx = (pageNum + 1) * pageSize;
    if(arrProduct.length < lastIdx) return;
    else pageNum++;

    var activePageBtn = $('.btn_pageIdx.active');
    if(activePageBtn.length > 0) activePageBtn.removeClass('active');
    $('.page_' + pageNum)[0].classList.add("active");

    createPage(arrProduct, $('.dataTable'), $('.dataGroup'), 'headerRow', "dataRow", pageNum, dispType, onItemClick);
}