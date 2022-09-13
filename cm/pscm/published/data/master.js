var g_masterInfo = {};
var g_masterList = [];
var g_masterNameList = [];
var g_masterHistory = [];
let marketGbCode ={ //마켓구분코드
    '0':'KOSPI',
    '1':'KOSDAQ',
    'n':'ELW',
    '2':'지수선물',
    'd':'지수선물 스프레드',
    '3':'지수옵션',
    'U':'지수업종',
    'g':'K-OTC',
    'f':'미니지수선물',
    'o':'미니지수옵션',
    'L':'변동성지수선물',
    'K':'tprxjwltntjsanf',
    'S':'주식선물',
    'C':'상품선물',
    'N':'KONEX'
}
let masterManager = {};
masterManager.getMaster = function(){
    g_masterInfo = {};
    g_masterList = [];
    g_masterNameList = [];
    g_masterHistory = [];

    g_masterHistory = masterManager.getLocalMasterData('masterHistory') || [];
    console.log('g_masterHistory',JSON.stringify(g_masterHistory))
    
    //if(버전처리마스터유무조건)
    let objPaths = hi5_hash.paths({ key: ["mtsjname", "mtsoutjname"], path: true, json: true });
    $.when(
        $.get(objPaths.mtsjname.url, objPaths.mtsjname.cb),
        $.get(objPaths.mtsoutjname.url, objPaths.mtsoutjname.cb),
    ).done(function (mtsjname, mtsoutjname) {
        
        if (mtsjname[1] == "success") {
            console.log('mtsjname getJSON success!!');
            masterManager.setKospiStock(mtsjname[0])
        }
        if (mtsoutjname[1] == "success") {
            console.log('mtsoutjname getJSON success!!');
            masterManager.setKosdaqStock(mtsoutjname[0])
        }
        masterManager.setMasterList();
        masterManager.setLocalMasterData('masterInfo',JSON.stringify(g_masterInfo));
        masterManager.setLocalMasterData('masterList',JSON.stringify(g_masterList));
        masterManager.setLocalMasterData('masterNameList',JSON.stringify(g_masterNameList));
    })
    //else{
    //     g_masterInfo = masterManager.getLocalMasterData('masterInfo')
    //     g_masterList = masterManager.getLocalMasterData('masterList')
    //     g_masterNameList = masterManager.getLocalMasterData('masterNameList')
    // }
}

masterManager.setKospiStock = function(item){
    let lineSplit = item.split('\n');
    lineSplit.forEach(line => {
        line = line.replaceAll('\r');
        let lineItem = line.split('|')
        let jmCodeInfo = {}
        if(lineItem.length == 0) return;
        ///////////////마블 미니 마스터에서는 주식 종목만 체크
        if(lineItem[3] != 'ST' && lineItem[3] != 'MF' && lineItem[3] != 'RT' && lineItem[3] != 'SC' && lineItem[3] != 'IF' && lineItem[3] != 'DR'){
            return;
        }
        jmCodeInfo.m_sMarketType                            = '0';
        jmCodeInfo.m_sCode		                            = lineItem[0];
        jmCodeInfo.m_sKorName	                            = lineItem[1];
        try{
            if (jmCodeInfo.m_sKorName.trim().length == 0) return;
        }
        catch{
            return
        }
        jmCodeInfo.m_sEngName	                            = "";
    
        jmCodeInfo.lowfyn 		                            = lineItem[2];
        jmCodeInfo.sgrpid 		                            = lineItem[3];
        jmCodeInfo.sheatcd 		                            = lineItem[4];
        jmCodeInfo.cltradeyn 	                            = lineItem[5];
        jmCodeInfo.kospi200yn 	                            = lineItem[6];
        jmCodeInfo.etpyn 		                            = lineItem[7];
        jmCodeInfo.upcls 		                            = lineItem[8];
        jmCodeInfo.marketgb 	                            = lineItem[9];
        jmCodeInfo.buyunit 		                            = lineItem[10];
        jmCodeInfo.cashrate 	                            = lineItem[11];
        jmCodeInfo.creditrate 	                            = lineItem[12];
        jmCodeInfo.invwarn 		                            = lineItem[13];
        jmCodeInfo.trdstopyn 	                            = lineItem[14];
        jmCodeInfo.feecd 		                            = lineItem[15];
        jmCodeInfo.slevinvgbcd 	                            = lineItem[16];
        jmCodeInfo.rlevinvgbcd 	                            = lineItem[17];
        jmCodeInfo.oddyn		                            = lineItem[18];
        if (lineItem.length > 19) jmCodeInfo.data1		 	= lineItem[19];
        if (lineItem.length > 20) jmCodeInfo.data3		 	= lineItem[20];
        if (lineItem.length > 21) jmCodeInfo.data4		 	= lineItem[21];
        if (lineItem.length > 22) jmCodeInfo.data5		 	= lineItem[22];
        if (lineItem.length > 23) jmCodeInfo.data6		 	= lineItem[23];
        jmCodeInfo.is_cd                                    = jmCodeInfo.m_sCode;
        jmCodeInfo.is_nm                                    = jmCodeInfo.m_sKorName;
        jmCodeInfo.mkt_clsf                                 = jmCodeInfo.m_sMarketType;

        g_masterInfo[jmCodeInfo.m_sCode] = jmCodeInfo;
        g_masterList.push({is_cd : jmCodeInfo.is_cd, is_nm : jmCodeInfo.is_nm, mkt_clsf : jmCodeInfo.mkt_clsf })
    });
}

