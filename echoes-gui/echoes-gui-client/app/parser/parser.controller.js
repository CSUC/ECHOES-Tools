(function () {

    'use strict';

    angular
        .module('app')
        .controller('ParserController', parserController);

    parserController.$inject = ['$scope', 'authService', 'uuid', 'NgTableParams', '$http', '$log', '$stateParams', '$interval',
        'echoesChart', 'restApi', 'ngDialog', '$state', '$timeout'];

    function parserController($scope, authService, uuid, NgTableParams, $http, $log, $stateParams, $interval,
                              echoesChart, restApi, ngDialog, $state) {
        var vm = this;
        vm.title = 'Parser';
        vm.auth = authService;
        vm.data;
        vm.tableParams;
        vm.chart;

        vm.profile;

        vm.page = $stateParams.page;
        vm.count = $stateParams.count;

        if (authService.getCachedProfile()) {
            vm.profile = authService.getCachedProfile();
            run(vm.page, vm.count);
        } else {
            authService.getProfile(function (err, profile) {
                vm.profile = profile;
                $scope.$apply();
                run(vm.page, vm.count)
            });
        }

        function run(_page, _count) {
            $log.info(vm.profile);

            vm.selectedClass = function (status) {
                if (status === 'END') return 'alert alert-success';
                if (status === 'ERROR') return 'alert alert-danger';
                return 'alert';
            }

            restApi.getParser({
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

            restApi.getParserStatusAggregation({
                user: vm.profile.sub
            }).then(function (_data) {
                vm.chart = echoesChart.getAggregationDoughnut(_data.data)
            }).catch(function (_data) {
                console.log(_data)
                $window.location.href = "/404.html";
            })
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

                    return restApi.getParser({
                        user: vm.profile.sub,
                        pagesize: vm.count,
                        page: vm.page
                    }).then(function (d) {
                        return d.data._embedded;
                        //return d.data._embedded;
                    }).catch(function (_data) {
                        $log.info(_data);
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
            restApi.deleteParser({
                user: vm.profile.sub,
                id: id
            }).then(function (_data) {
                $log.info(_data);

                $state.go($state.current, {}, {reload: true});
            }).catch(function (_data) {
                $log.info(_data);
                //$window.location.href = "/404.html";
            });
        }

        $scope.clickToOpen = function () {
            var dailog =
                ngDialog.open({
                    template: 'parser.tpl.html',
                    width: '60%',
                    data: vm,
                    controller: ['$scope', '$state', '$log', function ($scope, $state, $log) {
                        $log.info(vm.profile.sub)

                        $scope.options = {
                            methods: ["sax", "dom4j", "dom", "xslt"],
                            formats: ["xml", "json"],
                            types: ["oai", "url", "file"]
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

                                    $state.go($state.current, {}, {reload: true});
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