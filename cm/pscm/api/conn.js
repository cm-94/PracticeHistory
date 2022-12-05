const dbconn = require('../dbconn.js');
const sqlquery = dbconn.sqlquery;
var requestIp = require('request-ip');

var formidable = require('formidable')
var fs = require('fs')

var path = require('path');
var mime = require('mime');
var iconvLite = require('iconv-lite');
var contentDisposition = require('content-disposition');
const { Console } = require('console');

const initConn = (app) => {
    /* 1. 상품 관리 */
    /// 1.1 상품 조회
    app.get('/products', (req, res) => {
        console.log("GET","/products");
        let query = `SELECT * FROM product`;
        
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    /// 1.2 상품 추가
    app.post('/productsInsert', (req, res) => {
        var body = req.body;
        
        let query1 = `INSERT INTO product VALUES ('${body.name}', ${body.price}, '${body.inputCd}', '${body.outputCd}', '${body.etc}', '${body.image}');`;
        let query2 = `INSERT INTO stock VALUES ('${body.name}', ${body.price}, '${body.image}', '${body.inputCd}', '${body.etc}', 0, 0, 0);`;

        sqlquery(query1, (err1, data1) => {
            if (err1) return res.status(500).send(err1);
            else {
                sqlquery(query2, (err2, data2) => {
                    if (err2) return res.status(500).send(err2);
                    else return res.send(data2);
                });
            }
        });
    });
    
    /// 1.3 상품 수정
    app.post('/productsUpdate', (req, res) => {
        let query = `Update product AS a, stock AS b
        SET a.name = '${req.body.name}',
            a.price = '${req.body.price}',
            a.inputCd = '${req.body.inputCd}',
            a.outputCd = '${req.body.outputCd}',
            a.etc = '${req.body.etc}',
            a.image = '${req.body.image}',
            b.name = '${req.body.name}',
            b.price = '${req.body.price}',
            b.image = '${req.body.image}',
            b.inputCd = '${req.body.inputCd}'

            WHERE a.inputCd = '${req.body.inputCd}' AND b.inputCd = '${req.body.inputCd}'`;

        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    /// 1.4 상품 삭제
    app.post('/productsDelete', (req, res) => {
        var items = req.body.items;
        let query1 = `DELETE FROM product WHERE `
        for (var i = 0; i < items.length; i++){
            query1 += `inputCd = '${items[i].inputCd}' OR `;
        }
        query1 = query1.substr(0,query1.length-4);

        let query2 = `DELETE FROM stock WHERE `
        for (var i = 0; i < items.length; i++){
            query2 += `inputCd = '${items[i].inputCd}' OR `;
        }
        query2 = query2.substr(0,query2.length-4);

        sqlquery(query1, (err1, data1) => {
            if (err1) return res.status(500).send(err1);
            else {
                sqlquery(query2, (err2, data2) => {
                    if (err2) return res.status(500).send(err2);
                    else return res.send(data2);
                });
            }
        });
    });

    /* 2. 재고 현황 */
    /// 2.1 재고 현황 조회
    app.get('/stocks', (req, res) => {
        let query = `SELECT * FROM stock`;

        if(req.query && req.query.inputCd){
            query = `SELECT * FROM stock WHERE inputCd = '${req.query.inputCd}';`;
        }
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    /// 2.2 재고 현황 수정
    app.post('/stocksUpdate', (req, res) => {
        let query = `UPDATE stock SET
        name = ${req.body.name},
        price = ${req.body.price},
        inputCd = ${req.body.inputCd},
        etc = ${req.body.etc},
        restQT = ${req.body.restQT},
        recvWaitQT = ${req.body.recvWaitQT},
        deliWaitQT = ${req.body.deliWaitQT}
        WHERE inputCd = '${req.body.name}'
        `;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    /* 입출고 내역 */
    /// 01 : 입고 입력(수정), 02 : 입고 확정, 03 : 출고 입력(수정), 04 : 출고 확정, 00 : 확정 내역
    /// 2.3 입출고 내역 조회
    app.get('/orderlist', (req, res) => {
        let query = `SELECT * FROM orderlist`
        
        if(req.query && req.query.inputCd){
            query += ` WHERE inputCd = '${req.query.inputCd}' ORDER BY orderDT DESC;`
        }

        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    /// 2.4 입출고 내역 추가
    app.post('/orderlistInsert', (req, res) => {
        var body = req.body;
        var type = body.orderType;
        
        let query1 = "INSERT INTO orderlist VALUES";
        orderCd = body.inputCd + body.orderDT;
        /// 입고 입력
        if(type == "01"){
            query1 += ` ('${body.name}', ${body.price}, '${body.inputCd}', '', '${body.orderType}', '${orderCd}',
            ${body.recvQT}, null, '${body.orderDT}', null, null, '${body.etc}');`;
        }
        /// 줄고 입력
        else if(type == "03"){
            query1 += ` ('${body.name}', ${body.price}, '${body.inputCd}', '${body.placeCd}', '${body.orderType}', '${orderCd}',
            null, ${body.deliQT}, '${body.orderDT}', null, null, '${body.etc}');`;
        }

        sqlquery(query1, (err1, data1) => {
            if (err1) return res.status(500).send(err1);
            else{
                var query2 = "UPDATE stock SET";
                
                /// 입고 입력
                if(type == "01"){
                    query2 += ` recvWaitQT = recvWaitQT + ${body.recvQT}
                    WHERE inputCd = '${body.inputCd}';
                    `
                }
                /// 줄고 입력
                else if(type == "03"){
                    query2 += ` deliWaitQT = deliWaitQT + ${body.deliQT}
                    WHERE inputCd = '${body.inputCd}';
                    `
                }
                
                sqlquery(query2, (err2, data2) => {
                    if (err2) return res.status(500).send(err2);
                    else return res.send(data2);
                });
            }
        });
    });
    
    /// 2.4 입출고 내역 수정
    app.post('/orderlistUpdate', (req, res) => {
        let query1 = "UPDATE orderlist SET ";
        var body = req.body;
        var type = req.body.orderType;
        
        /// 입고 수정
        if(type == "01") query1 += `recvQT = ${body.recvNewQT}`;
        /// 입고 확정
        else if(type == "02") query1 += `recvQT = '${body.recvQT}', recvDT = '${body.recvDT}', orderType = '00'`;
        /// 출고 수정
        else if(type == "03") query1 += `deliQT = ${body.deliNewQT}`;
        /// 출고 확정
        else if(type == "04") query1 += `deliDT = '${body.deliDT}', orderType = '00'`;
        
        query1 += ` WHERE orderCd = '${body.orderCd}';`;

        sqlquery(query1, (err1, data1) => {
            if (err1) return res.status(500).send(err1);
            else {
                var query2 = "UPDATE stock SET ";
                
                /// 입고 수정
                if(type == "01") query2 += `recvWaitQT = recvWaitQT + ${body.recvNewQT - body.recvQT}`;
                /// 입고 확정
                else if(type == "02") query2 += `restQT = restQT + ${body.recvQT}, recvWaitQT = recvWaitQT - ${body.recvQT}`;
                /// 출고 수정
                else if(type == "03") query2 += `deliWaitQT = deliWaitQT + ${body.deliNewQT - body.deliQT}`;
                /// 출고 확정
                else if(type == "04") query2 += `restQT = restQT - ${body.deliQT}, deliWaitQT = deliWaitQT - ${body.deliQT}`;
                
                query2 += ` WHERE inputCd = '${body.inputCd}'`;
                
                sqlquery(query2, (err2, data2) => {
                    if (err2) return res.status(500).send(err2);
                    else return res.send(data2);
                });
            }
        });
    });

    /// 2.5 입출고 내역 삭제
    app.post('/orderlistDelete', (req, res) => {
        let query1 = `DELETE FROM orderlist WHERE orderCd = '${req.body.orderCd}';`
        
        sqlquery(query1, (err1, data1) => {
            if (err1) return res.status(500).send(err1);
            else {
                let query2 = "UPDATE stock SET ";

                /// 입고 수정
                if(req.body.orderType == "01") query2 += `recvWaitQT = recvWaitQT - ${req.body.recvQT}`;
                /// 출고 수정
                else if(req.body.orderType == "03") query2 += `deliWaitQT = deliWaitQT - ${req.body.deliQT}`;

                query2 += ` WHERE inputCd = '${req.body.inputCd}'`;

                sqlquery(query2, (err2, data2) => {
                    if (err2) return res.status(500).send(err2);
                    else {
                        return res.send(data2);
                    }
                });
            }
        });
    });

    /* 3. 매장 관리 */
    /// 3.1 매장 내역
    app.get('/orderplace', (req, res) => {
        let query = `SELECT * FROM orderplace`;

        if(req.query && req.query.placeCd){
            query = `SELECT * FROM orderplace WHERE inputCd = '${req.query.placeCd}';`;
        }

        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    /// 3.2 매장 추가
    app.post('/orderplaceInsert', (req, res) => {
        let query = `INSERT INTO orderplace VALUES
        ('${req.body.name}', '${req.body.place}', '${req.body.placeCd}', '${req.body.entranceFee}', '${req.body.entranceType}', '${req.body.salesCommi}', '${req.body.salesCommiType}',        
        '${req.body.sattleDate}', '${req.body.taxBill}', '${req.body.conTerm}', '${req.body.etc}')`;
        
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    /// 3.3 매장 수정
    app.post('/orderplaceUpdate', (req, res) => {
        var item = req.body;
        let query = `UPDATE orderplace SET
        name = '${item.name}',
        place = '${item.place}',
        placeCd = '${item.newPlaceCd}',
        entranceFee = '${item.entranceFee}',
        entranceType = '${item.entranceType}',
        salesCommi = '${item.salesCommi}',
        salesCommiType = '${item.salesCommiType}',
        sattleDate = '${item.sattleDate}',
        taxBill = '${item.taxBill}',
        conTerm = '${item.conTerm}',
        etc = '${item.etc}'
        WHERE placeCd = '${item.placeCd}';`;
        
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else {
                if(item.placeCd != item.newPlaceCd){
                    var query2 = `UPDATE orderlist SET
                    placeCd = '${item.newPlaceCd}'
                    WHERE placeCd = '${item.placeCd}';`;

                    sqlquery(query2, (err2, data2) => {
                        if (err2) return res.status(500).send(err2);
                        else return res.send(data2);
                    });
                    
                }
            }
        });
    });

    /// 3.4 매장 삭제
    app.post('/orderplaceDelete', (req, res) => {
        var items = req.body.items;
        let query = `DELETE FROM orderplace WHERE `;
        for (var i = 0; i < items.length; i++){
            query += `placeCd = '${items[i].placeCd}' OR `;
        }
        query = query.substr(0,query.length-4);
        
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    /// 4.1 휴지통 조회
    app.get('/trash', (req, res) => {
        let query = `SELECT * FROM trash`;

        if(req.query){
            if(req.query.input){
                query += ` WHERE data like = '%${req.query.input}%'`;
                if(req.query.type){
                    if(req.query.type != "00") query += ` and type = '${req.query.type}'`;
                }
            }else{
                if(req.query.type){
                    if(req.query.type != "00") query += ` WHERE type = '${req.query.type}'`;
                }
            }
        }
        
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    // 클라이언트 IP
    app.get('/clientIp', (req, res) => {
        let ip = requestIp.getClientIp(req);
        return res.send(ip);
    });

    // 파일명 한글 포함시 처리
    function getDownloadFilename(req, filename) {
        var header = req.headers['user-agent'];        
        if (header.includes("MSIE") || header.includes("Trident")) { 
            return encodeURIComponent(filename).replace(/\\+/gi, "%20");
        } else if (header.includes("Chrome")) {
            return iconvLite.decode(iconvLite.encode(filename, "UTF-8"), 'ISO-8859-1');
        } else if (header.includes("Opera")) {
            return iconvLite.decode(iconvLite.encode(filename, "UTF-8"), 'ISO-8859-1');
        } else if (header.includes("Firefox")) {
            return iconvLite.decode(iconvLite.encode(filename, "UTF-8"), 'ISO-8859-1');
        }    
        return filename;
    }
}

module.exports = initConn;

// console.log("sqlquery_err ==> ",JSON.stringify(err))
// console.log("sqlquery_data ==> ",JSON.stringify(data))