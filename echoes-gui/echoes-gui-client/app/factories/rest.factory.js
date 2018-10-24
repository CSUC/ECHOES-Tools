(function () {
    'use strict';
    angular
        .module('app')
        .factory('restApi', restApi);

    restApi.$inject = ['$http', 'restApiService', '$log'];

    function restApi($http, restApiService, $log) {
        var data = {};

        data.getParser = function (_params) {
            var searchData = restApiService.getNew("getParser", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getParserError = function (_params) {
            var searchData = restApiService.getNew("getParserError", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.createParser = function (_params) {
            var searchData = restApiService.getNew("createParser", _params);

            $log.info(searchData);

            return $http({
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                url: searchData.url,
                data: searchData.object
            });
        };

        data.createRecollect = function (_params) {
            var searchData = restApiService.getNew("createRecollect", _params);

            $log.info(searchData);

            return $http({
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                url: searchData.url,
                data: searchData.object
            });
        };

        data.deleteParser = function (_params) {
            var searchData = restApiService.getNew("deleteParser", _params);

            $log.info(searchData);

            return $http({
                method: 'DELETE',
                url: searchData.url
            });
        };

        data.deleteRecollect = function (_params) {
            var searchData = restApiService.getNew("deleteRecollect", _params);

            $log.info(searchData);

            return $http({
                method: 'DELETE',
                url: searchData.url
            });
        };

        data.recollectZip = function (_params) {
            var searchData = restApiService.getNew("recollectZip", _params);

            $log.info(searchData);

            return $http({
                method: 'POST',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getParserStatusAggregation = function (_params) {
            var searchData = restApiService.getNew("getParserStatusAggregation", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getRecollect = function (_params) {
            var searchData = restApiService.getNew("getRecollect", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.recollectById = function (_params) {
            var searchData = restApiService.getNew("recollectById", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getRecollectStatusAggregation = function (_params) {
            var searchData = restApiService.getNew("getRecollectStatusAggregation", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        return data;
    }
})();