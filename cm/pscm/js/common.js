var dialogData;

var putThousandSeparate = function(num,type){
    const numberFormatter = Intl.NumberFormat('en-US');
    const formatted = numberFormatter.format(num);
    return formatted;
}

var openDialog = function(type,data){
    dialogData = data;

    var dialog = $('.dialog');

    var dialog_main = document.createElement('div');
    dialog_main.classList.add('dialog_main');
    dialog.append(dialog_main);

    var header = document.createElement('div');
    header.classList.add('dialog_header');
    dialog_main.append(header);
    
    var btnClose = document.createElement('button');
    btnClose.textContent = "닫기";
    btnClose.addEventListener('click',closeDialog);
    header.append(btnClose);

    var body = document.createElement('div');
    body.classList.add('dialog_body');
    dialog_main.append(body);

    $('.dialog_body').load(`./screen/${type}Dialog.html`);
    dialog.css('display','block');
}

var closeDialog = function(){
    dialogData = null;
    $('.dialog_main').remove();
    $('.dialog').css('display','none');
}


var getUrl = function(url){
    return 'http://' + location.hostname +  ":3306" + url;
}

var requestApi = function(url,type,data,callback){
    $.ajax({
        url: getUrl(url), //request 보낼 서버의 경로
        type: type,       // 메소드(get, post, put 등)
        data: data,
        success: function (data,textStatus) {
            if(callback) callback(data,textStatus)
        },
        error: function(err) {
            if(callback) callback(err)
        }
    });
}