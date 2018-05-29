(function () {

    'use strict';

    angular
        .module('app')
        .controller('RecollectController', recollect);

    recollect.$inject = ['$scope', 'authService', 'uuid', 'NgTableParams', '$http', '$log', '$stateParams', '$interval',
        'echoesChart', 'restApi', 'ngDialog', '$state'];

    function recollect($scope, authService, uuid, NgTableParams, $http, $log, $stateParams, $interval,
                       echoesChart, restApi, ngDialog, $state) {
        var vm = this;
        vm.title = 'Recollect-Core';
        vm.auth = authService;
        vm.data;
        vm.tableParams;
        vm.chart;

        vm.profile;

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

        function run() {
            $log.info(vm.profile);

            restApi.getRecollectStatusAggregation({
                user: vm.profile.sub
            }).then(function (_data) {
                vm.chart = echoesChart.getAggregationDoughnut(_data.data)
            }).catch(function (_data) {
                console.log(_data)
                $window.location.href = "/404.html";
            })

            restApi.getRecollect({
                user: vm.profile.sub,
                pagesize: 1000,
                page: 1
            }).then(function (_data) {
                $log.info(_data);

                vm.data = _data.data;
                vm.tableParams = new NgTableParams({

                }, {
                    dataset: vm.data._embedded,
                });
            }).catch(function (_data) {
                $log.info(_data);
                $window.location.href = "/404.html";
            });
        }

        $interval(function () {
            run();
        }, 10000);

        $scope.remove = function (id) {
            restApi.deleteRecollect({
                user: vm.profile.sub,
                id: id
            }).then(function (_data) {
                $log.info(_data);

                $state.go($state.current, {}, {reload : true});
            }).catch(function (_data) {
                $log.info(_data);
                $window.location.href = "/404.html";
            });
        }

        $scope.clickToOpen = function () {
            var dailog =
                ngDialog.open({
                    template: 'recollect.tpl.html',
                    width: '60%',
                    data: vm,
                    controller: ['$scope' , '$state', '$log', 'NgTableParams', '$window', function ($scope, $state, $log, NgTableParams, $window) {
                        $log.info(vm.profile.sub)

                        $scope.model = {};

                        $scope.submitForm = function (isValid) {
                            var properties = {};
                            if (isValid) {
                                $scope.tableProperties.forEach(function(value, index) {
                                    if(value.key != null && value.value != null){
                                        var k = value.key;
                                        var v = value.value;
                                        Object.assign(properties, { [k] : v});
                                    }
                                });

                                var data = {
                                    'host': $scope.model.host,
                                    'set': $scope.model.set,
                                    'metadataPrefix': $scope.model.metadataPrefix,
                                    'from': vm.profile.from,
                                    'until': $scope.model.until,
                                    'granularity': $scope.model.granularity,
                                    'properties': properties,
                                    'user': vm.profile.sub
                                };

                                $log.info(data);

                                restApi.createRecollect(data).then(function (_data) {
                                    $log.info(_data);

                                    ngDialog.close();

                                    $state.go($state.current, {}, {reload : true});
                                }).catch(function (_data) {
                                    $log.info(_data);
                                    $window.location.href = "/404.html";
                                });
                            }
                        };
                        ///

                        $scope.tableProperties = [
                            {
                                'key': null,
                                'value': null
                            }];

                        $scope.addNew = function(personalDetail){
                            $scope.tableProperties.push({
                                'key': null,
                                'value':null
                            });
                        };

                        $scope.remove = function(){
                            var newDataList=[];
                            $scope.selectedAll = false;
                            angular.forEach($scope.tableProperties, function(selected){
                                if(!selected.selected){
                                    newDataList.push(selected);
                                }
                            });
                            $scope.tableProperties = newDataList;
                        };

                        $scope.checkAll = function () {
                            if (!$scope.selectedAll) {
                                $scope.selectedAll = true;
                            } else {
                                $scope.selectedAll = false;
                            }
                            angular.forEach($scope.tableProperties, function(properties) {
                                properties.selected = $scope.selectedAll;
                            });
                        };

                        ///
                    }]
                });
        };

    }
})();