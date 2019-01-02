(function () {

    'use strict';

    angular
        .module('app')
        .controller('HomeController', homeController);

    homeController.$inject = ['authService', '$scope', '$log', 'restApi'];

    function homeController(authService, $scope, $log, restApi) {

        var vm = this;
        vm.auth = authService;
        vm.profile;

        if (authService.getCachedProfile()) {
            vm.profile = authService.getCachedProfile();
            run(vm.page, vm.count)
        } else {
            authService.getProfile(function (err, profile) {
                vm.profile = profile;
                $scope.$apply();
                run(vm.page, vm.count)
            });
        }


        function run(){
            status('END').then(function (d) {
                increase('END').then(function (i) {
                    vm.end = d;
                    vm.increase_end = i;
                });
            });
            status('ERROR').then(function (d) {
                increase('ERROR').then(function (i) {
                    vm.error = d;
                    vm.increase_error = i;
                });
            });
            status('QUEUE').then(function (d) {
                increase('QUEUE').then(function (i) {
                    vm.queue = d;
                    vm.increase_queue = i;
                });
            });
            status('PROGRESS').then(function (d) {
                increase('PROGRESS').then(function (i) {
                    vm.progress = d;
                    vm.increase_progress = i;
                });
            });

            dashboard();
        }


        function dashboard(){
            var result = [];
            var recollect = [];
            var analyse = [];
            var labels = [];

            restApi.getDashboardRecollect({
                user: vm.profile.sub
            }).then(function (_data) {
                angular.forEach(_data.data, function(value, key){
                    recollect.push(value)
                });
                result.push(recollect);
            }).catch(function (_data) {
                $log.error(_data);
            });

            restApi.getDashboardAnalyse({
                user: vm.profile.sub
            }).then(function (_data) {
                angular.forEach(_data.data, function(value, key){
                    analyse.push(value)
                    labels.push(key);
                });
                result.push(analyse);
            }).catch(function (_data) {
                $log.error(_data);
            });

            $scope.labels = labels;
            $scope.series = ['Analyse', 'Recollect'];
            $scope.data = [
                analyse,
                recollect
            ];

            $scope.colors = ["#467fcf", "#a55eea"]

            $scope.options = {
                legend: {
                    display: true,
                },
                showLines:true,
                responsive: true,
                scales: {
                    yAxes: [
                        {
                            id: 'y-axis-1',
                            type: 'linear',
                            display: true,
                            position: 'left',
                            gridLines: false,
                            ticks: {
                                suggestedMin: 0,    // minimum will be 0, unless there is a lower value.
                                // OR //
                                beginAtZero: true   // minimum value will be 0.
                            }
                        }
                    ],
                    xAxes:[
                        {
                            gridLines: false,
                        }
                    ]
                },
                elements: {
                    line: {
                        fill: true,
                        tension: 0.2
                    }
                }
            };
        }

        function status(_status){
           return restApi.getStatusMonth({
                user: vm.profile.sub,
                status: _status
            }).then(function (_data) {
                $log.info('getStatusMonth', _status, _data.data);
                return _data.data;
            }).catch(function (_data) {
                $log.error(_data);
            });
        }
        
        function increase(_status) {
            return restApi.getStatusLastMonthIncrease({
                user: vm.profile.sub,
                status: _status
            }).then(function (_data) {
                $log.info('getStatusLastMonthIncrease', _status, _data.data);
                return _data.data;
            }).catch(function (_data) {
                $log.error(_data);
            });
        }
    }

})();