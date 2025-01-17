jQuery(document).ready( function($) {
	initSearch();
	if( $.fn.fsvs ) {
		var slider = $.fn.fsvs({
			speed : 1000,
			nthClasses : 4,
			mouseDragEvents : false
		});
	}

	if( $.fn.flare ) {
		var flares = $('.flare').flare();
		for( var flare in flares ) {
			//flares[flare].reset();
		}
	}

	var sectionHeight = $('#fsvs-body > .slide:eq(0)').height();
	$('#fsvs-body > .bettslide').each( function(){
		var section = $(this),
			item = $('.item', section ),
			demo = $('.demo', section ),
			itemHeight = item.outerHeight(),
			demoHeight = demo.outerHeight();
		item.css({
			marginTop : ( ( sectionHeight - itemHeight ) / 2 ) + 'px'
		});
		demo.css({
			marginTop : ( ( sectionHeight - demoHeight ) / 2 ) + 'px'
		});

	});

	$(window).load(function(){

	});
	
	var SliderModule = (function() {
		var pb = {};
		pb.el = $('#slider');
		pb.items = {
			panel: pb.el.find('li')
		}

		// Variables Necesarias
		var SliderInterval,
			currentSlider = 0,
			nextSlider = 1,
			lengthSlider = pb.items.panel.length;

		// Initialize
		pb.init = function(settings) {
			this.settings = settings || {duration: 8000} 
			var output = '';

			// Activamos nuestro slider
			SliderInit();

			for(var i = 0; i < lengthSlider; i++) {
				if (i == 0) {
					output += '<li class="active"></li>'; 
				} else {
					output += '<li></li>';
				}
			}

			// Controles del Slider
			$('#slider-controls').html(output).on('click', 'li', function (e){
				var $this = $(this);
				if (currentSlider !== $this.index()) {
					changePanel($this.index());
				};
			});
		}

		var SliderInit = function() {
			SliderInterval = setInterval(pb.startSlider, pb.settings.duration);
		}

		pb.startSlider = function() {
			var panels = pb.items.panel,
				controls = $('#slider-controls li');

			if (nextSlider >= lengthSlider) {
				nextSlider = 0;
				currentSlider = lengthSlider-1;
			}

			// Efectos
			controls.removeClass('active').eq(nextSlider).addClass('active');
			panels.eq(currentSlider).fadeOut('slow');
			panels.eq(nextSlider).fadeIn('slow');

			// Actualizamos nuestros datos
			currentSlider = nextSlider;
			nextSlider += 1; 
		}

		// Funcion para controles del slider
		var changePanel = function(id) {
			clearInterval(SliderInterval);
			var panels = pb.items.panel,
				controls = $('#slider-controls li');

			// Comprobamos el ID
			if (id >= lengthSlider) {
				id = 0;
			} else if (id < 0) {
				id = lengthSlider-1;
			}

			// Efectos
			controls.removeClass('active').eq(id).addClass('active');
			panels.eq(currentSlider).fadeOut('slow');
			panels.eq(id).fadeIn('slow');

			// Actualizamos nuestros datos
			currentSlider = id;
			nextSlider = id+1;

			// Reactivamos el slider
			SliderInit();
		}


		return pb;
	}());
	SliderModule.init({duration: 4000});

});