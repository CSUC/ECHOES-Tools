(function () {

    'use strict';

    angular
        .module('app')
        .controller('QualityControllerDetail', qualityDetail);

    qualityDetail.$inject = ['$scope', 'authService', '$stateParams', 'echoesChart', 'restApi', '$log', '$window', '$interval', '$state'];

    function qualityDetail($scope, authService, $stateParams, echoesChart, restApi, $log, $window, $interval, $state) {
        var vm = this;
        vm.title = 'Quality-detail';
        vm.auth = authService;
        vm.profile;
        vm._id = $stateParams._id;

        //vm.data = JSON.parse($stateParams.data);

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
            restApi.qualityById({
                user: vm.profile.sub,
                id: vm._id,
            }).then(function (_data) {
                $log.info(_data);

                vm.data = _data.data;
            }).catch(function (_data) {
                $log.info(_data);
                //$window.location.href = "/404.html";
            });
        }

        $interval(function () {
            run();
        }, 10000);

    }
})();