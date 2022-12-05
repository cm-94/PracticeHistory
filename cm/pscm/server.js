const DPORT = 3300;
const express = require('express');
const bodyParser = require('body-parser');

const connApi = require('./api/conn');

const app = express(express);
app.use(bodyParser.urlencoded({
    limit: '50mb',
    extended: true
}));
app.use(bodyParser.json({
    limit: '50mb'
}));
app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "*");
    next();
});

app.all(__dirname + '/*', (req, res, next) => {
    let headers = req.headers;
    try{
        let contentTypes = headers['content-type'].split(";");
        contentTypes.forEach(elem => {
            let types = elem.split("=");
            if(types.length >= 2) {
                headers[types[0].trim().toLowerCase()] = types[1];
            }
        });
    }
    catch(e) {

    }
    next();
});

connApi(app)

app.use((err, req, res, next) => console.log('There was an error', err));

app.listen(DPORT,'0.0.0.0', () => {
    console.log(`Application started at port ${DPORT}`);
});