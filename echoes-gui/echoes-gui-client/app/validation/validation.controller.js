(function () {

  'use strict';

  angular
    .module('app')
    .controller('ValidationController', validation);

  validation.$inject = ['$scope', 'authService', 'uuid'];

  function validation($scope, authService, uuid) {

    var vm = this;
    vm.title = 'Validation-Core';
    vm.auth = authService;
    vm.profile;

    console.log(random(10, uuid));

    if (authService.getCachedProfile()) {
      vm.profile = authService.getCachedProfile();
    } else {
      authService.getProfile(function(err, profile) {
        vm.profile = profile;
        $scope.$apply();
      });
    }

  }


  function random(value, uuid){
    var result = [];
    for (var i = 0; i < value +1 ; i++) { 
       var data = { 'uuid' : uuid.v4() , 'status': randomStatus()};
       result.push(data);
    }
    return result;
  }


  function randomStatus(){
    var status = ['QUEUE', 'END', 'ERROR', 'END', 'PROGRESS'];    

    return status[Math.floor(Math.random() * status.length)]; 
  }

})();