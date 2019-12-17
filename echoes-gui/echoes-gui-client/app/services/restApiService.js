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
                case "getAnalyse":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'analyse/user/' + _params.user;
                    break;
                case "getAnalyseById":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'analyse/user/' + _params.user + '/id/' + _params.id;
                    break;
                case "getAnalyseError":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'analyse/user/' + _params.user + '/id/' + _params.id + '/error';
                    break;
                case "createAnalyse":
                    data = this.fillDataInObjectByList(data, _params, [
                        'method', 'type', 'format', 'user', 'value', 'filename'
                    ]);
                    data.url = this.getApiBaseUrl() + 'analyse/create';
                    break;
                case "createRecollect":
                    data = this.fillDataInObjectByList(data, _params, [
                        'input', 'properties', 'type', 'filename', 'user', 'format', 'schema'
                    ]);
                    data.url = this.getApiBaseUrl() + 'recollect/create';
                    break;
                case "createQuality":
                    data = this.fillDataInObjectByList(data, _params, [
                        'dataset', 'format', 'user', 'quality'
                    ]);
                    data.url = this.getApiBaseUrl() + 'quality/create';
                    break;
                case "createQualityReports":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'quality/user/' + _params.user + '/id/' + _params.id + '/create-report';
                    break;
                case "downloadQualityReport":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'quality/user/' + _params.user + '/id/' + _params.id + '/download-report';
                    break;
                case "deleteQuality":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'quality/user/' + _params.user + '/id/' + _params.id + '/delete';
                    break;
                case "getQualityStatusAggregation":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'quality/user/' + _params.user + '/status/aggregation';
                    break;
                case "getQuality":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'quality/user/' + _params.user;
                    break;
                case "createLoader":
                    data = this.fillDataInObjectByList(data, _params, [
                        'endpoint', 'contentType', 'contextUri', 'uuid', 'user'
                    ]);
                    data.url = this.getApiBaseUrl() + 'loader/create';
                    break;
                case "deleteAnalyse":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'analyse/user/' + _params.user + '/id/' + _params.id + '/delete';
                    break;
                case "deleteRecollect":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user + '/id/' + _params.id + '/delete';
                    break;
                case "deleteLoader":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'loader/user/' + _params.user + '/id/' + _params.id + '/delete';
                    break;
                case "recollectZip":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user + '/id/' + _params.id + '/zip';
                    break;
                case "getAnalyseStatusAggregation":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'analyse/user/' + _params.user + '/status/aggregation';
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
                case "qualityById":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'quality/user/' + _params.user + '/id/' + _params.id;
                    break;
                case "qualityErrors":
                    data = this.fillDataInObjectByList(data, _params, ['pagesize']);
                    data.url = this.getApiBaseUrl() + 'quality/user/' + _params.user + '/id/' + _params.id + '/error/' + _params.page;
                    break;
                case "loaderErrors":
                    data = this.fillDataInObjectByList(data, _params, ['pagesize']);
                    data.url = this.getApiBaseUrl() + 'loader/user/' + _params.user + '/id/' + _params.id + '/error/' + _params.page;
                    break;
                case "getRecollectStatusAggregation":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'recollect/user/' + _params.user + '/status/aggregation';
                    break;
                case "getLoader":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'loader/user/' + _params.user;
                    break;
                case "loaderById":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'loader/user/' + _params.user + '/id/' + _params.id;
                    break;
                case "getLoaderStatusAggregation":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    data.url = this.getApiBaseUrl() + 'loader/user/' + _params.user + '/status/aggregation';
                    break;
                case "getValidation":
                    data = this.fillDataInObjectByList(data, _params, [
                        'page', 'pagesize'
                    ]);
                    break;
                case "getDashboardAnalyse":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/analyse';
                    break;
                case "getDashboardRecollect":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/recollect';
                    break;
                case "getStatus":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/status/' + _params.status;
                    break;
                case "getStatusMonth":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/status/' + _params.status + '/month';
                    break;
                case "getStatusLastMonth":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/status/' + _params.status + '/lastMonth';
                    break;
                case "getStatusLastMonthIncrease":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/status/' + _params.status + '/lastMonthIncrease';
                    break;
                case "getStatusLastYear":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/status/' + _params.status + '/lastYear';
                    break;
                case "getStatusLastDay":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'dashboard/user/' + _params.user + '/status/' + _params.status + '/lastDay';
                    break;
                case "loaderCountByUser":
                    data = this.fillDataInObjectByList(data, _params, []);
                    data.url = this.getApiBaseUrl() + 'loader/user/' + _params.user + '/count';
                    break;
            }
            return data;
        }
    }
})();