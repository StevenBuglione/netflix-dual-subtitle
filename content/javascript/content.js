import Settings from './settings';

const settings = new Settings();

//Solution for note 2 above, This is not a foolproof way to detect which browser the user is on, but it works well enough for now

if (window.navigator.userAgent.includes('Edg/')){
    console.log("EDGE VERSION");
    settings.setEdge(1)
}
else{
    console.log("CHROME VERSION");
    settings.setEdge(0)
}

//

try{
    getSetting('button_up_down_mode'); //I don't like putting this here but need to for now, the storage retrieval happens takes too long to affect the first subs
    getSetting('on_off');
    //Or maybe it isn't so bad to pull local preferences on every script load rather than waiting for the llsubs trigger? haven't decided yet.
}
catch(e){
    // console.log("Error retrieving Stacked Subs preference, setting to default");
    settings.setUpDownMode(1)
}


function waitForElement(selector) {
    return new Promise(function(resolve, reject) {
      var element = document.querySelector(selector);

      if(element) {
        resolve(element);
        return;
      }

      var observer = new MutationObserver(function(mutations) {

        mutations.forEach(function(mutation){
        var nodes = Array.from(mutation.addedNodes);
        for(var node of nodes) {

            if(node.matches && node.matches(selector)) {

              observer.disconnect();
              resolve(node);
              return;

            }

          };
        });

      });
  
      observer.observe(document.documentElement, { childList: true, subtree: true });

    });
}

function getSetting(setting){ //Pulling User Preferences from Chrome Storage

    chrome.storage.sync.get(setting,function(data){

        if (setting === "on_off"){
            settings.setOnOff(data[setting])
        }

        else if (setting === "button_on_off"){
            settings.setButtonOnOff(data[setting])
        }
        else if (setting ==="button_up_down_mode"){
            settings.setUpDownMode(data[setting])
        }

        else if (setting === "font_multiplier"){
            settings.setCurrentMultiplier(parseFloat(data[setting]))
        }

        /*else if (setting === "sub_distance"){ Don't feel this is necessary anymore
            window.sub_distance= data[setting];
           // console.log("Retrieved Sub Distance From Storage: ",window.sub_distance);
        }*/

        else if (setting === "text_color"){
            settings.setTextColor(data[setting])
        }

        else if (setting === "opacity"){
            settings.setOpacity(data[setting])
        }
        else if (setting === "originaltext_opacity"){
            settings.setOriginalTextOpacity(data[setting])
        }
        else if (setting === "originaltext_color"){
            settings.setOriginalTextColor(data[setting])
        }
        else{
            console.log("No setting:",setting);
        }

    });
}

function wait_for_player_to_finish_loading(){
    //Waiting for flex elements doesn't work, so we have to use the full xpath instead
    waitForElement("#appMountPoint > div > div >div > div > div > div:nth-child(1) > div > div > div > div").then(function(element) {
        
        try{
            actual_create_buttons();}
        catch(e){
           // console.log("Error creating buttons, likely no bar visible");
        }

        //Once the player is found, we pull color settings in anticipation for subtitles 
        getSetting('text_color');
        getSetting('opacity');
        getSetting('originaltext_opacity');
        getSetting('font_multiplier');
        //getSetting('sub_distance'); disabled for now, unnecessary imo
        //getSetting('text_side'); 

        settings.setOriginalTextSide(0)
        initialize_button_observer();
        llsubs();
    
    });
}

//Button Stuff
//Need an observer to wait until the control button row element is created, then the buttons are added, and THEN we can wait for subtitles
//This observer was made "quick and dirty", worked the first try so I'll just leave it and worry about more efficient approach later 
settings.setVideoChangeObserverConfig({childList:true, subtree:true,})
var last_url=location.href;

