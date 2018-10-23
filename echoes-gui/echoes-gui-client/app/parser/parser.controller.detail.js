(function () {

    'use strict';

    angular
        .module('app')
        .controller('ParserControllerDetail', parserDetail);

    parserDetail.$inject = ['$scope', 'authService', '$stateParams', 'echoesChart', 'restApi', '$log', '$window'];

    function parserDetail($scope, authService, $stateParams, echoesChart, restApi, $log, $window) {
        var vm = this;
        vm.title = 'Parser-detail';
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
            restApi.getParserError({
                user: vm.profile.sub,
                id: vm._id,
            }).then(function (_data) {
                $log.info(_data);

                vm.data = _data.data;
            }).catch(function (_data) {
                $log.info(_data);
                $window.location.href = "/404.html";
            });
        }
    }
})();