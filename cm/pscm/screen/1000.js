var arrProduct = [];
var pageNum = 0;
var pageSize = 10;

$(document).ready(function(){
    dataManager.getData(DATA_URL_PRODUCT,function(res){
        arrProduct = res;
        if(arrProduct.length > 0){
            createPage(arrProduct,0);
        }
    });
});

function createPage(data,page){
    pageNum = page;
    var startIdx = pageNum * pageSize;
    var endIdx = startIdx + pageSize;

    $('.dataRow').remove();

    var table = $('.dataTable');
    debugger

    for (var i = 0; i < endIdx; i++){
        if(!data[i]) break;

        var dataRow = document.createElement('tr');
        dataRow.classList.add('dataRow');
        table.append(dataRow);


        var keys = Object.keys(data[i]);
        for(var j = 0; j < keys.length; j++){
            var td = document.createElement('td');
            var tdClass = keys[j];
            var text = data[i][tdClass];

            td.classList.add(tdClass);
            if(!text || text.length == 0){
                td.textContent = '-';
            }
            else if(tdClass == "price"){
                td.textContent = putThousandSeparate(text) + '원';
            }else{
                td.textContent = text;
            }
            

            dataRow.append(td);
        }

        console.log('data ==>',JSON.stringify(data[i]));
    }
}