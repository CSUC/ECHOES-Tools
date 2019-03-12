(function () {

    'use strict';

    angular
        .module('app')
        .controller('LoaderControllerError', loaderControllerError);

    loaderControllerError.$inject = ['$scope', 'authService', 'uuid', 'NgTableParams', '$http', '$log', '$stateParams', '$interval',
        'echoesChart', 'restApi', 'ngDialog', '$state', '$timeout'];

    function loaderControllerError($scope, authService, uuid, NgTableParams, $http, $log, $stateParams, $interval,
                          echoesChart, restApi, ngDialog, $state) {
        var vm = this;

        vm.title = 'Publish-detail-error';
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
            restApi.loaderErrors({
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

                    return restApi.loaderErrors({
                        id: vm._id,
                        user: vm.profile.sub,
                        pagesize: vm.count,
                        page: vm.page
                    }).then(function (d) {
                        return d.data._embedded;
                    }).catch(function (_data) {
                        $log.info(_data);
                    })
                        .finally(function () { $state.go("loader-error", {_id: vm._id, page: vm.page, pagesize: vm.count}); });
                }
            });
        }
    }
})();