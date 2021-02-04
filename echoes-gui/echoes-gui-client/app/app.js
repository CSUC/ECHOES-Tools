(function () {

    'use strict';

    angular
        .module('app', ['auth0.auth0', 'ui.bootstrap', 'angular-jwt', 'ui.router', 'angular-uuid', 'ngTable', 'ngDialog', 'ngMessages','chart.js',
        'thatisuday.dropzone'])
        .config(config);

    config.$inject = [
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider',
        '$httpProvider',
        'angularAuth0Provider',
        'jwtOptionsProvider',
        'ChartJsProvider',
        'ngDialogProvider',
        'dropzoneOpsProvider'
    ];

    function config($stateProvider,
                    $locationProvider,
                    $urlRouterProvider,
                    $httpProvider,
                    angularAuth0Provider,
                    jwtOptionsProvider,
                    ChartJsProvider,
                    ngDialogProvider,
                    dropzoneOpsProvider
                    ) {

        $stateProvider
            .state('home', {
                url: '/',
                controller: 'HomeController',
                templateUrl: 'app/home/home.html',
                controllerAs: 'vm',
                onEnter: checkAuthentication
            })
            .state('profile', {
                url: '/profile',
                controller: 'ProfileController',
                templateUrl: 'app/profile/profile.html',
                controllerAs: 'vm',
                onEnter: checkAuthentication
            })
            .state('ping', {
                url: '/ping',
                controller: 'PingController',
                templateUrl: 'app/ping/ping.html',
                controllerAs: 'vm'
            })
            .state('callback', {
                url: '/callback',
                controller: 'CallbackController',
                templateUrl: 'app/callback/callback.html',
                controllerAs: 'vm',
                onEnter: checkAuthentication
            })
            .state('analyse', {
                url: '/analyse',
                controller: 'AnalyseController',
                templateUrl: 'app/analyse/analyse.html',
                controllerAs: 'vm',
                params: {
                    profile: null,
                    page: 1,
                    count: 10
                },
                onEnter: checkAuthentication
            })
            .state('analyse-detail', {
                url: '/analyse/:_id',
                controller: 'AnalyseControllerDetail',
                templateUrl: 'app/analyse/analyse.detail.html',
                controllerAs: 'vm',
                params:{
                    data: null
                },
                onEnter: checkAuthentication
            })
            .state('recollect', {
                url: '/recollect',
                controller: 'RecollectController',
                templateUrl: 'app/recollect/recollect.html',
                controllerAs: 'vm',
                params: {
                    profile: null,
                    page: 1,
                    count: 10
                },
                onEnter: checkAuthentication
            })
            .state('recollect-detail', {
                url: '/recollect/:_id',
                controller: 'RecollectControllerDetail',
                templateUrl: 'app/recollect/recollect.detail.html',
                controllerAs: 'vm',
                params:{
                    data: null
                },
                onEnter: checkAuthentication
            })
            .state('quality', {
                url: '/quality',
                controller: 'QualityController',
                templateUrl: 'app/quality/quality.html',
                controllerAs: 'vm',
                params: {
                    profile: null,
                    page: 1,
                    count: 10
                },
                onEnter: checkAuthentication
            })
            .state('quality-detail', {
                url: '/quality/:_id',
                controller: 'QualityControllerDetail',
                templateUrl: 'app/quality/quality.detail.html',
                controllerAs: 'vm',
                params: {
                    data: null
                },
                onEnter: checkAuthentication
            })
            .state('quality-error', {
                url: '/quality/:_id/error/:page?pagesize',
                controller: 'QualityControllerError',
                templateUrl: 'app/quality/quality.detail.error.html',
                controllerAs: 'vm',
                onEnter: checkAuthentication
            })
            .state('loader', {
                url: '/loader',
                controller: 'LoaderController',
                templateUrl: 'app/loader/loader.html',
                controllerAs: 'vm',
                params: {
                    profile: null,
                    page: 1,
                    count: 10
                },
                onEnter: checkAuthentication
            })
            .state('loader-detail', {
                url: '/loader/:_id',
                controller: 'LoaderControllerDetail',
                templateUrl: 'app/loader/loader.detail.html',
                controllerAs: 'vm',
                params:{
                    data: null
                },
                onEnter: checkAuthentication
            })
            .state('loader-error', {
                url: '/loader/:_id/error/:page?pagesize',
                controller: 'LoaderControllerError',
                templateUrl: 'app/loader/loader.detail.error.html',
                controllerAs: 'vm',
                onEnter: checkAuthentication
            })
            .state('404', {
                url: "/404",
                templateUrl: "404.html",
                onEnter: function($timeout, $state) {
                    $timeout(function () {
                        $state.go('home');
                    }, 5000)
                }
            });

        dropzoneOpsProvider.setOptions({
            url: '/upload',
            maxFiles: '1',
            timeout: 100000,
            maxFilesize: 1024,
            acceptedFiles: '.xml',
            addRemoveLinks: true,
            dictDefaultMessage: 'Click to add or drop XML',
            dictRemoveFile: 'Remove XML',
            dictResponseError: 'Could not upload this XML',
            autoDiscover: false,
            createImageThumbnails: false,
            previewTemplate: '<div class="uploaded-image"><span data-dz-name></span> <strong class="dz-size" data-dz-size></strong><div class="dz-error-message" data-dz-errormessage></div><div class="dz-progress"><span class="dz-upload" data-dz-uploadprogress></span></div></div>'
        });

        // Initialization for the angular-auth0 library
        angularAuth0Provider.init({
            clientID: AUTH0_CLIENT_ID,
            domain: AUTH0_DOMAIN,
            responseType: 'token id_token',
            audience: AUTH0_AUDIENCE,
            redirectUri: AUTH0_CALLBACK_URL,
            scope: 'openid profile'
        });

        jwtOptionsProvider.config({
            tokenGetter: function () {
                return localStorage.getItem('access_token');
            },
            whiteListedDomains: ['localhost']
        });

        $httpProvider.interceptors.push('jwtInterceptor');

        $urlRouterProvider.otherwise('/');

        $locationProvider.hashPrefix('');

        // Comment out the line below to run the app
        // without HTML5 mode (will use hashes in routes)
        $locationProvider.html5Mode(true);

        function checkAuthentication($transition$) {
            var $state = $transition$.router.stateService;
            var auth = $transition$.injector().get('authService');
            if (!auth.isAuthenticated()) {
                return $state.target('home');
            }
        }

        ChartJsProvider.setOptions({ colors : [ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'] });

        ngDialogProvider.setForceBodyReload(true);

    }

})();
