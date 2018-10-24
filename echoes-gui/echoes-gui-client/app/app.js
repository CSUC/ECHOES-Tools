(function () {

    'use strict';

    angular
        .module('app', ['auth0.auth0', 'ui.bootstrap', 'angular-jwt', 'ui.router', 'angular-uuid', 'ngTable', 'ngDialog', 'ngMessages','chart.js'])
        .config(config);

    config.$inject = [
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider',
        '$httpProvider',
        'angularAuth0Provider',
        'jwtOptionsProvider',
        'ChartJsProvider',
        'ngDialogProvider'
    ];

    function config($stateProvider,
                    $locationProvider,
                    $urlRouterProvider,
                    $httpProvider,
                    angularAuth0Provider,
                    jwtOptionsProvider,
                    ChartJsProvider,
                    ngDialogProvider
                    ) {

        $stateProvider
            .state('home', {
                url: '/',
                controller: 'HomeController',
                templateUrl: 'app/home/home.html',
                controllerAs: 'vm'
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
            .state('parser', {
                url: '/parser',
                controller: 'ParserController',
                templateUrl: 'app/parser/parser.html',
                controllerAs: 'vm',
                params: {
                    profile: null,
                    page: 1,
                    count: 10
                },
                onEnter: checkAuthentication
            })
            .state('parser-detail', {
                url: '/parser/:_id',
                controller: 'ParserControllerDetail',
                templateUrl: 'app/parser/parser.detail.html',
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
            .state('validation', {
                url: '/validation',
                controller: 'ValidationController',
                templateUrl: 'app/validation/validation.html',
                controllerAs: 'vm',
                onEnter: checkAuthentication
            });

        // Initialization for the angular-auth0 library
        angularAuth0Provider.init({
            clientID: AUTH0_CLIENT_ID,
            domain: AUTH0_DOMAIN,
            responseType: 'token id_token',
            audience: AUTH0_AUDIENCE,
            redirectUri: AUTH0_CALLBACK_URL,
            scope: 'openid profile read:messages write:messages'
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
