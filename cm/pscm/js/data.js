var dataManager = {
    products : [],
    orders : [],
    orders : [],
}

dataManager.putData = function(inputData,type){
    debugger
    var path = type;
    writeFile(path,inputData,function(err){
        debugger
    });
}

dataManager.getData = function(type,callback) {
    var url = type;
    $.when(
        $.get(url),
    ).done(function (data, result) {
        if (result == "success") {
            if(url == DATA_URL_PRODUCT){
                dataManager.products = [];
                processFile(data,DATA_TPYE_PRODUCT);
                if(callback) callback(dataManager.products);
            }
        }
    })
}

const processFile = function(data,type){
    if(!data || data.length == 0){
        return null;
    }
    var lines = data.split('\n');
    
    for(var i = 0; i < lines.length; i++){
        if(type == DATA_TPYE_PRODUCT && lines[i].indexOf('|') > -1){
            var arrData = lines[i].split('|');
            dataManager.products.push(
                {
                    name : arrData[0],
                    price : arrData[1],
                    inputCd : arrData[2],
                    outputCd : arrData[3],
                    etc : arrData[4],
                }
            )
        }    
    }
}

const DATA_URL_PRODUCT = "./published/data/product.txt";
const DATA_URL_ORDER = "./published/data/order.txt";

const DATA_TPYE_PRODUCT = "DATA_TPYE_PRODUCT"