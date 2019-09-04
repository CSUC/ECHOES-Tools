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

        data.getAnalyseById = function (_params) {
            var searchData = restApiService.getNew("getAnalyseById", _params);

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

        data.createLoader = function (_params) {
            var searchData = restApiService.getNew("createLoader", _params);

            $log.info(searchData);

            return $http({
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                url: searchData.url,
                data: searchData.object
            });
        };

        data.createQuality = function (_params) {
            var searchData = restApiService.getNew("createQuality", _params);

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

        data.deleteLoader = function (_params) {
            var searchData = restApiService.getNew("deleteLoader", _params);

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

        data.qualityById = function (_params) {
            var searchData = restApiService.getNew("qualityById", _params);

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

        data.getDashboardAnalyse = function (_params) {
            var searchData = restApiService.getNew("getDashboardAnalyse", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getDashboardRecollect = function (_params) {
            var searchData = restApiService.getNew("getDashboardRecollect", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getStatus = function (_params) {
            var searchData = restApiService.getNew("getStatus", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getStatusMonth = function (_params) {
            var searchData = restApiService.getNew("getStatusMonth", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getStatusLastMonth = function (_params) {
            var searchData = restApiService.getNew("getStatusLastMonth", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getStatusLastMonthIncrease = function (_params) {
            var searchData = restApiService.getNew("getStatusLastMonthIncrease", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getStatusLastYear = function (_params) {
            var searchData = restApiService.getNew("getStatusLastYear", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getStatusLastDay = function (_params) {
            var searchData = restApiService.getNew("getStatusLastDay", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        //LOADER
        data.getLoader = function (_params) {
            var searchData = restApiService.getNew("getLoader", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.loaderById = function (_params) {
            var searchData = restApiService.getNew("loaderById", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getLoaderStatusAggregation = function (_params) {
            var searchData = restApiService.getNew("getLoaderStatusAggregation", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.loaderCountByUser = function (_params) {
            var searchData = restApiService.getNew("loaderCountByUser", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getQuality = function (_params) {
            var searchData = restApiService.getNew("getQuality", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.getQualityStatusAggregation = function (_params) {
            var searchData = restApiService.getNew("getQualityStatusAggregation", _params);

            $log.info(searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.deleteQuality = function (_params) {
            var searchData = restApiService.getNew("deleteQuality", _params);

            $log.info(searchData);

            return $http({
                method: 'DELETE',
                url: searchData.url
            });
        };

        data.qualityErrors = function (_params) {
            var searchData = restApiService.getNew("qualityErrors", _params);

            $log.info('qualityErrors: ', searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        data.loaderErrors = function (_params) {
            var searchData = restApiService.getNew("loaderErrors", _params);

            $log.info('loaderErrors: ', searchData);

            return $http({
                method: 'GET',
                url: searchData.url,
                params: searchData.object
            });
        };

        //

        return data;
    }
})();