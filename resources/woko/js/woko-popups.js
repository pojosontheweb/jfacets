var form_x=0;
var form_y=0;
var theForm=null;
var input_x_num=null;
var input_y_num=null;
var elements=null;	
	
function showDebugPopup(theCount) {
    // hide all popup divs
    var elements=document.getElementsByTagName('div');
	for(i=0;i<elements.length;i++) {
		if (elements[i].className.indexOf('popup')>=0) {	
			hide(elements[i].id);
	 	}			
	}

    // show the good one
    var myDiv = document.getElementById('popup' + theCount);
	myDiv.style.display='block';		
	if(window.innerWidth) {
		max_x=window.innerWidth-(myDiv.offsetWidth)-20;
		max_y=window.innerHeight-(myDiv.offsetHeight)-20;
	} else {
		max_x=document.body.clientWidth-(myDiv.offsetWidth)-20;
		max_y=document.body.clientHeight-(myDiv.offsetHeight)-20;
	}
	
	if(form_x<max_x) myDiv.style.left=form_x+"px";
	else 
		 myDiv.style.left=max_x+"px";
	
	if(form_y<max_y) myDiv.style.top=form_y+"px";
	else 
	myDiv.style.top=max_y+"px";
}
		
function hide(theId) {
	myDiv = document.getElementById(theId);	
	myDiv.style.display='none';
}
	
function select(myDiv) {	
	deselect();
	myDiv.className='facetDebug-selected';
}
	
function deselect() {
	elements=document.getElementsByTagName('div');
	for(i=0;i<elements.length;i++) {
		if (elements[i].className.indexOf('facetDebug-selected')>=0) {	
			elements[i].className='facetDebug';
	 	}			
	}
}

function highlightFacetDebug(count) {
    // disable added style for divs that are not currently selected...
    var elements = document.getElementsByTagName('div');
	for(i=0;i<elements.length;i++) {
		if (elements[i].id == ('facetDebugDiv' + count)) {
			elements[i].style.border = '2px solid green';
	 	} else {
            elements[i].style.border = 'none;';
        }
	}
}