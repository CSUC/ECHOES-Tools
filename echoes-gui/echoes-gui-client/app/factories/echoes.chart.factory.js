(function () {
    'use strict';
    angular
        .module('app')
        .factory('echoesChart', echoesChart);

    echoesChart.$inject = ['$log'];

    function echoesChart($log) {
        var chart = {};

        var COLORS = {
            GREEN: "#2e8b57",
            RED: "#ff0000",
            BLUE: "#0000ff",
            GRAY: "#bebebe"
        }



        chart.getAggregationDoughnut = function (_data) {
            $log.info(_data);

            var labels = [];
            var data = [];
            var colors = [];

            var options = {
                legend: {
                    display: true
                },
                rotation: -Math.PI,
                cutoutPercentage: 30,
                circumference: Math.PI,
            };

            _data.forEach(function(value, index) {
                switch(value._id) {
                    case 'END':
                        colors.push(COLORS.GREEN);
                        break;
                    case 'ERROR':
                        colors.push(COLORS.RED);
                        break;
                    case 'QUEUE':
                        colors.push(COLORS.GRAY);
                        break;
                    case 'PROGRESS':
                        colors.push(COLORS.BLUE);
                        break;
                    default:
                        break;
                }
                labels.push(value._id);
                data.push(value.total);
            });

            return chart = {
                labels: labels,
                data: data,
                options: options,
                colors: colors
            };
        };

        return chart;
    }
})();