var callback = function(mutationsList, observer){ //The main observer to check for video changes

    //console.log("Debug - Waiting for Video");
    for (const mutation of mutationsList){

        try {var current_id = location.href.split('/watch/')[1].split('?')[0];}catch(e){var current_id=0;}

        // New way to determine video changes, way more efficient
        // To be fair though, this wouldn't have worked before the netflix interface update as the observers would have persisted and caused endless instances to be created  
        if (mutation.type === 'childList' && (mutation.target.className===" ltr-1b8gkd7-videoCanvasCss" || mutation.target.className== " ltr-op8orf" || mutation.target.className==" ltr-1212o1j") && mutation.addedNodes.length){
            //console.log("New Video!");
            if(mutation.target.className===" ltr-1b8gkd7-videoCanvasCss"){
                settings.setWeirdClassnameMode(1)
            }
            prepare_for_dual_subs();
        }
        if (mutation.target.parentNode && (mutation.target.parentNode.className=== " ltr-1b8gkd7-videoCanvasCss"|| mutation.target.className== " ltr-op8orf" || mutation.target.className==" ltr-1212o1j")){
            if (mutation.previousSibling && mutation.addedNodes[0].id != mutation.previousSibling.id){
                //console.log("Video Change");
                if(mutation.target.parentNode.className===" ltr-1b8gkd7-videoCanvasCss"){
                    settings.setWeirdClassnameMode(1)
                }
                prepare_for_dual_subs();
            }
        }
        if (mutation.addedNodes.length==1 && mutation.previousSibling){ //9/3/22 - bug fix, observer wasnt being renewed on autoplay
            if(parseInt(mutation.addedNodes[0].id),parseInt(mutation.previousSibling.id)){
                if(parseInt(current_id)!=parseInt(mutation.previousSibling.id)){
                    
                    if(mutation.target.parentNode.className===" ltr-1b8gkd7-videoCanvasCss"){
                        settings.setWeirdClassnameMode(1)
                    }
                    prepare_for_dual_subs();
                }
                
            }
            
        }
        
    }
}
settings.setVideoChangeObserver(new MutationObserver(callback))
settings.getVideoChangeObserver().observe(document.documentElement,settings.getVideoChangeObserverConfig())
function prepare_for_dual_subs(){ //Starts the observer that waits for the video player to finish loading after a page/video change
        //Enables right click
        var elements = document.getElementsByTagName("*");
        for(var id = 0; id < elements.length; ++id) { elements[id].addEventListener('contextmenu',function(e){e.stopPropagation()},true);elements[id].oncontextmenu = null; }
        
        // getSetting('button_up_down_mode');
        getSetting('originaltext_color');
        getSetting('button_on_off');
        
        //Use to be able to create buttons before bottom bar was visible, can't anymore so button creation
        //is moved to after player is detected now

        wait_for_player_to_finish_loading();


}

