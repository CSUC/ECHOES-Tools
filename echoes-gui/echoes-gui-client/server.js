const express = require('express'),
    app = express(),
    path = require('path'),
    requestify = require('requestify'),
    request = require('request'),
    bodyParser  = require("body-parser"),
    methodOverride = require("method-override"),
    fs = require('fs'),
    util = require('util'),
    fileUpload = require('express-fileupload');

require('dotenv').config();

if (!process.env.GUI_HOSTNAME || !process.env.GUI_PORT) {
    throw 'Make sure you have GUI_HOSTNAME, and GUI_PORT in your .env file'
}

console.log(process.env.GUI_PORT)

app.use('/', express.static(__dirname + '/'));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(methodOverride());
app.use(fileUpload({
    useTempFiles : true,
    tempFileDir : '/tmp/',
    limits: { fileSize: 50 * 1024 * 1024 },
    safeFileNames: true,
    preserveExtension: true
}));


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
        'Content-Type': 'application/json'
    };

    // console.log(headers)

    var r = request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/analyse/user/` + req.params.user + '/id/' + req.params.id + '/download'
    });
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

        if(response && response.statusCode == 202 ) res.json(JSON.parse(body));
        else{
            res.status(response.statusCode).end();
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
        value: req.body.value,
        filename: req.body.filename
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
            res.status(response.statusCode).end();
        }
    });
});

app.post('/rest/api/quality/create', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    var formData = {
        dataset: req.body.dataset,
        format: req.body.format,
        user: req.body.user
    }

    request({
        method: 'POST',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/create`,
        json: formData
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});

app.post('/rest/api/quality/user/:user/id/:ud/create-report', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: 'POST',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` + req.params.user + '/id/' +  req.params.id +  `/create-report`
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/quality/user/:user/id/:id/download-report', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    var r = request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` + req.params.user + '/id/' +  req.params.id +  `/download-report`
    });
    r.pipe(res);

    r.on('response', function (resp) {
        console.log(req.method, util.format('%s', resp && resp.statusCode), req.url);
    });
});

app.get('/rest/api/quality/user/:user/id/:id/:filename/download', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
    };

    // console.log(headers)

    var r = request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` + req.params.user + '/id/' + req.params.id + '/' + req.params.filename + '/download'
    })
    r.pipe(res);

    r.on('response', function (resp) {
        console.log(req.method, util.format('%s', resp && resp.statusCode), req.url);
    });
});

app.delete('/rest/api/quality/user/:user/id/:id/delete', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: 'DELETE',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` + req.params.user + '/id/' + req.params.id + '/delete',
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/quality/user/:user', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    //console.log(headers)

    var pagesize = req.query.pagesize;
    var page = req.query.page;

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` +req.params.user + '?pagesize=' + pagesize + '&page=' + page
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(JSON.parse(body));
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

app.get('/rest/api/quality/user/:user/id/:id', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` +req.params.user + '/id/' + req.params.id
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/quality/user/:user/id/:id/error/:page', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` +req.params.user + '/id/' + req.params.id + '/error/'+ req.params.page + '?pagesize=' + req.query.pagesize
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/loader/user/:user/id/:id/error/:page', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` +req.params.user + '/id/' + req.params.id + '/error/'+ req.params.page + '?pagesize=' + req.query.pagesize
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/loader/user/:user/id/:id/:filename/download', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
    };

    // console.log(headers)

    var r = request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` + req.params.user + '/id/' + req.params.id + '/' + req.params.filename + '/download'
    })
    r.pipe(res);

    r.on('response', function (resp) {
        console.log(req.method, util.format('%s', resp && resp.statusCode), req.url);
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

app.get('/rest/api/quality/user/:user/status/aggregation', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/quality/user/` + req.params.user + '/status/aggregation'
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
        input: req.body.input,
        user: req.body.user,
        type: req.body.type,
        format: req.body.format,
        schema: req.body.schema,
        properties: req.body.properties,
        filename: req.body.filename
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

app.get('/rest/api/dashboard/user/:user/status/:status', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/dashboard/user/` + req.params.user + '/status/' + req.params.status
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/dashboard/user/:user/status/:status/lastMonth', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/dashboard/user/` + req.params.user + '/status/' + req.params.status + '/lastMonth'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/dashboard/user/:user/status/:status/month', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/dashboard/user/` + req.params.user + '/status/' + req.params.status + '/month'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/dashboard/user/:user/status/:status/lastMonthIncrease', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/dashboard/user/` + req.params.user + '/status/' + req.params.status + '/lastMonthIncrease'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});


app.get('/rest/api/dashboard/user/:user/recollect', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/dashboard/user/` + req.params.user + '/recollect'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.get('/rest/api/dashboard/user/:user/analyse', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/dashboard/user/` + req.params.user + '/analyse'
    }, function (error, response, body) {
        console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

/****************************************** LOADER ******************************/
app.post('/rest/api/loader/create', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    var formData = {
        endpoint: req.body.endpoint,
        contentType: req.body.contentType,
        contextUri: req.body.contextUri,
        user: req.body.user,
        uuid: req.body.uuid
    }

    request({
        method: 'POST',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/create`,
        json: formData
    }, function (error, response, body) {
        //console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});


app.get('/rest/api/loader/user/:user', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    var pagesize = req.query.pagesize;
    var page = req.query.page;

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` +req.params.user + '?pagesize=' + pagesize + '&page=' + page
    }, function (error, response, body) {
        //console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/loader/user/:user/count', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` +req.params.user + '/count'
    }, function (error, response, body) {
        //console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});


app.get('/rest/api/loader/user/:user/id/:id', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` +req.params.user + '/id/' + req.params.id
    }, function (error, response, body) {
        //console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.get('/rest/api/loader/user/:user/status/aggregation', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` + req.params.user + '/status/aggregation'
    }, function (error, response, body) {
        //console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });
});

app.delete('/rest/api/loader/user/:user/id/:id/delete', function (req, res, next) {
    var headers = {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization
    };

    // console.log(headers)

    request({
        method: 'DELETE',
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` + req.params.user + '/id/' + req.params.id + '/delete',
    }, function (error, response, body) {
       // console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202)  res.json(body);
        else{
            res.status(400).end();
        }
    });
});


app.get('/rest/api/loader/user/:user/getDatasets', function (req, res, next) {
    var headers = {
        'Authorization': req.headers.authorization
    };

    request({
        method: "GET",
        headers: headers,
        url: `http://${process.env.API_HOSTNAME}:${process.env.API_PORT}/rest/loader/user/` +req.params.user + '/getDatasets'
    }, function (error, response, body) {
        //console.log(req.method, util.format('%s', response && response.statusCode), req.url);

        if(response && response.statusCode == 202) res.json(JSON.parse(body));
        else{
            res.status(400).end();
        }
    });

});

app.post('/upload', function(req, res) {
    if (Object.keys(req.files).length == 0) {
        return res.status(400).send('No files were uploaded.');
    }
    let dropzone1 = req.files.file;
    console.log(req.files.file)
    res.status(200).send(req.files.file)
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
