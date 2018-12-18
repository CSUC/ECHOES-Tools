const express = require('express'),
    app = express(),
    path = require('path'),
    requestify = require('requestify'),
    request = require('request'),
    bodyParser  = require("body-parser"),
    methodOverride = require("method-override"),
    fs = require('fs'),
    util = require('util');

require('dotenv').config();

if (!process.env.GUI_HOSTNAME || !process.env.GUI_PORT) {
    throw 'Make sure you have GUI_HOSTNAME, and GUI_PORT in your .env file'
}

console.log(process.env.GUI_PORT)

app.use('/', express.static(__dirname + '/'));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(methodOverride());


app.get('/rest/api/analyse/user/:user/id/:id', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/user/` + req.params.user +'/id/' + req.params.id
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode),req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/analyse/user/:user/id/:id/download', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
    };

    // console.log(headers)

    var r = request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/user/` + req.params.user + '/id/' + req.params.id + '/download'
    })
    r.pipe(res);

    r.on('response', function (resp) {
        console.log(req.method, util.format('%s', resp && resp.statusCode), req.url);
    });


});

app.get('/rest/api/analyse/user/:user/id/:id/error', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/user/` + req.params.user + '/id/' + req.params.id + '/error'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode),req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/analyse/user/:user', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    //console.log(headers)

    var pagesize = req.query.pagesize;
    var page = req.query.page;

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/user/` +req.params.user + '?pagesize=' + pagesize + '&page=' + page
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/analyse/user/:user/status/aggregation', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/user/` + req.params.user + '/status/aggregation'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.post('/rest/api/analyse/create', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    var formData = {
        method: req.body.method,
        type: req.body.type,
        format: req.body.format,
        user: req.body.user,
        value: req.body.value
    }

    request({
        method: 'POST',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/create`,
        json: formData
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});

app.delete('/rest/api/analyse/user/:user/id/:id/delete', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: 'DELETE',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/user/` + req.params.user + '/id/' + req.params.id + '/delete',
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});


/************************ RECOLLECT *****************************/

app.get('/rest/api/recollect/user/:user', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    var pagesize = req.query.pagesize;
    var page = req.query.page;

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/recollect/user/` +req.params.user + '?pagesize=' + pagesize + '&page=' + page
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});


app.get('/rest/api/recollect/user/:user/id/:id', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/recollect/user/` +req.params.user + '/id/' + req.params.id
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/recollect/user/:user/status/aggregation', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/recollect/user/` + req.params.user + '/status/aggregation'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.post('/rest/api/recollect/create', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    var formData = {
        host: req.body.host,
        set: req.body.set,
        metadataPrefix: req.body.metadataPrefix,
        user: req.body.user,
        from: req.body.from,
        until: req.body.until,
        granularity: req.body.granularity,
        format: req.body.format,
        schema: req.body.schema,
        properties: req.body.properties,
    }

    request({
        method: 'POST',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/recollect/create`,
        json: formData
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});

app.delete('/rest/api/recollect/user/:user/id/:id/delete', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: 'DELETE',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/recollect/user/` + req.params.user + '/id/' + req.params.id + '/delete',
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});

app.post('/rest/api/recollect/user/:user/id/:id/zip', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: 'POST',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/recollect/user/` + req.params.user + '/id/' + req.params.id + '/zip'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/recollect/user/:user/id/:id/download', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
    };

    // console.log(headers)

    var r = request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/recollect/user/` + req.params.user + '/id/' + req.params.id + '/download'
    })
    r.pipe(res);

    r.on('response', function (resp) {
        console.log(req.method, util.format('%s', resp && resp.statusCode), req.url);
    });


});

/****************************************************************/

app.get('/*', function(req, res) {
    res.sendFile(path.join(__dirname + '/index.html'));
});

const hostname = process.env.GUI_HOSTNAME;
const port = process.env.GUI_PORT;

const server = app.listen(port, hostname, () => {
    console.log(`Server running at http://${hostname}:${port}/`);
});
