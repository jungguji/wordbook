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

const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

$(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
});