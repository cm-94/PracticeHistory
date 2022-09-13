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
    if(type == DATA_URL_PRODUCT){
        requestApi('/products',"GET",{},function(data,textStatus){
            if(textStatus ='success' && data && data.length > 0){
                dataManager.products = data;
            }else{
                dataManager.products;
            }
            if(callback) callback(dataManager.products);
        });
    }    
}

const DATA_URL_PRODUCT = "./published/data/product.txt";
const DATA_URL_ORDER = "./published/data/order.txt";

const DATA_TPYE_PRODUCT = "DATA_TPYE_PRODUCT"
