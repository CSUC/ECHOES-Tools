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
        vm.title = 'Transformation';
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
                // $window.location.href = "/404.html";
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
                // $window.location.href = "/404.html";
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
                        //$window.location.href = "/404.html";
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
                    closeByDocument: false, // to prevent popup close by clicking outside
                    closeByEscape: false,   // to prevent popup close by ESC key
                    closeByNavigation : true, // to close popup on state navigation
                    template: 'recollect.tpl.html',
                    width: '60%',
                    data: vm,
                    controller: ['$scope' , '$state', '$log', 'NgTableParams', '$window', function ($scope, $state, $log, NgTableParams, $window) {
                        $log.info(vm.profile.sub)

                        $scope.model = {};

                        $scope.options = {
                            schema: ["A2A", "DC", "MEMORIX", "EAD"],
                            format: ["RDFXML","NTRIPLES","TURTLE","JSONLD","RDFJSON","NQ","NQUADS","TRIG","RDFTHRIFT","TRIX"],
                            edmType: ["TEXT", "VIDEO", "IMAGE", "SOUND", "3D"],
                            provider: ["Erfgoed", "Tresoar", "Gencat","Test"],
                            dataProvider: ["Erfgoed", "Tresoar", "Gencat","Test"],
			    language:["ar","az","be","bg","bs","ca","cs","cy","da","de","el","en","es","et","eu","fi","fr","ga","gd","gl","he","hi","hr","hu","hy","ie","is","it","ja","ka","ko","lt","lv","mk","mt","mul","nl","no","pl","pt","ro","ru","sk","sl","sq","sr","sv","tr","uk","yi","zh"],
                            rights: ["http://creativecommons.org/publicdomain/mark/1.0/","http://rightsstatements.org/vocab/NoC-NC/1.0/","http://rightsstatements.org/vocab/NoC-OKLR/1.0/","http://creativecommons.org/publicdomain/zero/1.0/","http://creativecommons.org/licenses/by/4.0/","http://creativecommons.org/licenses/by-sa/4.0/","http://creativecommons.org/licenses/by-nd/4.0/","http://creativecommons.org/licenses/by-nc/4.0/","http://creativecommons.org/licenses/by-nc-sa/4.0/","http://creativecommons.org/licenses/by-nc-nd/4.0/","http://rightsstatements.org/vocab/InC/1.0/","http://rightsstatements.org/vocab/InC-EDU/1.0/","http://rightsstatements.org/vocab/InC-OW-EU/1.0/","http://rightsstatements.org/vocab/CNE/1.0/"],
			    //properties: ["language", "rights"],
                            properties: ["rights"],
                            types: ["oai", "url", "file"]
                        };

                        $scope.properties = [
                            {key:'', value:''},
                        ];

                        $scope.submitForm = function (isValid) {
                            var properties = {};
                            if (isValid) {

                                Object.assign(properties, { edmType : $scope.model.edmType});
                                Object.assign(properties, { provider : $scope.model.provider});
                                Object.assign(properties, { dataProvider : $scope.model.dataProvider});
				Object.assign(properties, { language : $scope.model.language});
				Object.assign(properties, { rights : $scope.model.rights});

                                $scope.properties.forEach(function(value, index) {
                                    if(value.key != null && value.value != null){
                                        var k = value.key;
                                        var v = value.value;
                                        if(!properties.hasOwnProperty([k]) && ( k != '' || v != ''))
                                            Object.assign(properties, { [k] : v});
                                    }
                                });

                                var data = {
                                    'format': $scope.model.format,
                                    'type': $scope.model.type,
                                    'schema': $scope.model.schema,
                                    'properties': properties,
                                    'user': vm.profile.sub,
                                    'filename': ( typeof $scope.model.file === 'undefined' ) ? null : $scope.model.file.name,
                                    'input': ( typeof $scope.model.input === 'undefined' ) ? $scope.model.file.tempFilePath : $scope.model.input
                                };

                                $log.info(data);

                                restApi.createRecollect(data).then(function (_data) {
                                    $log.info(_data);

                                    ngDialog.close();

                                    $state.go($state.current, {}, {reload : true});
                                }).catch(function (_data) {
                                    $log.info(_data);
                                    // $window.location.href = "/404.html";
                                });
                            }
                        };

                        $scope.dzCallbacks = {
                            'addedfile' : function(file){
                                $log.log('dzCallbacks file added', file);
                            },
                            'success': function (file, response) {
                                $log.log('dzCallbacks success', file, response);
                                $scope.model.file = response
                            }
                        };


                        //Apply methods for dropzone
                        //Visit http://www.dropzonejs.com/#dropzone-methods for more methods
                        $scope.dzMethods = {};
                    }]
                });
        };

        $scope.sendQuality = function (data) {
            var dailog =
                ngDialog.open({
                    closeByDocument: false, // to prevent popup close by clicking outside
                    closeByEscape: false,   // to prevent popup close by ESC key
                    closeByNavigation : true, // to close popup on state navigation
                    template: 'quality.tpl.html',
                    width: '60%',
                    data: vm,
                    controller: ['$scope', '$state', '$log', function ($scope, $state, $log) {
                        $log.info(vm.profile.sub)


                        $scope.model = {};

                        $scope.model.dataset = data._id;
                        $scope.model.type = data.format;

                        $scope.level = ["OFF", "WARNING","ERROR", "INFO"]

                        $http({
                            method: 'GET',
                            url: '/rest/api/quality/config-default.json'
                        }).then(function successCallback(response) {
                            $scope.config = response.data;
                            console.log("config", response.data);
                        }, function errorCallback(response) {
                            console.log(response);
                        });

                        $scope.submitForm = function (isValid) {
                            if (isValid) {
                                var data = {
                                    'contentType': $scope.model.type,
                                    "uuid": $scope.model.dataset,
                                    'user': vm.profile.sub,
                                    'quality': angular.toJson($scope.config)
                                };

                                $log.info("createQuality", data);

                                restApi.createQuality({
                                    user: vm.profile.sub,
                                    format: $scope.model.type,
                                    dataset: $scope.model.dataset,
                                    quality: angular.toJson($scope.config)
                                }).then(function (_data) {
                                    $log.info(_data);
                                    ngDialog.close();
                                    $state.go("quality", {}, {reload: true});
                                }).catch(function (_data) {
                                    $log.info(_data);
                                });
                            }
                        };
                        $scope.update = function(k,k2,v) {
                            $scope.config[k][k2].level = v;
                        };
                    }]
                });
        };
    }
})();

