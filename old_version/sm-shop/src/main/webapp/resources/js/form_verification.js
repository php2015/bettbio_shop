var FieldHandlerExample = {
    nullable: false, // can be true|false
    inputType: 'text', // can be text | checkbox | radio | null
    inputElement: 'xxx', // if inputType is radio, should be radio group 'name', else, should its 'id'
    isValid : function(value) { // return value shoule be true or false
        return value == 'field handler example';
    },
    onError : function(isNull, isValid) {
        // any code you need to show error code as you want
    },
    onCorrect : function() {
        // any code you want
    }
};

var FormVerification = new function (){
    var me = this;
    var myHandlers = new Array();
    function checkIsNull(value) {
        return !value || !/[^\s]+/.test(value);
    }
    function isOnFocus(handler){
        var elemType = handler.inputType;
        var elemKey = handler.inputElement;
        if (elemType == 'radio'){
            return $("input[name='"+elemKey+"']").is(":focus");
        }
        return $('#'+elemKey).is(":focus");
    }
    function getValueOfField(elemType, elemKey){
        if (elemType == 'radio'){
            return $("input[name='"+elemKey+"']:checked").val();
        }
        return $('#'+elemKey).val();
    }
    function updateShouldVerify(event){
        var elemObj = $(event.currentTarget);
        var elemType = elemObj.attr('type');
        var elemKey;
        if (elemType == 'radio'){
            elemKey = elemObj.attr('name');
        }else{
            elemKey = elemObj.attr('id');
        }
        $.each(myHandlers, function(i, data){
            if (data.inputElement == elemKey){
                data.shouldVerify = true;
            }
        });
        
    }
    me.setFieldHandlers = function (handlerArray){
        for (var i=0;i<handlerArray.length;i++){
            var handler = handlerArray[i];
            if (typeof handler.isValid != "function" || typeof handler.onError != "function" || typeof handler.onCorrect != "function"){
                alert("You must set handler as per FieldHandlerExample. Please take a look.");
                return;
            }
            handler.shouldVerify = false;
            if (handler.inputType == 'radio'){
                $("input[name='"+handler.inputElement+"']").focus(function() {
                   updateShouldVerify(event);
                });
            }else{
                $('#'+handler.inputElement).focus(function(event) {
                    updateShouldVerify(event);
                });
            }
            myHandlers.push(handler);
        }
    };

    function doVerify(stopAtFirst) {
        var result = true;
        var onFocusedHandler = null;
        var lastData = {isNull: false, isValid: true};
        for(var i=0;i<myHandlers.length;i++){
            var handler = myHandlers[i];
            var value = getValueOfField(handler.inputType, handler.inputElement);
            var isNull = checkIsNull(value);
            if (isNull) {
                if (handler.nullable){
                    handler.onCorrect();
                    continue;
                }
                // not nullable, but maybe never touched
                result = false;
            }
            var isValid = handler.isValid(value);
            if (!isValid){
                result = false;
            }

            if (!handler.shouldVerify){
                // maybe error, but never touched, maybe typing in other field.
                continue;
            }
            if (isNull || !isValid){
                handler.onError(isNull, isValid);
                if (isOnFocus(handler)){
                    // console.log(handler.inputElement+" is on focus");
                    onFocusedHandler = handler;// myHandlers[i];
                    lastData.isNull = isNull;
                    lastData.isValid = isValid;
                }else{
                    // console.log(handler.inputElement+" is not focus");
                }
            }else{
                handler.onCorrect();
            }
            if (stopAtFirst && !result){
                break;
            }
        }
        if (onFocusedHandler != null){
            onFocusedHandler.onError(lastData.isNull, lastData.isValid);
        }
        return result;
    }

    me.verifyAndStopAtFirst = function(){
        return doVerify(true);
    };

    me.verifyAll = function(){
        return doVerify(false);
    };
}