masterManager.setKosdaqStock = function(item){
    let lineSplit = item.split('\n');
    lineSplit.forEach(line => {
        line = line.replaceAll('\r');
        let lineItem = line.split('|')
        let jmCodeInfo = {}
        if(lineItem.length == 0) return;
        jmCodeInfo.m_sMarketType	                                = '1';
        jmCodeInfo.m_sCode		                            = lineItem[0];
        jmCodeInfo.m_sKorName	                            = lineItem[1];
        try{
            if (jmCodeInfo.m_sKorName.trim().length == 0) return;
        }
        catch{
            return
        }        
        jmCodeInfo.m_sEngName	                            = "";
        jmCodeInfo.cashrate 		                        = lineItem[2];
        jmCodeInfo.creditrate 	                            = lineItem[3];
        jmCodeInfo.invwarn	 	                            = lineItem[4];
        jmCodeInfo.invwarnyn	 	                        = lineItem[5];
        jmCodeInfo.cltradeyn	 	                        = lineItem[6];
        jmCodeInfo.sheatcd	 	                            = lineItem[7];
        jmCodeInfo.lowfyn		 	                        = lineItem[8];
        jmCodeInfo.oddyn			                        = lineItem[9];
        jmCodeInfo.deptcd		 	                        = lineItem[10];
        jmCodeInfo.marketgb	 	                            = lineItem[11];
        if (lineItem.length > 12) jmCodeInfo.data1		 	= lineItem[12];
        if (lineItem.length > 13) jmCodeInfo.data3		 	= lineItem[13];
        if (lineItem.length > 14) jmCodeInfo.data4		 	= lineItem[14];
        if (lineItem.length > 15) jmCodeInfo.data5		 	= lineItem[15];
        if (lineItem.length > 16) jmCodeInfo.data6		 	= lineItem[16];
        jmCodeInfo.is_cd                                    = jmCodeInfo.m_sCode;
        jmCodeInfo.is_nm                                    = jmCodeInfo.m_sKorName;
        jmCodeInfo.mkt_clsf                                 = jmCodeInfo.m_sMarketType;

        g_masterInfo[jmCodeInfo.m_sCode] = jmCodeInfo;
        g_masterList.push({is_cd : jmCodeInfo.is_cd, is_nm : jmCodeInfo.is_nm, mkt_clsf : jmCodeInfo.mkt_clsf})
    });
}
masterManager.getLogoCls = function(code){
    return 'stockImg stock_' + code;
}
masterManager.setMasterList = function(){
    g_masterList.sort(function(a, b) {
        return a['is_cd'] > b['is_cd'] ? 1 : -1;
    });
    // g_masterList.forEach(function (item) {
    //     var dis = Hangul.disassemble(item.is_nm, true);
    //     var cho = dis.reduce(function (prev, elem) {
    //         elem = elem[0] ? elem[0] : elem;
    //         return prev + elem;
    //     }, "");
    //     item.diassembled = cho;
    // });
    g_masterNameList = hi5.clone(g_masterList);
    g_masterNameList.sort(function(a, b) {
        return a['is_nm'] > b['is_nm'] ? 1 : -1;
    });
}

masterManager.getBankCls = function(code){
    return 'bankImg_' + code;
}

masterManager.getJmCodeForNm = function(is_nm){
    let selectMaster;
    Object.keys(g_masterInfo).forEach(function(item,idx){
        console.log("item",JSON.stringify(item), g_masterInfo[item].is_nm, is_nm)
        if(g_masterInfo[item].is_nm == is_nm)
            selectMaster = g_masterInfo[item];
    })
    // let selectMaster = g_masterList.filter(function(item){
    //     return item.is_nm == is_nm;
    // })
    console.log("getJmCodeForNm", JSON.stringify(selectMaster))
    if(selectMaster){
        return selectMaster.is_cd;
    }else{
        return '';
    }
}

masterManager.setJmCode = function(code){
    try{
        code = code.replaceAll("\u0000", "").trim();
    }
    catch(e){
        hi5.SetToast(3,'지원하지 않는 종목입니다.',3);
        return false;
    }
    if(g_masterInfo[code]){
        console.log("setJmCode : ", g_masterInfo[code]);
        g_jmCode = code;
        g_masterHistory = g_masterHistory.filter(function(info){
            return info.is_cd != code;
        })
        g_masterHistory.unshift(g_masterInfo[code]);
        if(g_masterHistory.length > 100){
            g_masterHistory.pop();
        }
        masterManager.setLocalMasterData('masterHistory',g_masterHistory);
        return true;
    }
    else{
        hi5.SetToast(3,'지원하지 않는 종목입니다.',3);
        return false;
    }
}
masterManager.setLocalMasterData = function(key,data){
    localStorage.setItem(key,JSON.stringify(data));
}
masterManager.getLocalMasterData = function(key){
    let localData = localStorage.getItem(key)
    if(localData)
        return JSON.parse(localData);
    else
        return
}