function actual_create_buttons(){
   // console.log("Creating buttons..");

    if (!settings.getOnOff() || !settings.getButtonOnOff()){ //Buttons disabled for now so I can get working subs out as fast as possible
        return;
    }
    if (document.getElementById('myTutorialButton')){
       // console.log("Buttons alreayd made");
        return;
    }

    let buttonSpacing = document.createElement('DIV');
    buttonSpacing.innerHTML='<div class="ltr-14rufaj" style="min-width: 3rem; width: 3rem;"></div>';
    buttonSpacing=buttonSpacing.firstElementChild;
    try{
    document.querySelector('button[aria-label="Seek Back"]').parentElement.parentElement.appendChild(buttonSpacing);
    }
    catch(e){
       // console.log("No bar 1");
        return;
    }

    // '<div class="medium ltr-my293h" id="myTutorialButton"><button aria-label="Decrease Font Size" class=" ltr-1enhvti" data-uia="control-fontsize-minus"><div class="control-medium ltr-18dhnor" role="presentation"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="Hawkins-Icon Hawkins-Icon-Standard">\
    // <path clip-rule="evenodd" \
    // d="M9.405 1.05c-.413-1.4-2.397-1.4-2.81 0l-.1.34a1.464 1.464 0 0 1-2.105.872l-.31-.17c-1.283-.698-2.686.705-1.987 1.987l.169.311c.446.82.023 1.841-.872 2.105l-.34.1c-1.4.413-1.4 2.397 0 2.81l.34.1a1.464 1.464 0 0 1 .872 2.105l-.17.31c-.698 1.283.705 2.686 1.987 1.987l.311-.169a1.464 1.464 0 0 1 2.105.872l.1.34c.413 1.4 2.397 1.4 2.81 0l.1-.34a1.464 1.464 0 0 1 2.105-.872l.31.17c1.283.698 2.686-.705 1.987-1.987l-.169-.311a1.464 1.464 0 0 1 .872-2.105l.34-.1c1.4-.413 1.4-2.397 0-2.81l-.34-.1a1.464 1.464 0 0 1-.872-2.105l.17-.31c.698-1.283-.705-2.686-1.987-1.987l-.311.169a1.464 1.464 0 0 1-2.105-.872l-.1-.34zM8 10.93a2.929 2.929 0 1 1 0-5.86 2.929 2.929 0 0 1 0 5.858z" \
    // fill="none" stroke="yellow" stroke-width="2"></path></svg></div></button></div>'; 

    let buttonOne = document.createElement('DIV');
    buttonOne.innerHTML ='<div class="medium ltr-my293h" id="myTutorialButton"><button aria-label="Open Tutorial" class=" ltr-14ph5iy" data-uia="control-fontsize-minus"><div class="control-medium ltr-1evcx25" role="presentation"><svg width="24" height="24" viewBox="-1 0 24 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg" class="Hawkins-Icon Hawkins-Icon-Standard"><path fill-rule="evenodd" clip-rule="evenodd" d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z M5.255 5.786a.237.237 0 0 0 .241.247h.825c.138 0 .248-.113.266-.25.09-.656.54-1.134 1.342-1.134.686 0 1.314.343 1.314 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.003.217a.25.25 0 0 0 .25.246h.811a.25.25 0 0 0 .25-.25v-.105c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.267 0-2.655.59-2.75 2.286zm1.557 5.763c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z" fill="none" stroke="yellow" stroke-width=".7"></path></svg></div></button></div>'; 
    if (settings.getWeirdClassnameMode()){
        buttonOne.innerHTML ='<div class="medium ltr-my293h" id="myTutorialButton"><button aria-label="Open Tutorial" class=" ltr-14ph5iy" data-uia="control-fontsize-minus"><div class="control-medium ltr-1evcx25" role="presentation"><svg width="24" height="24" viewBox="-1 0 24 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg" class="Hawkins-Icon Hawkins-Icon-Standard"><path fill-rule="evenodd" clip-rule="evenodd" d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z M5.255 5.786a.237.237 0 0 0 .241.247h.825c.138 0 .248-.113.266-.25.09-.656.54-1.134 1.342-1.134.686 0 1.314.343 1.314 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.003.217a.25.25 0 0 0 .25.246h.811a.25.25 0 0 0 .25-.25v-.105c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.267 0-2.655.59-2.75 2.286zm1.557 5.763c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z" fill="none" stroke="yellow" stroke-width=".7"></path></svg></div></button></div>'; 

    }
    buttonOne=buttonOne.firstElementChild;

    try{
    document.querySelector('button[aria-label="Seek Back"]').parentElement.parentElement.appendChild(buttonOne);
    }
    catch(e){
       // console.log("No bar 2");
        return;
    }
    buttonOne.onmouseenter=function(){
        if (settings.getWeirdClassnameMode()){
            buttonOne.firstChild.className='active ltr-14ph5iy-controlButtonCss';
        }
        else{
            buttonOne.firstChild.className='active ltr-14ph5iy';
        }
    }
    buttonOne.onmouseleave=function(){
        if (settings.getWeirdClassnameMode()){
        buttonOne.firstChild.className=' ltr-14ph5iy-controlButtonCss';
    }
    else{
        buttonOne.firstChild.className=' ltr-14ph5iy';
    }
    }

    buttonSpacing = document.createElement('DIV');
    buttonSpacing.innerHTML='<div class="ltr-14rufaj" style="min-width: 3rem; width: 3rem;"></div>';
    buttonSpacing=buttonSpacing.firstElementChild;

    try{
        document.querySelector('button[aria-label="Seek Back"]').parentElement.parentElement.appendChild(buttonSpacing);
    }
    catch(e){
       // console.log("No bar 3");
        return;
    }

    if (settings.getOriginalTextColor()){
    document.getElementById('myTutorialButton').firstChild.firstChild.firstChild.firstElementChild.setAttribute('stroke',settings.getOriginalTextColor());
    }

    buttonOne.addEventListener("click", function() {

        open_browser_action();

    });

    //bump up text
   

        
}
function open_browser_action(){

    chrome.runtime.sendMessage({
        "message":"open_popup",
        "value": this.checked
        });

}
function initialize_button_observer(){ 
    //This keeps track of the bottom playback bar, 
    //it has to create buttons every time it appears since the element is destroyed rather than hidden

    var id = "watch-video--player-view";
    const bottom_bar = document.getElementsByClassName(id)[0];

    settings.setButtonConfig({subtree:true,childList:false,attributes:true,attributeFilter:["class"]});

    const callback = function(mutationsList,button_observer){
        for (const mutation of mutationsList){
           
             if (mutation.target.className==='active ltr-omkt8s' || mutation.target.className==='active ltr-gwjau2-playerCss'){
                 //console.log("Bottom bar visible");
                if (mutation.target.className==='active ltr-gwjau2-playerCss'){
                    settings.setNetflixMode(2)
                }
                actual_create_buttons();
             }
           
        }
       
    };

    settings.setButtonObserver(new MutationObserver(callback));
    settings.getButtonObserver().observe(bottom_bar,settings.getButtonConfig());

}
function llsubs(){
    //console.log("Starting llsubs");
    // Enabled Right Click
    var elements = document.getElementsByTagName("*");
    for(var id = 0; id < elements.length; ++id) { elements[id].addEventListener('contextmenu',function(e){e.stopPropagation()},true);elements[id].oncontextmenu = null; }

    //Pull Original Sub Container
    var id = "player-timedtext";
    const timedtext = document.getElementsByClassName(id)[0]; //Original Container

    //My container creation my-timedtext-container

    $(".my-timedtext-container").remove(); // should actually do this after video exit rather than before video start since it will fix the text lingering a bit on exit
    try{ //for lingering injected css
        document.querySelector('.injected-style').remove();
    }
    catch(e){
        // console.log("No injected css");
    }


    if (settings.getUpDownMode()){ //Stacked Subtitles

        // console.log("Up Down Mode");
        //Pointer-events: none added to prevent bigger sized text from stopping you interacting with the seekbar
        $(".watch-video").append(`<div class='my-timedtext-container' style='pointer-events: none; display: block; white-space: nowrap; max-width:100%; text-align: center; position: absolute; left: 50%; bottom: 22%;-webkit-transform: translateX(-50%); transform: translateX(-50%); font-size:21px;line-height:normal;font-weight:normal;color:#ffffff;text-shadow:#000000 0px 0px 7px;font-family:Netflix Sans,Helvetica Nueue,Helvetica,Arial,sans-serif;font-weight:bolder'><span id=my_subs_innertext></span></div>`)
        
        
        if(settings.getOnOff()){
        let st = document.createElement('style'); 
        
        st.innerText='.player-timedtext br{content: "";}' +
        '.my-timedtext-container br{content: "";}' +
        '.player-timedtext br:after{content: " ";}' +
        '.my-timedtext-container br:after{content: " ";}';

        st.className='injected-style';
        document.head.appendChild(st);
        }
        else{
            // console.log("DIDNT INJECT");
        }

        //uhh I didn't realize I could just inject css like this lmao.. for now using it for vertical text feature but will try to apply this to everything else later for cleaner code
        //it hides <br>'s to keep things on one line

    } else{

        // console.log("Left Right Mode");
        $(".watch-video").append(`<div class='my-timedtext-container' style='display: block; white-space: pre-wrap; text-align: center; position: absolute; left: 2.5%; bottom: 18%; font-size:21px;line-height:normal;font-weight:normal;color:#ffffff;text-shadow:#000000 0px 0px 7px;font-family:Netflix Sans,Helvetica Nueue,Helvetica,Arial,sans-serif;font-weight:bolder'><span id=my_subs_innertext></span></div>`)
    
    }
    settings.setCounter(1)
    //Create an observer to track when a translation happens. Need for dealing with text going offscreen
    const translation_tracker_callback = function(mutationsList,observer){
        for (const mutation of mutationsList){
            if(mutation.target.className==='my-timedtext-container' && mutation.type==='attributes' && mutation.attributeName==='_msttexthash'){
            //edge
            settings.setCounter(settings.getCounter() + 1);
            let lines = document.querySelector('.my-timedtext-container'); 
            
            let temp_size=parseFloat(lines.style['font-size'].replace('px',''));
            while(lines.offsetWidth > lines.parentNode.clientWidth-50 && temp_size > 8){
                temp_size-=2;
                lines.style['font-size']=temp_size+'px';
                // console.log("Changing from: "+window.current_size+ ' To: '+temp_size+'px');
    
            }

            }
            else if(mutation.target.className==='my-timedtext-container' && mutation.addedNodes.length==1 && mutation.addedNodes[0].nodeName==='FONT'){ //Chrome
                settings.setCounter(settings.getCounter() + 1);
                let lines = document.querySelector('.my-timedtext-container'); 
                let temp_size=parseFloat(lines.style['font-size'].replace('px',''));
                while(lines.offsetWidth > lines.parentNode.clientWidth-50 && temp_size > 8){
                    temp_size-=2;
                    lines.style['font-size']=temp_size+'px';
                    // console.log("Changing from: "+window.current_size+ ' To: '+temp_size+'px');
        
                }

            }
            // console.log(mutation);
        }
    }
    settings.setTranslationTrackerConfig({ attributes: true, childList: true, subtree:true})
    settings.setMyTimedTextElement(document.getElementsByClassName('my-timedtext-container')[0])
    settings.getMyTimedTextElement().setAttribute('translate','yes');
    settings.setLastSubs('')
    
    //For Placement
    settings.setOldInset(timedtext.style.inset)
    settings.setOriginalSubsPlacement(parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().width)*.025)
    settings.setCleared(1)
    settings.setConfig({ attributes: true, childList: true, subtree:true,attributeFilter: [ "style"]})

    settings.setOldText("")

    const callback = function(mutationsList, observer){ //Observes original text box for changes
        for (const mutation of mutationsList){
            // console.log(mutation);
            if (mutation.type === 'childList' && mutation.target.className && mutation.target.className==="player-timedtext"){ //track removal/addition to subtitle container
                
                if (mutation.addedNodes.length===1){ //If added rather than removed..

                    if (mutation.target.innerText!==settings.getOldText()){
                        //I added this functionality last but it's much better than the clear flag, eventually i'll make this the only way to trigger a sub update,
                        //but for now I'll just make it support the current clear flag functionality

                        settings.setOldText(mutation.target.innerText)
                        settings.setCleared(1)
                        //console.log("Sub changed detected");

                    }

                    this.disconnect(); //stop observer so I can add subs without triggering this infinitely
                    addSubs(timedtext); //add subs
                    
                }
                else{
                
                    if (mutation.target.childElementCount===0){ //No children means the mutation was a subtitle CLEAR rather than refresh, double check necessary because refresh would make it here as well but with children (..i think? I forget at this point)
                        
                        settings.setCleared(1)
                        document.getElementsByClassName('my-timedtext-container')[0].innerText = "";
                        settings.setLastSubs("")

                    }
                    
                }
                
                
            }
            
            else if(settings.getOnOff() && mutation.type==='attributes' && mutation.target.className==="player-timedtext" && mutation.target.firstChild && mutation.target.style.inset != settings.getOldInset()){ //For adjusting subtitle style when window is resized
                    //Netflix constantly refreshes the text so I have to constantly reapply them
                    try{ //Reapplying edge translator skip just in case. this thing is stubborn
                        Array.from(document.querySelector('.player-timedtext').children).forEach(e=>e.setAttribute('_istranslated','1')); //MIGHT WORK FOR DUAL COMPATIBILITY!!!! (spoofs edge translator to skip since translate tag doesnt work)
                        }
                        catch(e){
                            console.log("No subs");
                        }

                    const caption_row = document.getElementsByClassName("player-timedtext")[0];
                    var container_count = caption_row.childElementCount;
                    if (container_count == 2){ // Why work around Netflix sometimes using a seperate container for each row when I can just force it back into using one.. wish I'd done this earlier
                        document.getElementsByClassName('player-timedtext-text-container')[0].firstChild.innerText= document.getElementsByClassName('player-timedtext-text-container')[0].firstChild.innerText + '\n '+ document.getElementsByClassName("player-timedtext-text-container")[1].firstChild.innerText;
                        $('.player-timedtext-text-container')[1].remove();    
                        container_count=0;
                    }


                    settings.setBaseFont(parseFloat(mutation.target.firstChild.firstChild.firstChild.style.fontSize.replace('px',''))); //font size changes way more often than on nrk so will take basefont after every clear instead (if inset updates, update this as well)
                    settings.setCurrentSize(settings.getBaseFont() * settings.getCurrentMultiplier()+'px');
                    update_style('font_size');
                    //update_style('originaltext_opacity');
                    //update_style('originaltext_color');

                    if (settings.getUpDownMode()){
                        // window.my_timedtext_element.style['left']='2.5%';
                        settings.getMyTimedTextElement().left='50%';
                        settings.getMyTimedTextElement().transform='translate(-50%)';
                        settings.getMyTimedTextElement().webkitTransform='translateX(-50%)';
                        

                    }
                    else{
                        if (settings.getOriginalTextSide() == 0){
                            settings.setOriginalSubsPlacement(parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().x)+ (parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().width)*.025));
                            var sub_dist = (parseInt(document.getElementsByClassName("player-timedtext")[0].firstChild.getBoundingClientRect().width)+(settings.getOriginalSubsPlacement())+10);
                            settings.getMyTimedTextElement().style['left']=sub_dist+'px';

                        }
                        else{
                            settings.setOriginalSubsPlacement(parseInt(my_timedtext_element.getBoundingClientRect().x)+ parseInt(my_timedtext_element.getBoundingClientRect().width));
                            var sub_dist = (settings.getOriginalSubsPlacement())+10 - parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().x);
                            document.getElementsByClassName("player-timedtext")[0].firstChild.style['left']=sub_dist+'px';
                        }
                    }

                
            }

            if(settings.getOnOff() && mutation.type==='attributes' && (mutation.target.className==="player-timedtext-text-container notranslate" || mutation.target.className==='player-timedtext-text-container') ){
                // console.log("TRANSLATION");
                // let lines = document.querySelector('.my-timedtext-container'); 
                // if(lines.offsetWidth === lines.parentNode.clientWidth){
                //     console.log('OVERLAPPING');
                //     let temp_size=parseFloat(lines.style['font-size'].replace('px',''));
                //     temp_size-=5;
                //     lines.style['font-size']=temp_size+'px';
                //     console.log("Changing from: "+window.current_size+ ' To: '+temp_size+'px');
        
                // }
                
            }

            
        }
    };

    settings.setObserver(new MutationObserver(callback))
    settings.getObserver().observe(timedtext,settings.getConfig())
    settings.setTranslationTracker(new MutationObserver(translation_tracker_callback))
    settings.getTranslationTracker().observe(settings.getMyTimedTextElement(), settings.getTranslationTrackerConfig())

}

