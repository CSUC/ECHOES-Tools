(function () {
    'use strict';
    angular
        .module('app')
        .factory('restApi', restApi);

    restApi.$inject = ['$http', 'restApiService', '$log'];

    function restApi($http, restApiService, $log) {
        var data = {};

        data.getAnalyse = function (_params) {
            var searchData = restApiService.getNew("getAnalyse", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getAnalyseError = function (_params) {
            var searchData = restApiService.getNew("getAnalyseError", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.createAnalyse = function (_params) {
            var searchData = restApiService.getNew("createAnalyse", _params);

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

        data.deleteAnalyse = function (_params) {
            var searchData = restApiService.getNew("deleteAnalyse", _params);

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

        data.getAnalyseStatusAggregation = function (_params) {
            var searchData = restApiService.getNew("getAnalyseStatusAggregation", _params);

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