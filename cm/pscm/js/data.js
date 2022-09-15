const RQ_SELECT_PRODUCTS = {url : '/products', type : "GET"};
const RQ_INSERT_PRODUCTS = {url : '/productsInsert', type : "POST"};
const RQ_UPDATE_PRODUCTS = {url : '/productsUpdate', type : "POST"};
const RQ_DELETE_PRODUCTS = {url : '/productsDelete', type : "POST"};

var dataManager = {
    products : [],
    orders : [],
    markets : [],
}

dataManager.getData = function(option,callback) {
    if(!option || !option.url || !option.type){
        if(callback) callback(null,"error");
    }
    requestApi(option,function(data,status){
        if(status == "success") onRequestSuccess(data,status,option,callback);
        else onRequestError(data,status,option,callback);
    });
}

function onRequestSuccess(data, status, option, callback){
    if(option.url.indexOf('/products') > -``){
        dataManager.products = data;
    }
    else if(option.url.indexOf('/orders') > -1){
        dataManager.orders = data;
    }

    if(callback) callback(data,status);
}

function onRequestError(data, status, type, callback){
    if(callback) callback(null,status);
}

// $.when(
//     $.get(type),
// ).done(function (data, result) {
//     debugger
//     if (result == "success") {
//         if(type == DATA_URL_PRODUCT) processFile(data,DATA_TPYE_PRODUCT);
//         else if(type == DATA_URL_PRODUCT) processFile(data,DATA_TPYE_ORDER);
//         else if(type == DATA_URL_PRODUCT) processFile(data,DATA_TPYE_MARKET);

//         if(callback) callback(dataManager, result);
//     }
// })

// const DATA_URL_PRODUCT = "./published/data/product.txt";
// const DATA_URL_ORDER = "./published/data/order.txt";
// const DATA_URL_MARKET = "./published/data/market.txt";

// const DATA_TPYE_PRODUCT = "DATA_TPYE_PRODUCT"
// const DATA_TPYE_ORDER = "DATA_TPYE_ORDER"
// const DATA_TPYE_MARKET = "DATA_TPYE_MARKET"


// function processFile(data,type){
//     var lines = data.split('\r');
//     if(type == DATA_TPYE_PRODUCT) dataManager.products = [];
//     else if(type == DATA_TPYE_ORDER) dataManager.orders = [];
//     else if(type == DATA_TPYE_MARKET) dataManager.markets = [];

//     for(var i = 0; i < lines.length; i++){
//         var line = lines[i];
//         var arrData = line.split('|');
        
//         for(var j = 0; j < arrData.length; j++){
//             if(type == DATA_TPYE_PRODUCT) dataManager.products.push({
//                 name : arrData[0],
//                 price : arrData[1],
//                 inputCd : arrData[2],
//                 outputCd : arrData[3],
//                 etc : arrData[4],
//                 image : arrData[5],
//             });
//             else if(type == DATA_TPYE_ORDER) dataManager.orders.push({
//                 name : arrData[0],
//                 price : arrData[1],
//                 inputCd : arrData[2],
//                 outputCd : arrData[3],
//                 etc : arrData[4],
//                 image : arrData[5],
//             });
//             else if(type == DATA_TPYE_MARKET) dataManager.markets.push({
//                 name : arrData[0],
//                 price : arrData[1],
//                 inputCd : arrData[2],
//                 outputCd : arrData[3],
//                 etc : arrData[4],
//                 image : arrData[5],
//             });
//         }
//     }
// }