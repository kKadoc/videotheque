'use strict';

angular.module('videothequeApp')
    .directive('droppable', function() {
    return {
        restrict: 'A',
        link: function(scope, element) {
            var el = element[0];
            
            el.addEventListener(
        	    'dragover',
        	    function(e) {
        	        e.dataTransfer.dropEffect = 'move';
        	        // allows us to drop
        	        
        	        e.preventDefault()
        	        this.classList.add('drop-over');
        	        return false;
        	    },
        	    false
        	);
            
            el.addEventListener(
        	    'dragenter',
        	    function(e) {
        	        this.classList.add('drop-over');
        	        return false;
        	    },
        	    false
        	);

        	el.addEventListener(
        	    'dragleave',
        	    function(e) {
        	        this.classList.remove('drop-over');
        	        return false;
        	    },
        	    false
        	);
        	
        	el.addEventListener(
    		    'drop',
    		    function(e) {
    		        // Stops some browsers from redirecting.
    		        e.stopPropagation();
    		        e.preventDefault()

    		        this.classList.remove('drop-over');

    		        scope.dropFiles(e.dataTransfer.files);

    		        return false;
    		    },
    		    false
    		);
        }
    }
});