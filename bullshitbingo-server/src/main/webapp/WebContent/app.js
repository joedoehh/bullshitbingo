'use strict';

angular.module('bullshitBingoApp', ['ngRoute', 'ngMessages'])

  .config(function($routeProvider) {
    $routeProvider
      .when('/', { templateUrl: 'game.html' })
      .when('/recorder', { templateUrl: 'recorder.html' })
      .otherwise({ redirectTo: '/'});
  })
    
  
  .controller('GameCtrl', function($http, $scope, $timeout){
	  
	  // var GAME_REST_SERVICE_BASE_URL = 'http://localhost:9080/bullshitbingo-server/bsb/game';	  
	  var GAME_REST_SERVICE_BASE_URL = 'http://bsb.eu-gb.mybluemix.net/bullshitbingo-server/bsb/game';
	  var gameController = this;
	  	  
	  gameController.gameState = [];
	  gameController.serverError = 1;
	  gameController.userName;
	  gameController.words;
	  gameController.lastUpdateTimestamp;
	  gameController.automaticReloadFlag;
	  
	  gameController.automaticReload = function () {
		  if (gameController.automaticReloadFlag == true) {
			  $timeout(gameController.reload(), 5000);			  
		  } else {
			  $timeout.cancel();
		  }		  	
	  };
	  
	  gameController.reload = function () {	    
	    	$http.get(GAME_REST_SERVICE_BASE_URL)
	    	.success(
	    			function(data) {
	    				gameController.gameState = data;
	    				gameController.serverError = 0;
	    				gameController.lastUpdateTimestamp = new Date();	    				
	    			})
	    	.error(
	    			function(data, status) {
	    				gameController.gameState = [];
	    				gameController.serverError = 1;
	    			});
	    	 };
	    			
	    			
	  gameController.join = function () {	
		  $http.post(GAME_REST_SERVICE_BASE_URL+"?player="+gameController.userName+"&wordsCommaSeperated="+gameController.words)
		  .success(
				  function(data) {
					  gameController.reload();
				  })
	      .error(
	    		  function(data, status) {
	    			  gameController.gameState = [];
	    			  gameController.serverError = 1;
	    		  })};
	    		  
	   gameController.changeState = function (newState) {	
		   $http.put(GAME_REST_SERVICE_BASE_URL+"/"+newState)
	    	 .success(
	    		  function(data) {
	    			  gameController.reload();
	    		  })
	    	 .error(
	    	 	  function(data, status) {
	    	 		  gameController.gameState = [];
	    		   	  gameController.serverError = 1;
	    		  })};	 
	    		 	    		  
	   gameController.fieldIsInvalid = function (field) {
		   return $scope.gameForm[field].$touched
		   			&& $scope.gameForm[field].$invalid;};
		   			
	  gameController.reload();			
	  	 
  })  
  
  .controller('RecordingsCtrl', function($http){

	  // var RECORDINGS_REST_SERVICE_BASE_URL = 'http://localhost:9080/bullshitbingo-server/bsb/recording';
	  var RECORDINGS_REST_SERVICE_BASE_URL = 'http://bsb.eu-gb.mybluemix.net/bullshitbingo-server/bsb/recording';
	  var recordingsController = this;
	  
	  recordingsController.recordings = [];
	  recordingsController.serverError = 1;
	  recordingsController.newRecording = "";
	  
	  recordingsController.deleteAll = function () {
		  $http.delete(RECORDINGS_REST_SERVICE_BASE_URL)
		  .success(
				  function(data) {
					  recordingsController.reload();
				  })
		  .error(
				  function(data, status) {
					  recordingsController.recordings = [];
					  recordingsController.serverError = 1;
		    })};	  

	  recordingsController.addRecording = function () {
		  if (recordingsController.newRecording == "") {
			  return;
		  }
		  var words = recordingsController.newRecording.split(/\b\s+/);
		  var wordsQueryParam = "";
		  for (var i = 0; i < words.length; i++) { 
			  wordsQueryParam += "word="+encodeURIComponent(words[i]);
			  if (i < words.length-1) {
				  wordsQueryParam += "&";
			  }  
		  }		  
		  $http.put(RECORDINGS_REST_SERVICE_BASE_URL+"?"+wordsQueryParam)
		  .success(
				  function(data) {
					  recordingsController.reload();
				  })
		  .error(
				  function(data, status) {
					  recordingsController.recordings = [];
					  recordingsController.serverError = 1;
		    })};
	  
	  recordingsController.deleteRecording = function (recording) {
		  $http.delete(RECORDINGS_REST_SERVICE_BASE_URL+"\\"+recording.id)
		  .success(
				  function(data) {
					  recordingsController.reload();
				  })
		  .error(
				  function(data, status) {
					  recordingsController.recordings = [];
					  recordingsController.serverError = 1;
		    })};
	  
	    recordingsController.reload = function (recording) {	    
	    	$http.get(RECORDINGS_REST_SERVICE_BASE_URL)
	    	.success(
	    			function(data) {
	    				recordingsController.recordings = data;
	    				recordingsController.serverError = 0;
	    			})
	    	.error(
	    			function(data, status) {
	    				recordingsController.recordings = [];
	    				recordingsController.serverError = 1;
	    			})};
	    
	    recordingsController.reload();			
	  	 
  });


  