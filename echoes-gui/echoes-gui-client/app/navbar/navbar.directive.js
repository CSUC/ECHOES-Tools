(function () {

    'use strict';

    angular
        .module('app')
        .directive('navbar', navbar);

    function navbar() {
        return {
            templateUrl: 'app/navbar/navbar.html',
            controller: navbarController,
            controllerAs: 'vm'
        }
    }

    navbarController.$inject = ['authService', '$scope', '$log', 'restApi'];

    function navbarController(authService, $scope, $log, restApi) {

        var vm = this;
        vm.auth = authService;
        vm.profile;
        vm.isAdmin = false;

        if (authService.isAuthenticated()) {
            if (authService.getCachedProfile()) {
                restApi.getRoles({
                    user: authService.getCachedProfile().sub
                }).then(function (_data) {
                    console.log("ROLES:", _data)
                    angular.forEach(angular.fromJson(_data.data), function (role) {
                        console.log("ROLE:", role);
                        if (role['name'] == 'ADMIN') {
                            vm.isAdmin = true;
                        }
                    });
                }).catch(function (_data) {
                    console.log(_data);
                });
            } else {
                authService.getProfile(function (err, profile) {
                    restApi.getRoles({
                        user: profile.sub
                    }).then(function (_data) {
                        console.log("ROLES:", _data)
                        angular.forEach(angular.fromJson(_data.data), function (role) {
                            console.log("ROLE:", role);
                            if (role['name'] == 'ADMIN') {
                                vm.isAdmin = true;
                            }
                        });
                    }).catch(function (_data) {
                        console.log(_data);
                    });
                });
            }
        }
    }
})();