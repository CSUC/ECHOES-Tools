(function () {

    'use strict';

    angular
        .module('app')
        .controller('AnalyseControllerDetail', analyseDetail);

    analyseDetail.$inject = ['$scope', 'authService', '$stateParams', 'echoesChart', 'restApi', '$log'];

    function analyseDetail($scope, authService, $stateParams, echoesChart, restApi, $log) {
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
            restApi.getAnalyseError({
                user: vm.profile.sub,
                id: vm._id,
            }).then(function (_data) {
                $log.info(_data);

                vm.data = _data.data;
            }).catch(function (_data) {
                $log.info(_data);
                $state.go("404");
            });
        }
    }
})();