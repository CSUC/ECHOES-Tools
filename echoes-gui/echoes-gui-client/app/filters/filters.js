(function () {
    'use strict';
    angular
        .module('app')
        .filter('na', na)
        .filter('urlencode', urlencode);

    function na() {
        return function (input) {
            return (angular.equals(input, 0)) ? 'N/A' : input;
        };
    }
    
    function urlencode(){
        return function (input) {
            return window.encodeURIComponent(input);
        }
    }
    
})();