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
                id: vm._id
            }).then(function (_data) {
                $log.info(_data);
                vm.data = _data.data;
            }).catch(function (_data) {
                $log.info(_data);
                //$window.location.href = "/404.html";
            });
        }

        vm.saveTextAsFile = function (filename) {
            if (!vm.data.qualityConfig) {
                console.error('Console.save: No data')
                return;
            }

            if (!filename) filename = 'quality.conf'

            var blob = new Blob([vm.data.qualityConfig], {type: 'application/json'}),
                e = document.createEvent('MouseEvents'),
                a = document.createElement('a')
            // FOR IE:

            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                var e = document.createEvent('MouseEvents'),
                    a = document.createElement('a');

                a.download = filename;
                a.href = window.URL.createObjectURL(blob);
                a.dataset.downloadurl = ['text/plain', a.download, a.href].join(':');
                e.initEvent('click', true, false, window,
                    0, 0, 0, 0, 0, false, false, false, false, 0, null);
                a.dispatchEvent(e);
            }
        }

        $interval(function () {
            run();
        }, 10000);

    }
})();