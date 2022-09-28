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
        let query = `SELECT * FROM product`;
        
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    /// 1.2 상품 추가
    app.post('/productsInsert', (req, res) => {
        var body = req.body;
        
        let query1 = `INSERT INTO product VALUES ('${body.name}', '${body.price}', '${body.inputCd}', '${body.outputCd}', '${body.etc}', '${body.image}');`;
        let query2 = `INSERT INTO stock VALUES ('${body.name}', '${body.price}', '${body.image}', '${body.inputCd}', '${body.etc}', 0, 0, 0);`;

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
            query += ` WHERE inputCd = ${req.query.inputCd} ORDER BY orderDT DESC;`
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
            query1 += ` ('${body.name}', '${body.price}', '${body.inputCd}', "", '${body.orderType}', '${orderCd}',
            '${body.recvQT}', null, '${body.orderDT}', null, null, '${body.etc}');`;
        }
        /// 줄고 입력
        else if(type == "03"){
            query1 += ` ('${body.name}', '${body.price}', '${body.inputCd}', '${body.placeCd}', '${body.orderType}', '${orderCd}',
            null, '${body.deliQT}', '${body.orderDT}', null, null, '${body.etc}');`;
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
    

    /* 예시 */
    // 회원관리 목록 조회
    app.get('/memberList', (req, res) => {
        let query = 'SELECT * FROM g5_member LIMIT 100';
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 회원관리 데이터 조회
    app.get('/getMemberInfo', (req, res) => {
        let query = `SELECT * FROM g5_member WHERE mb_id = '${req.query.mb_id}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data[0]);
        });
    });
    // 회원관리 데이터 조회
    app.post('/postMemberInfo', (req, res) => {
        let query = `UPDATE g5_member SET
            
            WHERE mb_id = '${req.query.mb_id}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 
    app.get('/getBasicSettings', (req, res) => {
        let sql = '';
        sql = 'SELECT * FROM g5_config';
        sqlquery(sql, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    app.post('/postBasicSettings', (req, res) => {
        var query = `UPDATE g5_config SET
        cf_title = '${req.body.cf_title}',
        cf_icode_server_ip = '${req.body.cf_icode_server_ip}',
        cf_icode_server_port = '${req.body.cf_icode_server_port}'
        `
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 하단설정 조회
    app.get('/tailContent', (req, res) => {
        let query = `SELECT * FROM g5_shop_default WHERE de_type = '${req.query.lang || "KOR"}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 하단설정 수정
    app.post('/tailContent', (req, res) => {
        let sql = `
            UPDATE g5_shop_default
            SET de_admin_company_name       = '${req.body.de_admin_company_name}',
                de_admin_company_addr       = '${req.body.de_admin_company_addr}',
                de_admin_info_name          = '${req.body.de_admin_info_name}',
                de_admin_info_email         = '${req.body.de_admin_info_email}'
                WHERE de_type = '${req.query.lang || "KOR"}'
        `;
        sqlquery(sql, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 배너관리
    app.get('/getManageBanner', (req, res) => {
        let query = "SELECT * FROM g5_shop_banner ORDER BY bn_order, bn_id desc";
        sqlquery(query, (err, data) => {
            if(err) return res.status(500).send(err);
            else return res.send(data);
        })
    });
    // 배너관리
    app.get('/getNewWin', (req, res) => {
        let query = "SELECT * FROM g5_new_win ORDER BY nw_id desc";
        sqlquery(query, (err, data) => {
            if(err) return res.status(500).send(err);
            else return res.send(data);
        })
    });

    /* User */
    // 고객지원 - 공지사항
    app.get('/noticeList', (req, res) => {
        let query = '';
        query = `select * from ${(req.query.lang == "ENG")? 'g5_write_notice_en' : 'g5_write_notice'} where wr_is_comment = 0 order by wr_num ${(req.query.start && req.query.end)? `limit ${req.query.start}, ${req.query.end}` : ''}`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 고객지원 - 자료실
    app.get('/referList', (req, res) => {
        let query = `SELECT * FROM ${(req.query.lang == "KOR")? 'g5_write_reference' : 'g5_write_reference_en'}`;
        if (req.query.type == '1') query += ` WHERE wr_subject LIKE '%${req.query.text}%'`;      // 제목만
        else if (req.query.type == '2') query += ` WHERE wr_content LIKE '%${req.query.text}%'`; // 내용만
        else if (req.query.type == '3') query += ` WHERE wr_subject LIKE '%${req.query.text}%' OR wr_content LIKE '%${req.query.text}%'` // 제목 + 내용
        else if (req.query.type == '4') query += ` WHERE wr_name LIKE '%${req.query.text}%'`;    // 글쓴이
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 문의하기 - 목록
    app.get('/qnaList', (req, res) => {
        let query = `SELECT * FROM ${(req.query.lang == "KOR")? 'g5_write_contact' : 'g5_write_contact_en'}`;
        if (req.query.type == '1') query += ` WHERE wr_subject LIKE '%${req.query.text}%'`;      // 제목만
        else if (req.query.type == '2') query += ` WHERE wr_content LIKE '%${req.query.text}%'`; // 내용만
        else if (req.query.type == '3') query += ` WHERE wr_subject LIKE '%${req.query.text}%' OR wr_content LIKE '%${req.query.text}%'` // 제목 + 내용
        else if (req.query.type == '4') query += ` WHERE wr_name LIKE '%${req.query.text}%'`;    // 글쓴이
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 문의하기 - 수정
    app.post('/qnaUpdate', (req, res) => {
        var data = req.body;
        let query = `UPDATE ${(req.body.lang == "KOR")? 'g5_write_contact' : 'g5_write_contact_en'} SET wr_subject = '${data.title}', wr_content = '${data.content}', wr_name = '${data.name}', wr_email = '${data.email}', wr_ip = '${data.ip}', wr_1 = '${data.tell}', wr_password ='${data.pass}', wr_last = '${data.last}', wr_filename = '${data.filename}' WHERE wr_id = '${data.id}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 문의하기 - 작성
    app.post('/qnaInsert', (req, res) => {
        var data = req.body;
        let query = `INSERT INTO ${(req.body.lang == "KOR")? 'g5_write_contact' : 'g5_write_contact_en'} (wr_subject, wr_content, wr_name, wr_email, wr_datetime, wr_last, wr_ip, wr_1, wr_password, wr_filename)
        VALUES ('${data.title}', '${data.content}', '${data.name}', '${data.email}', '${data.date}', '${data.last}', '${data.ip}', '${data.tell}', '${data.pass}', '${data.filename}');`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 문의하기 - 삭제
    app.post('/qnaDelete', (req, res) => {
        let query = `DELETE FROM ${(req.body.lang == "KOR")? 'g5_write_contact' : 'g5_write_contact_en'} WHERE wr_id = '${req.body.id}';`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 빠른문의하기
    app.get('/qk_qnaList', (req, res) => {
        let query = `SELECT * FROM ${(req.query.lang == "KOR")? 'g5_write_qk_inquiry' : 'g5_write_qk_inquiry_en'}`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 빠른문의하기 - 작성
    app.post('/qk_qnaInsert', (req, res) => {
        var data = req.body;
        let query = `INSERT INTO ${(req.body.lang == "KOR")? 'g5_write_qk_inquiry' : 'g5_write_qk_inquiry_en'} (wr_subject, wr_content, wr_name, wr_datetime, wr_last, wr_ip, wr_1, wr_num)
        VALUES ('${data.title}', '${data.content}', '${data.name}', '${data.date}', '${data.last}', '${data.ip}', '${data.tell}', '${data.num}');`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 인재채용 - 인재채용
    app.get('/recruitList', (req, res) => {
        let query = `SELECT * FROM ${(req.query.lang == "KOR")? 'g5_write_recruit' : 'g5_write_recruit_en'}`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });        
    });    
    // 메인 - 고객사
    app.get('/customerList', (req, res) => {
        let query = `SELECT * from g5_board where bo_table = '${(req.query.lang == "KOR")? 'customer' : 'customer_en'}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 메인 - 고객사 이미지
    app.get('/customerImgList', (req, res) => {
        let query = `select * from ${(req.query.lang == "KOR")? 'g5_write_customer' : 'g5_write_customer_en'} where ca_name = '${req.query.customer}' order by wr_id asc`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 고객사 - 증권사
    app.get('/ksmList', (req, res) => {
        let query = `SELECT * from g5_board where bo_table = '${(req.query.lang == "KOR")? 'ksm' : 'ksm_en'}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 고객사 - 증권사 이미지
    app.get('/ksmImageList', (req, res) => {
        let query = `select * from ${(req.query.lang == "KOR")? 'g5_write_ksm' : 'g5_write_ksm_en'} where ca_name = '${req.query.ksm}' order by wr_id asc`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 파일(DB)
    app.get('/fileData', (req, res) => {
        let query = `select * from g5_board_file where bo_table = '${req.query.table}' and wr_id = '${req.query.id}'`
        if(req.query.table == 'contact') query += ` order by bf_no limit 0, 1 `; // 메인 - 고객사
        else if(req.query.table == 'reference') {
            if(req.query.no) query += ` AND bf_no = '${req.query.no}'`; // 이력서 파일            
        }
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 파일 업로드(서버)
    app.post('/fileUpload', (req, res) => {
        var form = new formidable.IncomingForm();
        // parsing
        form.parse(req, function(err, fields, files) {
            var oldpath = files.file.filepath;
            let fileName = files.file.originalFilename.split('/')[0]
            let fileTable = files.file.originalFilename.split('/')[1]
            let fileId = files.file.originalFilename.split('/')[2]

            var newpath = `/var/www/html/file/${fileTable}_${fileId}/`;
            if (!fs.existsSync(newpath)){
                fs.mkdirSync(newpath);
            }
            newpath += `${fileName}`; // 수정 필요 
            fs.rename(oldpath, newpath, function (err) {
                if (err) throw err;
                res.write('File uploaded and moved!');
                res.end();
            });            
        });
    });
    // 파일 다운로드(서버)
    app.get('/fileDownload', (req, res) => {        
        var upload_folder = `/var/www/html/file/${req.query.table}_${req.query.id}/`;
        var filePath = upload_folder + req.query.fileName; // ex) /upload/files/refer_5/sample.txt ==> refer : table / 5 : wr_id
        try {
            if (fs.existsSync(filePath)) { // 파일이 존재하는지 체크
                var filename = path.basename(filePath); // 파일 경로에서 파일명(확장자포함)만 추출
                var mimetype = mime.getType(filePath);  // 파일의 타입(형식)을 가져옴
                
                res.setHeader('Content-type', mimetype); // 파일 형식 지정
                res.setHeader('Content-disposition', contentDisposition(getDownloadFilename(req,filename))); // 다운받아질 파일명 설정
                
                var filestream = fs.createReadStream(filePath);
                filestream.pipe(res);
            } else {
                res.send('해당 파일이 없습니다.');  
                return;
            }
        } catch (e) { // 에러 발생시
            res.send('파일을 다운로드하는 중에 에러가 발생하였습니다.');
            return;
        }
    });
    // 조회수 업데이트
    app.post('/hitUp', (req, res) => {
        let query = `UPDATE g5_write_${req.body.table}${(req.query.lang == "ENG")? '_en' : ''}  SET wr_hit = wr_hit + 1 WHERE wr_id = ${req.body.id};`
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