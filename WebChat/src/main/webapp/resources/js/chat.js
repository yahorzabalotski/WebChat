/**
 * Created by ze on 3/15/15.
 */

var token = "TN11EN";
var editId = null;

function run(){
    document.getElementById('send').addEventListener("click", sendMessage, false);
    document.getElementById('select_user_name').addEventListener("click", selectUser, false);
    document.getElementById('clear').addEventListener("click", onClearClick, false);
    restoreUser();
    if(getUserName() != ""){
        connect();
    }
}

function displayAllMessage(mess){
    for(var i = 0; i < mess.length; i++){
        updateMessageBox(mess[i]);
    }
}

function onClearClick(){
    document.getElementById('messageBox').innerHTML = "";
}

function getUserName(){
    return document.getElementById("user_name").value;
}

function setUserName(name){
    return document.getElementById("user_name").value = name;
}

function message(mess, id){
    return {
        user: getUserName(),
        message: mess,
        id: id
    };
}

function sendMessage(){
    if(editId == null) {
        var mess = message(document.getElementById('message').value, "");
        document.getElementById('message').value = "";
        ajax('POST', 'http://localhost:8080/WebChat/chat', JSON.stringify(mess), function (serverResponse) {
        }, function (error) {
            alert(error)
        });
    }
    else{
        var mess = message(document.getElementById('message').value, editId);
        document.getElementById('message').value = "";
        ajax('PUT', 'http://localhost:8080/WebChat/chat', JSON.stringify(mess), function (serverResponse) {
        }, function (error) {
            alert(error)
        });
        editId = null;
    }
}

function updateMessageBox(tmp){
    var id = tmp.user + tmp.id;
    document.getElementById('messageBox').appendChild(createMessageTable(tmp.id, tmp));
    document.getElementById(tmp.id + 'b').addEventListener("click", onClickRemoveMessage, false);
    document.getElementById(tmp.id + 'e').addEventListener("click", onClickEditMessage, false);
    document.getElementById('message').value = "";
}

function createMessageTable(id, mess){
    var messTable = document.createElement('table');
    messTable.setAttribute('id', id);
    messTable.setAttribute('class', 'messageTable');
    messTable.innerHTML = "<tr>" +
    "<td class=\"firstColumn\"><div class=\"divUserName\">" + mess.user + ':' + "</div></td>" +
    "<td class=\"messageColumn\"><div class=\"divMessageBox\" id=\"" + id + 't' + "\">" + mess.message + "</div></td>" +
    "<td class=\"editButtonColumn\"><button id=\"" + id + 'e' + "\" class=\"editMessageButton\"><img src=\"edit.gif\"></button></td>" +
    "<td class='deleteButtonColumn'><button class='editMessageButton' id=\"" + id + 'b' + "\"><img src=\"close.png\"></button></td>" +
    "</tr>";
    return messTable;
}

function deleteMessage(id){
    var del = document.getElementById(id);
    document.getElementById('messageBox').removeChild(del);
}


function onClickEditMessage(){
    editId = this.id.substring(0, this.id.length - 1);
    document.getElementById('message').value = document.getElementById(editId + 't').innerHTML;
}

function editMessage(id, text){
    document.getElementById(id + 't').innerHTML = text;
}

function onClickRemoveMessage(){
    var messId = this.id.substring(0, this.id.length - 1);
    ajax('DELETE', 'http://localhost:8080/WebChat/chat', JSON.stringify(message("", messId)),  function(serverResponse){
    }, function(error){ alert(error)});
}

function messageTextFieldKeyDown(e){
    e = e||window.event;
    if(e.keyCode===13){
        sendMessage();
    }
}

function selectUser() {
    var userName = document.getElementById('user_name').value;
    if(userName != ""){
        storeUser();
        connect();
    }
}

function restoreUser(){
    if(typeof(Storage) == "undefined"){
        alert('localStorage is not accessible');
        return;
    }
    var item = localStorage.getItem("userName");
    setUserName(item);
}

function storeUser(){
    if(typeof(Storage) == "undefined"){
        alert('localStorage is not accessible');
        return;
    }
    localStorage.setItem("userName", getUserName());
}

/*
function addUserName(name){
    var option = document.createElement('option');
    option.innerHTML = name;
    document.getElementById('names').appendChild(option);
}
*/

function output(value){
    alert(value);
}

function defaultErrorHandler(message) {
    console.error(message);
    output(message);
}

function isError(text) {
    return false;
    if(text == "")
        return false;

    try {
        var obj = JSON.parse(text);
        return !!obj.error;
    } catch(ex) {
        return true;
    }
}

function ajax(method, url, data, continueWith , continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method /* || 'GET'*/, url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4)
            return;

        if(xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }

        if(isError(xhr.responseText)) {
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }

        continueWith(xhr.responseText);
    };

    xhr.ontimeout = function () {
        ontinueWithError('Server timed out !');
    }

    xhr.onerror = function (e) {
        var errMsg = 'Server connection error !\n'+
            '\n' +
            'Check if \n'+
            '- server is active\n'+
            '- server sends header "Access-Control-Allow-Origin:*"';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

function connect(){
    setInterval(function(){
        ajax('GET', 'http://localhost:8080/WebChat/chat?token=' + token, function(error){ alert(error)}, getLastMessage)
    }, 1000);
}

function getLastMessage(serverResponse){
    var tmp = JSON.parse(serverResponse);
    doAllInstruction(tmp.instruction);
    if(tmp.token.toString() != "TN11EN") {
        token = tmp.token;
    }
}

function doAllInstruction(instruction){
    for(var i = 0; i < instruction.length; i++){
        doInstruction(instruction[i]);
    }
}

function doInstruction(instruction){
    if("add" == instruction.state){
        updateMessageBox(JSON.parse(instruction.message));
    } else if("del" == instruction.state){
        deleteMessage(JSON.parse(instruction.message).id);
    } else if("edit" == instruction.state){
        editMessage(JSON.parse(instruction.message).id, JSON.parse(instruction.message).message);
    }
}