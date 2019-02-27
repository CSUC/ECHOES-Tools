(function () {

    'use strict';

    angular
        .module('app')
        .controller('QualityControllerError', qualityError);

    qualityError.$inject = ['$scope', 'authService', 'uuid', 'NgTableParams', '$http', '$log', '$stateParams', '$interval',
        'echoesChart', 'restApi', 'ngDialog', '$state', '$timeout'];

    function qualityError($scope, authService, uuid, NgTableParams, $http, $log, $stateParams, $interval,
                          echoesChart, restApi, ngDialog, $state) {
        var vm = this;

        vm.title = 'Quality-detail-Error';
        vm.auth = authService;

        vm.data;
        vm.tableParams;

        vm._id = $stateParams._id;
        vm.page = $stateParams.page;
        vm.count = $stateParams.pagesize;

        $log.info("count: ", $state.params)

        if (authService.getCachedProfile()) {
            vm.profile = authService.getCachedProfile();

            run()
        } else {
            authService.getProfile(function (err, profile) {
                vm.profile = profile;
                $scope.$apply();

                run()
            });
        }

        function run() {
            restApi.qualityErrors({
                user: vm.profile.sub,
                id: vm._id,
                page: vm.page,
                pagesize: vm.count
            })
                .then(function (_data) {
                    $log.info(_data.data);

                    vm.data = _data.data;
                    if(_data.data._size > 0)
                        vm.tableParams = ngTableParams(vm.data, vm.count)

                })
                .catch(function (reason) {
                    console.log(reason)
                }).finally(function() {
                console.log("finally finished gists");
            });
        }


        function ngTableParams(data, count) {
            return new NgTableParams({
                page: vm.page,
                count: count
            }, {
                total: data._size,
                getData: function (params) {
                    vm.page = params.page();
                    vm.count = params.count();

                    return restApi.qualityErrors({
                        id: vm._id,
                        user: vm.profile.sub,
                        pagesize: vm.count,
                        page: vm.page
                    }).then(function (d) {
                        return d.data._embedded;
                    }).catch(function (_data) {
                        $log.info(_data);
                    })
                        .finally(function () { $state.go("quality-error", {_id: vm._id, page: vm.page, pagesize: vm.count}); });
                }
            });
        }


        vm.schematron = function (data) {
            ngDialog.open({
                template: 'schematron.tpl.html',
                data: data,
                className: 'ngdialog-theme-default',
                width: '70%'
            });
        }

        vm.schema = function (data) {
            ngDialog.open({
                template: 'schema.tpl.html',
                data: data,
                className: 'ngdialog-theme-default',
                width: '70%'
            });
        }
    }
})();