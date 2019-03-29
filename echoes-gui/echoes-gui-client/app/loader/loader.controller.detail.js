(function () {

    'use strict';

    angular
        .module('app')
        .controller('LoaderControllerDetail', loaderDetail)
        .filter('urlencode');

    loaderDetail.$inject = ['$scope', 'authService', '$stateParams', 'echoesChart', 'restApi', '$log'];

    function loaderDetail($scope, authService, $stateParams, echoesChart, restApi, $log) {
        var vm = this;
        vm.title = 'Publish-detail';
        vm.auth = authService;
        vm.profile;
        vm._id = $stateParams._id;

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

        function run(){
            restApi.loaderById({
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