$(document).ready(function(){
    $('.menu').on('click',function(e){
        if(event.target.tagName == 'A'){
            $('.menu .clicked').removeClass('clicked');

            var screenId = event.target.classList[0];
            moveScreen(screenId);
            event.target.classList.add('clicked');
        }
    });
});