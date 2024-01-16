const map = document.getElementById('map');

map.init = function (latitude, longitude) {
    if (latitude === undefined || longitude === undefined) {
        navigator.geolocation.getCurrentPosition(function () {
            map.init(36.31002549601922, 127.99374440243683);
        });
        return;
    }
    const option = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 12
    };
    map.instance = new kakao.maps.Map(map, option);

    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseObject = JSON.parse(xhr.responseText);
            const items = responseObject['data'];
            const geocoder = new kakao.maps.services.Geocoder();
            for (const item of items) {
                geocoder.addressSearch(item['GNG_CS'], function (result, status) {
                    if (status !== kakao.maps.services.Status.OK) {
                        return;
                    }
                    const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                    // 마커 생성
                    const marker = new kakao.maps.Marker({
                        map: map.instance, // 마커를 표시할 지도
                        position: coords // 마커의 위치
                    });

                    // 마커에 표시할 인포윈도우를 생성합니다
                    const infowindow = new kakao.maps.InfoWindow({
                        content: '<div class="info-title">' + item["BZ_NM"] + '</div>' // 인포윈도우에 표시할 내용
                    });

                    // 마커에 이벤트를 등록하는 함수 만들고 즉시 호출하여 클로저를 만듭니다
                    // 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
                    (function(marker, infowindow) {
                        // 마커에 mouseover 이벤트를 등록하고 마우스 오버 시 인포윈도우를 표시합니다
                        kakao.maps.event.addListener(marker, 'mouseover', function() {
                            infowindow.open(map.instance, marker);
                            // infowindow 위치 css(infowindow가 open된 이후에 실행되어야 함)
                            var infoTitle = document.querySelectorAll('.info-title');
                            infoTitle.forEach(function(e) {
                                var w = e.offsetWidth;
                                var ml = w/2;
                                e.parentElement.style.top = "82px";
                                e.parentElement.style.left = "50%";
                                e.parentElement.style.marginLeft = -ml+"px";
                                e.parentElement.style.width = w+"px";
                                e.parentElement.previousSibling.style.display = "none";
                                e.parentElement.parentElement.style.border = "0px";
                                e.parentElement.parentElement.style.background = "unset";
                            });
                        });

                        // 마커에 mouseout 이벤트를 등록하고 마우스 아웃 시 인포윈도우를 닫습니다
                        kakao.maps.event.addListener(marker, 'mouseout', function() {
                            infowindow.close();
                        });
                    })(marker, infowindow);

                });
            }
        } else {
            alert('지도를 불러오지 못하였습니다.\n\n확인을 누르면 페이지를 새로 고칩니다.');
            location.reload();
        }
    };
    xhr.open('GET', '/hotel/resources/json/XHR3.json');
    xhr.send();
};

map.init();