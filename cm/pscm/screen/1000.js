var arrProduct = [];
var pageNum = 0;
var pageSize = 10;

var dispType = "LIST";

$(document).ready(function(){
    dataManager.getData(DATA_URL_PRODUCT,function(res){
        debugger
        arrProduct = res;
        if(arrProduct.length > 0){
            createPage(arrProduct,pageNum);
        }
    });
});

function createPage(data,page){
    pageNum = page;
    var startIdx = pageNum * pageSize;
    var endIdx = startIdx + pageSize;

    $('.dataRow').remove();
    $('.gItem').remove();

    var table = $('.dataTable');
    var group = $('.dataGroup');

    if(dispType == "LIST"){        
        table.css('display','table');
        group.css('display','none');

        for (var i = startIdx; i < endIdx; i++){
            if(!data[i]) break;

            var dataRow = document.createElement('tr');
            dataRow.classList.add('dataRow');
            table.append(dataRow);

            var keys = Object.keys(data[i]);
            for(var j = 0; j < keys.length; j++){
                var td = document.createElement('td');
                var tdClass = keys[j];
                var text = data[i][tdClass];

                if(tdClass == 'id' || tdClass == 'image') continue;

                td.classList.add(tdClass);
                if(!text || text.length == 0){
                    td.textContent = '-';
                }
                else if(tdClass == "price"){
                    td.textContent = putThousandSeparate(text) + '원';
                }else{
                    td.textContent = text;
                }
                td.setAttribute('dataIdx',i);
                dataRow.append(td);
            }
        }
    }
    else{
        table.css('display','none');
        group.css('display','flex');

        for (var i = startIdx; i < endIdx; i++){
            if(!data[i]) break;

            var dataItem = document.createElement('div');
            dataItem.classList.add('gItem');
            group.append(dataItem);

            
            var itemImage = document.createElement('div');
            itemImage.classList.add("image");
            if(data[i]['image'].length > 0){

            }

            var itemName = document.createElement('div');
            itemName.textContent = data[i]['name'];
            itemName.classList.add("name");

            itemImage.setAttribute('dataIdx',i);
            itemName.setAttribute('dataIdx',i);

            dataItem.append(itemImage);
            dataItem.append(itemName);
        }
    }
    $('.dataRow').on('click',onItemClick);
    $('.gItem').on('click',onItemClick);
}

function changeType(){
    debugger
    if(!event.target.classList.contains('active')){
        if(event.target.classList.contains('btn_list')){
            dispType = "LIST"
        }else{
            dispType = "IMAGE"
        }

        debugger
        $('.active').removeClass('active');
        event.target.classList.add('active');
        
        createPage(arrProduct,pageNum);
    }
}

function pagePrev(){
    if(pageNum == 0) return;
    else pageNum--;

    createPage(arrProduct,pageNum);
}

function onItemClick(){
    var idx = event.target.getAttribute('dataIdx');
    var data = arrProduct[idx];
    openDialog('product',data);
}

function pageNext(){
    debugger
    var lastIdx = (pageNum + 1) * pageSize;
    if(arrProduct.length < lastIdx) return;
    else pageNum++;

    createPage(arrProduct,pageNum);
}


// // 이미지 파일 로드 로직
// if (window.File && window.FileReader && window.FormData) {
// 	var $inputField = $('#imgFile');
// 	$inputField.on('change', function (e) {
// 		var file = e.target.files[0];
// 		fileName = file.name
// 		if (file) {
// 			if (/^image\//i.test(file.type)){// 10mb 이하 이미지
//     			if (file.size < 10000000) {// 10mb 이하 이미지
//     			    readFile(file);
//     			}
// 			} else {
// 				// alert('Not a valid image!');
// 			}
// 		}
// 	});
// } else {
// // 	alert("File upload is not supported!");
// }

// // 파일 읽기 함수
// // 이미지 파일 로드 시점에 프로세싱
// function readFile(file) {
// 	var reader = new FileReader();
    
// 	reader.onloadend = function () {
// 	    // 파일명 세팅 & 데이터 처리
// 	    fileName = file.name;
// 		processFile(reader.result, file.type);
// 	}
	
	
// 	reader.onerror = function () {
// // 		alert('There was an error reading the file!');
// 	}
// 	reader.readAsDataURL(file);
// }

// 이미지 파일 처리 함수
// 이미지 파일 화면에 표현
// @param dataURL    데이터
// @param fileType   데이터 타입
// function processFile(dataURL, fileType) {
//     // 이미지
// 	var image = new Image();
// 	image.src = dataURL;
//     // 이미지 로드
// 	image.onload = function () {
// 	    // 이미지 리스트에 추가(여러개 로드할 수 있으므로 --> 업로드 이미지 1개 시 삭제)
//         imgList.push(dataURL);
        
//         // 이미지
// 		var imgHtml = document.createElement('canvas');
// 		imgHtml.width = 72;
// 		imgHtml.height = 72;
// 		imgHtml.style.borderRadius = '6px';
// 		imgHtml.style.marginRight = '8px';
// 		imgHtml.toDataURL(fileType)
// 		imgHtml.style.position = 'absolute';
// 		var context = imgHtml.getContext('2d');
// 		context.drawImage(this, 0, 0, imgHtml.width, imgHtml.height);
        
//         // 이미지 상위 div
// 		var parent = document.createElement('div');
// 		parent.id = `imgData_${imgList.length}`;
// 		parent.appendChild(imgHtml);
// 		parent.style.position = 'absolute';
		
// 		// x 표시
// 		var imgClose = document.createElement('div');
// 		imgClose.className = 'clearIcon2';
// 		imgClose.style.width = '20px';
// 		imgClose.style.height = '20px';
// 		imgClose.style.left = '50px';
// 		imgClose.style.top = '5px';
// 		imgClose.style.position = 'absolute';
// 		parent.appendChild(imgClose);
		
// 		// x 클릭 이벤트
// 		let imgClick = function(){
// 		    // 이미지 첨부파일 데이터 삭제(초기화)
// 		    div_img._html.removeChild(parent);
// 		    fileName = '';
// 		    btn_camera.SetStyle('display','block');
// 		    imgList = [];
//         }
//         imgClose.removeEventListener('click',imgClick);
//         imgClose.addEventListener('click',imgClick);

//         // 이미지 세팅
// 		div_img._html.appendChild(parent);
// 		div_img.SetStyle('display','block');   // 사진 display -> block
// 		btn_camera.SetStyle('display','none'); // 카메라(사진 등록) 버튼 display -> none
// 	};

// 	image.onerror = function () {
// // 		alert('There was an error processing your file!');
// 	};
// }