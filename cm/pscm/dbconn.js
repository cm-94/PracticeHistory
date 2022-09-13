const mysql = require("mysql");
const mysql2 = require('mysql2/promise')
const dbConfig = require("./config/db.config.js");
const dbConfigObj = {
    host: dbConfig.HOST,
    user: dbConfig.USER,
    password: dbConfig.PASSWORD,
    database: dbConfig.DB
}

// Create a connection to the database
const pool = mysql.createPool(dbConfigObj);
const sqlquery = (query, cb, db) => {
    if (!query) {
        cb(null, []);
        return;
    }
    pool.getConnection((err, connection) => {
        if (err) {
            return cb(err);
        }
        if(db){
            connection.changeUser({
                database: db
            });
        }
        connection.query(query, (err, data) => {
            connection.release();
            cb(err, data);
        });
    });
}
module.exports.sqlquery = sqlquery;