(function () {

    'use strict';

    angular
        .module('app')
        .controller('AnalyseControllerDetail', analyseDetail);

    analyseDetail.$inject = ['$scope', 'authService', '$stateParams', 'echoesChart', 'restApi', '$log', '$state'];

    function analyseDetail($scope, authService, $stateParams, echoesChart, restApi, $log, $state) {
        var vm = this;
        vm.title = 'analyse-detail';
        vm.auth = authService;
        vm.profile;
        vm._id = $stateParams._id;

        if (authService.getCachedProfile()) {
            vm.profile = authService.getCachedProfile();
            run();
        } else {
            authService.getProfile(function (err, profile) {
                vm.profile = profile;
                $scope.$apply();
                run();
            });
        }

        function run(){
            restApi.getAnalyseById({
                user: vm.profile.sub,
                id: vm._id,
            }).then(function (_d) {
                $log.info(_d);
                vm.data = _d.data;
            }).catch(function (_d) {
                $log.info(_d);
                // $state.go("404");
            });

            restApi.getAnalyseError({
                user: vm.profile.sub,
                id: vm._id,
            }).then(function (_d) {
                $log.info(_d);

                if(angular.equals(_d.status, 200))
                    vm.exception = _d.data.exception;
            }).catch(function (_d) {
                $log.info(_d);
                // $state.go("404");
            });
        }
    }
})();