var addSubs = function(caption_row){ 

   if(caption_row.firstChild!=null && settings.getOnOff()){ // Ensures Subs were added rather than removed, probably redundant
        
        var container_count = caption_row.childElementCount; 
        try{
        settings.setBaseFont(parseFloat(caption_row.firstChild.firstChild.firstChild.style.fontSize.replace('px','')));
        }
        catch(e){
            settings.setBaseFont(parseFloat(caption_row.firstChild.firstChild.style.fontSize.replace('px','')));
            //console.log('error getting font');
        }
        if (container_count >1){ // Why work around Netflix sometimes using a seperate container for each row when I can just force it back into using one.. wish I'd done this earlier
            
            let count = caption_row.childElementCount;
            let final_innerText = '';
            
            let final_style = caption_row.firstChild.firstChild.firstChild.getAttribute('style');

            for (let i = 0; i<count;i++){
                
                final_innerText+=document.getElementsByClassName('player-timedtext-text-container')[i].firstChild.innerText;
                if (i<caption_row.childElementCount-1){
                    final_innerText+='\n';
                }
            }
            document.getElementsByClassName('player-timedtext-text-container')[0].firstChild.innerText=final_innerText;

            for (let j = 0; j<caption_row.childElementCount;j++){
                document.getElementsByClassName('player-timedtext-text-container')[1].remove();
            }
            document.getElementsByClassName('player-timedtext-text-container')[0].firstChild.setAttribute('style',final_style);

        }

        var old_style = caption_row.firstChild.style
        if(settings.getUpDownMode()){ //Stacked
            caption_row.firstChild.setAttribute('style','display: block; white-space: nowrap; max-width:100%;text-align: center; position: absolute; left: 50%; bottom:22%; -webkit-transform: translateX(-50%); transform: translateX(-50%);');   
        }
        else{ //Left - Right subs
        caption_row.firstChild.setAttribute('style','display: block; white-space: pre-wrap; text-align: center; position: absolute; left: 2.5%; bottom: 18%;');
        }
        caption_row.firstChild.setAttribute('translate','no'); //stopped working for edge
        // 
        
        if(settings.getEdge()){ //This seems to be the best option for now, keep an eye on it though
            caption_row.firstChild.className+=' notranslate';
        }
        
        //Graveyard of attempts to block edge translation
        // caption_row.firstChild.setAttribute('_isTranslated',1);
        // caption_row.firstChild.setAttribute('skiptranslate',1);
        // caption_row.firstChild.setAttribute('mstnotranslate',1);
        // caption_row.firstChild.setAttribute('_mstHidden',1);
        // caption_row.firstChild.setAttribute('_mstHiddenAttr',1); //One of these worked.. but not consistently
        // try{ //Edge translator so annoying.. This should work for now
        // Array.from(document.querySelector('.player-timedtext-text-container').querySelectorAll('*')).forEach(function(e){e.setAttribute('_isTranslated',1);e.setAttribute('_mstMutation','1')}); 
        // }
        // catch(e){
        //     // console.log("No subs");
        // }

        //caption_row.firstChild.className+=' notranslate'; //dont think multi-class will break the rest of the code but we'll see
        //This actually slows down the chrome translation time for some reason, will have to implement modes for each browser

        settings.setOriginalSubs(caption_row.firstChild.innerText)


        if (settings.getOriginalTextSide() == 1){
            caption_row.firstChild.style['left']='97.5%';
        }
        

        if (settings.getOriginalSubs() !== settings.getLastSubs()){
            settings.setLastSubs(settings.getOriginalSubs())
            settings.getMyTimedTextElement().innerText = settings.getOriginalSubs()
        }
        else if (settings.getOriginalSubs() ===''){
            settings.setMyTimedTextElement(settings.getOriginalSubs());
        }
        //window.baseFont = parseFloat(caption_row.firstChild.firstChild.firstChild.style.fontSize.replace('px','')); //font size changes way easily than on nrk so will take basefont after every clear instead (if change inset update, change this as well)
        settings.setCurrentSize(settings.getBaseFont() * settings.setCurrentMultiplier() +'px');

    
        if(settings.getUpDownMode()){
            var sub_bot = parseFloat(document.getElementsByClassName('player-timedtext')[0].style.inset.split(' ')[0].replace('px','')) + parseFloat('.'+document.getElementsByClassName('player-timedtext')[0].firstChild.style['bottom'])*document.getElementsByClassName('player-timedtext')[0].getBoundingClientRect().height;
            settings.getMyTimedTextElement().style['bottom']=(sub_bot-(settings.getBaseFont() * settings.getCurrentMultiplier())-10)+'px';

            let orig = document.getElementsByClassName('player-timedtext')[0].firstChild;
            // console.log(' orig.offsetWidth= ',orig.offsetWidth,' orig.parentNode.clientWidth= ',orig.parentNode.clientWidth,' baseFont=',window.baseFont)
            
            //Deal with overflow
            let temp_size= settings.getBaseFont();
            //1/31/23 - In edge, this causes translation so will need to apply translation block to all spans under player-timedtext.
            //On both, this only changes font size for the firstChild, should do all children instead
            while(orig.offsetWidth > orig.parentNode.clientWidth-150 && temp_size>8){ //doesnt allow smaller than 15, to prevent this running too long in some edge case I tested
                temp_size-=2;
                orig.firstChild.firstChild.style.fontSize=temp_size+'px';
                
                if(settings.getEdge()){orig.firstChild.className+=' notranslate';}
                for (let i =0;i<document.getElementsByClassName("player-timedtext")[0].firstChild.firstChild.children.length;i++){
                    if(settings.getEdge()){
                    orig.firstChild.children[i].className+=' notranslate';
                    }
                    orig.firstChild.children[i].style.fontSize=temp_size+'px';
                }
                // console.log("Changing orig from: "+window.current_size+ ' To: '+temp_size+'px');

            }

        }
        else{

            var sub_bot = parseFloat(document.getElementsByClassName('player-timedtext')[0].style.inset.split(' ')[0].replace('px','')) + parseFloat('.'+document.getElementsByClassName('player-timedtext')[0].firstChild.style['bottom'])*document.getElementsByClassName('player-timedtext')[0].getBoundingClientRect().height;
            settings.getMyTimedTextElement().style['bottom']=sub_bot+'px';

            if(settings.getOriginalTextSide()== 0){
                settings.setOriginalSubsPlacement(parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().x)+ (parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().width)*.025));
                var sub_dist = (parseInt(document.getElementsByClassName("player-timedtext")[0].firstChild.getBoundingClientRect().width)+(settings.getOriginalSubsPlacement())+10);
                settings.getMyTimedTextElement().style['left']=sub_dist+'px';

            }
            else{
                settings.getMyTimedTextElement().style['left']='2.5%';

                //Same but applied to my element instead
                settings.setOriginalSubsPlacement(parseInt(my_timedtext_element.getBoundingClientRect().x) + parseInt(my_timedtext_element.getBoundingClientRect().width));
                var sub_dist = (settings.getOriginalSubsPlacement())+10 - parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().x);
                caption_row.firstChild.style['left']=sub_dist+'px';

            }
        }
        
        if (settings.getFirstRun()){
            actual_create_buttons;
            settings.setFirstRun(0)
        }

        update_style('text_color');
        update_style('opacity');
        update_style('font_size');
        //update_style('original_font_size');//I'll do this later, currently a bit tricky since the current font size modification is uses the original subs as the base value 
        
    }
    settings.getObserver().observe(caption_row,settings.getConfig())

}

