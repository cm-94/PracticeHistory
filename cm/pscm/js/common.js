var dialogData;
var dialogCb;

var subscreenData;

var moveScreen = function(screenId){
    $('.contentScreen').remove();
    var screenNo = screenId.replaceAll('#','');
    $('.contents').load(`./screen/${screenNo}.html`);
}

var putThousandSeparate = function(num,type){
    const numberFormatter = Intl.NumberFormat('en-US');
    const formatted = numberFormatter.format(num);
    return formatted;
}

var openDialog = function(type,data,callback){
    dialogData = data;

    var dialog = $('.dialog');

    var dialog_main = document.createElement('div');
    dialog_main.classList.add('dialog_main');
    dialog.append(dialog_main);

    var header = document.createElement('div');
    header.classList.add('dialog_header');
    header.style.display = 'flex';
    dialog_main.append(header);
    
    var btnClose = document.createElement('button');
    btnClose.style.marginLeft = 'auto';
    btnClose.classList.add('dialog_btnClose');
    btnClose.addEventListener('click',closeDialog);
    header.append(btnClose);

    var body = document.createElement('div');
    body.classList.add('dialog_body');
    dialog_main.append(body);

    $('.dialog_body').load(`./subscreen/${type}Dialog.html`);
    dialog[0].classList.toggle('act');
    dialogCb = callback;
}

var closeDialog = function(state){
    $('.dialog_main').remove();
    $('.dialog')[0].classList.toggle('act');
    if(dialogCb && state == true) dialogCb(state);

    dialogData = null;
    dialogCb = null;
}

function showDialog(state){
    if(state) $('.dialog_loading').css('display','block');
    else $('.dialog_loading').css('display','none')
}

function createPage(data, table, group, pageIdx, dispType){
    if(data.length == 0) return;
    pageNum = pageIdx;
    var startIdx = pageNum * pageSize;
    var endIdx = startIdx + pageSize;

    if($('.dataRow').length > 0) $('.dataRow').remove();
    if($('.gItem').length > 0) $('.gItem').remove();
    if($('.spreadRow').length > 0) $('.spreadRow').remove();        

    if(!dispType || dispType == "LIST"){
        table.css('display','table');
        if(group) group.css('display','none');

        for (var i = startIdx; i < endIdx; i++){
            if(!data[i]) break;

            var dataRow = document.createElement('tr');
            dataRow.classList.add('dataRow');
            table.append(dataRow);

            var arrTd = document.getElementsByClassName('headerRow')[0].children;
            
            for(var j = 0; j < arrTd.length; j++){
                var td = document.createElement('td');
                var tdClass = arrTd[j].classList[0]
                var text = data[i][tdClass];

                if(tdClass == 'id' || tdClass == 'image') continue;

                td.classList.add(tdClass);
                if(tdClass.indexOf("check") > -1){
                    var chk = document.createElement('input');
                    chk.classList.add("chk_" + i);
                    chk.classList.add("chk_delete");
                    chk.type = "checkbox";
                    td.append(chk);
                }
                else{
                    if(!text && text != 0){
                        // td.textContent = '-';
                    }
                    else if(tdClass == "price"){
                        td.textContent = putThousandSeparate(text) + '원';
                    }
                    else if(tdClass.indexOf("QT") > -1){
                        td.textContent = putThousandSeparate(text) + '개';
                    }
                    else{
                        td.textContent = text;
                    }
                }
                
                td.setAttribute('dataIdx',i);
                dataRow.append(td);
            }
        }
    }
    else{
        if(table) table.css('display','none');
        group.css('display','flex');

        for (var i = startIdx; i < endIdx; i++){
            if(!data[i]) break;

            var dataItem = document.createElement('div');
            dataItem.classList.add('gItem');
            group.append(dataItem);

            
            var itemImage = document.createElement('img');
            itemImage.classList.add("image");

            
            if(data[i]['image'].length > 0){
                itemImage.src = data[i]['image'];
            }

            var itemName = document.createElement('div');
            itemName.textContent = data[i]['name'];
            itemName.classList.add("name");
            
            var itemPrice = document.createElement('div');
            itemPrice.textContent = putThousandSeparate(data[i]['price']) + '원';
            itemPrice.classList.add("price");

            itemImage.setAttribute('dataIdx',i);
            itemName.setAttribute('dataIdx',i);
            itemPrice.setAttribute('dataIdx',i);

            dataItem.append(itemImage);
            dataItem.append(itemName);
            dataItem.append(itemPrice);
        }
    }
    $('.dataRow').on('click',onItemClick);
    $('.gItem').on('click',onItemClick);
}

function createPagination(data, pageSize, pageArea){
    if(data.length == 0) return;

    var lastIdx = Math.ceil(data.length / pageSize);
    
    for(var i = 0; i < lastIdx; i++){
        var btnPage = document.createElement('div');
        btnPage.textContent = (i+1);
        btnPage.classList.add(`page_${i}`);
        btnPage.classList.add(`btn_pageIdx`);
        if(i == 0) btnPage.classList.add(`active`);
        btnPage.addEventListener('click',onBtnPageClick);
        pageArea.append(btnPage);
    }
}

// 헤더 체크박스 클릭 이벤트
var onCheckHeaderClick = function(){
    var arrChk = $(event.target.parentElement.parentElement.parentElement).find('.dataRow > td > input');
    
    for(var i = 0; i < arrChk.length; i++){
        arrChk[i].checked = event.target.checked;
    }
}

// 그리드 상세 펼치기 / 닫기
var spreadPage = function(data,pNode,screenId){
    // 펼치기
    if(data){
        subscreenData = data;
        
        if($('.spreadRow').length > 0){
            $('.spreadRow').remove();

            if($('table .arrow_up').length > 0){
                $('table .arrow_up').addClass('arrow_down');
                $('table .arrow_up').removeClass('arrow_up');
            }
        }
        var colSpan = pNode.children.length;
        var spreadRow = document.createElement('tr');
        spreadRow.classList.add('spreadRow');
        spreadRow.style.minHeight = pNode.offsetHeight + 'px';
        insertAfter(pNode,spreadRow);

        var td = document.createElement('td');
        td.colSpan = colSpan;
        spreadRow.append(td);

        $(td).load(`./subscreen/${screenId}Screen.html`,function(){
        }).hide().fadeIn(200);
    }
    // 접기
    else{
        if($('.spreadRow').length > 0){
            subscreenData = null;

            $('.spreadRow').fadeOut(200,function(){
                $('.spreadRow').remove();
            });
        }
    }    
}

function insertAfter(referenceNode, newNode) {   
    if (!!referenceNode.nextSibling) {
      referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
    } else {
      referenceNode.parentNode.appendChild(newNode);
    }  
  }