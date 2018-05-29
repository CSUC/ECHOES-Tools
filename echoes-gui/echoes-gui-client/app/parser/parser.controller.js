(function () {

    'use strict';

    angular
        .module('app')
        .controller('ParserController', parserController);

    parserController.$inject = ['$scope', 'authService', 'uuid', 'NgTableParams', '$http', '$log', '$stateParams', '$interval',
    'echoesChart', 'restApi', 'ngDialog', '$state'];

    function parserController($scope, authService, uuid, NgTableParams, $http, $log, $stateParams, $interval,
                              echoesChart, restApi, ngDialog, $state) {
        var vm = this;
        vm.title = 'Parser-Core';
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

        function run(){
            $log.info(vm.profile);

            vm.selectedClass = function (status) {
                if (status === 'END') return 'alert alert-success';
                if (status === 'ERROR') return 'alert alert-danger';
                return 'alert';
            }

            restApi.getParser({
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


            restApi.getParserStatusAggregation({
                user: vm.profile.sub
            }).then(function (_data) {
                vm.chart = echoesChart.getAggregationDoughnut(_data.data)
            }).catch(function (_data) {
                console.log(_data)
                $window.location.href = "/404.html";
            })
        }

        $interval(function () {
            run();
        }, 10000);

        $scope.remove = function (id) {
            restApi.deleteParser({
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
                    template: 'parser.tpl.html',
                    width: '60%',
                    data: vm,
                    controller: ['$scope' , '$state', '$log', function ($scope, $state, $log) {
                        $log.info(vm.profile.sub)

                        $scope.options = {
                            methods : ["sax", "dom4j", "dom", "xslt"],
                            formats : ["xml", "json"],
                            types : ["oai", "url", "file"]
                        }

                        $scope.model = {};

                        $scope.submitForm = function (isValid) {
                            if (isValid) {
                                var data = {
                                    'method': $scope.model.method,
                                    'type': $scope.model.type,
                                    'format': $scope.model.format,
                                    'user': vm.profile.sub,
                                    'value': $scope.model.text
                                };
                                $log.info(data);

                                restApi.createParser({
                                    user: vm.profile.sub,
                                    method: $scope.model.method,
                                    type: $scope.model.type,
                                    format: $scope.model.format,
                                    value: $scope.model.text
                                }).then(function (_data) {
                                    $log.info(_data);

                                    ngDialog.close();

                                    $state.go($state.current, {}, {reload : true});
                                }).catch(function (_data) {
                                    $log.info(_data);
                                    $window.location.href = "/404.html";
                                });
                            }
                        };
                    }]
                });
        };
    }
})();