function update_style(setting){
    
    var lines = settings.getMyTimedTextElement();
    try{
    
        var original_lines = document.getElementsByClassName("player-timedtext")[0].firstChild.firstChild; //8/30/22 bug here

    }
    catch (e){
        return;
    }

    if (setting === 'font_size'){

        lines.style["font-size"]= settings.getCurrentSize();
        //Deal with overflowing text
        let temp_size=parseFloat(lines.style['font-size'].replace('px',''));
        while(lines.offsetWidth > lines.parentNode.clientWidth-50 && temp_size > 8){
            temp_size-=2;
            lines.style['font-size']=temp_size+'px';
            // console.log("Changing from: "+window.current_size+ ' To: '+temp_size+'px');

        }
        // let orig = document.getElementsByClassName('player-timedtext')[0].firstChild;

        // let temp_size=window.baseFont;
        // while(orig.offsetWidth > orig.parentNode.clientWidth-50){
        //     temp_size-=1;
        //     orig.style['font-size']=temp_size+'px';
        //     console.log("Changing orig from: "+window.current_size+ ' To: '+temp_size+'px');

        // }
        
    }
    else if (setting === "text_color"){

        lines.style['color']= settings.getTextColor();
        
        //following line is for multi-container support, but doesn't affect single container mode so I didn't bother with an if(container_count)
        document.getElementsByClassName('player-timedtext')[0].firstChild.firstChild.style['color']= settings.getOriginalTextColor();

        for (let i =0;i<document.getElementsByClassName("player-timedtext")[0].firstChild.firstChild.children.length;i++){
            original_lines.children[i].style['color']= settings.getOriginalTextColor();
        }
        //original_lines.style["color"]=window.originaltext_color;

    }

    else if (setting === "opacity"){

            lines.style["opacity"]= settings.getOpacity();
            original_lines.style["opacity"]= settings.getOriginalTextOpacity();

    }

    /*if (setting === "sub_distance"){ //Not quite sure how to pick the color yet..
        var test = parseInt(window.baseOffset)+parseInt(window.sub_distance);
        document.getElementsByClassName("mysubs")[0].style.left=test+'px';
       // console.log("Set sub distance");
    }
    */

}

