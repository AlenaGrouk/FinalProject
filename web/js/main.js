/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function popup() {
    alert("Hello World");
}

function fsrc(n){
    
    
    
    document.getElementById("images").src = n;
    
    
    
    var div = document.getElementById("textDiv");
    div.textContent = n;
    var text = div.textContent;
    
        
//    var StringArray = Java.type("java.lang.String[]");
//    var a = new StringArray(5);
//
//    // Set the value of the first element
//    a[0] = "Scripting is great!";
//    
//    var div2 = document.getElementById("textDiv2");
//    div2.textContent = a[0];
//    var text2 = div2.textContent;
    
}

function post(path, params, method) {
    method = method || "post";
    
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);
    
    for (var key in params) {
        if (params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);
            
            form.appendChild(hiddenField);
        }
        
        
    }
    
    document.body.appendChild(form);
    form.submit();
    
}