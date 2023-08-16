export default class Settings{
    constructor() {
        this.playerActive = 0;
        this.weirdClassnameMode = 0;
        this.edge = 0;
        this.upDownMode = null;
        this.onOff = null;
        this.buttonOnOff = null;
        this.currentMultiplier = null;
        this.textColor = null;
        this.opacity = null;
        this.originalTextOpacity = null;
        this.originalTextColor = null;
        this.originalTextSide = null;
    }

    getPlayerActive(){
        return this.playerActive;
    }

    setPlayerActive(value){
        this.playerActive = value;
    }

    getWeirdClassnameMode(){
        return this.weirdClassnameMode;
    }

    setWeirdClassnameMode(value){
        this.weirdClassnameMode = value;
    }

    getEdge(){
        return this.edge
    }

    setEdge(value){
        this.edge = value
    }

    getUpDownMode(){
        return this.getUpDownMode();
    }

    setUpDownMode(value){
        this.upDownMode = value;
    }

    getOnOff(){
        return this.onOff;
    }

    setOnOff(value){
        this.onOff = value
    }

    getButtonOnOff(){
        return this.buttonOnOff
    }

    setButtonOnOff(value){
        this.buttonOnOff = value
    }

    //currentMultiplier
    getCurrentMultiplier(){
        return this.currentMultiplier
    }

    setCurrentMultiplier(value){
        this.currentMultiplier = value
    }

    //textColor
    getTextColor(){
        return this.textColor;
    }

    setTextColor(value){
        this.textColor = value
    }

    //originalTextOpacity
    getOriginalTextOpacity(){
        return this.originalTextOpacity
    }

    setOriginalTextOpacity(value){
        this.originalTextOpacity = value
    }

    //originalTextColor
    getOriginalTextColor(){
        return this.originalTextColor
    }

    setOriginalTextColor(value){
        this.originalTextColor = value
    }

    //opacity
    getOpacity(){
        return this.opacity
    }

    setOpacity(value){
        this.opacity = value;
    }

    //originalTextSide
    getOriginalTextSide(){
        return this.originalTextSide
    }

    setOriginalTextSide(value){
        this.originalTextSide = value
    }
}