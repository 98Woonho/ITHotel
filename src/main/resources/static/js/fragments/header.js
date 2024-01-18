const userArea = document.querySelector('.userArea');
const hotelArea = document.querySelector('.hotelArea');


// function checkCookie(cookieName) {
//     var cookies = document.cookie.split(';');
//     for (var i = 0; i < cookies.length; i++) {
//         var cookie = cookies[i].trim();
//         if (cookie.indexOf(cookieName + '=') === 0) {
//             return true; // Cookie found
//         }
//     }
//     return false; // Cookie not found
// }

window.onload = function (qualifiedName, value) {
    // var cookieName = 'JWT-AUTHENTICATION-ADMIN';
    // var isCookiePresentJwtAdmin = checkCookie(cookieName);

    var cookies = document.cookie.split(';');

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].trim();
        if (cookie.indexOf('JWT-AUTHENTICATION-ADMIN=') === 0) {
            userArea.querySelector('#logout').removeAttribute('hidden');
            userArea.querySelector('#adminPage').removeAttribute('hidden');
            hotelArea.querySelector('#customerInquiry').setAttribute('hidden', '');
        }
        else if (cookie.indexOf('JWT-AUTHENTICATION=') === 0) {
            userArea.querySelector('#logout').removeAttribute('hidden');
            userArea.querySelector('#myPage').removeAttribute('hidden');
            userArea.querySelector('#message').removeAttribute('hidden');
        } else {
            userArea.querySelector('#login').removeAttribute('hidden');
            userArea.querySelector('#join').removeAttribute('hidden');
        }
    }
};