var myModule = angular.module("moduleName", []);

myModule.controller("moduleController", ['$scope',
    function($scope) {
        $scope.greeting = {
            text: 'Hello'
        };
    }
]);