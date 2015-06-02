/**
 * Created by zE on 02.05.2015.
 */

function run(){
    document.getElementById('signIn').addEventListener("click", onSignInClick, false);
    document.getElementById('send').addEventListener("click", onSendClick, false);
    document.getElementById('name').addEventListener("keydown", signInKeyDown, false);
    document.getElementById('inputMessage').addEventListener("keydown", sendMessageKeyDown, false);
    document.getElementById('LogOut').addEventListener("click", onLogOutClick, false);
    $(function() {
        //$('#messageBox').perfectScrollbar();
        Ps.initialize(document.getElementById('messageBox'));
        Ps.suppressScrollY = true;
    });
    setEdit("");
}

function onSignInClick() {
    var name = document.getElementById('name').value;
    if (name != "" && name.length > 3) {
        addUser(name);
        $("[data-dismiss=modal]").trigger({ type: "click" });
        postUser(name);
    }
    else{
        $('#nameBlock').addClass('has-error');
        $('#nameError').text("Name ...");
        $('#nameError').show();
    }
}

function onLogOutClick(){
    location.reload();
}

function postUser(userName){
    var data = JSON.stringify({name: userName});
    doRequest('POST', data).done(function(Response){
        (function poll() {
            var jqxhr = doRequest('GET');
            jqxhr.done(function(response){
                parseResponse(response);
                setGoodServerStatus();
            });
            jqxhr.error(function(response){
                setBadServerStatus()
            });
            jqxhr.always(function(response){
                poll();
            })
        })();
    }).error(function(response){
        setBadServerStatus()
    });
}

function parseResponse(response){
    if( response != "") {
        var mess = JSON.parse(response);
        for (var i = 0; i < mess.length; i++) {
            viewMessage(JSON.parse(mess[i]));
        }
    }
}

function setBadServerStatus() {
    document.getElementById('serverStatus').style.color = 'red';
}

function setGoodServerStatus() {
    document.getElementById('serverStatus').style.color = 'green';
}

function addUser(name) {
    document.getElementById('user').innerHTML = name;
}

function createNewUser(name) {
    var div = document.createElement('div');
    div.setAttribute('id','username' + name);
    div.innerText = name;
    return div;
}

function getUserName(){
    return document.getElementById('user').innerHTML;

}

function getMessage(){
    return $('#inputMessage').val();
}

function setMessage(text) {
    $('#inputMessage').val(text);
}

function setEdit(value) {
    $('#inputMessage').data("editId", value);
}

function getEdit() {
    return $('#inputMessage').data("editId");
}

function isEdit() {
    var cheak = $("#inputMessage").data("editId");
    if(cheak != "") {
        return true;
    }
    return false;
}

function onSendClick(){
    var mess = message(getMessage(), 0);
    setMessage("");
    if(isEdit()) {
        mess.id = getEdit();
        setEdit("");
    }
    var data = '{\"text\":\"' + mess.text + '\", \"id\":\"' + mess.id + '\"}';
    doRequest('PUT', data);
}

function message(mess, id){
    return {
        author: getUserName(),
        text: mess,
        id: id
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

function viewMessage(message){
    var element = document.getElementById(message.id);

    if(element != null) {
        element.firstElementChild.children[1].innerHTML = message.text;
    } else {
        $('#messageBox').append(createMessage(message));
    }


    document.getElementById('messageBox').scrollTop = document.getElementById('messageBox').scrollHeight;
}

function createMessage(mess) {
    var div = document.createElement('div');
    div.setAttribute("class", "mainDiv");
    div.setAttribute("id", mess.id);
    //div.innerHTML = getMessageContent1(mess);
    div.appendChild(getMessageContent(mess));
    var innerDiv = document.createElement('div');
    innerDiv.setAttribute("style","display: inline-block; vertical-align: top");
    if(getUserName() == mess.author) {
        innerDiv.appendChild(getRemoveMessageSpan());
        innerDiv.appendChild(getEditMessageSpan());

    }
    div.appendChild(innerDiv);
    return div;
}

function getMessageContent(mess){
    var div = document.createElement('div');
    div.setAttribute("style", 'display: inline-block; width: 95%; columns:  2; paddin-left: 10px; margin-left: 10px; ')
    var author = document.createElement('p');
    author.setAttribute("style", 'display:inline; padding-left: 20px; word-wrap: break-word;');
    author.innerHTML ="<b style='padding-left: 10px; margin-left: 10px'>" +  mess.author + "</b>";
    author.style.color = "rgb(" + Math.floor( Math.random() * 255) +  "," + Math.floor( Math.random() * 255) + "," + Math.floor( Math.random() * 255) + ")"
    var text = document.createElement('p');
    text.setAttribute("style", 'display:inline; word-break: break-word; padding-left: 20px;');
    text.innerHTML = mess.text;
    text.style.color = "rgb(" + Math.floor( Math.random() * 255) +  "," + Math.floor( Math.random() * 255) + "," + Math.floor( Math.random() * 255) + ")"
    div.appendChild(author);
    div.appendChild(text);
    return div;
}

function getMessageContent1(mess) {
    var div = document.createElement('div');
    div.innerHTML = "<p style='display:inline; padding-left: 20px; color='" +
    + "rgb(" + Math.floor( Math.random() * 255) +  "," + Math.floor( Math.random() * 255) + "," + Math.floor( Math.random() * 255) + ")" +
    + "'><b>" + mess.author + "</b></p>" +
    "<p style='display:inline; word-break: break-all; padding-left: 20px; color='" +
    + "rgb(" + Math.floor( Math.random() * 255) +  "," + Math.floor( Math.random() * 255) + "," + Math.floor( Math.random() * 255) + ")"
    + "'></p>";
}

function getRemoveMessageSpan(){
    var span = document.createElement('span');
    span.setAttribute("class", "glyphicon glyphicon-remove");
    span.setAttribute("style", "padding-right: 10px; color:red");
    span.addEventListener("click", onRemoveClick, false);
    return span;
}

function getEditMessageSpan() {
    var span = document.createElement('span');
    span.setAttribute("class", "glyphicon glyphicon-pencil");
    span.setAttribute("style", "padding-right: 10px; color: green;");
    span.addEventListener("click", onEditClick, false);
    return span;
}

function onEditClick(e){
    var sender = (e && e.target) || (window.event && window.event.srcElement);
    var mess = sender.parentElement.parentElement;
    var text = mess.children[0].children[1].innerHTML;
    if(text != "") {
        setEdit(mess.id);
        $('#inputMessage').val(text);
    }
}

function onRemoveClick(e){
    var sender = (e && e.target) || (window.event && window.event.srcElement);
    var delId = sender.parentElement.parentElement.id;
    var data = "{\"id\":\"" + delId + "\"}";
    doRequest('DELETE', data);
}

function doRequest(method, info){
    return $.ajax({
        method: method,
        url: 'http://localhost:8080/WebChat/chat',
        data: info
    });
}