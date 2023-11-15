var arrProduct = [];
var pageNum = 0;
var pageSize = 10;

$(document).ready(function(){
    dataManager.requestApi(RQ_SELECT_TRASH, null, function(data,result){
        debugger
        if(result == 'success' && data.length > 0){
            // arrProduct = data;
            // createPage(arrProduct, $('.dataTable'), $('.dataGroup'), 'headerRow', 'dataRow', pageNum, dispType, onItemClick);
            // createPagination(arrProduct, pageSize, $('.pageArea'),onProductPageClick);
        }
    });
});

function onTrashTypeChange(){
    debugger
}