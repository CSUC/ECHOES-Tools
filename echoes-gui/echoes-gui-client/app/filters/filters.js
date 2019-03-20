(function () {
    'use strict';
    angular
        .module('app')
        .filter('na', na);

    function na() {
        return function (input) {
            return (angular.equals(input, 0)) ? 'N/A' : input;
        };
    }
})();