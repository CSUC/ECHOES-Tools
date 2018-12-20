(function () {

    'use strict';

    angular
        .module('app')
        .controller('HomeController', homeController);

    homeController.$inject = ['authService', '$scope', '$log'];

    function homeController(authService, $scope, $log) {

        var vm = this;
        vm.auth = authService;

    }

})();