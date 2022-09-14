const dbconn = require('../dbconn.js');
const sqlquery = dbconn.sqlquery;
var requestIp = require('request-ip');

var formidable = require('formidable')
var fs = require('fs')

var path = require('path');
var mime = require('mime');
var iconvLite = require('iconv-lite');
var contentDisposition = require('content-disposition')

const initConn = (app) => {
    app.get('/products', (req, res) => {
        let query = `SELECT * FROM products`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data,"success");
        });
    });

    app.get('/admLogin', (req, res) => {
        console.log("CONN - admLogin");
        let query = `SELECT * FROM g5_member WHERE mb_id = '${req.query.id}' and mb_password = '${req.query.pw}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });

    // 회원관리 목록 조회
    app.get('/memberList', (req, res) => {
        debugger
        let query = 'SELECT * FROM g5_member LIMIT 100';
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data);
        });
    });
    // 회원관리 데이터 조회
    app.get('/getMemberInfo', (req, res) => {
        debugger
        let query = `SELECT * FROM g5_member WHERE mb_id = '${req.query.mb_id}'`;
        sqlquery(query, (err, data) => {
            if (err) return res.status(500).send(err);
            else return res.send(data[0]);
        });
    });
    // 회원관리 데이터 조회
    app.post('/postMemberInfo', (req, res) => {
        debugger
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
        cf_admin = '${req.body.cf_admin}',
        cf_admin_email = '${req.body.cf_admin_email}',
        cf_admin_email_name = '${req.body.cf_admin_email_name}',
        cf_cut_name = '${req.body.cf_cut_name}',
        cf_new_del = '${req.body.cf_new_del}',
        cf_write_pages = '${req.body.cf_write_pages}',
        cf_mobile_pages = '${req.body.cf_mobile_pages}',
        cf_editor = '${req.body.cf_editor}',
        cf_captcha = '${req.body.cf_captcha}',
        cf_captcha_mp3 = '${req.body.cf_captcha_mp3}',
        cf_recaptcha_site_key = '${req.body.cf_recaptcha_site_key}',
        cf_recaptcha_secret_key = '${req.body.cf_recaptcha_secret_key}',
        cf_possible_ip = '${req.body.cf_possible_ip}',
        cf_intercept_ip = '${req.body.cf_intercept_ip}',
        cf_analytics = '${req.body.cf_analytics}',
        cf_add_meta = '${req.body.cf_add_meta}',
        cf_syndi_token = '${req.body.cf_syndi_token}',
        cf_syndi_except = '${req.body.cf_syndi_except}',
        cf_delay_sec = '${req.body.cf_delay_sec}',
        cf_link_target = '${req.body.cf_link_target}',
        cf_search_part = '${req.body.cf_search_part}',
        cf_image_extension = '${req.body.cf_image_extension}',
        cf_flash_extension = '${req.body.cf_flash_extension}',
        cf_movie_extension = '${req.body.cf_movie_extension}',
        cf_filter = '${req.body.cf_filter}',
        cf_privacy = '${req.body.cf_privacy}',
        cf_privacy_en = '${req.body.cf_privacy_en}',
        cf_bbs_rewrite = '${req.body.cf_bbs_rewrite}',
        cf_email_use = '${req.body.cf_email_use}',
        cf_use_email_certify = '${req.body.cf_use_email_certify}',
        cf_formmail_is_member = '${req.body.cf_formmail_is_member}',
        cf_email_wr_super_admin = '${req.body.cf_email_wr_super_admin}',
        cf_email_wr_group_admin = '${req.body.cf_email_wr_group_admin}',
        cf_email_wr_board_admin = '${req.body.cf_email_wr_board_admin}',
        cf_email_wr_write = '${req.body.cf_email_wr_write}',
        cf_email_wr_comment_all = '${req.body.cf_email_wr_comment_all}',
        cf_add_script = '${req.body.cf_add_script}',
        cf_sms_use = '${req.body.cf_sms_use}',
        cf_sms_type = '${req.body.cf_sms_type}',
        cf_icode_id = '${req.body.cf_icode_id}',
        cf_icode_pw = '${req.body.cf_icode_pw}',
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
                de_admin_company_saupja_no  = '${req.body.de_admin_company_saupja_no}',
                de_admin_company_owner      = '${req.body.de_admin_company_owner}',
                de_admin_company_tel        = '${req.body.de_admin_company_tel}',
                de_admin_company_fax        = '${req.body.de_admin_company_fax}',
                de_admin_tongsin_no         = '${req.body.de_admin_tongsin_no}',
                de_admin_buga_no            = '${req.body.de_admin_buga_no}',
                de_admin_company_zip        = '${req.body.de_admin_company_zip}',
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
        debugger
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