chrome.runtime.onMessage.addListener( //Listens for messages sent from background script (Preferences Controller)
    function (request, sendRespone, sendResponse){

        if (request.message === "update_on_off"){
            settings.setOnOff(request.value);
            if (!settings.getOnOff()){
                
                try{

                    settings.getMyTimedTextElement().style['display']='none';
                    Array.from(document.querySelector('.player-timedtext').querySelectorAll('*')).forEach(e=>e.style['color']='#FFFFFF');
                    document.querySelector('.player-timedtext-text-container').style['left']='50%';
                    document.querySelector('.player-timedtext-text-container').style['transform']='translate(-50%)';
                    document.querySelector('.player-timedtext-text-container').style['-webkit-transform']='translateX(-50%)'; 
                    document.querySelector('.injected-style').remove();
                    
                }
                catch(e){
                   console.log(e);
                }
                try{

                    document.getElementById("myTutorialButton").style.display='none';
                }
                catch(e){
                   // console.log(e);
                }
            }
            else{

                let st = document.createElement('style'); 
        
                st.innerText='.player-timedtext br{content: "";}' +
                '.my-timedtext-container br{content: "";}' +
                '.player-timedtext br:after{content: " ";}' +
                '.my-timedtext-container br:after{content: " ";}';

                st.className='injected-style';
                document.head.appendChild(st);

                try{
                    settings.getMyTimedTextElement().style['display']='block';
                    
                    for (let i =0;i<document.getElementsByClassName("player-timedtext")[0].firstChild.children.length;i++){
                        document.getElementsByClassName("player-timedtext")[0].firstChild.children[i].style['color']= settings.getOriginalTextColor();
                    }
                }
                catch(e){
                // console.log(e);
                }
                
                try{
                    document.getElementById("myTutorialButton").style.display='block';
                }
                catch(e){
                    actual_create_buttons();
                }
            }
        }

        if (request.message === "update_button_on_off"){
            settings.setButtonOnOff(request.value);
            if (!settings.getButtonOnOff()){
                
                try{
                document.getElementById("myTutorialButton").style.display='none';
                }
                catch(e){
                   // console.log(e);
                }
            }
            else{
                
                try{
                document.getElementById("myTutorialButton").style.display='block';
                }
                catch(e){
                   // console.log(e);
                    actual_create_buttons();
                }
            }
        }
        
        if (request.message==='update_font_multiplier'){

            //console.log("Recieved Message from BACKGROUND.JS to CHANGE font_multiplier to " + request.value);

            settings.getCurrentMultiplier(parseFloat(request.value));
            settings.getCurrentSize(settings.getBaseFont()*request.value+'px');
            update_style('font_size');

        }

        /*if (request.message ==='update_sub_distance'){ inconsistent functionality for some reason.. but I don't think people would need this option anyways so I'll disable for now
           // console.log("Recieved Message from BACKGROUND.JS to CHANGE side to " + request.value);
            window.sub_distance=parseInt(request.value);
            //StoreSetting('current_size',window.current_size)
            update_style('sub_distance');
        }
        */

        if (request.message ==='update_text_color'){

            //console.log("Recieved Message from BACKGROUND.JS to CHANGE color to " + request.value);
            settings.setTextColor(request.value)
            update_style('text_color');

        }

        if (request.message ==='update_opacity'){
            //console.log("Recieved Message from BACKGROUND.JS to CHANGE opacity to " + request.value);
            settings.setOpacity(parseFloat(request.value))
            update_style('opacity');

        }

        if (request.message ==='update_originaltext_opacity'){

            //console.log("Recieved Message from BACKGROUND.JS to CHANGE opacity to " + request.value);
            settings.setOriginalTextOpacity(parseFloat(request.value))
            update_style('opacity');

        }

        if (request.message ==='update_originaltext_color'){

            //console.log("Recieved Message from BACKGROUND.JS to CHANGE opacity to " + request.value);
            settings.setOriginalTextColor(request.value)
            
            update_style('text_color');
            try{
                document.getElementById('myTutorialButton').firstChild.firstChild.firstChild.firstElementChild.setAttribute('stroke', settings.getOriginalTextColor());
            }
            catch(e){
               // console.log(e);
            }
        }

        if (request.message ==='update_button_up_down_mode'){

            settings.setUpDownMode(request.value);

            if (!settings.getUpDownMode()){ //turning off

                settings.getMyTimedTextElement().style['left']='';
                settings.getMyTimedTextElement().style['transform']='';
                settings.getMyTimedTextElement().style['-webkit-transform']='';
                settings.getMyTimedTextElement().style['white-space']='pre-wrap';
                try{
                    document.querySelector('.injected-style').remove();
                    
                }
                catch(e){
                    // console.log("No injected css");
                    // console.log(e);
                }

                try{

                    settings.setOriginalSubsPlacement(parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().x)+ (parseInt(document.getElementsByClassName("player-timedtext")[0].getBoundingClientRect().width)*.025));
                    var sub_dist = (parseInt(document.getElementsByClassName("player-timedtext")[0].firstChild.getBoundingClientRect().width)+(settings.getOriginalSubsPlacement())+10);
                    settings.getMyTimedTextElement().style['left']=sub_dist+'px';
                    
                    
                    try{
                    document.querySelector('.player-timedtext-text-container').setAttribute('style','display: block; white-space: pre-wrap; text-align: center; position: absolute; left: 2.5%; bottom: 18%;');
                    }
                    catch(e){
                        //console.log("No subs onscreen");
                    }
                    var sub_bot = parseFloat(document.getElementsByClassName('player-timedtext')[0].style.inset.split(' ')[0].replace('px','')) + parseFloat('.'+document.getElementsByClassName('player-timedtext')[0].firstChild.style['bottom'])*document.getElementsByClassName('player-timedtext')[0].getBoundingClientRect().height;
                    settings.getMyTimedTextElement().style['bottom']=sub_bot+'px';

                }
                catch(e){
                //    console.log(e);
                }

            }
            else{
                
                let st = document.createElement('style'); 
                
                
                st.innerText='.player-timedtext br{content: "";}' +
                '.my-timedtext-container br{content: "";}' +
                '.player-timedtext br:after{content: " ";}' +
                '.my-timedtext-container br:after{content: " ";}';
        
                document.head.appendChild(st);
                

                try{
                    settings.getMyTimedTextElement().style['left']='50%';
                    settings.getMyTimedTextElement().style['transform']='translate(-50%)';
                    settings.getMyTimedTextElement().style['-webkit-transform']='translateX(-50%)';
                    settings.getMyTimedTextElement().style['white-space']='nowrap';
                    try{
                    document.querySelector('.player-timedtext-text-container').setAttribute('style','display: block; white-space: nowrap; max-width:100%; text-align: center; position: absolute;left: 50%; bottom:20%; -webkit-transform: translateX(-50%); transform: translateX(-50%);');   
                    var sub_bot = parseFloat(document.getElementsByClassName('player-timedtext')[0].style.inset.split(' ')[0].replace('px','')) + parseFloat('.'+document.getElementsByClassName('player-timedtext')[0].firstChild.style['bottom'])*document.getElementsByClassName('player-timedtext')[0].getBoundingClientRect().height;
                    settings.getMyTimedTextElement().style['bottom']=(sub_bot-(settings.getBaseFont() * settings.getCurrentMultiplier())-10)+'px';
                    }
                    catch(e){
                        // console.log("No subs on the screen");
                    }

                }
                catch(e){
                //    console.log(e);
                }

            }

        }

});