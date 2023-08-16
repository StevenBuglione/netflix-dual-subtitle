export default class Settings{

    #playerActive
    #weirdClassnameMode
    #edge
    #upDownMode
    #onOff
    #buttonOnOff
    #currentMultiplier
    #textColor
    #opacity
    #originalTextOpacity
    #originalTextColor
    #originalTextSide
    #videoChangeObserverConfig
    #videoChangeObserver
    #buttonConfig
    #buttonObserver
    #counter
    #translationTrackerConfig
    #myTimedTextElement
    #lastSubs
    #oldInset
    #originalSubsPlacement
    #cleared
    #config
    #oldText
    #observer
    #translationTracker
    #baseFont
    #currentSize
    #netflixMode
    #originalSubs
    #firstRun



    constructor() {
        this.#playerActive = 0;
        this.#weirdClassnameMode = 0;
        this.#edge = 0;
        this.#upDownMode = null;
        this.#onOff = null;
        this.#buttonOnOff = null;
        this.#currentMultiplier = null;
        this.#textColor = null;
        this.#opacity = null;
        this.#originalTextOpacity = null;
        this.#originalTextColor = null;
        this.#originalTextSide = null;
        this.#videoChangeObserverConfig = null;
        this.#videoChangeObserver = null;
        this.#buttonConfig = null;
        this.#buttonObserver = null;
        this.#counter = null;
        this.#translationTrackerConfig = null;
        this.#myTimedTextElement = null;
        this.#lastSubs = null;
        this.#oldInset = null;
        this.#originalSubsPlacement = null;
        this.#cleared = null;
        this.#config = null;
        this.#oldText = null;
        this.#observer = null;
        this.#translationTracker = null;
        this.#baseFont = null;
        this.#currentSize = null;
        this.#netflixMode = null;
        this.#originalSubs = null;
        this.#firstRun = null;
    }

    getPlayerActive(){
        return this.#playerActive;
    }

    setPlayerActive(value){
        this.#playerActive = value;
    }

    getWeirdClassnameMode(){
        return this.#weirdClassnameMode;
    }

    setWeirdClassnameMode(value){
        this.#weirdClassnameMode = value;
    }

    getEdge(){
        return this.#edge
    }

    setEdge(value){
        this.#edge = value
    }

    getUpDownMode(){
        return this.#upDownMode();
    }

    setUpDownMode(value){
        this.#upDownMode = value;
    }

    getOnOff(){
        return this.#onOff;
    }

    setOnOff(value){
        this.#onOff = value
    }

    getButtonOnOff(){
        return this.#buttonOnOff
    }

    setButtonOnOff(value){
        this.#buttonOnOff = value
    }

    //currentMultiplier
    getCurrentMultiplier(){
        return this.#currentMultiplier
    }

    setCurrentMultiplier(value){
        this.#currentMultiplier = value
    }

    //textColor
    getTextColor(){
        return this.#textColor;
    }

    setTextColor(value){
        this.#textColor = value
    }

    //originalTextOpacity
    getOriginalTextOpacity(){
        return this.#originalTextOpacity
    }

    setOriginalTextOpacity(value){
        this.#originalTextOpacity = value
    }

    //originalTextColor
    getOriginalTextColor(){
        return this.#originalTextColor
    }

    setOriginalTextColor(value){
        this.#originalTextColor = value
    }

    //opacity
    getOpacity(){
        return this.#opacity
    }

    setOpacity(value){
        this.#opacity = value;
    }

    //originalTextSide
    getOriginalTextSide(){
        return this.#originalTextSide
    }

    setOriginalTextSide(value){
        this.#originalTextSide = value
    }

    //videoChangeObserverConfig
    getVideoChangeObserverConfig(){
        return this.#videoChangeObserverConfig
    }

    setVideoChangeObserverConfig(object){
        this.#videoChangeObserverConfig = object;
    }

    //videoChangeObserver
    getVideoChangeObserver(){
        return this.#videoChangeObserver
    }

    setVideoChangeObserver(object){
        this.#videoChangeObserver = object;
    }

    //#buttonConfig
    //     #buttonObserver


    getButtonConfig(){
        return this.#buttonConfig
    }

    setButtonConfig(object){
        this.#buttonConfig = object;
    }

    getButtonObserver(){
        return this.#buttonObserver;
    }

    setButtonObserver(object){
        this.#buttonObserver = object;
    }

    getCounter(){
        return this.#counter;
    }

    setCounter(value){
        this.#counter = value;
    }

    //    #translationTrackerConfig
    getTranslationTrackerConfig(){
        return this.#translationTrackerConfig;
    }

    setTranslationTrackerConfig(object){
        this.#translationTrackerConfig = object;
    }

    //#myTimedTextElement
    getMyTimedTextElement(){
        return this.#myTimedTextElement;
    }

    setMyTimedTextElement(element){
        this.#myTimedTextElement = element
    }

    getLastSubs(){
        return this.#lastSubs
    }

    setLastSubs(value){
        this.#lastSubs = value;
    }

    //  #oldInset
    getOldInset(){
        return this.#oldInset;
    }

    setOldInset(value){
        this.#oldInset = value;
    }

    //originalSubsPlacement
    getOriginalSubsPlacement(){
        return this.#originalSubsPlacement;
    }

    setOriginalSubsPlacement(value){
        this.#originalSubsPlacement = value
    }

    getCleared(){
        return this.#cleared
    }

    setCleared(value){
        this.#cleared = value;
    }

    getConfig(){
        return this.#config
    }

    setConfig(object){
        this.#config = object;
    }

    getOldText(){
        return this.#oldText;
    }

    setOldText(value){
        this.#oldText = value;
    }

    getObserver(){
        return this.#observer;
    }

    setObserver(object){
        this.#observer = object;
    }

    getTranslationTracker(){
        return this.#translationTracker
    }

    setTranslationTracker(object){
        return this.#translationTracker
    }

    getBaseFont(){
        return this.#baseFont
    }

    setBaseFont(value){
        this.#baseFont = value
    }

    getCurrentSize(){
        return this.#currentSize
    }

    setCurrentSize(value){
        this.#currentSize = value;
    }

    getNetflixMode(){
        return this.getNetflixMode();
    }

    setNetflixMode(value){
        this.#netflixMode = value;
    }

    getOriginalSubs(){
        return this.#originalSubs
    }

    setOriginalSubs(value){
        this.#originalSubs = value
    }

    getFirstRun(){
        return this.#firstRun
    }

    setFirstRun(value){
        this.#firstRun = value;
    }
}