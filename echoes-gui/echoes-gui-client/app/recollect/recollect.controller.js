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
        vm.title = 'Recollect';
        vm.auth = authService;
        vm.data;
        vm.tableParams;
        vm.chart;

        vm.profile;

        vm.page = $stateParams.page;
        vm.count = $stateParams.count;

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

        function run(_page, _count) {
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
                pagesize: _count,
                page: _page
            }).then(function (_data) {
                $log.info(_data);

                vm.data = _data.data;
                if(_data.data._size > 0)
                    vm.tableParams = ngTableParams(vm.data, _count)
            }).catch(function (_data) {
                $log.info(_data);
                $window.location.href = "/404.html";
            });
        }

        function ngTableParams(data, count) {
            return new NgTableParams({
                page: vm.page,
                count: count
            }, {
                total: data._size,
                getData: function (params) {
                    vm.page = params.page();
                    vm.count = params.count();

                    return restApi.getRecollect({
                        user: vm.profile.sub,
                        pagesize: vm.count,
                        page: vm.page
                    }).then(function (d) {
                        //return d.data._embedded;
                        return d.data._embedded;
                        //$defer.resolve(d.data._embedded)
                    }).catch(function (_data) {
                        $log.info(_data);
                        $defer.reject();
                        $window.location.href = "/404.html";
                    });
                }
            });
        }

        $interval(function () {
            //$state.go($state.current, {page: vm.page, count: vm.count}, {reload: true});
            run(vm.page, vm.count)
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
                //$window.location.href = "/404.html";
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

                    }]
                });
        };

    }
})();