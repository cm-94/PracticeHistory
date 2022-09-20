var getUrl = function(url){
    return 'http://' + location.hostname +  ":3306" + url;
}

var requestApi = function(option,inputData,callback){
    if(!option || !option.url || !option.type){
        if(callback) callback(null,'error');
        return;
    }
    showDialog(true);
    $.ajax({
        url: getUrl(option.url),
        type: option.type,
        data: inputData || {},
        success: function (data,textStatus) {
            showDialog(false);
            if(callback) callback(data,textStatus)
        },
        error: function(err,textStatus) {
            showDialog(false);
            if(callback) callback(err)
        }
    });
}