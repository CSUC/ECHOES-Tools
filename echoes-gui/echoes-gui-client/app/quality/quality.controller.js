(function () {

  'use strict';

  angular
    .module('app')
    .controller('QualityController', quality);

  quality.$inject = ['$scope', 'authService', 'uuid', 'NgTableParams', '$http', '$log', '$stateParams', '$interval',
    'echoesChart', 'restApi', 'ngDialog', '$state', '$timeout'];

  function quality($scope, authService, uuid, NgTableParams, $http, $log, $stateParams, $interval,
                   echoesChart, restApi, ngDialog, $state) {

    var vm = this;
    vm.title = 'Quality assurance';
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

      vm.selectedClass = function (status) {
        if (status === 'END') return 'alert alert-success';
        if (status === 'ERROR') return 'alert alert-danger';
        return 'alert';
      }

      restApi.getQuality({
        user: vm.profile.sub,
        pagesize: _count,
        page: _page
      }).then(function (_data) {
        $log.info(_data.data);

        vm.data = _data.data;
        if(_data.data._size > 0)
          vm.tableParams = ngTableParams(vm.data, _count)
      }).catch(function (_data) {
        $log.info(_data);
        //$state.go("404");
      });

      restApi.getQualityStatusAggregation({
        user: vm.profile.sub
      }).then(function (_data) {
        vm.chart = echoesChart.getAggregationDoughnut(_data.data)
      }).catch(function (_data) {
        console.log(_data)
        // $state.go("404");
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

          return restApi.getQuality({
            user: vm.profile.sub,
            pagesize: vm.count,
            page: vm.page
          }).then(function (d) {
            return d.data._embedded;
          }).catch(function (_data) {
            $log.info(_data);
            // $state.go("404");
          });
        }
      });
    }

    $interval(function () {
      run(vm.page, vm.count)
    }, 10000);

    // $scope.remove = function (id) {
    //   restApi.deleteQuality({
    //     user: vm.profile.sub,
    //     id: id
    //   }).then(function (_data) {
    //     $log.info(_data);
    //
    //     $state.go($state.current, {}, {reload: true});
    //   }).catch(function (_data) {
    //     $log.info(_data);
    //     // $state.go("404");
    //   });
    // }

    $scope.sendLoader = function (data) {
      var dailog =
          ngDialog.open({
            template: 'loader.tpl.html',
            width: '60%',
            data: vm,
            controller: ['$scope', '$state', '$log', function ($scope, $state, $log) {
              $log.info(vm.profile.sub)

              $scope.options = {
                endpoints: [
                  "http://blazegraph.test.csuc.cat/namespace/test/sparql"
                ],
                types: ["RDFXML","NTRIPLES","TURTLE","JSONLD","RDFJSON","NQ","NQUADS","TRIG","RDFTHRIFT","TRIX"]
              }

              $scope.model = {};

              $scope.model.dataset = data._id;
              $scope.model.type = data.contentType;

              $scope.submitForm = function (isValid) {
                if (isValid) {
                  var data = {
                    'sparqlEndpoint': $scope.model.endpoint,
                    'contentType': $scope.model.type,
                    'contextUri' : $scope.model.context,
                    "uuid": $scope.model.dataset,
                    'user': vm.profile.sub
                  };

                  $log.info(data);

                  restApi.createLoader({
                    user: vm.profile.sub,
                    endpoint: $scope.model.endpoint,
                    contentType: $scope.model.type,
                    contextUri : $scope.model.context,
                    uuid: $scope.model.dataset,
                  }).then(function (_data) {
                    $log.info(_data);
                    ngDialog.close();
                    $state.go("loader", {}, {reload: true});
                  }).catch(function (_data) {
                    $log.info(_data);
                    //$state.go("404");
                  });

                }
              };
            }]
          });
    };

  }

})();