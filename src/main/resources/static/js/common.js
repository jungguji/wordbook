/**
 * 
 */

function clearliTagClass() {
	const liTagList = document.querySelectorAll("li.nav-item");
	
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

const empty = (value) => { 
    if (value === null) return true 
    if (typeof value === 'undefined') return true 
    if (typeof value === 'string' && value === '') return true 
    if (Array.isArray(value) && value.length < 1) return true 
    if (typeof value === 'object' && value.constructor.name === 'Object' && Object.keys(value).length < 1 && Object.getOwnPropertyNames(value) < 1) return true 
    if (typeof value === 'object' && value.constructor.name === 'String' && Object.keys(value).length < 1) return true // new String() return false }
};