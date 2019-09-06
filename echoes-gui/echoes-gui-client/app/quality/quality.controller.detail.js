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
                if(vm.data.errorSize > 0)   vm.downloadReport = downloadReport;
            }).catch(function (_data) {
                $log.info(_data);
                //$window.location.href = "/404.html";
            });
        }

        $interval(function () {
            run();
        }, 10000);

        function downloadReport() {
            return restApi.downloadQualityReport({
                user: vm.profile.sub,
                id: vm._id,
            }).then(function (_data) {
                $log.info(_data);
                var file = new Blob(([data]), { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });

                startBlobDownload(file, vm._id + ".xslx")
            }).catch(function (_data) {
                $log.info(_data);
            })
        }


        function startBlobDownload(dataBlob, suggestedFileName) {
            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                // for IE
                window.navigator.msSaveOrOpenBlob(dataBlob, suggestedFileName);
            } else {
                // for Non-IE (chrome, firefox etc.)
                var urlObject = URL.createObjectURL(dataBlob);

                var downloadLink = angular.element('<a>Download</a>');
                downloadLink.css('display','none');
                downloadLink.attr('href', urlObject);
                downloadLink.attr('download', suggestedFileName);
                angular.element(document.body).append(downloadLink);
                downloadLink[0].click();

                // cleanup
                downloadLink.remove();
                URL.revokeObjectURL(urlObject);
            }
        }
    }
})();