const RQ_SELECT_PRODUCTS = {url : '/products', type : "GET"};
const RQ_INSERT_PRODUCTS = {url : '/productsInsert', type : "POST"};
const RQ_UPDATE_PRODUCTS = {url : '/productsUpdate', type : "POST"};
const RQ_DELETE_PRODUCTS = {url : '/productsDelete', type : "POST"};

const RQ_SELECT_STOCKS = {url : '/stocks', type : "GET"};
const RQ_UPDATE_STOCKS = {url : '/stocksUpdate', type : "POST"};

const RQ_SELECT_ORDER_LIST = {url : '/orderlist', type : "GET"};
const RQ_INSERT_ORDER_LIST = {url : '/orderlistInsert', type : "POST"};
const RQ_UPDATE_ORDER_LIST = {url : '/orderlistUpdate', type : "POST"};
const RQ_DELETE_ORDER_LIST = {url : '/orderlistDelete', type : "POST"};

const RQ_SELECT_ORDERPLACE = {url : '/orderplace', type : "GET"};
const RQ_INSERT_ORDERPLACE = {url : '/orderplaceInsert', type : "POST"};
const RQ_UPDATE_ORDERPLACE = {url : '/orderplaceUpdate', type : "POST"};
const RQ_DELETE_ORDERPLACE = {url : '/orderplaceDelete', type : "POST"};

const RQ_SELECT_TRASH = {url : '/trash', type : "GET"};
const RQ_INSERT_TRASH = {url : '/trashInsert', type : "POST"};
const RQ_UPDATE_TRASH = {url : '/trashUpdate', type : "POST"};
const RQ_DELETE_TRASH = {url : '/trashDelete', type : "POST"};

var dataManager = {
    products : [],
    orders : [],
    markets : [],
}

dataManager.requestApi = function(option,data,callback) {
    requestApi(option,data,function(data,status){
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