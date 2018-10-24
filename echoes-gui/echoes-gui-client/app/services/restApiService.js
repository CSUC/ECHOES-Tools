(function () {
    'use strict';
    angular
        .module('app')
        .service('restApiService', restApiService);

    function restApiService() {
        this.getApiBaseUrl = function () {
            return 'rest/api/';
        };
        this.fillDataInObjectByList = function (_object, _params, _list) {
            angular.forEach(_list, function (value, key) {
                if (angular.isDefined(_params[value])) {
                    _object.object[value] = _params[value];
                }
            });
            return _object;
        };
        this.getNew = function (_type, _params) {
            var data = {
                object: {},
                url: '',
            };
            switch (_type) {
                case "getParser":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'parser/user/' + _params.user;
                    break;
                case "getParserError":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'parser/user/' + _params.user + '/id/' + _params.id + '/error';
                    break;
                case "createParser":
                    data = this.fillDataInObjectByList(data, _params, [
                        'method', 'type', 'format', 'user', 'value'
                    ]);
                    data.url = this.getApiBaseUrl() + 'parser/create';
                    break
                case "createRecollect":
                    data = this.fillDataInObjectByList(data, _params, [
                        'host', 'set', 'metadataPrefix', 'from', 'until', 'granularity', 'properties', 'user'
                    ]);
                    data.url = this.getApiBaseUrl() + 'recollect/create';
                    break
                case "deleteParser":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'parser/user/' + _params.user + '/id/' + _params.id + '/delete';
                    break;
                case "deleteRecollect":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user + '/id/' + _params.id + '/delete';
                    break;
                case "recollectZip":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user + '/id/' + _params.id + '/zip';
                    break;
                case "getParserStatusAggregation":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'parser/user/' + _params.user + '/status/aggregation';
                    break;
                case "getRecollect":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user;
                    break;
                case "recollectById":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user + '/id/' + _params.id;
                    break;
                case "getRecollectStatusAggregation":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user + '/status/aggregation';
                    break;
                case "getValidation":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    //data.url =
                    break;
            }
            return data;
        }
    }
})();