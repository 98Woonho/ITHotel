const listReq = () => {
    if (history.length > 1) {
        history.back();
    } else {
        window.close();
    }
}