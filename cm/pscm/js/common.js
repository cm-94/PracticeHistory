var dialogData;
var dialogCb;

var subscreenData;

var spreadType;
var spreadCb;

var moveScreen = function(screenId){
    $('.contentScreen').remove();
    var screenNo = screenId.replaceAll('#','');
    $('.contents').load(`./screen/${screenNo}.html`);
}

var putThousandSeparate = function(num,type){
    const numberFormatter = Intl.NumberFormat('en-US');
    const formatted = numberFormatter.format(num);
    if(formatted == 'NaN') return null;
    else return formatted;
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

    if(dialogCb) dialogCb(state);

    dialogData = null;
    dialogCb = null;
}

function showDialog(state){
    if(state) $('.dialog_loading').css('display','block');
    else $('.dialog_loading').css('display','none')
}

function createPage(data, table, group, header, child, pageIdx, dispType, onItemClick){
    if(data.length == 0) return;
    var startIdx = pageIdx * pageSize;
    var endIdx = startIdx + pageSize;

    if($('.' + child).length > 0) $('.' + child).remove();
    if($('.gItem').length > 0) $('.gItem').remove();
    if($('.spreadRow').length > 0) $('.spreadRow').remove();        

    if(!dispType || dispType == "LIST"){
        table.css('display','table');
        if(group) group.css('display','none');

        for (var i = startIdx; i < endIdx; i++){
            if(!data[i]) break;

            var dataRow = document.createElement('tr');
            dataRow.classList.add(child);
            table.append(dataRow);

            var arrTd = document.getElementsByClassName(header)[0].children;
            
            for(var j = 0; j < arrTd.length; j++){
                var td = arrTd[j].cloneNode(true);
                var tdClass = arrTd[j].classList[0]
                var text = data[i][tdClass];

                if(tdClass == 'id' || tdClass == 'image') continue;
                
                if(onColumnRender){
                    onColumnRender(td,tdClass,text,data[i]);
                    continue;
                }
                
                if(tdClass.indexOf("check") > -1){
                    td.children[0].classList.add("chk_" + i);
                    td.children[0].classList.add("chk_delete");
                    td.children[0].onchange = null;
                }
                else if(tdClass.indexOf("btn") > -1){
                    td.children[0].classList.add("btn_" + i);
                }
                else if(tdClass.indexOf("DT") > -1){
                    td.textContent = setDate(text);
                }
                else{
                    if(!text && text != 0){
                        td.textContent = '-';
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
        if(onItemClick) $('.' + child).on('click',onItemClick);
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
        if(onItemClick) $('.' + child).on('click',onItemClick);
    }
}

function createPagination(data, pageSize, pageArea, onClickListener){
    if(data.length == 0) return;

    var lastIdx = Math.ceil(data.length / pageSize);
    
    
    for(var i = 0; i < lastIdx; i++){
        var btnPage = document.createElement('div');
        btnPage.textContent = (i+1);
        btnPage.classList.add(`page_${i}`);
        btnPage.classList.add(`btn_pageIdx`);
        if(i == 0) btnPage.classList.add(`active`);
        if(onClickListener) btnPage.addEventListener('click',onClickListener);
        pageArea.append(btnPage);
    }
}

// 헤더 체크박스 클릭 이벤트
var onCheckHeaderClick = function(child){
    var arrChk = $(event.target.parentElement.parentElement.parentElement).find(`.${child} > td > input`);
    
    for(var i = 0; i < arrChk.length; i++){
        arrChk[i].checked = event.target.checked;
    }
}

// 그리드 상세 펼치기 / 닫기
var spreadPage = function(data,pNode,screenId,type,cb){
    if(cb) spreadCb = cb;

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

        if(type) spreadType = type;
        else spreadType = "CREATE";

        var td = document.createElement('td');
        td.colSpan = colSpan;
        spreadRow.append(td);

        $(td).load(`./subscreen/${screenId}Screen.html`,function(){
        }).hide().fadeIn(200);
    }
    // 접기
    else{
        if($('.spreadRow').length > 0){
            spreadType = null;
            subscreenData = null;
            $('.spreadRow').fadeOut(200,function(){
                $('.spreadRow').remove();
            });
            if(spreadCb) spreadCb(true);
            spreadCb = null;
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

  function setDate(date){
    if(date && date.length > 8){
        return `${date.substring(0,4)}/${date.substring(4,6)}/${date.substring(6,8)}`
    }
    return '-'
  }