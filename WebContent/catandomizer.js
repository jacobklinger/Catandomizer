var Catandomizer = {} || Catandomizer;

Catandomizer.tiles = document.getElementsByTagName("polygon");

Catandomizer.randomize = function() {
	var http = new XMLHttpRequest();
	
	http.onreadystatechange = function() {
		if (http.readyState == 4 && http.status == 200) {
			
			var response = JSON.parse(http.responseText);
			console.log(response);
			for (i = 0; i < Catandomizer.tiles.length; i++) {
				Catandomizer.tiles[i].setAttribute("terrain", response.tiles[i]);
		    }
		}
	}
	
	if (document.getElementById("desert-edge").checked) {
		if (document.getElementById("limit-clusters").checked) {
			http.open("GET", "/CatandomizerServlet?desert=true&cluster=true", true);
		}
		else {
			http.open("GET", "/CatandomizerServlet?desert=true", true);
		}
	}
	else {
		if (document.getElementById("limit-clusters").checked) {
			http.open("GET", "/CatandomizerServlet?cluster=true", true);
		}
		else {
			http.open("GET", "/CatandomizerServlet", true);
		}
	}
	
	http.send();
}

Catandomizer.useImages = function() {
	for (i = 0 ; i < Catandomizer.tiles.length; i++) {
		if (document.getElementById("image-option").checked) {
			Catandomizer.tiles[i].setAttribute("image", "true");
		}
		else {
			Catandomizer.tiles[i].removeAttribute("image", "true");
		}
	}
}

window.onresize = function() {
	var svg = document.getElementsByTagName("svg")[0];
	var parentHeight = svg.parentNode.clientHeight
	var paddingTop = parseInt(window.getComputedStyle(svg.parentNode).getPropertyValue("padding-top")) * 2;
	svg.setAttribute("height", parentHeight-paddingTop);
}

window.onload = function() {
	document.getElementById("image-option").addEventListener("change", Catandomizer.useImages);
}