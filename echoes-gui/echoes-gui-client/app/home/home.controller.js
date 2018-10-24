(function () {

    'use strict';

    angular
        .module('app')
        .controller('HomeController', homeController);

    homeController.$inject = ['authService', '$scope'];

    function homeController(authService, $scope) {

        var vm = this;
        vm.auth = authService;

    }

})();