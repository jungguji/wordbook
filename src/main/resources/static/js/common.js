/**
 * 
 */

function clearliTagClass() {
	const liTagList = document.querySelectorAll("li");
	
	for (li of liTagList) {
		li.removeAttribute("class");
	}
}

function addClassliTag(index) {
	document.querySelectorAll("li")[index].setAttribute("class", "active");
}