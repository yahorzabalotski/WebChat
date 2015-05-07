/**
 * Created by zE on 02.05.2015.
 */

function run(){
    document.getElementById('signIn').addEventListener("click", onSignInClick, false);
    document.getElementById('send').addEventListener("click", onSendClick, false);
    document.getElementById('name').addEventListener("keydown", signInKeyDown, false);
    document.getElementById('inputMessage').addEventListener("keydown", sendMessageKeyDown, false);
    doGet();
}

function onSignInClick() {
    var name = document.getElementById('name').value;
    if (name != "" && name.length > 3) {
        addUser(name);
        $("[data-dismiss=modal]").trigger({ type: "click" });
    }
    else{
        $('#nameBlock').addClass('has-error');
        $('#nameError').text("Name ...");
        $('#nameError').show();
    }
}

function doGet(){
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/WebChat/chat',
        success: function(serverResponse){
            parseResponse(serverResponse);
        }
    });
}

function parseResponse(serverResponse){
    var response = JSON.parse(serverResponse);
    alert(response);
    alert(response[0]);
    doAllInstruction(response);
}

function doAllInstruction(instruction){
    for(var i = 0; i < instruction.length; i += 2){
        doInstruction(instruction[i], instruction[i + 1]);
    }
}

function doInstruction(instruction, value){
    var tmp = JSON.parse(value);
    if("add" === instruction){
        document.getElementById('messageBox').appendChild(createMessage(tmp.date, tmp.author, tmp.text));
        document.getElementById('messageBox').scrollTop = document.getElementById('messageBox').scrollHeight;
    }
}

function addUser(name) {
    var userBox = document.getElementById('userBox');
    userBox.appendChild(createNewUser(name));
}

function createNewUser(name) {
    var div = document.createElement('div');
    div.setAttribute('id','username' + name);
    div.innerText = name;
    return div;
}

function getUserName(){
    return document.getElementById('userBox').getElementsByTagName('*')[0].innerText;
}

function getMessage(){
    return document.getElementById('inputMessage').value;
}

function setMessage(text) {
    document.getElementById('inputMessage').value = text;
}

function setEdit(value) {
    document.getElementById('inputMessage').setAttribute('checked', value);
}

function isEdit() {
    var cheak = document.getElementById('inputMessage').getAttribute('checked');
    if(cheak == 'true') {
        return true;
    }
    return false;
}

function onSendClick(){
    var mess = message(getMessage());
    setMessage("");
    var method = 'POST';
    if(isEdit()){
       method = 'PUT';
        setEdit('false');
    }

    $.ajax({
        method: method,
        url: 'http://localhost:8080/WebChat/chat',
        data: JSON.stringify(mess)
    });


    //add mess
    document.getElementById('messageBox').appendChild(createMessage(1, getUserName(), mess.text));
    document.getElementById('messageBox').scrollTop = document.getElementById('messageBox').scrollHeight;
}

function message(mess){
    return {
        author: getUserName(),
        text: mess
    };
}

function signInKeyDown(e){
    e = e||window.event;
    if(e.keyCode === 13){
        onSignInClick();
    }
}

function isAllowMessage(mess){
    if(mess != ""){
        return true;
    }
    return false;
}

function sendMessageKeyDown(e){
    e = e||window.event;
    if(e.keyCode === 13 && isAllowMessage(getMessage())){
        onSendClick();
    }
}

function createMessage(date, author, text) {
    var div = document.createElement('div');
    div.setAttribute("class", "mainDiv");
    div.innerHTML = "<div style='display: inline-block; width: 95%; columns:  2'>" +
    "<p style='display:inline; padding-left: 20px'>" + author + ":" + "</p>" +
    "<p style='display:inline; word-break: break-all; padding-left: 20px'>" + text +"</p></div>" +
    "<div style='display: inline-block; vertical-align: top'>" +
    "<span class='glyphicon glyphicon-remove' style='padding-right: 10px'></span>" +
    "<span class='glyphicon glyphicon-pencil' style='padding-right: 10px'></span></div>";
    return div;
}